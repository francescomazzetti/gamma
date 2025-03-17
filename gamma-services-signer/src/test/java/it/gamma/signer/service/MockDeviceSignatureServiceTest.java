package it.gamma.signer.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import it.gamma.service.signer.account.UserAccount;
import it.gamma.service.signer.service.MockDeviceSignatureService;

public class MockDeviceSignatureServiceTest
{
	@Test
	public void test1() throws Exception {
		String signed = new MockDeviceSignatureService().sign("xxxxxxxxxxxxx", new UserAccount("mzzfnc84a28h501c", "device"));
		assertTrue(signed.length() > 0);
	}
}
