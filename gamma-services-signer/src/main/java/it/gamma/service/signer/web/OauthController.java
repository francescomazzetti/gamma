package it.gamma.service.signer.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.gamma.service.signer.service.OauthUserinfoService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("federation")
public class OauthController
{
	private Logger log;
	private OauthUserinfoService _oauthUserinfoService;
	
	@Autowired
	public OauthController(
			@Qualifier("oauth.userinfoService") OauthUserinfoService oauthUserinfoService
			)
	{
		_oauthUserinfoService = oauthUserinfoService;
		log = LoggerFactory.getLogger(OauthController.class);
	}
	
	@PostMapping(path= "/signAttachments")
	public String signAttachments(
			@RequestParam(name="attachmentDataIn", required = false) String attachmentData,
			HttpSession session,
			Model model)
	{
		return "index";
	}
}
