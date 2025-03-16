package it.gamma.service.idp.web.metadata;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class MockMetadataReader implements IMetadataReader
{
	public static final String REDIRECT_URI_1 = "http://localhost:8083/gamma-service-orchestrator/oauth/client-redirect-1";
	public static final String CLIENT_ID = "gamma-orchestrator";
	private Map<String, JSONObject> _metadata;
	
	public MockMetadataReader() {
		_metadata = new HashMap<String, JSONObject>();
		JSONObject gammaTestMetadata = new JSONObject();
		gammaTestMetadata.put(KEY_ISSUER, CLIENT_ID);
		gammaTestMetadata.put(KEY_CLIENT_NAME, "Gamma Service");
		gammaTestMetadata.put(KEY_SECRET, "{SHA-256}5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");
		JSONArray redirectUris = new JSONArray();
		redirectUris.put(REDIRECT_URI_1);
		redirectUris.put("http://localhost:8083/gamma-service-orchestrator/oauth/client-redirect-2");
		redirectUris.put("http://localhost:8083/gamma-service-orchestrator/oauth/client-redirect-logout");
		gammaTestMetadata.put(KEY_REDIRECT_URIS, redirectUris);
		_metadata.put(CLIENT_ID, gammaTestMetadata);
	}
	
	public JSONObject read(String clientId) {
		return _metadata.get(clientId);
	}

	public String type() {
		return "mock";
	}

}
