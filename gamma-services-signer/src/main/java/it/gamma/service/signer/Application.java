package it.gamma.service.signer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"it.gamma.service.signer"
	}
)
public class Application
{
	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(Application.class);
		new SpringApplication(Application.class).run(args);
		logger.debug("LOG DEBUG ENABLED");
		logger.info("APPLICATION " + IConstants.APPLICATION_NAME + " STARTED");
	}
	
}
