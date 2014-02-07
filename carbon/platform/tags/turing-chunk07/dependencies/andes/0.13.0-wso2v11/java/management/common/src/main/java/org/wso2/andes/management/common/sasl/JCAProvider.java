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
package org.wso2.andes.management.common.sasl;

import java.security.Provider;
import java.util.Map;

import javax.security.sasl.SaslClientFactory;

public class JCAProvider extends Provider
{
    private static final long serialVersionUID = 1L;

    /**
     * Creates the security provider with a map from SASL mechanisms to implementing factories.
     *
     * @param providerMap The map from SASL mechanims to implementing factory classes.
     */
    public JCAProvider(Map<String, Class<? extends SaslClientFactory>> providerMap)
    {
        super("AMQSASLProvider", 1.0, "A JCA provider that registers all "
              + "AMQ SASL providers that want to be registered");
        register(providerMap);
    }

    /**
     * Registers client factory classes for a map of mechanism names to client factory classes.
     *
     * @param providerMap The map from SASL mechanims to implementing factory classes.
     */
    private void register(Map<String, Class<? extends SaslClientFactory>> providerMap)
    {
        for (Map.Entry<String, Class<? extends SaslClientFactory>> me : providerMap.entrySet())
        {
            put("SaslClientFactory." + me.getKey(), me.getValue().getName());
        }
    }
}
