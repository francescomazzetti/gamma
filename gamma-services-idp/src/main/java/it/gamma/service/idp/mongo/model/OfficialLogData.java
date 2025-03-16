package it.gamma.service.idp.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("OfficialLogData")
public class OfficialLogData
{
	@Id
	private String id;
	private String ip;
	private String username;
	private String date;
	private String eventType;
	private String eventDescription;
	private String eventMessage;
	private String sessionId;
	private String system;
	
	public OfficialLogData(String id, String ip, String username, String date, String eventType, String eventDescription, String eventMessage, String sessionId, String system) {
		this.setId(id);
		this.setIp(ip);
		this.setUsername(username);
		this.setDate(date);
		this.setEventType(eventType);
		this.setEventDescription(eventDescription);
		this.setEventMessage(eventMessage);
		this.setSessionId(sessionId);
		this.setSystem(system);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public String getEventMessage() {
		return eventMessage;
	}

	public void setEventMessage(String eventMessage) {
		this.eventMessage = eventMessage;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}
}
