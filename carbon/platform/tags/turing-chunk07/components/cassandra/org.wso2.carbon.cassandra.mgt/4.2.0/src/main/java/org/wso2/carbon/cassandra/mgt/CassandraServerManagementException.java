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
package org.wso2.carbon.cassandra.mgt;

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;

/**
 * Acts as a decorator and do log in addition to the existing behaviour of the exception
 * This exception is to be thrown for errors in Cassandra Server Management
 */

public class CassandraServerManagementException extends AxisFault {

    /**
     * Logs the given message and create a RuntimeException object
     *
     * @param msg Error Message
     * @param log Logger who need to consume message
     */
    public CassandraServerManagementException(String msg, Log log) {
        super(msg);
        log.error(msg);
    }

    /**
     * Logs the given message and the root error and create a RuntimeException object
     *
     * @param msg       Error Message
     * @param throwable Root Error
     * @param log       Logger who need to consume message
     */
    public CassandraServerManagementException(String msg, Throwable throwable, Log log) {
        super(msg, throwable);
        log.error(msg, throwable);
    }
}
