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
package org.wso2.andes.test.unit.client.forwardall;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;

public class ServiceCreator implements Runnable
{
    private static final Logger _logger = LoggerFactory.getLogger(ServiceCreator.class);

    private static Thread[] threads;
    private static ServiceCreator[] _services;

    private final String broker;
    private Service service;

    ServiceCreator(String broker)
    {
        this.broker = broker;
    }

    public void run()
    {
        try
        {
            service = new Service(broker);
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
        }
    }

    public void closeSC() throws JMSException
    {
        service.close();
    }

    static void closeAll()
    {
        for (int i = 0; i < _services.length; i++)
        {
            try
            {
                _services[i].closeSC();
            }
            catch (JMSException e)
            {
                // ignore
            }
        }
    }

    static void start(String broker, int services) throws InterruptedException
    {
        threads = new Thread[services];
        _services = new ServiceCreator[services];
        ServiceCreator runner = new ServiceCreator(broker);
        // start services
        _logger.info("Starting " + services + " services...");
        for (int i = 0; i < services; i++)
        {
            threads[i] = new Thread(runner);
            _services[i] = runner;
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++)
        {
            threads[i].join();
        }
    }

    public static void main(String[] argv) throws Exception
    {
        final String connectionString;
        final int services;
        if (argv.length == 0)
        {
            connectionString = "localhost:5672";
            services = 100;
        }
        else
        {
            connectionString = argv[0];
            services = Integer.parseInt(argv[1]);
        }

        start(connectionString, services);
    }
}
