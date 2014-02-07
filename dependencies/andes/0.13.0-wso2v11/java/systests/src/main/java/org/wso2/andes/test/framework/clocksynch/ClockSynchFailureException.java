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
package org.wso2.andes.test.framework.clocksynch;

/**
 * ClockSynchFailureException represents failure of a {@link ClockSynchronizer} to achieve synchronization. For example,
 * this could be because a reference signal is not available, or because a desired accurracy cannot be attained.
 *
 * <p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Represent failure to achieve synchronization.
 * </table>
 */
public class ClockSynchFailureException extends Exception
{
    /**
     * Creates a clock synch failure exception.
     *
     * @param message The detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause   The cause (which is saved for later retrieval by the {@link #getCause()} method).  (A <tt>null</tt>
     *                value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public ClockSynchFailureException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
