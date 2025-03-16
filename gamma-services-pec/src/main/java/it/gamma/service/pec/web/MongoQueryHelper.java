package it.gamma.service.pec.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import it.gamma.service.pec.configuration.MongoDbPecConfiguration;
import it.gamma.service.pec.mongo.model.Attachment;
import it.gamma.service.pec.mongo.model.UserMessage;
import it.gamma.service.pec.mongo.model.UserPec;
import it.gamma.service.pec.web.request.GetMessagesRequest;

public class MongoQueryHelper
{
	private MongoTemplate _mongoTemplate;
	private MongoDbPecConfiguration _mongoDbPecConfiguration;

	public MongoQueryHelper(MongoTemplate mongoTemplate, MongoDbPecConfiguration mongoDbPecConfiguration) {
		_mongoTemplate = mongoTemplate;
		_mongoDbPecConfiguration = mongoDbPecConfiguration;
	}

	public List<UserMessage> findUserMessages(GetMessagesRequest getMessagesRequest, List<UserPec> userPecList, String tenantId) {
		List<String> pecIdList = new ArrayList<String>();
		Iterator<UserPec> iterator = userPecList.iterator();
		while (iterator.hasNext()) {
			UserPec userPec = iterator.next();
			pecIdList.add(userPec.getId());
		}
		Criteria criteria = new Criteria();
		List<Criteria> criteriaList = new ArrayList<Criteria>();
		criteriaList.add(Criteria.where("pecId").in(pecIdList));
		criteriaList.add(Criteria.where("type").is(getMessagesRequest.getType()));
		criteriaList.add(Criteria.where("hasattachments").is(getMessagesRequest.getHasattachments()));
		
		Pattern pattern = Pattern.compile(Pattern.quote(getMessagesRequest.getEmail()), Pattern.CASE_INSENSITIVE);
		criteriaList.add(Criteria.where("address").regex(pattern));
		
		criteria.andOperator(criteriaList);
		
		List<UserMessage> messages = _mongoTemplate.find(
				new Query(criteria), 
				UserMessage.class, 
				_mongoDbPecConfiguration.getMessagesTablePrefix()+tenantId
				);
		return messages;
	}

	public List<Attachment> findUserAttachments(List<String> list, String tenantId) {
		Criteria criteria = Criteria.where("messageId").in(list);
		List<Attachment> attachments = _mongoTemplate.find(
				new Query(criteria), 
				Attachment.class, 
				_mongoDbPecConfiguration.getAttachmentsTablePrefix()+tenantId
				);
		return attachments;
	}

}
