package it.gamma.service.pec.web;

import java.util.List;

public class GetAttachmentRequest
{
	private List<String> messageIds;

	public List<String> getMessageIds() {
		return messageIds;
	}

	public void setMessageIds(List<String> messageIds) {
		this.messageIds = messageIds;
	}
}
