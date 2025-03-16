package it.gamma.service.worker.configuration;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import it.gamma.service.worker.service.retention.IRetentionService;
import it.gamma.service.worker.service.retention.MockRetentionService;
import it.gamma.service.worker.service.retention.RemoteFSRetentionService;

@Configuration
@ConfigurationProperties(prefix="gamma.worker")
public class WorkerDataConfiguration {
	
	private String instance;
	private Long queryMaxResults;
	private String retentionStrategy;

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public Long getQueryMaxResults() {
		return queryMaxResults;
	}

	public void setQueryMaxResults(Long queryMaxResults) {
		this.queryMaxResults = queryMaxResults;
	}
	
	public String getRetentionStrategy() {
		return retentionStrategy;
	}

	public void setRetentionStrategy(String retentionStrategy) {
		this.retentionStrategy = retentionStrategy;
	}
	
	@Bean(name="retention.retentionService")
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public IRetentionService retentionService() {
		if ("mock".equals(retentionStrategy)) {
			return new MockRetentionService();
		}
		return new RemoteFSRetentionService();
	}

}
