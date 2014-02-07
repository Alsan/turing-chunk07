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
package org.wso2.andes.server.registry;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.wso2.andes.AMQException;
import org.wso2.andes.server.configuration.ServerConfiguration;
import org.wso2.andes.server.logging.actors.BrokerActor;
import org.wso2.andes.server.logging.actors.CurrentActor;
import org.wso2.andes.server.management.JMXManagedObjectRegistry;
import org.wso2.andes.server.management.NoopManagedObjectRegistry;

public class ConfigurationFileApplicationRegistry extends ApplicationRegistry
{
    public ConfigurationFileApplicationRegistry(File configurationURL) throws ConfigurationException
    {
        super(new ServerConfiguration(configurationURL));
    }

    @Override
    public void close()
    {
        //Set the Actor for Broker Shutdown
        CurrentActor.set(new BrokerActor(_rootMessageLogger));
        try
        {
            super.close();
        }
        finally
        {
            CurrentActor.remove();
        }
    }


    @Override
    protected void initialiseManagedObjectRegistry() throws AMQException
    {
        if (_configuration.getManagementEnabled())
        {
            _managedObjectRegistry = new JMXManagedObjectRegistry();
        }
        else
        {
            _managedObjectRegistry = new NoopManagedObjectRegistry();
        }
    }

}
