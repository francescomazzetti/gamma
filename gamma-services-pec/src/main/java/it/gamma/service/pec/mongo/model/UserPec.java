package it.gamma.service.pec.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("UserPec")
public class UserPec
{
	@Id
	private String id;
	private String address;
	@Indexed(name="taxcode_idx", unique = false)
	private String taxcode;
	private String tenantId;
	
	public UserPec() {
		this.setId("");
		this.setAddress("");
		this.setTaxcode("");
		this.setTenantId("");
	}
	
	public UserPec(String id, String address, String taxcode, String tenantId) {
		this.setId(id);
		this.setAddress(address);
		this.setTaxcode(taxcode);
		this.setTenantId(tenantId);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTaxcode() {
		return taxcode;
	}

	public void setTaxcode(String taxcode) {
		this.taxcode = taxcode;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

}
