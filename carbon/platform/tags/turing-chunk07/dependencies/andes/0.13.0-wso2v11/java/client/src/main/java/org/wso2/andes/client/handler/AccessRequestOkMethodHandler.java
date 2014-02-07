package org.wso2.andes.client.handler;
/*
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.andes.AMQException;
import org.wso2.andes.client.protocol.AMQProtocolSession;
import org.wso2.andes.client.state.StateAwareMethodListener;
import org.wso2.andes.framing.AccessRequestOkBody;

public class AccessRequestOkMethodHandler implements StateAwareMethodListener<AccessRequestOkBody>
{
    private static final Logger _logger = LoggerFactory.getLogger(AccessRequestOkMethodHandler.class);

    private static AccessRequestOkMethodHandler _handler = new AccessRequestOkMethodHandler();

    public static AccessRequestOkMethodHandler getInstance()
    {
        return _handler;
    }

    public void methodReceived(AMQProtocolSession session, AccessRequestOkBody method, int channelId)
        throws AMQException
    {
        _logger.debug("AccessRequestOk method received");
        session.setTicket(method.getTicket(), channelId);

    }
}
