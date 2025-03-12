package it.gamma.service.idp.web.metadata;

import org.json.JSONObject;

public class FileMetadataReader implements IMetadataReader
{
	private String _path;

	public FileMetadataReader(String path) {
		_path = path;
	}

	public JSONObject read(String clientId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}

	public String type() {
		// TODO Auto-generated method stub
		return "file";
	}

}
