package it.gamma.service.worker.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("SignedData")
public class SignedAttachmentModel
{
	@Id
	private String id;
	private String hash;
	private String fileOrigin;
	private String fileName;
	private String signatureType;
	private String signature;
	private String owner;
	private String tenantId;
	private String timestamp;
	@Indexed(name="status_idx", unique = false)
	private String status;
	private String worker;
	private String retentionTimestamp;
	
	public SignedAttachmentModel() {
		this("", "", "", "", "", "", "", "", "", "");
	}
	
	public SignedAttachmentModel(String id, String hash, String fileOrigin, String fileName, String signatureType, String signature, String owner, String tenantId, String timestamp, String status) {
		this.id = id;
		this.hash = hash;
		this.fileOrigin = fileOrigin;
		this.setFileName(fileName);
		this.setSignatureType(signatureType);
		this.signature = signature;
		this.owner = owner;
		this.tenantId = tenantId;
		this.timestamp = timestamp;
		this.status = status;
		this.worker = "";
		this.retentionTimestamp = "";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getFileOrigin() {
		return fileOrigin;
	}

	public void setFileOrigin(String fileOrigin) {
		this.fileOrigin = fileOrigin;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getWorker() {
		return worker;
	}

	public void setWorker(String worker) {
		this.worker = worker;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSignatureType() {
		return signatureType;
	}

	public void setSignatureType(String signatureType) {
		this.signatureType = signatureType;
	}

	public String getRetentionTimestamp() {
		return retentionTimestamp;
	}

	public void setRetentionTimestamp(String retentionTimestamp) {
		this.retentionTimestamp = retentionTimestamp;
	}

}
