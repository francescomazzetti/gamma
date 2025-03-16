package it.gamma.service.worker.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import it.gamma.service.worker.IConstants;
import it.gamma.service.worker.configuration.WorkerDataConfiguration;
import it.gamma.service.worker.mongo.model.SignedAttachmentModel;
import it.gamma.service.worker.mongo.repository.SignedAttachmentRepository;
import it.gamma.service.worker.service.retention.IRetentionService;
import it.gamma.service.worker.service.retention.RetentionDataGenerator;

@Component
public class RetentionTaskService
{
	private SignedAttachmentRepository _signedAttachmentRepository;
	private WorkerDataConfiguration _workerDataConfiguration;
	private Logger log;
	private IRetentionService _retentionService;

	@Autowired
	public RetentionTaskService(
			SignedAttachmentRepository signedAttachmentRepository,
			WorkerDataConfiguration workerDataConfiguration,
			@Qualifier("retention.retentionService") IRetentionService retentionService
			)
	{
		_signedAttachmentRepository = signedAttachmentRepository;
		_workerDataConfiguration = workerDataConfiguration;
		_retentionService = retentionService;
		log = LoggerFactory.getLogger(RetentionTaskService.class);
	}
	
	@Scheduled(
			fixedRateString = "${gamma.worker.batch.scheduler.fixedRate}",
			initialDelayString = "${gamma.worker.batch.scheduler.initialDelay}"
			)
	public void execute() {
		log.info("START");
		List<SignedAttachmentModel> data = _signedAttachmentRepository.findByStatus(IConstants.STATUS_TO_BE_PROCESSED, PageRequest.of(0, _workerDataConfiguration.getQueryMaxResults().intValue()));
		if (data == null) {
			log.info("no data found with status: " + IConstants.STATUS_TO_BE_PROCESSED);
			log.info("END");
			return;
		}
		if (data.size() == 0) {
			log.info("no entries found with status: " + IConstants.STATUS_TO_BE_PROCESSED);
			log.info("END");
			return;
		}
		changeStatus(data);
		_signedAttachmentRepository.saveAll(data);
		
		Iterator<SignedAttachmentModel> iterator = data.iterator();
		while (iterator.hasNext()) {
			SignedAttachmentModel signedAttachmentModel = iterator.next();
			byte[] retentionFile = new RetentionDataGenerator().generate(signedAttachmentModel, log);
			if (retentionFile == null) {
				signedAttachmentModel.setStatus(IConstants.STATUS_ERROR);
				_signedAttachmentRepository.save(signedAttachmentModel);
				continue;
			}
			boolean saved = _retentionService.save(retentionFile, "zip", signedAttachmentModel.getTenantId(), signedAttachmentModel.getOwner(), log);
			if (!saved) {
				log.error("error in data retention");
				signedAttachmentModel.setStatus(IConstants.STATUS_TO_BE_PROCESSED);
				_signedAttachmentRepository.save(signedAttachmentModel);
				continue;
			}
			log.info("record " + signedAttachmentModel.getId() + " successfully worked - updating");
			signedAttachmentModel.setStatus(IConstants.STATUS_SUCCESSFUL);
			signedAttachmentModel.setRetentionTimestamp(new Date().getTime()+"");
			_signedAttachmentRepository.save(signedAttachmentModel);
		}
		log.info("END");
	}

	private void changeStatus(List<SignedAttachmentModel> data) {
		List<String> ids = new ArrayList<String>();
		Iterator<SignedAttachmentModel> iterator = data.iterator();
		while (iterator.hasNext()) {
			SignedAttachmentModel signedAttachmentModel = (SignedAttachmentModel) iterator.next();
			signedAttachmentModel.setStatus(IConstants.STATUS_IN_PROGRESS);
			signedAttachmentModel.setWorker(_workerDataConfiguration.getInstance());
		}
	}
	
}
