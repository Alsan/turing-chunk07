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

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.ProtocolException;
import org.apache.http.auth.AUTH;
import org.apache.http.auth.Credentials;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;

public class ProxyAuthenticator {

    private final Credentials proxycreds;
    
    public ProxyAuthenticator(final Credentials proxycreds) {
        super();
        this.proxycreds = proxycreds;
    }

    public void authenticatePreemptively(
            final HttpRequest request, final HttpContext context) throws ProtocolException {
        BasicScheme basicScheme = new BasicScheme();
        basicScheme.processChallenge(new BasicHeader(AUTH.PROXY_AUTH, "BASIC realm=\"proxy\""));
        Header authresp = basicScheme.authenticate(proxycreds, request, context);
        request.addHeader(authresp);
    }
    
}
