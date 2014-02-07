/*
 *  Copyright WSO2 Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.carbon.apimgt.api.model;

import java.io.InputStream;

public class Icon {
    
    private InputStream content;
    private String contentType;

    public Icon(InputStream content, String contentType) {
        this.content = content;
        this.contentType = contentType;
    }

    public InputStream getContent() {
        return content;
    }

    public String getContentType() {
        return contentType;
    }
}
