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

package org.apache.hadoop.hive.thrift;

import java.util.concurrent.ConcurrentMap;

import org.apache.hadoop.security.UserGroupInformation;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportFactory;

import com.google.common.collect.MapMaker;

/** TUGIContainingTransport associates ugi information with connection (transport).
 *  Wraps underlying <code>TSocket</code> transport and annotates it with ugi.
*/

public class TUGIContainingTransport extends TFilterTransport {

  private UserGroupInformation ugi;

  public TUGIContainingTransport(TTransport wrapped, UserGroupInformation ugi) {
    super(wrapped);
  }

  public UserGroupInformation getClientUGI(){
    return ugi;
  }

  public void setClientUGI(UserGroupInformation ugi){
    this.ugi = ugi;
  }

  /** Factory to create TUGIContainingTransport.
   */

  public static class Factory extends TTransportFactory {

    // Need a concurrent weak hashmap.
    private static final ConcurrentMap<TTransport, TUGIContainingTransport> transMap =
        new MapMaker().weakKeys().makeMap();

    /**
     * Get a new <code>TUGIContainingTransport</code> instance, or reuse the
     * existing one if a <code>TUGIContainingTransport</code> has already been
     * created before using the given <code>TTransport</code> as an underlying
     * transport. This ensures that a given underlying transport instance
     * receives the same <code>TUGIContainingTransport</code>.
     */
    @Override
    public TUGIContainingTransport getTransport(TTransport trans) {

      // UGI information is not available at connection setup time, it will be set later
      // via set_ugi() rpc.
      transMap.putIfAbsent(trans, new TUGIContainingTransport(trans,null));
      return transMap.get(trans);
    }
  }
}