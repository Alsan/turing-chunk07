/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.event.input.adaptor.core.exception;

/**
 * this class represents the message processing time issues
 */
public class InputEventAdaptorEventProcessingException extends RuntimeException {

    public InputEventAdaptorEventProcessingException() {
    }

    public InputEventAdaptorEventProcessingException(String message) {
        super(message);
    }

    public InputEventAdaptorEventProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public InputEventAdaptorEventProcessingException(Throwable cause) {
        super(cause);
    }
}
