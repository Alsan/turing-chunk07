/*
 * Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.registry.jcr.nodetype;

import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeIterator;
import java.util.Iterator;
import java.util.Set;


public class RegistryNodeTypeIterator implements NodeTypeIterator {

    private Set ntList;
    private Iterator nodeIt;
    private long counter = 0;

    public RegistryNodeTypeIterator(Set ntList) {

        this.ntList = ntList;
        this.nodeIt = this.ntList.iterator();
    }

    public NodeType nextNodeType() {

        return (NodeType) nodeIt.next();
    }

    public void skip(long l) {
        counter = l;
    }

    public long getSize() {

        if (ntList != null) {
            return ntList.size();
        } else {
            return 0;
        }
    }

    public long getPosition() {

        return counter;
    }

    public boolean hasNext() {
        return nodeIt.hasNext();
    }

    public Object next() {
        counter++;
        return nodeIt.next();
    }

    public void remove() {
        nodeIt.remove();
    }
}
