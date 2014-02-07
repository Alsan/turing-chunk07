/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
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
 */
package org.jclouds.openstack.keystone.v2_0.functions;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.openstack.keystone.v2_0.AuthenticationClient;
import org.jclouds.openstack.keystone.v2_0.config.CredentialType;
import org.jclouds.openstack.keystone.v2_0.config.CredentialTypes;
import org.jclouds.openstack.keystone.v2_0.domain.Access;
import org.jclouds.openstack.keystone.v2_0.domain.PasswordCredentials;
import org.jclouds.openstack.keystone.v2_0.functions.internal.BaseAuthenticator;

import com.google.common.base.Optional;

@CredentialType(CredentialTypes.PASSWORD_CREDENTIALS)
@Singleton
public class AuthenticatePasswordCredentials extends BaseAuthenticator<PasswordCredentials> {
   protected final AuthenticationClient client;

   @Inject
   public AuthenticatePasswordCredentials(AuthenticationClient client) {
      this.client = client;
   }

   @Override
   protected Access authenticateWithTenantName(Optional<String> tenantName, PasswordCredentials apiAccessKeyCredentials) {
      return client.authenticateWithTenantNameAndCredentials(tenantName.orNull(), apiAccessKeyCredentials);
   }

   @Override
   protected Access authenticateWithTenantId(Optional<String> tenantId, PasswordCredentials apiAccessKeyCredentials) {
      return client.authenticateWithTenantIdAndCredentials(tenantId.orNull(), apiAccessKeyCredentials);
   }

   @Override
   public PasswordCredentials createCredentials(String identity, String credential) {
      return PasswordCredentials.createWithUsernameAndPassword(identity, credential);
   }

   @Override
   public String toString() {
      return "authenticatePasswordCredentials()";
   }
}