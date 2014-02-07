/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include "hdfs.h"

#include "fuse_dfs.h"
#include "fuse_connect.h"
#include "fuse_users.h" 


#if PERMS

/**
 * Connects to the NN as the current user/group according to FUSE
 *
 */
hdfsFS doConnectAsUser(const char *hostname, int port) {
  uid_t uid = fuse_get_context()->uid;

  char *user = getUsername(uid);
  if (NULL == user)
    return NULL;
  int numgroups = 0;
  char **groups = getGroups(uid, &numgroups);
  hdfsFS fs = hdfsConnectAsUser(hostname, port, user, (const char **)groups, numgroups);
  freeGroups(groups, numgroups);
  if (user) 
    free(user);
  return fs;
}

#else

hdfsFS doConnectAsUser(const char *hostname, int port) {
  return hdfsConnect(hostname, port);
}

#endif
