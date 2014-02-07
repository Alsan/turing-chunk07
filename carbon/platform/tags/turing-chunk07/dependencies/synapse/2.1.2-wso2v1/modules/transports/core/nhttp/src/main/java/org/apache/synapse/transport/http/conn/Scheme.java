/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.synapse.transport.http.conn;

import java.util.Locale;

public final class Scheme {

    private final String name;
    private final int defaultPort;
    private final boolean ssl;
    
    public Scheme(String name, int defaultPort, boolean ssl) {
        super();
        this.name = name.toLowerCase(Locale.US);
        this.defaultPort = defaultPort;
        this.ssl = ssl;
    }

    public String getName() {
        return name;
    }

    public int getDefaultPort() {
        return defaultPort;
    }

    public boolean isSSL() {
        return ssl;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
