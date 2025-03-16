package it.gamma.service.worker.service.retention;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;

public class MockRetentionService implements IRetentionService
{

	public boolean save(byte[] retentionFile, String extension, String tenantId, String owner, Logger log) {
		String fileName = owner + "-" + new Date().getTime() + "."+extension;
		File folder = new File("./target/"+tenantId);
		if (!folder.exists()) {
			folder.mkdir();
		}
		try (FileOutputStream outputStream = new FileOutputStream("./target/"+tenantId+"/"+fileName)) {
		    outputStream.write(retentionFile);
		    outputStream.flush();
		    outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
