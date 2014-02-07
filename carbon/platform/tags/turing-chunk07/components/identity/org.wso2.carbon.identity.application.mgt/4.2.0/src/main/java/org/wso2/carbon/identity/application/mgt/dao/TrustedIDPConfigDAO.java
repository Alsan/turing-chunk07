package org.wso2.carbon.identity.application.mgt.dao;

import org.wso2.carbon.identity.application.mgt.dto.TrustedIDPConfigDTO;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.idp.mgt.IdentityProviderMgtService;
import org.wso2.carbon.idp.mgt.dto.TrustedIdPDTO;
import org.wso2.carbon.idp.mgt.exception.IdentityProviderMgtException;

public class TrustedIDPConfigDAO {

	public TrustedIDPConfigDTO getIDPData(String idpID) throws IdentityException {
		IdentityProviderMgtService idpmgtService = new IdentityProviderMgtService();
		try {
			TrustedIdPDTO idp = idpmgtService.getTenantIdP(idpID);

			TrustedIDPConfigDTO dto = new TrustedIDPConfigDTO();
			dto.setEndpoints(idp.getIdPUrl());
			dto.setIdpIdentifier(idp.getIdPName());
			dto.setTypes("DEFAULT");

			return dto;

		} catch (IdentityProviderMgtException e) {
			throw new IdentityException("Error while reading IDP info", e);
		}
	}

	public TrustedIDPConfigDTO[] getAllIDPData() throws IdentityException {
		IdentityProviderMgtService idpmgtService = new IdentityProviderMgtService();
		try {

			String[] idps = idpmgtService.getTenantIdPs();
			if (idps != null) {
				TrustedIDPConfigDTO[] idpDTOs = new TrustedIDPConfigDTO[idps.length];
				int i = 0;
				for (String idp : idps) {
					idpDTOs[i] = getIDPData(idp);
					i++;
				}
				return idpDTOs;
			}
		} catch (IdentityProviderMgtException e) {
			throw new IdentityException("Error while reading IDP info", e);
		}
		return null;
	}

}
