/*
 *  Copyright (c) 2005-2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.wso2.carbon.governance.sramp.exceptions;

import org.wso2.carbon.governance.sramp.SRAMPServlet;

import javax.servlet.ServletException;

/**
 * This is thrown when a exceptions occur while processing requests that are made to the
 * {@link SRAMPServlet}.
 */
@SuppressWarnings("unused")
public class SRAMPServletException extends ServletException {

    /**
     * Constructs a new exception with no additional information.
     */
    public SRAMPServletException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public SRAMPServletException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause of this exception.
     */
    public SRAMPServletException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the cause.
     *
     * @param cause the cause of this exception.
     */
    public SRAMPServletException(Throwable cause) {
        super(cause);
    }
}
