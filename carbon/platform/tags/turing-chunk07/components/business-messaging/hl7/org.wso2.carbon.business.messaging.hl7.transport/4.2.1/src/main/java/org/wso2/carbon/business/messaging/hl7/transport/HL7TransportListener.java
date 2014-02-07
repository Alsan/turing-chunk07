/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.wso2.carbon.business.messaging.hl7.transport;

import ca.uhn.hl7v2.app.Application;
import ca.uhn.hl7v2.app.SimpleServer;
import ca.uhn.hl7v2.llp.LowerLayerProtocol;

import org.apache.axis2.AxisFault;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.transport.base.AbstractTransportListenerEx;
import org.wso2.carbon.business.messaging.hl7.common.HL7Constants;
import org.wso2.carbon.business.messaging.hl7.transport.utils.HL7MessageProcessor;
import org.wso2.carbon.business.messaging.hl7.transport.utils.WorkerThreadFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class HL7TransportListener extends AbstractTransportListenerEx<HL7Endpoint> {
	
	//default timeout value to wait for backend response
	private  int timeOutVal = 1000;



    private Map<HL7Endpoint, SimpleServer> serverTable = new HashMap<HL7Endpoint, SimpleServer>();
    private ExecutorService executorService;

    @Override
	protected void doInit() throws AxisFault {
		Parameter transTimeOutParam = getTransportInDescription().getParameter(HL7Constants.HL7_TRANSPORT_TIMEOUT);
		if (transTimeOutParam != null) {
			timeOutVal = Integer.parseInt(transTimeOutParam.getValue().toString());
		}

		log.info("HL7 Transport Receiver initialized.");
	}

    @Override
    protected HL7Endpoint createEndpoint() {
        return new HL7Endpoint(timeOutVal);
    }

    @Override
    protected void startEndpoint(HL7Endpoint endpoint) throws AxisFault {

        executorService = new ThreadPoolExecutor(
                endpoint.getCorePoolSize(),
                endpoint.getMaxPoolSize(),
                endpoint.getIdleThreadKeepAlive(),
                TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>(),
                new WorkerThreadFactory("HL7Transport-WORKER"),
                new ThreadPoolExecutor.AbortPolicy());

        LowerLayerProtocol llp = LowerLayerProtocol.makeLLP();
        SimpleServer server = new SimpleServer(
                endpoint.getPort(),
                llp,
        		endpoint.getProcessingContext().getPipeParser(),
                false,
                executorService);
        Application callback = new HL7MessageProcessor(endpoint);
        server.registerApplication("*", "*", callback);

        server.start();
        serverTable.put(endpoint, server);

        log.info("Started HL7 endpoint on port: " + endpoint.getPort());
    }

    @Override
    protected void stopEndpoint(HL7Endpoint endpoint) {
        SimpleServer server = serverTable.remove(endpoint);
        if (server != null) {
            server.stop();
        }

        //Adding a delay to the server stop. This is to give some time for the socket to properly close.
        //See https://wso2.org/jira/browse/ESBJAVA-955
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.info("InterruptedException: SimpleServer stop delay interrupted");
        }

        log.info("Stopped HL7 endpoint on port: " + endpoint.getPort());
    }
}
