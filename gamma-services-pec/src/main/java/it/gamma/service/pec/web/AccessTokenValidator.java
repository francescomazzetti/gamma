package it.gamma.service.pec.web;

import org.slf4j.Logger;

import it.gamma.service.pec.service.OauthUserinfoService;
import jakarta.servlet.http.HttpServletRequest;

public class AccessTokenValidator {

	public static String validate(String authorization, OauthUserinfoService userInfoService, Logger log) {
		if (authorization == null || "".equals(authorization.trim())) {
			log.error("no authorization header found");
			return "";
		}
		if (!authorization.startsWith("Bearer ")) {
			log.error("invalid authorization header type found");
			/**
			res.setContentType("text/plain");
			res.setStatus(HttpServletResponse.SC_FORBIDDEN);
			res.resetBuffer();
			**/
			return "";
		}
		String accessToken = authorization.substring(7);
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
