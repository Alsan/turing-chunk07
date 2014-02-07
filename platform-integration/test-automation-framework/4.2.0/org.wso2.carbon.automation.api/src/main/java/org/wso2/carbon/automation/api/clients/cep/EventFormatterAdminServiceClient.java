package org.wso2.carbon.automation.api.clients.cep;


import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.automation.api.clients.utils.AuthenticateStub;
import org.wso2.carbon.event.formatter.stub.EventFormatterAdminServiceStub;
import org.wso2.carbon.event.formatter.stub.types.EventFormatterConfigurationDto;
import org.wso2.carbon.event.formatter.stub.types.EventFormatterConfigurationInfoDto;
import org.wso2.carbon.event.formatter.stub.types.EventFormatterPropertyDto;
import org.wso2.carbon.event.formatter.stub.types.EventOutputPropertyConfigurationDto;

import java.rmi.RemoteException;

public class EventFormatterAdminServiceClient {
    private static final Log log = LogFactory.getLog(EventFormatterAdminServiceClient.class);
    private final String serviceName = "EventFormatterAdminService";
    private EventFormatterAdminServiceStub eventFormatterAdminServiceStub;
    private String endPoint;

    public EventFormatterAdminServiceClient(String backEndUrl, String sessionCookie) throws
                                                                                     AxisFault {
        this.endPoint = backEndUrl + serviceName;
        eventFormatterAdminServiceStub = new EventFormatterAdminServiceStub(endPoint);
        AuthenticateStub.authenticateStub(sessionCookie, eventFormatterAdminServiceStub);

    }

    public EventFormatterAdminServiceClient(String backEndUrl, String userName, String password)
            throws AxisFault {
        this.endPoint = backEndUrl + serviceName;
        eventFormatterAdminServiceStub = new EventFormatterAdminServiceStub(endPoint);
        AuthenticateStub.authenticateStub(userName, password, eventFormatterAdminServiceStub);
    }

    public ServiceClient _getServiceClient() {
        return eventFormatterAdminServiceStub._getServiceClient();
    }

    public int getActiveEventFormatterCount()
            throws RemoteException {
        try {
            EventFormatterConfigurationInfoDto[] configs = eventFormatterAdminServiceStub.getAllActiveEventFormatterConfiguration();
            if (configs == null) {
                return 0;
            } else {
                return configs.length;
            }
        } catch (RemoteException e) {
            throw new RemoteException("RemoteException", e);
        }
    }


    public void addWso2EventFormatterConfiguration(String eventFormatterName, String streamId, String transportAdaptorName,
                                                   EventOutputPropertyConfigurationDto[] metaData, EventOutputPropertyConfigurationDto[] correlationData,
                                                   EventOutputPropertyConfigurationDto[] payloadData, EventFormatterPropertyDto[] eventFormatterPropertyDtos)
            throws RemoteException {
        try {
            eventFormatterAdminServiceStub.deployWSO2EventFormatterConfiguration(eventFormatterName, streamId, transportAdaptorName, metaData, correlationData, payloadData, eventFormatterPropertyDtos);
        } catch (RemoteException e) {
            log.error("RemoteException", e);
            throw new RemoteException();
        }
    }

    public void addXMLEventFormatterConfiguration(String eventFormatterName,
                                                  String streamNameWithVersion,
                                                  String transportAdaptorName,
                                                  String textData,
                                                  EventFormatterPropertyDto[] outputPropertyConfiguration,
                                                  String dataFrom)
            throws RemoteException {
        try {
            eventFormatterAdminServiceStub.deployXmlEventFormatterConfiguration(eventFormatterName, streamNameWithVersion, transportAdaptorName, textData, outputPropertyConfiguration,dataFrom);
        } catch (RemoteException e) {
            log.error("RemoteException", e);
            throw new RemoteException();
        }
    }

    public void removeEventFormatterConfiguration(String eventBuilderName)
            throws RemoteException {
        try {
            eventFormatterAdminServiceStub.undeployActiveEventFormatterConfiguration(eventBuilderName);
        } catch (RemoteException e) {
            log.error("RemoteException", e);
            throw new RemoteException();
        }
    }

    public EventFormatterConfigurationDto getEventFormatterConfiguration(String eventBuilderConfiguration)
            throws RemoteException {
        try {
            return eventFormatterAdminServiceStub.getActiveEventFormatterConfiguration(eventBuilderConfiguration);
        } catch (RemoteException e) {
            log.error("RemoteException", e);
            throw new RemoteException();
        }
    }
}