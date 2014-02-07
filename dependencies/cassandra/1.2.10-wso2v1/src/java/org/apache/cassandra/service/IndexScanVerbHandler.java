/*
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
package org.apache.cassandra.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.cassandra.db.*;
import org.apache.cassandra.net.IVerbHandler;
import org.apache.cassandra.net.MessageIn;
import org.apache.cassandra.net.MessagingService;
import org.apache.cassandra.thrift.ThriftValidation;
import org.apache.cassandra.tracing.Tracing;

@Deprecated // 1.1 implements index scan with RangeSliceVerb instead
public class IndexScanVerbHandler implements IVerbHandler<IndexScanCommand>
{
    private static final Logger logger = LoggerFactory.getLogger(IndexScanVerbHandler.class);

    public void doVerb(MessageIn<IndexScanCommand> message, String id)
    {
        try
        {
            IndexScanCommand command = message.payload;
            ColumnFamilyStore cfs = Table.open(command.keyspace).getColumnFamilyStore(command.column_family);
            List<Row> rows = cfs.search(command.index_clause.expressions,
                                        command.range,
                                        command.index_clause.count,
                                        ThriftValidation.asIFilter(command.predicate, cfs.getComparator()));
            RangeSliceReply reply = new RangeSliceReply(rows);
            Tracing.trace("Enqueuing response to {}", message.from);
            MessagingService.instance().sendReply(reply.createMessage(), id, message.from);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }
}
