package it.gamma.service.idp.web.metadata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class MetadataService
{
	private MetadataConfiguration _metadataConfiguration;

	@Autowired
	public MetadataService(MetadataConfiguration metadataConfiguration) {
		_metadataConfiguration = metadataConfiguration;
	}
	
	@Bean(name="metadata.readerService")
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public IMetadataReader metadataReader() {
		if ("mock".equals(_metadataConfiguration.getImplementation())) {
			return new MockMetadataReader();
		}
		FileMetadataReader fileMetadataReader = new FileMetadataReader(_metadataConfiguration.getPath());
		fileMetadataReader.init();
		return fileMetadataReader;
	}
}
