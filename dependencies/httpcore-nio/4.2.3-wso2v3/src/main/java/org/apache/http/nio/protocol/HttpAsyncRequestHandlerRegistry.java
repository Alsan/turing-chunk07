/*
 * ====================================================================
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
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.http.nio.protocol;

import java.util.Map;

import org.apache.http.annotation.ThreadSafe;
import org.apache.http.protocol.UriPatternMatcher;

/**
 * Maintains a map of HTTP request handlers keyed by a request URI pattern.
 * <br>
 * Patterns may have three formats:
 * <ul>
 *   <li><code>*</code></li>
 *   <li><code>*&lt;uri&gt;</code></li>
 *   <li><code>&lt;uri&gt;*</code></li>
 * </ul>
 * <br>
 * This class can be used to resolve an instance of {@link HttpAsyncRequestHandler}
 * matching a particular request URI. Usually the resolved request handler
 * will be used to process the request with the specified request URI.
 *
 * @since 4.2
 */
@ThreadSafe
public class HttpAsyncRequestHandlerRegistry implements HttpAsyncRequestHandlerResolver {

    private final UriPatternMatcher<HttpAsyncRequestHandler<?>> matcher;

    public HttpAsyncRequestHandlerRegistry() {
        matcher = new UriPatternMatcher<HttpAsyncRequestHandler<?>>();
    }

    /**
     * Registers the given {@link NHttpRequestHandler} as a handler for URIs
     * matching the given pattern.
     *
     * @param pattern the pattern to register the handler for.
     * @param handler the handler.
     */
    public void register(final String pattern, final HttpAsyncRequestHandler<?> handler) {
        matcher.register(pattern, handler);
    }

    /**
     * Removes registered handler, if exists, for the given pattern.
     *
     * @param pattern the pattern to unregister the handler for.
     */
    public void unregister(final String pattern) {
        matcher.unregister(pattern);
    }

    /**
     * Sets handlers from the given map.
     * @param map the map containing handlers keyed by their URI patterns.
     */
    public void setHandlers(final Map<String, HttpAsyncRequestHandler<?>> map) {
        matcher.setObjects(map);
    }

    /**
     * Get the handler map.
     * @return The map of handlers and their associated URI patterns.
     */
    public Map<String, HttpAsyncRequestHandler<?>> getHandlers() {
        return matcher.getObjects();
    }

    public HttpAsyncRequestHandler<?> lookup(String requestURI) {
        return matcher.lookup(requestURI);
    }

}
