package it.gamma.service.idp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.junit.jupiter.api.Test;

import jakarta.xml.bind.DatatypeConverter;

public class SpikeTest
{
	@Test
	public void test1() throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String hashValue = Base64.getEncoder().encodeToString(digest.digest("Password1".getBytes()));
        System.err.println(hashValue);
	}
}
