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

#include "fuse_dfs.h"
#include "fuse_impls.h"
#include "fuse_stat_struct.h"

int dfs_getattr(const char *path, struct stat *st)
{
  TRACE1("getattr", path)

  // retrieve dfs specific data
  dfs_context *dfs = (dfs_context*)fuse_get_context()->private_data;

  // check params and the context var
  assert(dfs);
  assert(path);
  assert(st);

  // if not connected, try to connect and fail out if we can't.
  if (NULL == dfs->fs && NULL == (dfs->fs = hdfsConnect(dfs->nn_hostname,dfs->nn_port))) {
    syslog(LOG_ERR, "ERROR: could not connect to %s:%d %s:%d\n", dfs->nn_hostname, dfs->nn_port,__FILE__, __LINE__);
    return -EIO;
  }

  // call the dfs API to get the actual information
  hdfsFileInfo *info = hdfsGetPathInfo(dfs->fs,path);

  if (NULL == info) {
    return -ENOENT;
  }

  fill_stat_structure(&info[0], st);

  // setup hard link info - for a file it is 1 else num entries in a dir + 2 (for . and ..)
  if (info[0].mKind == kObjectKindDirectory) {
    int numEntries = 0;
    hdfsFileInfo *info = hdfsListDirectory(dfs->fs,path,&numEntries);

    if (info) {
      hdfsFreeFileInfo(info,numEntries);
    }
    st->st_nlink = numEntries + 2;
  } else {
    // not a directory
    st->st_nlink = 1;
  }

  // free the info pointer
  hdfsFreeFileInfo(info,1);

  return 0;
}
