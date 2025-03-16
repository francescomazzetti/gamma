package it.gamma.service.orchestrator.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="orchestrator.pec")
public class PecServiceConfiguration {
	
	private String retrieveMessagesUrl;
	private String retrieveAttachmentsUrl;
	
	public String getRetrieveMessagesUrl() {
		return retrieveMessagesUrl;
	}

	public void setRetrieveMessagesUrl(String retrieveMessagesUrl) {
		this.retrieveMessagesUrl = retrieveMessagesUrl;
	}

	public String getRetrieveAttachmentsUrl() {
		return retrieveAttachmentsUrl;
	}

	public void setRetrieveAttachmentsUrl(String retrieveAttachmentsUrl) {
		this.retrieveAttachmentsUrl = retrieveAttachmentsUrl;
	}
}
