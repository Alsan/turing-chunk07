package org.wso2.carbon.identity.application.authentication.framework.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfigurationFacade {
	
	private static Log log = LogFactory.getLog(ConfigurationFacade.class);
	
	private static volatile ConfigurationFacade instance;
	
	public ConfigurationFacade() {
		//Read the default config from the files
		FileBasedConfigurationBuilder.getInstance().build();
	}
	
	public static ConfigurationFacade getInstance() {
		
		if (instance == null) {
			synchronized (ConfigurationFacade.class) {
				
				if (instance == null) {
					instance = new ConfigurationFacade();
				}
			}
		}
		
		return instance;
	}
	
	public SequenceConfig getSequenceConfig(String reqType, String relyingParty) {
		
		SequenceConfig sequenceConfig = null;
		
		if (relyingParty != null) {
			//Get SP config from SP Management component
			//Get IdPs from IdP Management component
			sequenceConfig = SPConfigurationBuilder.getInstance().getConfiguration(reqType, relyingParty);
			
			if (sequenceConfig == null) {
				sequenceConfig = FileBasedConfigurationBuilder.getInstance().findSequenceByRelyingParty(relyingParty);
			}
		} 
		
		if (sequenceConfig == null) { 
			//load default config from file
			sequenceConfig = FileBasedConfigurationBuilder.getInstance().findSequenceByRelyingParty("default");
		}
		
		return sequenceConfig;
	}
	
	public ExternalIdPConfig getIdPConfig(int step, String authenticator, String idpName) {
		ExternalIdPConfig idp = null;
		
		idp = FileBasedConfigurationBuilder.getInstance().getIdPConfigs(idpName);
		
		return idp;
	}
}
