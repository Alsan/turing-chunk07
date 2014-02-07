package org.apache.cassandra.io.util;
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


import java.io.IOException;

import org.apache.cassandra.cache.FreeableMemory;


public class MemoryInputStream extends AbstractDataInput
{
    private final FreeableMemory mem;
    private int position = 0;

    public MemoryInputStream(FreeableMemory mem)
    {
        this.mem = mem;
    }

    public int read() throws IOException
    {
        return mem.getByte(position++) & 0xFF;
    }

    public void readFully(byte[] buffer, int offset, int count) throws IOException
    {
        mem.getBytes(position, buffer, offset, count);
        position += count;
    }

    protected void seekInternal(int pos)
    {
        position = pos;
    }

    protected int getPosition()
    {
        return position;
    }

    public int skipBytes(int n) throws IOException
    {
        seekInternal(getPosition() + n);
        return position;
    }

    public void close()
    {
        // do nothing.
    }
}
