package it.gamma.service.signer.web;

import org.slf4j.Logger;

import it.gamma.service.signer.service.OauthUserinfoService;
import jakarta.servlet.http.HttpServletRequest;

public class AccessTokenValidator {

	public static String validate(String accessToken, OauthUserinfoService userInfoService, Logger log) {
		if (accessToken == null || "".equals(accessToken.trim())) {
			log.error("no authorization header found");
			return "";
		}
		StringBuffer cleanTokenBuffer = new StringBuffer();
		boolean valid = userInfoService.verify(accessToken, cleanTokenBuffer, log);
		if (!valid) {
			log.error("access token validation failed");
			return "";
		}
		log.info("access token validation successful");
		return cleanTokenBuffer.toString();
	}

	public static String validate(HttpServletRequest request, OauthUserinfoService userinfoService, Logger log) {
		String authorization = request.getHeader("Authorization");
		return validate(authorization, userinfoService, log);
	}

}
