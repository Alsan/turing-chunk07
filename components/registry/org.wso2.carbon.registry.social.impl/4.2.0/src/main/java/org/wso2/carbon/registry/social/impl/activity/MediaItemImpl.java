/*
 * Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.carbon.registry.social.impl.activity;

import org.wso2.carbon.registry.social.api.activity.MediaItem;

/**
 * An implementation of the {@link org.wso2.carbon.registry.social.api.activity.MediaItem}
 *
 */
public class MediaItemImpl implements MediaItem {

    private String mimeType;
    private Type type;
    private String url;
    private String thumbnailUrl;

    public MediaItemImpl() {
    }

    public MediaItemImpl(String mimeType, Type type, String url) {
        this.mimeType = mimeType;
        this.type = type;
        this.url = url;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    public void setThumbnailUrl(String url) {
        this.thumbnailUrl = url;
    }
}