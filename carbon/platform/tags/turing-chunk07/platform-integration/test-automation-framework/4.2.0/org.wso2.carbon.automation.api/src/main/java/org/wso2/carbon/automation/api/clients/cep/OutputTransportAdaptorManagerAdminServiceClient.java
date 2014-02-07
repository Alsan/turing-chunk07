package org.wso2.carbon.automation.api.clients.cep;


import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.automation.api.clients.utils.AuthenticateStub;
import org.wso2.carbon.output.transport.adaptor.manager.stub.OutputTransportAdaptorManagerAdminServiceStub;
import org.wso2.carbon.output.transport.adaptor.manager.stub.types.OutputTransportAdaptorConfigurationInfoDto;
import org.wso2.carbon.output.transport.adaptor.manager.stub.types.OutputTransportAdaptorPropertiesDto;
import org.wso2.carbon.output.transport.adaptor.manager.stub.types.OutputTransportAdaptorPropertyDto;

import java.rmi.RemoteException;

public class OutputTransportAdaptorManagerAdminServiceClient {
    private static final Log log = LogFactory.getLog(OutputTransportAdaptorManagerAdminServiceClient.class);
    private final String serviceName = "OutputTransportAdaptorManagerAdminService";
    private OutputTransportAdaptorManagerAdminServiceStub outputTransportAdaptorManagerAdminServiceStub;
    private String endPoint;

    public OutputTransportAdaptorManagerAdminServiceClient(String backEndUrl, String sessionCookie)
            throws AxisFault {
        this.endPoint = backEndUrl + serviceName;
        outputTransportAdaptorManagerAdminServiceStub = new OutputTransportAdaptorManagerAdminServiceStub(endPoint);
        AuthenticateStub.authenticateStub(sessionCookie, outputTransportAdaptorManagerAdminServiceStub);

    }

    public OutputTransportAdaptorManagerAdminServiceClient(String backEndUrl, String userName, String password)
            throws AxisFault {
        this.endPoint = backEndUrl + serviceName;
        outputTransportAdaptorManagerAdminServiceStub = new OutputTransportAdaptorManagerAdminServiceStub(endPoint);
        AuthenticateStub.authenticateStub(userName, password, outputTransportAdaptorManagerAdminServiceStub);

    }

    public ServiceClient _getServiceClient() {
        return outputTransportAdaptorManagerAdminServiceStub._getServiceClient();
    }

    public String[] getAllOutputTransportAdaptorNames() throws RemoteException {
        String[] inputTransportAdaptorNames = null;
        try {
            OutputTransportAdaptorConfigurationInfoDto[] inputTransportAdaptorConfigurationInfoDtos = outputTransportAdaptorManagerAdminServiceStub.getAllActiveOutputTransportAdaptorConfiguration();
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

    public OutputTransportAdaptorPropertiesDto getOutputTransportAdaptorProperties(String brokerName) throws RemoteException {
        try {
            return outputTransportAdaptorManagerAdminServiceStub.getActiveOutputTransportAdaptorConfiguration(brokerName);
        } catch (RemoteException e) {
            log.error("RemoteException", e);
            throw new RemoteException();
        }
    }

    public int getActiveOutputTransportAdaptorConfigurationCount()
            throws RemoteException {
        try {
            OutputTransportAdaptorConfigurationInfoDto[] configs = outputTransportAdaptorManagerAdminServiceStub.getAllActiveOutputTransportAdaptorConfiguration();
            if (configs == null) {
                return 0;
            } else {
                return configs.length;
            }
        } catch (RemoteException e) {
            throw new RemoteException("RemoteException", e);
        }
    }

    public OutputTransportAdaptorConfigurationInfoDto[] getActiveOutputTransportAdaptorConfigurations()
            throws RemoteException {
        try {
            return outputTransportAdaptorManagerAdminServiceStub.getAllActiveOutputTransportAdaptorConfiguration();
        } catch (RemoteException e) {
            throw new RemoteException("RemoteException", e);
        }
    }

    public void addOutputTransportAdaptorConfiguration(String transportAdaptorName, String brokerType,
                                                       OutputTransportAdaptorPropertyDto[] brokerProperty) throws RemoteException {
        try {
            outputTransportAdaptorManagerAdminServiceStub.deployOutputTransportAdaptorConfiguration(transportAdaptorName, brokerType, brokerProperty);
        } catch (RemoteException e) {
            log.error("RemoteException", e);
            throw new RemoteException();
        }
    }

    public void removeOutputTransportAdaptorConfiguration(String brokerName) throws RemoteException {
        try {
            outputTransportAdaptorManagerAdminServiceStub.undeployActiveOutputTransportAdaptorConfiguration(brokerName);
        } catch (RemoteException e) {
            log.error("RemoteException", e);
            throw new RemoteException();
        }
    }
}
