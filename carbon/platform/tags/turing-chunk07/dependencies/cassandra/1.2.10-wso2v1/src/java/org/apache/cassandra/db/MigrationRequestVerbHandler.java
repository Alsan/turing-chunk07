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
package org.apache.cassandra.db;

import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.cassandra.net.IVerbHandler;
import org.apache.cassandra.net.MessageIn;
import org.apache.cassandra.net.MessageOut;
import org.apache.cassandra.net.MessagingService;
import org.apache.cassandra.service.MigrationManager;

/**
 * Sends it's current schema state in form of row mutations in reply to the remote node's request.
 * Such a request is made when one of the nodes, by means of Gossip, detects schema disagreement in the ring.
 */
public class MigrationRequestVerbHandler implements IVerbHandler
{
    private static final Logger logger = LoggerFactory.getLogger(MigrationRequestVerbHandler.class);

    public void doVerb(MessageIn message, String id)
    {
        logger.debug("Received migration request from {}.", message.from);

        if (message.version < MessagingService.VERSION_12)
            logger.debug("Returning empty response to the migration request from {} (version < 1.2).", message.from);

        Collection<RowMutation> schema = message.version < MessagingService.VERSION_12
                                         ? Collections.EMPTY_SET
                                         : SystemTable.serializeSchema();

        MessageOut<Collection<RowMutation>> response = new MessageOut<Collection<RowMutation>>(MessagingService.Verb.INTERNAL_RESPONSE,
                                                                                               schema,
                                                                                               MigrationManager.MigrationsSerializer.instance);
        MessagingService.instance().sendReply(response, id, message.from);
    }
}