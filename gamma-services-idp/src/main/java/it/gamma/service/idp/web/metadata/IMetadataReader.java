package it.gamma.service.idp.web.metadata;

import org.json.JSONObject;

public interface IMetadataReader
{
	String KEY_REDIRECT_URIS = "redirect_uris";
	String KEY_SECRET = "secret";
	String KEY_CLIENT_NAME = "client_name";
	String KEY_ISSUER = "issuer";
	
	String type();
	JSONObject read(String clientId);
}
