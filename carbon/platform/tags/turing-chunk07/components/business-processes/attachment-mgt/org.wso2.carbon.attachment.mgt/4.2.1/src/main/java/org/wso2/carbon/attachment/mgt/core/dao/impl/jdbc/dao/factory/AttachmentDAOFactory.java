/*
 * Copyright (c) 2012, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.attachment.mgt.core.dao.impl.jdbc.dao.factory;

import org.wso2.carbon.attachment.mgt.core.dao.AttachmentDAO;
import org.wso2.carbon.attachment.mgt.core.dao.impl.jdbc.dao.AttachmentDAOImpl;

/**
 * Factory class to create new JDBC specific DAO objects
 */
public class AttachmentDAOFactory {
    /**
     * Create a new Attachment DAO
     *
     * @return
     */
    public static AttachmentDAO create() {
        return new AttachmentDAOImpl();
    }
}
