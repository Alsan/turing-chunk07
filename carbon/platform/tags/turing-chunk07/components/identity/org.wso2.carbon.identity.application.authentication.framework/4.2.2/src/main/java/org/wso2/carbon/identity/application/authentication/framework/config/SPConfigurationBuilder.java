package org.wso2.carbon.identity.application.authentication.framework.config;

public class SPConfigurationBuilder {
	
	private static volatile SPConfigurationBuilder instance;
	
	public static SPConfigurationBuilder getInstance() {
		if (instance == null) {
			synchronized (ConfigurationFacade.class) {
				if (instance == null) {
					instance = new SPConfigurationBuilder();
				}
			}
		}
		
		return instance;
	}
	
	public SequenceConfig getConfiguration(String reqType, String sp) {
		SequenceConfig sequenceConfig = null;
		return sequenceConfig;
	}
}
