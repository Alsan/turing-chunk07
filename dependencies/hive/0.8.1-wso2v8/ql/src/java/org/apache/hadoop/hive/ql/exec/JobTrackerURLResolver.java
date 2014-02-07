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

package org.apache.hadoop.hive.ql.exec;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.net.NetUtils;

/**
 * JobTrackerURLResolver.
 *
 */
public final class JobTrackerURLResolver {
  public static String getURL(JobConf conf) throws IOException {
    String infoAddr = conf.get("mapred.job.tracker.http.address");
    if (infoAddr == null) {
      throw new IOException("Unable to find job tracker info port.");
    }
    InetSocketAddress infoSocAddr = NetUtils.createSocketAddr(infoAddr);
    int infoPort = infoSocAddr.getPort();

    String jobTrackerStr =
      conf.get("mapred.job.tracker", "localhost:8012");
    InetSocketAddress jobTrackerSocAddr =
      NetUtils.createSocketAddr(jobTrackerStr);

    String tracker = "http://" + jobTrackerSocAddr.getHostName()
        + ":" + infoPort;

    return tracker;
  }

  private JobTrackerURLResolver() {
    // prevent instantiation
  }
}
