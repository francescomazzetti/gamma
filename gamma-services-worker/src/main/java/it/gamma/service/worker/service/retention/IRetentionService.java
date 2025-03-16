package it.gamma.service.worker.service.retention;

import org.slf4j.Logger;

public interface IRetentionService
{
	boolean save(byte[] retentionFile, String extension, String tenantId, String owner, Logger log);
}
