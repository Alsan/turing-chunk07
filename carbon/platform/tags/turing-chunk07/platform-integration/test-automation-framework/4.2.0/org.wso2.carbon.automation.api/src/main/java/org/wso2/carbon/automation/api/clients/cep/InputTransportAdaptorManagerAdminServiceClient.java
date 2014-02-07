package org.wso2.carbon.automation.api.clients.cep;


import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.automation.api.clients.utils.AuthenticateStub;
import org.wso2.carbon.input.transport.adaptor.manager.stub.InputTransportAdaptorManagerAdminServiceStub;
import org.wso2.carbon.input.transport.adaptor.manager.stub.types.InputTransportAdaptorConfigurationInfoDto;
import org.wso2.carbon.input.transport.adaptor.manager.stub.types.InputTransportAdaptorPropertiesDto;
import org.wso2.carbon.input.transport.adaptor.manager.stub.types.InputTransportAdaptorPropertyDto;

import java.rmi.RemoteException;

public class InputTransportAdaptorManagerAdminServiceClient {
    private static final Log log = LogFactory.getLog(InputTransportAdaptorManagerAdminServiceClient.class);
    private final String serviceName = "InputTransportAdaptorManagerAdminService";
    private InputTransportAdaptorManagerAdminServiceStub inputTransportAdaptorManagerAdminServiceStub;
    private String endPoint;

    public InputTransportAdaptorManagerAdminServiceClient(String backEndUrl, String sessionCookie)
            throws AxisFault {
        this.endPoint = backEndUrl + serviceName;
        inputTransportAdaptorManagerAdminServiceStub = new InputTransportAdaptorManagerAdminServiceStub(endPoint);
        AuthenticateStub.authenticateStub(sessionCookie, inputTransportAdaptorManagerAdminServiceStub);

    }

    public InputTransportAdaptorManagerAdminServiceClient(String backEndUrl, String userName, String password)
            throws AxisFault {
        this.endPoint = backEndUrl + serviceName;
        inputTransportAdaptorManagerAdminServiceStub = new InputTransportAdaptorManagerAdminServiceStub(endPoint);
        AuthenticateStub.authenticateStub(userName, password, inputTransportAdaptorManagerAdminServiceStub);

    }

    public ServiceClient _getServiceClient() {
        return inputTransportAdaptorManagerAdminServiceStub._getServiceClient();
    }

    public String[] getAllInputTransportAdaptorNames() throws RemoteException {
        String[] inputTransportAdaptorNames = null;
        try {
            InputTransportAdaptorConfigurationInfoDto[] inputTransportAdaptorConfigurationInfoDtos = inputTransportAdaptorManagerAdminServiceStub.getAllActiveInputTransportAdaptorConfiguration();
            inputTransportAdaptorNames = new String[inputTransportAdaptorConfigurationInfoDtos.length];
            for (int i = 0; i < inputTransportAdaptorConfigurationInfoDtos.length; i++) {
                inputTransportAdaptorNames[i] = inputTransportAdaptorConfigurationInfoDtos[i].getTransportAdaptorName();
            }
        } catch (RemoteException e) {
            log.error("RemoteException", e);
            throw new RemoteException("RemoteException", e);

        }
        return inputTransportAdaptorNames;
    }

    public InputTransportAdaptorPropertiesDto getInputTransportAdaptorProperties(String brokerName) throws RemoteException {
        try {
            return inputTransportAdaptorManagerAdminServiceStub.getActiveInputTransportAdaptorConfiguration(brokerName);
        } catch (RemoteException e) {
            log.error("RemoteException", e);
            throw new RemoteException();
        }
    }

    public int getActiveInputTransportAdaptorConfigurationCount()
            throws RemoteException {
        try {
            InputTransportAdaptorConfigurationInfoDto[] configs = inputTransportAdaptorManagerAdminServiceStub.getAllActiveInputTransportAdaptorConfiguration();
            if (configs == null) {
                return 0;
            } else {
                return configs.length;
            }
        } catch (RemoteException e) {
            throw new RemoteException("RemoteException", e);
        }
    }

    public InputTransportAdaptorConfigurationInfoDto[] getActiveInputTransportAdaptorConfigurations()
            throws RemoteException {
        try {
            return inputTransportAdaptorManagerAdminServiceStub.getAllActiveInputTransportAdaptorConfiguration();
        } catch (RemoteException e) {
            throw new RemoteException("RemoteException", e);
        }
    }

    public void addInputTransportAdaptorConfiguration(String transportAdaptorName, String brokerType,
                                                      InputTransportAdaptorPropertyDto[] brokerProperty) throws RemoteException {
        try {
            inputTransportAdaptorManagerAdminServiceStub.deployInputTransportAdaptorConfiguration(transportAdaptorName, brokerType, brokerProperty);
        } catch (RemoteException e) {
            log.error("RemoteException", e);
            throw new RemoteException();
        }
    }

    public void removeInputTransportAdaptorConfiguration(String brokerName) throws RemoteException {
        try {
            inputTransportAdaptorManagerAdminServiceStub.undeployActiveInputTransportAdaptorConfiguration(brokerName);
        } catch (RemoteException e) {
            log.error("RemoteException", e);
            throw new RemoteException();
        }
    }
}
