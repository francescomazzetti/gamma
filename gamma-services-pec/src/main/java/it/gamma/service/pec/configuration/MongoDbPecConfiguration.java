package it.gamma.service.pec.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="spring.data.mongodb.pec")
public class MongoDbPecConfiguration {
	
	private String messagesTablePrefix;
	private String attachmentsTablePrefix;
	
	public String getMessagesTablePrefix() {
		return messagesTablePrefix;
	}
	public void setMessagesTablePrefix(String messagesTablePrefix) {
		this.messagesTablePrefix = messagesTablePrefix;
	}
	public String getAttachmentsTablePrefix() {
		return attachmentsTablePrefix;
	}
	public void setAttachmentsTablePrefix(String attachmentsTablePrefix) {
		this.attachmentsTablePrefix = attachmentsTablePrefix;
	}

}
