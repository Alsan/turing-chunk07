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
package org.wso2.andes;

import org.wso2.andes.protocol.AMQConstant;

/**
 * AMQChannelClosedException indicates that an operation cannot be performed becauase a channel has been closed.
 *
 * <p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Represents a failed operation on a closed channel.
 * </table>
 *
 * @todo Does this duplicate AMQChannelException?
 */
public class AMQChannelClosedException extends AMQException
{
    public AMQChannelClosedException(AMQConstant errorCode, String msg, Throwable cause)
    {
        super(errorCode, msg, cause);
    }
}
