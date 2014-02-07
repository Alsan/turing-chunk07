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
package org.wso2.andes.client.message;

import org.wso2.andes.client.AMQSession;


/**
 * This class contains everything needed to process a JMS message. It assembles the deliver body, the content header and
 * the content body/ies.
 *
 * Note that the actual work of creating a JMS message for the client code's use is done outside of the MINA dispatcher
 * thread in order to minimise the amount of work done in the MINA dispatcher thread.
 */
public abstract class UnprocessedMessage implements AMQSession.Dispatchable
{
    private final int _consumerTag;


    public UnprocessedMessage(int consumerTag)
    {
        _consumerTag = consumerTag;
    }


    abstract public long getDeliveryTag();


    public int getConsumerTag()
    {
        return _consumerTag;
    }

    public void dispatch(AMQSession ssn)
    {
        ssn.dispatch(this);
    }

}