package it.gamma.service.idp.web.metadata;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class MockMetadataReader implements IMetadataReader
{
	private Map<String, JSONObject> _metadata;
	
	public MockMetadataReader() {
		_metadata = new HashMap<String, JSONObject>();
		JSONObject gammaTestMetadata = new JSONObject();
		String issuer = "https://gamma-orchestrator.com";
		gammaTestMetadata.put(KEY_ISSUER, issuer);
		gammaTestMetadata.put(KEY_CLIENT_NAME, "Gamma Service");
		gammaTestMetadata.put(KEY_SECRET, "{SHA-256}5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");
		JSONArray redirectUris = new JSONArray();
		redirectUris.put("http://localhost:8082/gamma-service-orchestrator/client-redirect-1");
		redirectUris.put("http://localhost:8082/gamma-service-orchestrator/client-redirect-2");
		gammaTestMetadata.put(KEY_REDIRECT_URIS, redirectUris);
		_metadata.put(issuer, gammaTestMetadata);
	}
	
	public JSONObject read(String clientId) {
		return _metadata.get(clientId);
	}

	public String type() {
		return "mock";
	}

}
