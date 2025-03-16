package it.gamma.service.pec.web.response;

public class GetAttachmentResponse
{
	private String url;
	private String signedData;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSignedData() {
		return signedData;
	}
	public void setSignedData(String signedData) {
		this.signedData = signedData;
	}
}
