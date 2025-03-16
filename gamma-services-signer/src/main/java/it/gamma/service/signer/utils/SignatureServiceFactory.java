package it.gamma.service.signer.utils;

import java.util.HashMap;
import java.util.Map;

import it.gamma.service.signer.IConstants;
import it.gamma.service.signer.service.ISignatureService;
import it.gamma.service.signer.service.MockDeviceSignatureService;

public class SignatureServiceFactory
{
	private static Map<String, ISignatureService> _signatureServicesMap;
	
	static {
		_signatureServicesMap = new HashMap<String, ISignatureService>();
		_signatureServicesMap.put(IConstants.SIGNATURE_SERVICE_DEVICE, new MockDeviceSignatureService());
	}

	public static ISignatureService build(String signInstrument) {
		return _signatureServicesMap.get(signInstrument);
	}
}
