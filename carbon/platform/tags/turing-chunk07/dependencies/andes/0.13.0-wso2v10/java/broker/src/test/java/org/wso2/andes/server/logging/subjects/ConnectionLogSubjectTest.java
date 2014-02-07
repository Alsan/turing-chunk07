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
package org.wso2.andes.server.logging.subjects;

/**
 * Validate ConnectionLogSubjects are logged as expected
 */
public class ConnectionLogSubjectTest extends AbstractTestLogSubject
{

    public void setUp() throws Exception
    {
        super.setUp();

        _subject = new ConnectionLogSubject(getSession());
    }

    /**
     * MESSAGE [Blank][con:0(MockProtocolSessionUser@null/test)] <Log Message>
     *
     * @param message the message whos format needs validation
     */
    protected void validateLogStatement(String message)
    {
        verifyConnection(getSession().getSessionID(), "InternalTestProtocolSession", "127.0.0.1:1", "test", message);
    }

}
