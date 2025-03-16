package it.gamma.service.signer.configuration;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import it.gamma.service.signer.account.IUserAccountRetriever;
import it.gamma.service.signer.account.MockUserAccountRetriever;

@Configuration
@ConfigurationProperties("signer.user.account")
public class UserAccountConfiguration
{
	private static Map<String, IUserAccountRetriever> userRetrieverMap;
	private String retriever;
	
	static {
		userRetrieverMap = new HashMap<String, IUserAccountRetriever>();
		userRetrieverMap.put("mock", new MockUserAccountRetriever());
	}

	public String getRetriever() {
		return retriever;
	}

	public void setRetriever(String retriever) {
		this.retriever = retriever;
	}
	
	@Bean(name="signer.userAccountRetriever")
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public IUserAccountRetriever userAccountRetriever() {
		return userRetrieverMap.get(retriever);
	}
}
