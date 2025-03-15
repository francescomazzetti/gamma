package it.gamma.service.pec.web;

import java.util.Collection;
import java.util.List;

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

import it.gamma.service.pec.IConstants;
import it.gamma.service.pec.configuration.MongoDbPecConfiguration;
import it.gamma.service.pec.mongo.model.UserMessage;
import it.gamma.service.pec.mongo.model.UserPec;
import it.gamma.service.pec.mongo.repositories.UserPecRepository;
import it.gamma.service.pec.service.OauthUserinfoService;
import it.gamma.service.pec.web.request.GetMessagesRequest;
import it.gamma.service.pec.web.utils.PecMessagesHandler;
import it.gamma.service.pec.web.utils.PecMessagesHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class PecRestController
{
	private Logger log;
	private MongoTemplate _mongoTemplate;
	private OauthUserinfoService _userinfoService;
	private UserPecRepository _userPecRepository;
	private MongoDbPecConfiguration _mongoDbPecConfiguration;

	@Autowired
	public PecRestController(
			@Qualifier("oauth.userinfoService") OauthUserinfoService userinfoService,
			UserPecRepository userPecRepository,
			MongoTemplate mongoTemplate,
			MongoDbPecConfiguration mongoDbPecConfiguration
			) {
		_userinfoService = userinfoService;
		_userPecRepository = userPecRepository;
		_mongoTemplate = mongoTemplate;
		_mongoDbPecConfiguration = mongoDbPecConfiguration;
		log = LoggerFactory.getLogger(PecRestController.class);
	}
	
	@CrossOrigin //TODO ovviamente mockato, va preso almeno dalla configurazione
	@PostMapping("/getMessages")
	public Collection<PecMessagesHolder> getMessages(
			GetMessagesRequest getMessagesRequest,
			HttpServletRequest request, HttpSession httpSession
			) {
		boolean valid = AccessTokenValidator.validate(request, httpSession, _userinfoService, log);
		if (!valid) {
			log.error("access token validation ko");
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not logged in");
		}
		String userData = (String) httpSession.getAttribute(IConstants.TOKEN_DATA);
		JSONObject userDataJson = new JSONObject(userData);
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
}
