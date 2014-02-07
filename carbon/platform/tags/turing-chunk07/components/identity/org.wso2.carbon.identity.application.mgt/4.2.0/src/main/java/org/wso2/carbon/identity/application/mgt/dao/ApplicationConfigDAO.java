package org.wso2.carbon.identity.application.mgt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.application.mgt.DBQueries;
import org.wso2.carbon.identity.application.mgt.dto.ApplicationConfigDTO;
import org.wso2.carbon.identity.application.mgt.dto.AuthenticationStepConfigDTO;
import org.wso2.carbon.identity.application.mgt.dto.AuthenticatorConfigDTO;
import org.wso2.carbon.identity.application.mgt.dto.ClientConfigDTO;
import org.wso2.carbon.identity.application.mgt.dto.TrustedIDPConfigDTO;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.core.persistence.JDBCPersistenceManager;

public class ApplicationConfigDAO {

    // TODO : requires serialization

    public void StoreApplicationData(ApplicationConfigDTO appConfigDTO) throws IdentityException {
        // basic info
        int tenantID = CarbonContext.getThreadLocalCarbonContext().getTenantId();
        String username = CarbonContext.getThreadLocalCarbonContext().getUsername();
        String appID = appConfigDTO.getApplicatIdentifier();

        try {
            // store app basic info
            // APP_ID, TENANT_ID, USERNAME
            Connection connection = JDBCPersistenceManager.getInstance().getDBConnection();
            PreparedStatement storeAppPrepStmt = connection.prepareStatement(DBQueries.STORE_BASIC_APPINFO);
            storeAppPrepStmt.setString(1, appID);
            storeAppPrepStmt.setInt(2, tenantID);
            storeAppPrepStmt.setString(3, username);
            storeAppPrepStmt.execute();
            connection.commit();
            storeAppPrepStmt.close();

            // client info
            ClientConfigDTO[] clientConf = appConfigDTO.getClientConfig();
            if (clientConf != null && clientConf.length > 0) {
                for (ClientConfigDTO dto : clientConf) {
                    String clientid = dto.getClientID();
                    String clientSecrete = dto.getClientSecrete();
                    String callbackUrl = dto.getCallbackUrl();
                    String clientType = dto.getType();

                    // store client info
                    // APP_ID, CLIENT_ID, CLIENT_SECRETE, CALLBACK_URL, TYPE
                    PreparedStatement storeClientPrepStmt = connection.prepareStatement(DBQueries.STORE_CLIENT_INFO);
                    storeClientPrepStmt.setString(1, appID);
                    storeClientPrepStmt.setString(2, clientid);
                    storeClientPrepStmt.setString(3, clientSecrete);
                    storeClientPrepStmt.setString(4, callbackUrl);
                    storeClientPrepStmt.setString(5, clientType);
                    storeClientPrepStmt.execute();
                    connection.commit();
                    storeClientPrepStmt.close();
                }
            }

            // IDP info
            AuthenticationStepConfigDTO[] stepDTOs = appConfigDTO.getAuthenticationSteps();
            if (stepDTOs != null && stepDTOs.length > 0) {

                for (AuthenticationStepConfigDTO stepDTO : stepDTOs) {
                    String stepID = stepDTO.getStepIdentifier();
                    AuthenticatorConfigDTO[] authnDTOs = stepDTO.getAuthenticators();
                    if (authnDTOs != null && authnDTOs.length > 0) {

                        for (AuthenticatorConfigDTO authnDTO : authnDTOs) {
                            String authenID = authnDTO.getAuthnticatorIdentifier();
                            TrustedIDPConfigDTO[] idpDTOs = authnDTO.getIdps();
                            if (idpDTOs != null && idpDTOs.length > 0) {

                                for (TrustedIDPConfigDTO idpDTO : idpDTOs) {

                                    String idpID = idpDTO.getIdpIdentifier();
                                    String endpoints = idpDTO.getEndpointsString();
                                    String types = idpDTO.getTypesString();
                                    // store step info
                                    // APP_ID, STEP_ID, AUTHN_ID, IDP_ID, ENDPOINT, TYPE

                                    PreparedStatement storeStepPrepStmt = connection.prepareStatement(DBQueries.STORE_STEP_INFO);
                                    storeAppPrepStmt.setString(1, appID);
                                    storeAppPrepStmt.setString(2, stepID);
                                    storeAppPrepStmt.setString(3, authenID);
                                    storeAppPrepStmt.setString(4, idpID);
                                    storeAppPrepStmt.setString(5, endpoints);
                                    storeAppPrepStmt.setString(6, types);

                                    storeStepPrepStmt.execute();
                                    connection.commit();

                                    storeStepPrepStmt.close();
                                    connection.commit();
                                }
                            }

                        }
                    }

                }

            }
        } catch (SQLException e) {
            throw new IdentityException("Error while storing application");
        }

    }

    public ApplicationConfigDTO getApplicationData(String applicationID) throws IdentityException{

        ApplicationConfigDTO appConfigDTO = new ApplicationConfigDTO();
        appConfigDTO.setApplicatIdentifier(applicationID);
        ClientConfigDTO[] clientConfig;
        ArrayList<ClientConfigDTO> clientConfigList = new ArrayList<ClientConfigDTO>();
        AuthenticationStepConfigDTO[] authenticationStep;
        ArrayList<AuthenticationStepConfigDTO> authenticationStepList = new ArrayList<AuthenticationStepConfigDTO>();
        Connection connection = null;

        try {

             connection = JDBCPersistenceManager.getInstance().getDBConnection();

            PreparedStatement getClientInfo = connection.prepareStatement(DBQueries.GET_CLIENT_INFO);
            getClientInfo.setString(1, applicationID);
            ResultSet resultSet = getClientInfo.executeQuery();

            if (resultSet.next()) {
                ClientConfigDTO clientConfigDTO = new ClientConfigDTO();
                clientConfigDTO.setClientID(resultSet.getString(1));
                clientConfigDTO.setClientSecrete(resultSet.getString(2));
                clientConfigDTO.setCallbackUrl(resultSet.getString(3));
                clientConfigDTO.setType(resultSet.getString(4));
                clientConfigList.add(clientConfigDTO);

            }

            getClientInfo.close();
            clientConfig = clientConfigList.toArray(new ClientConfigDTO[clientConfigList.size()]);
            appConfigDTO.setClientConfig(clientConfig);

            PreparedStatement getStepInfo = connection.prepareStatement(DBQueries.GET_STEP_INFO);
            getStepInfo.setString(1, applicationID);
            ResultSet stepInfoResultSet = getStepInfo.executeQuery();


            HashMap<String, HashMap<String, ArrayList<TrustedIDPConfigDTO>>> stepInfoMap = new HashMap<String, HashMap<String, ArrayList<TrustedIDPConfigDTO>>>();
            if (stepInfoResultSet.next()) {
                String stepID = stepInfoResultSet.getString(1);

                if (stepInfoMap.containsKey(stepID)) {
                    String authID = stepInfoResultSet.getString(2);
                    HashMap<String, ArrayList<TrustedIDPConfigDTO>> authMap = stepInfoMap.get(stepID);
                    if (authMap.containsKey(authID)) {
                        ArrayList<TrustedIDPConfigDTO> trustedIDPConfigList = authMap.get(authID);
                        TrustedIDPConfigDTO trustedIDP = new TrustedIDPConfigDTO();
                        trustedIDP.setIdpIdentifier(stepInfoResultSet.getString(3));
                        trustedIDP.setEndpoints(stepInfoResultSet.getString(4));
                        trustedIDP.setTypes(stepInfoResultSet.getString(5));
                        trustedIDPConfigList.add(trustedIDP);

                    } else {
                        ArrayList<TrustedIDPConfigDTO> trustedIDPConfigList = new ArrayList<TrustedIDPConfigDTO>();
                        TrustedIDPConfigDTO trustedIDP = new TrustedIDPConfigDTO();
                        trustedIDP.setIdpIdentifier(stepInfoResultSet.getString(3));
                        trustedIDP.setEndpoints(stepInfoResultSet.getString(4));
                        trustedIDP.setTypes(stepInfoResultSet.getString(5));
                        trustedIDPConfigList.add(trustedIDP);
                        authMap.put(authID, trustedIDPConfigList);
                    }

                } else {
                    String authID = stepInfoResultSet.getString(2);
                    HashMap<String, ArrayList<TrustedIDPConfigDTO>> authMap = new HashMap<String, ArrayList<TrustedIDPConfigDTO>>();
                    ArrayList<TrustedIDPConfigDTO> tempIDPList = new ArrayList<TrustedIDPConfigDTO>();
                    authMap.put(authID, tempIDPList);
                    TrustedIDPConfigDTO trustedIDP = new TrustedIDPConfigDTO();
                    trustedIDP.setIdpIdentifier(stepInfoResultSet.getString(3));
                    trustedIDP.setEndpoints(stepInfoResultSet.getString(4));
                    trustedIDP.setTypes(stepInfoResultSet.getString(5));
                    tempIDPList.add(trustedIDP);
                    stepInfoMap.put(stepID, authMap);
                }
            }


            for (String key : stepInfoMap.keySet()) {
                AuthenticationStepConfigDTO authConfigDTO = new AuthenticationStepConfigDTO();
                authConfigDTO.setStepIdentifier(key);
                ArrayList<AuthenticatorConfigDTO> authConfigList = new ArrayList<AuthenticatorConfigDTO>();

                HashMap<String, ArrayList<TrustedIDPConfigDTO>> authMap = stepInfoMap.get(key);
                for (String authKey : authMap.keySet()) {
                    AuthenticatorConfigDTO authConfig = new AuthenticatorConfigDTO();
                    authConfig.setAuthnticatorIdentifier(authKey);
                    ArrayList<TrustedIDPConfigDTO> tempTrustedList = authMap.get(authKey);
                    authConfig.setIdps(tempTrustedList.toArray(new TrustedIDPConfigDTO[tempTrustedList.size()]));
                    authConfigList.add(authConfig);

                }
                authConfigDTO.setAuthenticators(authConfigList.toArray(new AuthenticatorConfigDTO[authConfigList.size()]));
                authenticationStepList.add(authConfigDTO);

            }
            authenticationStep = authenticationStepList.toArray(new AuthenticationStepConfigDTO[authenticationStepList.size()]);
            stepInfoResultSet.close();
            connection.close();
            appConfigDTO.setAuthenticationSteps(authenticationStep);


        } catch (SQLException e) {
            throw new IdentityException("Error while retrieving application");
        }

        return appConfigDTO;
    }

    public ApplicationConfigDTO[] getAllApplicationData() throws IdentityException{

        Connection connection = null;
        ArrayList<ApplicationConfigDTO> appConfigList = new ArrayList<ApplicationConfigDTO>();
        ArrayList<String> appIds = new ArrayList<String>();
        int tenantID = CarbonContext.getThreadLocalCarbonContext().getTenantId();
        try {
            connection = JDBCPersistenceManager.getInstance().getDBConnection();
            PreparedStatement getClientInfo = connection.prepareStatement(DBQueries.GET_ALL_APP);
            getClientInfo.setInt(1, tenantID);
            ResultSet resultSet = getClientInfo.executeQuery();
            if (resultSet.next()) {
               appIds.add(resultSet.getString(1));
            }
            resultSet.close();
            connection.close();
        } catch (SQLException e) {
            throw new IdentityException("Error while retrieving all application");
        }

        for(String appId : appIds){
            appConfigList.add(getApplicationData(appId));
        }

        return appConfigList.toArray(new ApplicationConfigDTO[appConfigList.size()]);
    }


}
