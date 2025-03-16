package it.gamma.service.worker.service.file;

public interface IFileReaderStrategy
{

	byte[] read(String origin, String fileName) throws Exception;

}
