package it.gamma.service.pec.web.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.gamma.service.pec.web.data.UserPecMessage;

public class PecMessagesHolder implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String pec;
	private List<UserPecMessage> messages;
	
	public PecMessagesHolder(String address) {
		this.pec = address;
		messages = new ArrayList<UserPecMessage>();
	}
	public String getPec() {
		return pec;
	}
	public void setPec(String pec) {
		this.pec = pec;
	}
	public List<UserPecMessage> getMessages() {
		return messages;
	}
	public void setMessages(List<UserPecMessage> messages) {
		this.messages = messages;
	}
}
