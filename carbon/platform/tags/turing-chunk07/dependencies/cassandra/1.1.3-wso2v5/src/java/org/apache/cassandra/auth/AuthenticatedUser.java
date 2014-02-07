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

package org.apache.cassandra.auth;

import java.util.Collections;
import java.util.Set;

/**
 * An authenticated user, her domain name and her groups.
 */
public class AuthenticatedUser
{
    public final String username;
    public final Set<String> groups;
    public final String domainName;

    public AuthenticatedUser(String username)
    {
        this.username = username;
        this.groups = Collections.emptySet();
		this.domainName = null;
    }

    public AuthenticatedUser(String username, Set<String> groups, String domainName)
    {
        this.username = username;
        this.groups = Collections.unmodifiableSet(groups);
		this.domainName = domainName;
    }

    @Override
    public String toString()
    {
        return String.format("#<User %s groups=%s domain=%s>", username, groups, domainName);
    }
}
