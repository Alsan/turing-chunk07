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
package org.apache.cassandra.io.sstable;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;

import org.junit.Test;

import org.apache.cassandra.db.marshal.AbstractType;
import org.apache.cassandra.db.marshal.IntegerType;
import static org.apache.cassandra.io.sstable.IndexHelper.IndexInfo;
import static org.apache.cassandra.utils.ByteBufferUtil.bytes;

public class IndexHelperTest
{
    @Test
    public void testIndexHelper()
    {
        List<IndexInfo> indexes = new ArrayList<IndexInfo>();
        indexes.add(new IndexInfo(bytes(0L), bytes(5L), 0, 0));
        indexes.add(new IndexInfo(bytes(10L), bytes(15L), 0, 0));
        indexes.add(new IndexInfo(bytes(20L), bytes(25L), 0, 0));

        AbstractType comp = IntegerType.instance;

        assertEquals(0, IndexHelper.indexFor(bytes(-1L), indexes, comp, false, -1));
        assertEquals(0, IndexHelper.indexFor(bytes(5L), indexes, comp, false, -1));
        assertEquals(1, IndexHelper.indexFor(bytes(12L), indexes, comp, false, -1));
        assertEquals(2, IndexHelper.indexFor(bytes(17L), indexes, comp, false, -1));
        assertEquals(3, IndexHelper.indexFor(bytes(100L), indexes, comp, false, -1));
        assertEquals(3, IndexHelper.indexFor(bytes(100L), indexes, comp, false, 0));
        assertEquals(3, IndexHelper.indexFor(bytes(100L), indexes, comp, false, 1));
        assertEquals(3, IndexHelper.indexFor(bytes(100L), indexes, comp, false, 2));
        assertEquals(-1, IndexHelper.indexFor(bytes(100L), indexes, comp, false, 3));

        assertEquals(-1, IndexHelper.indexFor(bytes(-1L), indexes, comp, true, -1));
        assertEquals(0, IndexHelper.indexFor(bytes(5L), indexes, comp, true, -1));
        assertEquals(1, IndexHelper.indexFor(bytes(17L), indexes, comp, true, -1));
        assertEquals(2, IndexHelper.indexFor(bytes(100L), indexes, comp, true, -1));
        assertEquals(0, IndexHelper.indexFor(bytes(100L), indexes, comp, true, 0));
        assertEquals(1, IndexHelper.indexFor(bytes(12L), indexes, comp, true, -1));
        assertEquals(1, IndexHelper.indexFor(bytes(100L), indexes, comp, true, 1));
        assertEquals(2, IndexHelper.indexFor(bytes(100L), indexes, comp, true, 2));
        assertEquals(-1, IndexHelper.indexFor(bytes(100L), indexes, comp, true, 4));
    }
}
