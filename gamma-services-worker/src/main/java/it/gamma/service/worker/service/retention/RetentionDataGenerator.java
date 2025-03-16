package it.gamma.service.worker.service.retention;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;

import it.gamma.service.worker.mongo.model.SignedAttachmentModel;
import it.gamma.service.worker.service.file.IFileReaderStrategy;
import it.gamma.service.worker.service.file.LocalFileReaderStrategy;

public class RetentionDataGenerator
{
	private static Map<String, IFileReaderStrategy> _fileReaderStrategyMap;
	
	static {
		_fileReaderStrategyMap = new HashMap<String, IFileReaderStrategy>();
		_fileReaderStrategyMap.put("local", new LocalFileReaderStrategy());
		// TODO remote, db, ...
	}

	public byte[] generate(SignedAttachmentModel signedAttachmentModel, Logger log) {
		String fileName = signedAttachmentModel.getFileName();
		String fileOrigin = signedAttachmentModel.getFileOrigin();
		String[] strategyArr = fileOrigin.split(":");
		IFileReaderStrategy fileReaderStrategy = _fileReaderStrategyMap.get(strategyArr[0]);
		try {
			int indexOf = strategyArr[0].length()+1;
			byte[] fileBytes = fileReaderStrategy.read(fileOrigin.substring(indexOf), signedAttachmentModel.getFileName());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ZipOutputStream zos = new ZipOutputStream(baos);
			addEntry(zos, fileName, fileBytes);
		    
			byte[] signatureBytes = signedAttachmentModel.getSignature().getBytes();
			addEntry(zos, "signature.p7m", signatureBytes);
		    
		    zos.close();
		    return baos.toByteArray();
		} catch (Exception e) {
			log.info("error generating signature file - id: " + signedAttachmentModel.getId()
			+ " - fileName: " + signedAttachmentModel.getFileName() + " - origin: " + signedAttachmentModel.getFileOrigin() + " - " + e.getMessage());
			return null;
		}
	}

	private void addEntry(ZipOutputStream zos, String fileName, byte[] fileBytes) throws IOException {
		ZipEntry entry = new ZipEntry(fileName);
	    entry.setSize(fileBytes.length);
	    zos.putNextEntry(entry);
	    zos.write(fileBytes);
	    zos.closeEntry();
	}
	
}
