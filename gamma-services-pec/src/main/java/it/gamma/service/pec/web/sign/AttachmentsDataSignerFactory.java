package it.gamma.service.pec.web.sign;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nimbusds.jose.JOSEException;

import it.gamma.service.pec.mongo.model.Attachment;

public class AttachmentsDataSignerFactory
{
	private PecSigner _pecSigner;

	public AttachmentsDataSignerFactory(PecSigner pecSigner) {
		_pecSigner = pecSigner;
	}

	public String sign(List<Attachment> userAttachments, String accessToken) throws JOSEException {
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		Iterator<Attachment> iterator = userAttachments.iterator();
		while (iterator.hasNext()) {
			Attachment attachment = iterator.next();
			JSONObject attachmentJson = new JSONObject();
			attachmentJson.put("hash", attachment.getHashValue());
			attachmentJson.put("name", attachment.getName());
			attachmentJson.put("owner", attachment.getOwner());
			attachmentJson.put("origin", attachment.getOrigin());
			jsonArray.put(attachmentJson);
		}
		jsonObject.put("attachments", jsonArray);
		jsonObject.put("token_hint", accessToken);
		String signature = _pecSigner.sign(jsonObject.toString());
		return signature;
	}

}
