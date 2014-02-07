/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.ode.bpel.runtime;

import org.apache.ode.bpel.o.OLink;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Link stack frame allowing resolution of {@link OLink} objects to the
 * current {@link LinkInfo} in context.
 */
class LinkFrame implements Serializable {

    private static final long serialVersionUID = 1L;
    LinkFrame next;
  Map<OLink, LinkInfo> links = new HashMap<OLink, LinkInfo>();

  LinkFrame(LinkFrame next) {
    this.next = next;
  }

  LinkInfo resolve(OLink link) {
    LinkInfo li = links.get(link);
    if (li == null && next != null)
      return next.resolve(link);
    return li;
  }

}
