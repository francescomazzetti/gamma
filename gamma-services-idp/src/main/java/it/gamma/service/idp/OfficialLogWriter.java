package it.gamma.service.idp;

import java.util.Date;
import java.util.UUID;

import it.gamma.service.idp.mongo.model.OfficialLogData;
import it.gamma.service.idp.mongo.respositories.OfficialLogDataRepository;
import jakarta.servlet.http.HttpServletRequest;

public class OfficialLogWriter
{
	private OfficialLogDataRepository _officialLogDataRepository;

	public OfficialLogWriter(OfficialLogDataRepository officialLogDataRepository) {
		_officialLogDataRepository = officialLogDataRepository;
	}

	public void login(HttpServletRequest request, String sid, String username, boolean valid) {
		_officialLogDataRepository.save(
				new OfficialLogData(
						UUID.randomUUID().toString()+sid,
						request.getRemoteAddr(),
						username,
						new Date().getTime()+"",
						"login",
						"login utente",
						"login effettuata con successo")
				);
	}

}
