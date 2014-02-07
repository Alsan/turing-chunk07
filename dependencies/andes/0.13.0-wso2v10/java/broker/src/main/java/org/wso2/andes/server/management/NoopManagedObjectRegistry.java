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
package org.wso2.andes.server.management;

import javax.management.JMException;

import org.apache.log4j.Logger;

/**
 * This managed object registry does not actually register MBeans. This can be used in tests when management is
 * not required or when management has been disabled.
 *
 */
public class NoopManagedObjectRegistry implements ManagedObjectRegistry
{
    private static final Logger _log = Logger.getLogger(NoopManagedObjectRegistry.class);

    public NoopManagedObjectRegistry()
    {
        _log.info("Management is disabled");
    }

    public void start()
    {
        //no-op
    }

    public void registerObject(ManagedObject managedObject) throws JMException
    {
    }

    public void unregisterObject(ManagedObject managedObject) throws JMException
    {
    }

    public void close()
    {
        
    }
}
