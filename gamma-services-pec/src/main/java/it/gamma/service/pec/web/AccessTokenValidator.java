package it.gamma.service.pec.web;

import org.slf4j.Logger;

import it.gamma.service.pec.IConstants;
import it.gamma.service.pec.service.OauthUserinfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class AccessTokenValidator {

	public static boolean validate(HttpServletRequest request, HttpSession session, OauthUserinfoService userInfoService, Logger log) {
		String authorization = request.getHeader("Authorization");
		session.removeAttribute(IConstants.TOKEN_DATA);
		if (authorization == null || "".equals(authorization.trim())) {
			log.error("no authorization header found");
			return false;
		}
		if (!authorization.startsWith("Bearer ")) {
			log.error("invalid authorization header type found");
			/**
			res.setContentType("text/plain");
			res.setStatus(HttpServletResponse.SC_FORBIDDEN);
			res.resetBuffer();
			**/
			return false;
		}
		String accessToken = authorization.substring(7);
		StringBuffer cleanTokenBuffer = new StringBuffer();
		boolean valid = userInfoService.verify(accessToken, cleanTokenBuffer, log);
		if (!valid) {
			log.error("access token validation failed");
			return false;
		}
		session.setAttribute(IConstants.TOKEN_DATA, cleanTokenBuffer.toString());
		log.info("access token validation successful");
		return true;
	}

}
