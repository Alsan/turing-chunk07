package org.wso2.carbon.appfactory.jenkinsext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class AppfactoryConfigurationDeployer {
	protected void copyBundledConfigs(String src, String dest) throws IOException {
		 
		 String configPath = src + File.separator + JenkinsTenantConstants.COMMON_CONFIGS_DIR;
		 File source = new File(configPath);
		 
		 if(!source.exists()){
			 throw new IllegalArgumentException("Common plugin location cannot be found at " + configPath );
		 }
		 
		 File destination = new File(dest, JenkinsTenantConstants.COMMON_CONFIG_DESTINATION);
		 
		 //Copy if doesnot exists
		 String files[] = source.list();
		 for (String file : files) {
			 File srcFile = new File(src, file);
			 File destFile;
			 try{
				 destFile = new File(destination, file);
				 if(!destFile.exists()){
					 destFile.createNewFile();
					 FileUtils.copyFile(srcFile, destFile);
				 }   
			 }catch(FileNotFoundException e){}
		 }
	 }
}
