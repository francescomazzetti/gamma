package it.gamma.service.signer.service;

import it.gamma.service.signer.account.UserAccount;

public interface ISignatureService
{
	public String sign(String value, UserAccount userAccount) throws Exception;
}
