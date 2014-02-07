/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.vfs2.provider.http;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.UserAuthenticationData;
import org.apache.commons.vfs2.UserAuthenticator;
import org.apache.commons.vfs2.util.UserAuthenticatorUtils;

/**
 * Create a HttpClient instance.
 *
 * @author <a href="http://commons.apache.org/vfs/team-list.html">Commons VFS team</a>
 * @version $Revision: 1040766 $ $Date: 2010-12-01 02:06:53 +0530 (Wed, 01 Dec 2010) $
 */
public final class HttpClientFactory
{
    private HttpClientFactory()
    {
    }

    public static HttpClient createConnection(String scheme, String hostname, int port, String username,
                                              String password, FileSystemOptions fileSystemOptions)
            throws FileSystemException
    {
        return createConnection(HttpFileSystemConfigBuilder.getInstance(), scheme, hostname, port,
            username, password, fileSystemOptions);
    }

    /**
     * Creates a new connection to the server.
     * @param builder The HttpFileSystemConfigBuilder.
     * @param scheme The protocol.
     * @param hostname The hostname.
     * @param port The port number.
     * @param username The username.
     * @param password The password
     * @param fileSystemOptions The file system options.
     * @return a new HttpClient connection.
     * @throws FileSystemException if an error occurs.
     * @since 2.0
     */
    public static HttpClient createConnection(HttpFileSystemConfigBuilder builder, String scheme,
                                              String hostname, int port, String username,
                                              String password, FileSystemOptions fileSystemOptions)
            throws FileSystemException
    {
        HttpClient client;
        try
        {
            HttpConnectionManager mgr = new MultiThreadedHttpConnectionManager();
            HttpConnectionManagerParams connectionMgrParams = mgr.getParams();

            client = new HttpClient(mgr);

            final HostConfiguration config = new HostConfiguration();
            config.setHost(hostname, port, scheme);

            if (fileSystemOptions != null)
            {
                String proxyHost = builder.getProxyHost(fileSystemOptions);
                int proxyPort = builder.getProxyPort(fileSystemOptions);

                if (proxyHost != null && proxyHost.length() > 0 && proxyPort > 0)
                {
                    config.setProxy(proxyHost, proxyPort);
                }

                UserAuthenticator proxyAuth = builder.getProxyAuthenticator(fileSystemOptions);
                if (proxyAuth != null)
                {
                    UserAuthenticationData authData = UserAuthenticatorUtils.authenticate(proxyAuth,
                        new UserAuthenticationData.Type[]
                        {
                            UserAuthenticationData.USERNAME,
                            UserAuthenticationData.PASSWORD
                        });

                    if (authData != null)
                    {
                        final UsernamePasswordCredentials proxyCreds =
                            new UsernamePasswordCredentials(
                                UserAuthenticatorUtils.toString(UserAuthenticatorUtils.getData(authData,
                                    UserAuthenticationData.USERNAME, null)),
                                UserAuthenticatorUtils.toString(UserAuthenticatorUtils.getData(authData,
                                    UserAuthenticationData.PASSWORD, null)));

                        AuthScope scope = new AuthScope(proxyHost, AuthScope.ANY_PORT);
                        client.getState().setProxyCredentials(scope, proxyCreds);
                    }

                    if (builder.isPreemptiveAuth(fileSystemOptions))
                    {
                        HttpClientParams httpClientParams = new HttpClientParams();
                        httpClientParams.setAuthenticationPreemptive(true);
                        client.setParams(httpClientParams);
                    }
                }

                Cookie[] cookies = builder.getCookies(fileSystemOptions);
                if (cookies != null)
                {
                    client.getState().addCookies(cookies);
                }
            }
            /**
             * ConnectionManager set methodsmust be called after the host & port and proxy host & port
             * are set in the HostConfiguration. They are all used as part of the key when HttpConnectionManagerParams
             * tries to locate the host configuration.
             */
            connectionMgrParams.setMaxConnectionsPerHost(config, builder.getMaxConnectionsPerHost(fileSystemOptions));
            connectionMgrParams.setMaxTotalConnections(builder.getMaxTotalConnections(fileSystemOptions));

            client.setHostConfiguration(config);

            if (username != null)
            {
                final UsernamePasswordCredentials creds =
                    new UsernamePasswordCredentials(username, password);
                AuthScope scope = new AuthScope(hostname, AuthScope.ANY_PORT);
                client.getState().setCredentials(scope, creds);
            }

            client.executeMethod(new HeadMethod());
        }
        catch (final Exception exc)
        {
            throw new FileSystemException("vfs.provider.http/connect.error", new Object[]{hostname}, exc);
        }

        return client;
    }
}
