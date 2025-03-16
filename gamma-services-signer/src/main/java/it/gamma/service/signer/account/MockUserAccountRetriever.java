package it.gamma.service.signer.account;

import java.util.HashMap;
import java.util.Map;

import it.gamma.service.signer.IConstants;

public class MockUserAccountRetriever implements IUserAccountRetriever
{
	private Map<String, UserAccount> accounts;
	
	public MockUserAccountRetriever() {
		accounts = new HashMap<String, UserAccount>();
		accounts.put("MZZFNC84A28H501C", new UserAccount("MZZFNC84A28H501C", IConstants.SIGNATURE_SERVICE_DEVICE));
	}
	
	public UserAccount retrieve(String taxcode) {
		return accounts.get(taxcode);
	}

}
