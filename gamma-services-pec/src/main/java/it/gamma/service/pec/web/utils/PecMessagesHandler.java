package it.gamma.service.pec.web.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.gamma.service.pec.mongo.model.UserMessage;
import it.gamma.service.pec.mongo.model.UserPec;
import it.gamma.service.pec.web.data.UserPecMessage;

public class PecMessagesHandler
{

	public Collection<PecMessagesHolder> merge(List<UserPec> userPecAddresses, List<UserMessage> userMessages) {
		Map<String, PecMessagesHolder> pecMessagesMap = new HashMap<String, PecMessagesHolder>();
		Iterator<UserPec> iterator = userPecAddresses.iterator();
		while (iterator.hasNext()) {
			UserPec userPec = iterator.next();
			Iterator<UserMessage> messagesIterator = userMessages.iterator();
			while (messagesIterator.hasNext()) {
				UserMessage userMessage = messagesIterator.next();
				if (!userMessage.getPecId().equals(userPec.getId())) {
					continue;
				}
				UserPecMessage userPecMessage = new UserPecMessage(userMessage.getType(), userMessage.getAddress(), userMessage.getHasattachments(), userMessage.getTimestamp());
				if (!pecMessagesMap.containsKey(userPec.getAddress())) {
					pecMessagesMap.put(userPec.getAddress(), new PecMessagesHolder(userPec.getAddress()));
				}
				pecMessagesMap.get(userPec.getAddress()).getMessages().add(userPecMessage);
			}
		}
		return pecMessagesMap.values();
	}

}
