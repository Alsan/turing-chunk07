package org.wso2.carbon.identity.application.mgt.service;

import org.wso2.carbon.identity.application.mgt.dao.ApplicationConfigDAO;
import org.wso2.carbon.identity.application.mgt.dao.TrustedIDPConfigDAO;
import org.wso2.carbon.identity.application.mgt.dto.ApplicationConfigDTO;
import org.wso2.carbon.identity.application.mgt.dto.TrustedIDPConfigDTO;
import org.wso2.carbon.identity.base.IdentityException;

public class ApplicationManagementService {

	public void storeApplicationData(ApplicationConfigDTO appConfiDto) throws IdentityException {
		ApplicationConfigDAO configDAO = new ApplicationConfigDAO();
		configDAO.StoreApplicationData(appConfiDto);
	}

	public ApplicationConfigDTO getApplicationData(String applicationID) {
		ApplicationConfigDAO configDAO = new ApplicationConfigDAO();
		return configDAO.getApplicationData(applicationID);
	}

	public ApplicationConfigDTO[] getAllApplicationData() {
		ApplicationConfigDAO configDAO = new ApplicationConfigDAO();
		return configDAO.getAllApplicationData();
	}

	public TrustedIDPConfigDTO getIDPData(String idpID) throws IdentityException {
		TrustedIDPConfigDAO idpdao = new TrustedIDPConfigDAO();
		return idpdao.getIDPData(idpID);
	}

	public TrustedIDPConfigDTO[] getAllIDPData() throws IdentityException {
		TrustedIDPConfigDAO idpdao = new TrustedIDPConfigDAO();
		return idpdao.getAllIDPData();
	}

}
