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
package org.apache.ode.bpel.o;

import org.apache.ode.utils.stl.CollectionsX;
import org.apache.ode.utils.stl.MemberOfFunction;

import java.util.HashSet;
import java.util.Set;


/**
 */
public class OFlow extends OActivity {
    static final long serialVersionUID = -1L  ;

    /** Links delcared within this activity. */
    public final Set<OLink> localLinks = new HashSet<OLink>();

    public final Set<OActivity> parallelActivities = new HashSet<OActivity>();

    public OFlow(OProcess owner, OActivity parent) {
        super(owner, parent);
    }

    public OLink getLocalLink(final String linkName) {
        return CollectionsX.find_if(localLinks, new MemberOfFunction<OLink>() {
            public boolean isMember(OLink o) {
                return o.name.equals(linkName);
            }
        });
    }

}
