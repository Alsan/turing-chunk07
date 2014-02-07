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
package org.wso2.andes.server.txn;

import org.wso2.andes.server.txn.ServerTransaction.Action;

/** 
 * Mock implementation of a ServerTranaction Action
 * allowing its state to be observed.
 * 
 */
class MockAction implements Action
{
    private boolean _rollbackFired = false;
    private boolean _postCommitFired = false;

    public void postCommit()
    {
        _postCommitFired = true;
    }

    public void onRollback()
    {
        _rollbackFired = true;
    }

    public boolean isRollbackActionFired()
    {
        return _rollbackFired;
    }

    public boolean isPostCommitActionFired()
    {
        return _postCommitFired;
    }
}