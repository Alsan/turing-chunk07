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
package org.apache.cassandra.db.compaction;

import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.cassandra.SchemaLoader;
import org.apache.cassandra.db.ColumnFamilyStore;
import org.apache.cassandra.db.Table;
import org.apache.cassandra.utils.Pair;

public class SizeTieredCompactionStrategyTest extends SchemaLoader
{
    @Test
    public void testGetBuckets()
    {
        List<Pair<String, Long>> pairs = new ArrayList<Pair<String, Long>>();
        String[] strings = { "a", "bbbb", "cccccccc", "cccccccc", "bbbb", "a" };
        for (String st : strings)
        {
            Pair<String, Long> pair = Pair.create(st, new Long(st.length()));
            pairs.add(pair);
        }

        ColumnFamilyStore cfs = Table.open("Keyspace1").getColumnFamilyStore("Standard1");
        Map<String, String> opts = new HashMap<String, String>();
        opts.put(SizeTieredCompactionStrategy.MIN_SSTABLE_SIZE_KEY, "2");
        SizeTieredCompactionStrategy strategy = new SizeTieredCompactionStrategy(cfs, opts);
        List<List<String>> buckets = strategy.getBuckets(pairs);
        assertEquals(3, buckets.size());

        for (List<String> bucket : buckets)
        {
            assertEquals(2, bucket.size());
            assertEquals(bucket.get(0).length(), bucket.get(1).length());
            assertEquals(bucket.get(0).charAt(0), bucket.get(1).charAt(0));
        }

        pairs.clear();
        buckets.clear();

        String[] strings2 = { "aaa", "bbbbbbbb", "aaa", "bbbbbbbb", "bbbbbbbb", "aaa" };
        for (String st : strings2)
        {
            Pair<String, Long> pair = Pair.create(st, new Long(st.length()));
            pairs.add(pair);
        }

        buckets = strategy.getBuckets(pairs);
        assertEquals(2, buckets.size());

        for (List<String> bucket : buckets)
        {
            assertEquals(3, bucket.size());
            assertEquals(bucket.get(0).charAt(0), bucket.get(1).charAt(0));
            assertEquals(bucket.get(1).charAt(0), bucket.get(2).charAt(0));
        }

        // Test the "min" functionality
        pairs.clear();
        buckets.clear();

        String[] strings3 = { "aaa", "bbbbbbbb", "aaa", "bbbbbbbb", "bbbbbbbb", "aaa" };
        for (String st : strings3)
        {
            Pair<String, Long> pair = Pair.create(st, new Long(st.length()));
            pairs.add(pair);
        }

        opts.put(SizeTieredCompactionStrategy.MIN_SSTABLE_SIZE_KEY, "10");
        strategy = new SizeTieredCompactionStrategy(cfs, opts);
        buckets = strategy.getBuckets(pairs); // notice the min is 10
        assertEquals(1, buckets.size());
    }
}
