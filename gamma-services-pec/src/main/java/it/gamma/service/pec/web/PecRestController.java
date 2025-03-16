package it.gamma.service.pec.web;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.nimbusds.jose.JOSEException;

import it.gamma.service.pec.IConstants;
import it.gamma.service.pec.configuration.AttachmentSignerServiceConfiguration;
import it.gamma.service.pec.configuration.MongoDbPecConfiguration;
import it.gamma.service.pec.mongo.model.Attachment;
import it.gamma.service.pec.mongo.model.UserMessage;
import it.gamma.service.pec.mongo.model.UserPec;
import it.gamma.service.pec.mongo.repositories.UserPecRepository;
import it.gamma.service.pec.service.OauthUserinfoService;
import it.gamma.service.pec.web.request.GetMessagesRequest;
import it.gamma.service.pec.web.response.GetAttachmentResponse;
import it.gamma.service.pec.web.sign.AttachmentsDataSignerFactory;
import it.gamma.service.pec.web.sign.PecSigner;
import it.gamma.service.pec.web.utils.PecMessagesHandler;
import it.gamma.service.pec.web.utils.PecMessagesHolder;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class PecRestController
{
	private Logger log;
	private MongoTemplate _mongoTemplate;
	private OauthUserinfoService _userinfoService;
	private UserPecRepository _userPecRepository;
	private MongoDbPecConfiguration _mongoDbPecConfiguration;
	private PecSigner _pecSigner;
	private AttachmentSignerServiceConfiguration _attachmentSignerServiceConfiguration;

	@Autowired
	public PecRestController(
			@Qualifier("oauth.userinfoService") OauthUserinfoService userinfoService,
			@Qualifier("signer.signerService") PecSigner pecSigner,
			UserPecRepository userPecRepository,
			MongoTemplate mongoTemplate,
			MongoDbPecConfiguration mongoDbPecConfiguration,
			AttachmentSignerServiceConfiguration attachmentSignerServiceConfiguration
			) {
		_userinfoService = userinfoService;
		_userPecRepository = userPecRepository;
		_mongoTemplate = mongoTemplate;
		_mongoDbPecConfiguration = mongoDbPecConfiguration;
		_pecSigner = pecSigner;
		_attachmentSignerServiceConfiguration = attachmentSignerServiceConfiguration;
		log = LoggerFactory.getLogger(PecRestController.class);
	}
	
	@CrossOrigin //TODO ovviamente mockato, va preso almeno dalla configurazione
	@PostMapping("/getMessages")
	public Collection<PecMessagesHolder> getMessages(
			GetMessagesRequest getMessagesRequest,
			HttpServletRequest request
			) {
		String atData = AccessTokenValidator.validate(request, _userinfoService, log);
		if (atData == "") {
			log.error("getMessages - access token validation ko");
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not logged in");
		}
		JSONObject userDataJson = new JSONObject(atData);
		String username = userDataJson.getString(IConstants.SUB);
		String claimsAsS = userDataJson.getString(IConstants.CLAIMS);
		JSONObject claims = new JSONObject(claimsAsS);
		String taxcode = claims.getString(IConstants.TAXCODE);
		String tenantId = claims.getString(IConstants.TENANT_ID);
		log.info("access token validation ok - username: " + username + " - tenant: " + tenantId);
		List<UserPec> userPecAddresses = _userPecRepository.findByTaxcode(taxcode);
		List<UserMessage> userMessages = new MongoQueryHelper(_mongoTemplate, _mongoDbPecConfiguration).findUserMessages(getMessagesRequest, userPecAddresses, tenantId);
		
		Collection<PecMessagesHolder> pecMessages = new PecMessagesHandler().merge(userPecAddresses, userMessages);
		log.info("pec messages retrieve ok - username: " + username + " - tenant: " + tenantId);
		return pecMessages;
	}
	
	@CrossOrigin //TODO ovviamente mockato, va preso almeno dalla configurazione
	@PostMapping("/getAttachments")
	public GetAttachmentResponse getAttachments(
			GetAttachmentRequest getAttachmentRequest,
			HttpServletRequest request
			) {
		String authorization = request.getHeader("Authorization");
		String atData = AccessTokenValidator.validate(authorization, _userinfoService, log);
		if (atData == "") {
			log.error("getAttachments - access token validation ko");
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not logged in");
		}
		JSONObject userDataJson = new JSONObject(atData);
		String username = userDataJson.getString(IConstants.SUB);
		String claimsAsS = userDataJson.getString(IConstants.CLAIMS);
		JSONObject claims = new JSONObject(claimsAsS);
		String taxcode = claims.getString(IConstants.TAXCODE);
		String tenantId = claims.getString(IConstants.TENANT_ID);
		log.info("access token validation ok - username: " + username + " - tenant: " + tenantId);
		log.info("messages: " + getAttachmentRequest.getMessageIds() + " - size: " + getAttachmentRequest.getMessageIds().size());
		List<Attachment> userAttachments = new MongoQueryHelper(_mongoTemplate, _mongoDbPecConfiguration).findUserAttachments(getAttachmentRequest.getMessageIds(), tenantId);
		Iterator<Attachment> iterator = userAttachments.iterator();
		while (iterator.hasNext()) {
			Attachment attachment = iterator.next();
			if (!attachment.getOwner().equalsIgnoreCase(taxcode)) {
				log.error("getAttachments - invalid data sent in input");
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Request");
			}
		}
		try {
			String accessToken = authorization.substring(7);
			String signedData = new AttachmentsDataSignerFactory(_pecSigner).sign(userAttachments, accessToken);
			log.info("pec attachments retrieve ok - username: " + username + " - tenant: " + tenantId);
			GetAttachmentResponse response = new GetAttachmentResponse();
			response.setSignedData(signedData);
			response.setUrl(_attachmentSignerServiceConfiguration.getUrl());
			return response;
		} catch (JOSEException e) {
			log.error("getAttachments - error signing request data");
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Generic Error");
		}
	}
}
