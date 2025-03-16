package it.gamma.service.signer.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import it.gamma.service.signer.IConstants;
import it.gamma.service.signer.account.IUserAccountRetriever;
import it.gamma.service.signer.account.UserAccount;
import it.gamma.service.signer.service.OauthUserinfoService;
import it.gamma.service.signer.service.RemoteSignVerifierService;
import it.gamma.service.signer.web.response.Attachment;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("remote")
public class SignerController
{
	private Logger log;
	private OauthUserinfoService _oauthUserinfoService;
	private RemoteSignVerifierService _remoteSignVerifierService;
	private IUserAccountRetriever _userAccountRetriever;
	
	@Autowired
	public SignerController(
			@Qualifier("oauth.userinfoService") OauthUserinfoService oauthUserinfoService,
			@Qualifier("signer.remoteSignService") RemoteSignVerifierService remoteSignVerifierService,
			@Qualifier("signer.userAccountRetriever") IUserAccountRetriever userAccountRetriever
			)
	{
		_oauthUserinfoService = oauthUserinfoService;
		_remoteSignVerifierService = remoteSignVerifierService;
		_userAccountRetriever = userAccountRetriever;
		log = LoggerFactory.getLogger(SignerController.class);
	}
	
	@PostMapping(path= "/signAttachments")
	public String signAttachments(
			@RequestParam(name="attachmentDataIn", required = false) String attachmentData,
			HttpSession session,
			Model model)
	{
		StringBuffer requestPlain = new StringBuffer();
		boolean verified = _remoteSignVerifierService.verify(attachmentData, requestPlain, log);
		if (!verified) {
			log.error("input signature verify ko");
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid request signature");
		}
		JSONObject requestAsJson = new  JSONObject(requestPlain.toString());
		String accessToken = requestAsJson.getString(IConstants.TOKEN_HINT);
		String userData = AccessTokenValidator.validate(accessToken, _oauthUserinfoService, log);
		if ("".equals(userData)) {
			log.error("access token verify ko");
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid access token");
		}
		JSONObject userDataJson = new JSONObject(userData);
		String claimsAsS = userDataJson.getString(IConstants.CLAIMS);
		JSONObject claims = new JSONObject(claimsAsS);
		String taxcode = claims.getString(IConstants.TAXCODE);
		UserAccount userAccount = _userAccountRetriever.retrieve(taxcode);
		if (userAccount == null) {
			log.error("user has not remote signature account - taxcode: " + taxcode);
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid user");
		}
		log.info("user account successfully retrieved - taxcode: " + taxcode + " - user signature instrument: " + userAccount.getSignInstrument());
		JSONArray attachmentsArray = requestAsJson.getJSONArray(IConstants.ATTACHMENTS);
		log.info("attachments length: " + attachmentsArray.length() + " - taxcode: " + taxcode);
		Map<String, Attachment> attachments = new HashMap<String, Attachment>();
		Iterator<Object> iterator = attachmentsArray.iterator();
		while (iterator.hasNext()) {
			String id = UUID.randomUUID().toString();
			JSONObject attachmentJson = (JSONObject) iterator.next();
			Attachment attachment = new Attachment();
			attachment.setOrigin(attachmentJson.getString("origin"));
			attachment.setHash(attachmentJson.getString("hash"));
			attachment.setName(attachmentJson.getString("name"));
			attachment.setOwner(attachmentJson.getString("owner"));
			attachments.put(id, attachment);
		}
		session.setAttribute(IConstants.SESSION_ATTACHMENTS, attachments);
		session.setAttribute(IConstants.SESSION_USER_ACCESS_TOKEN, accessToken);
		session.setAttribute(IConstants.SESSION_USER_ACCOUNT, userAccount);
		model.addAttribute(IConstants.ATTACHMENTS, attachments);
		model.addAttribute(IConstants.SIGN_ATTACHMENT_URL, "/gamma-service-signer/signer/sign");
		return "index";
	}
	
}
