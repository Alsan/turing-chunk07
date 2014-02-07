/*
 * Copyright (c) 2008, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.carbon.governance.api.schema;

import org.wso2.carbon.governance.api.exception.GovernanceException;
import org.wso2.carbon.governance.api.schema.dataobjects.Schema;

/**
 * This interface represents a mechanism to filter that can be used to identify a schema from a
 * given set of schemas.
 */
public interface SchemaFilter {

    /**
     * Whether the given schema artifact matches the expected filter criteria.
     *
     * @param schema the schema artifact.
     *
     * @return true if a match was found or false otherwise.
     * @throws GovernanceException if the operation failed.
     */
    public boolean matches(Schema schema) throws GovernanceException;
}
