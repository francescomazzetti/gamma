package it.gamma.service.pec.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UserMessage
{
	@Id
	private String id;
	private String address;
	@Indexed(unique = false)
	private String taxcode;
	
	public UserMessage(String id, String address, String taxcode) {
		this.setId(id);
		this.setAddress(address);
		this.setTaxcode(taxcode);
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

}
