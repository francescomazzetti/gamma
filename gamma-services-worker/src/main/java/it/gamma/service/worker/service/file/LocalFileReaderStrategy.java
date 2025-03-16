package it.gamma.service.worker.service.file;

import java.io.File;
import java.nio.file.Files;
import org.springframework.util.ResourceUtils;

public class LocalFileReaderStrategy implements IFileReaderStrategy
{
	public byte[] read(String path, String fileName) throws Exception {
		File file = ResourceUtils.getFile(path+fileName);
		byte[] bytes = Files.readAllBytes(file.toPath());
		return bytes;
	}

}
