package it.gamma.service.signer.ol;

import java.util.Date;
import java.util.UUID;

import it.gamma.service.signer.mongo.model.OfficialLogData;
import it.gamma.service.signer.mongo.respository.OfficialLogDataRepository;
import jakarta.servlet.http.HttpServletRequest;

public class OfficialLogWriter
{
	private static final String SYSTEM = "gamma-idp";
	private OfficialLogDataRepository _officialLogDataRepository;

	public OfficialLogWriter(OfficialLogDataRepository officialLogDataRepository) {
		_officialLogDataRepository = officialLogDataRepository;
	}

	public void sign(HttpServletRequest request, String sid, String username, boolean valid) {
		_officialLogDataRepository.save(
				new OfficialLogData(
						UUID.randomUUID().toString()+sid,
						request.getRemoteAddr(),
						username,
						new Date().getTime()+"",
						"sign",
						"firma documento",
						"firma documento effettuata con successo",
						sid,
						SYSTEM
						)
				);
	}

}
