package it.gamma.service.signer.web;

import java.util.Date;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import it.gamma.service.signer.IConstants;
import it.gamma.service.signer.account.IUserAccountRetriever;
import it.gamma.service.signer.account.UserAccount;
import it.gamma.service.signer.mongo.model.SignedAttachmentModel;
import it.gamma.service.signer.mongo.respository.SignedAttachmentRepository;
import it.gamma.service.signer.service.ISignatureService;
import it.gamma.service.signer.service.OauthUserinfoService;
import it.gamma.service.signer.service.RemoteSignVerifierService;
import it.gamma.service.signer.utils.SignatureServiceFactory;
import it.gamma.service.signer.web.response.Attachment;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("signer")
public class SignerRestController {
	
	private Logger log;
	private OauthUserinfoService _oauthUserinfoService;
	private RemoteSignVerifierService _remoteSignVerifierService;
	private IUserAccountRetriever _userAccountRetriever;
	private SignedAttachmentRepository _signedAttachmentRepository;
	
	@Autowired
	public SignerRestController(
			@Qualifier("oauth.userinfoService") OauthUserinfoService oauthUserinfoService,
			@Qualifier("signer.remoteSignService") RemoteSignVerifierService remoteSignVerifierService,
			@Qualifier("signer.userAccountRetriever") IUserAccountRetriever userAccountRetriever,
			SignedAttachmentRepository signedAttachmentRepository
			)
	{
		_oauthUserinfoService = oauthUserinfoService;
		_remoteSignVerifierService = remoteSignVerifierService;
		_userAccountRetriever = userAccountRetriever;
		_signedAttachmentRepository = signedAttachmentRepository;
		log = LoggerFactory.getLogger(SignerRestController.class);
	}
	
	@PostMapping(path= "/sign")
	public ResponseEntity signAttachment(
			@RequestParam(name="id", required = true) String attachmentId,
			HttpSession session)
	{
		String accessToken = (String) session.getAttribute(IConstants.SESSION_USER_ACCESS_TOKEN);
		if (accessToken == null) {
			log.error("access token read ko");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		String userData = AccessTokenValidator.validate(accessToken, _oauthUserinfoService, log);
		if ("".equals(userData)) {
			log.error("access token verify ko");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		JSONObject userDataJson = new JSONObject(userData);
		String claimsAsS = userDataJson.getString(IConstants.CLAIMS);
		JSONObject claims = new JSONObject(claimsAsS);
		Map<String, Attachment> attachmentsMap = (Map<String, Attachment>) session.getAttribute(IConstants.SESSION_ATTACHMENTS);
		if (attachmentsMap == null) {
			log.error("no valid user session found");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		Attachment att = attachmentsMap.get(attachmentId);
		if (att == null) {
			log.error("input id not valid: " + attachmentId);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		UserAccount userAccount = (UserAccount) session.getAttribute(IConstants.SESSION_USER_ACCOUNT);
		ISignatureService service = SignatureServiceFactory.build(userAccount.getSignInstrument());
		try {
			String signature = service.sign(att.getHash(), userAccount);
			SignedAttachmentModel signedAttachmentModel = new SignedAttachmentModel(
					attachmentId, att.getHash(), att.getOrigin(), att.getName(), 
					signature, att.getOwner(), claims.getString(IConstants.TENANT_ID), 
					new Date().getTime()+"", IConstants.STATUS_TO_BE_PROCESSED);
			SignedAttachmentModel savedModel = _signedAttachmentRepository.save(signedAttachmentModel);
			if (savedModel == null) {
				log.error("data signature storage failed");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			log.error("data signature failed: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
