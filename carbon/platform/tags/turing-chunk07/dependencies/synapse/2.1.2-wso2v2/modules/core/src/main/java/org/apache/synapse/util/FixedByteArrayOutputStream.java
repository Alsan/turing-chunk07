/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.synapse.util;

import org.apache.synapse.SynapseException;
import java.io.ByteArrayOutputStream;

public class FixedByteArrayOutputStream extends ByteArrayOutputStream {

    public FixedByteArrayOutputStream(int size) {
        super(size);
    }

    public synchronized void write(int b) {
        if (count+1 > buf.length) {
            throw new SynapseException("Fixed size of internal byte array exceeded");
        }
        super.write(b);
    }

    public synchronized void write(byte b[], int off, int len) {
        if (count+len > buf.length) {
            throw new SynapseException("Fixed size of internal byte array exceeded");
        }
        super.write(b, off, len);
    }
}
