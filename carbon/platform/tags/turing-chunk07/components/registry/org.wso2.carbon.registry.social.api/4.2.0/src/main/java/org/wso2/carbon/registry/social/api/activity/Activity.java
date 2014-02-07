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
package org.wso2.carbon.registry.social.api.activity;


import org.wso2.carbon.registry.social.api.people.userprofile.Person;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Representation of an activity.
 */

public interface Activity {

    /**
     * The fields that represent the activity object in json form.
     * <p/>
     * <p>
     * All of the fields that activities can have.
     * </p>
     * <p>
     * It is only OPTIONAL to set one of TITLE_ID or TITLE. In addition, if you are using any
     * variables in your title or title template, you must set TEMPLATE_PARAMS.
     * </p>
     * <p>
     * Other possible fields to set are: URL, MEDIA_ITEMS, BODY_ID, BODY, EXTERNAL_ID, PRIORITY,
     * STREAM_TITLE, STREAM_URL, STREAM_SOURCE_URL, and STREAM_FAVICON_URL.
     * </p>
     * <p>
     * Containers are only OPTIONAL to use TITLE_ID or TITLE, they may ignore additional parameters.
     * </p>
     */
    public static enum Field {
        /**
         * the json field for appId.
         */
        APP_ID("appId"),
        /**
         * the json field for body.
         */
        BODY("body"),
        /**
         * the json field for bodyId.
         */
        BODY_ID("bodyId"),
        /**
         * the json field for externalId.
         */
        EXTERNAL_ID("externalId"),
        /**
         * the json field for id.
         */
        ID("id"),
        /**
         * the json field for updated.
         */
        LAST_UPDATED("updated"), /* Needed to support the RESTful api */
        /**
         * the json field for mediaItems.
         */
        MEDIA_ITEMS("mediaItems"),
        /**
         * the json field for postedTime.
         */
        POSTED_TIME("postedTime"),
        /**
         * the json field for priority.
         */
        PRIORITY("priority"),
        /**
         * the json field for streamFaviconUrl.
         */
        STREAM_FAVICON_URL("streamFaviconUrl"),
        /**
         * the json field for streamSourceUrl.
         */
        STREAM_SOURCE_URL("streamSourceUrl"),
        /**
         * the json field for streamTitle.
         */
        STREAM_TITLE("streamTitle"),
        /**
         * the json field for streamUrl.
         */
        STREAM_URL("streamUrl"),
        /**
         * the json field for templateParams.
         */
        TEMPLATE_PARAMS("templateParams"),
        /**
         * the json field for title.
         */
        TITLE("title"),
        /**
         * the json field for titleId.
         */
        TITLE_ID("titleId"),
        /**
         * the json field for url.
         */
        URL("url"),
        /**
         * the json field for userId.
         */
        USER_ID("userId");

        /**
         * The json field that the instance represents.
         */
        private final String jsonString;

        /**
         * create a field base on the a json element.
         *
         * @param jsonString the name of the element
         */
        private Field(String jsonString) {
            this.jsonString = jsonString;
        }

        /**
         * emit the field as a json element.
         *
         * @return the field name
         */
        @Override
        public String toString() {
            return jsonString;
        }
    }

    /**
     * Get a string specifying the application that this activity is associated with. Container
     * support for this field is REQUIRED.
     *
     * @return A string specifying the application that this activity is associated with
     */
    String getAppId();

    /**
     * Set a string specifying the application that this activity is associated with. Container
     * support for this field is REQUIRED.
     *
     * @param appId A string specifying the application that this activity is associated with
     */
    void setAppId(String appId);

    /**
     * Get a string specifying an optional expanded version of an activity. Container support for this
     * field is OPTIONAL.
     *
     * @return a string specifying an optional expanded version of an activity.
     */
    String getBody();

    /**
     * Set a string specifying an optional expanded version of an activity. Container support for this
     * field is OPTIONAL.
     * <p/>
     * Bodies may only have the following HTML tags:&lt;b&gt; &lt;i&gt;, &lt;a&gt;, &lt;span&gt;. The
     * container may ignore this formatting when rendering the activity.
     *
     * @param body a string specifying an optional expanded version of an activity.
     */
    void setBody(String body);

    /**
     * Get a string specifying the body template message ID in the gadget spec. Container support for
     * this field is OPTIONAL.
     * <p/>
     * Bodies may only have the following HTML tags: &lt;b&gt; &lt;i&gt;, &lt;a&gt;, &lt;span&gt;. The
     * container may ignore this formatting when rendering the activity.
     *
     * @return a string specifying the body template message ID in the gadget spec.
     */
    String getBodyId();

    /**
     * Set a string specifying the body template message ID in the gadget spec. Container support for
     * this field is OPTIONAL.
     *
     * @param bodyId a string specifying the body template message ID in the gadget spec.
     */
    void setBodyId(String bodyId);

    /**
     * Get an optional string ID generated by the posting application. Container support for this
     * field is OPTIONAL.
     *
     * @return An optional string ID generated by the posting application.
     */
    String getExternalId();

    /**
     * Set an optional string ID generated by the posting application. Container support for this
     * field is OPTIONAL.
     *
     * @param externalId An optional string ID generated by the posting application.
     */
    void setExternalId(String externalId);

    /**
     * Get a string ID that is permanently associated with this activity. Container support for this
     * field is OPTIONAL.
     *
     * @return a string ID that is permanently associated with this activity.
     */
    String getId();

    /**
     * Set a string ID that is permanently associated with this activity. Container support for this
     * field is OPTIONAL.
     *
     * @param id a string ID that is permanently associated with this activity.
     */
    void setId(String id);

    /**
     * Get the last updated date of the Activity, additional to the Opensocial specification for the
     * REST-API. Container support for this field is OPTIONAL.
     *
     * @return the last updated date
     */
    Date getUpdated();

    /**
     * . Set the last updated date of the Activity, additional to the Opensocial specification for the
     * REST-API. Container support for this field is OPTIONAL.
     *
     * @param updated the last updated date
     */
    void setUpdated(Date updated);

    /**
     * Get any photos, videos, or images that should be associated with the activity.
     * <p/>
     * Container support for this field is OPTIONAL.
     *
     * @return A List of {@link MediaItem} containing any photos, videos, or images that should be
     *         associated with the activity.
     */
    List<MediaItem> getMediaItems();

    /**
     * Set any photos, videos, or images that should be associated with the activity. Container
     * support for this field is OPTIONAL.
     * <p/>
     * Higher priority ones are higher in the list.
     *
     * @param mediaItems a list of {@link MediaItem} to be associated with the activity
     */
    void setMediaItems(List<MediaItem> mediaItems);

    /**
     * Get the time at which this activity took place in milliseconds since the epoch. Container
     * support for this field is OPTIONAL.
     * <p/>
     * Higher priority ones are higher in the list.
     *
     * @return The time at which this activity took place in milliseconds since the epoch
     */
    Long getPostedTime();

    /**
     * Set the time at which this activity took place in milliseconds since the epoch Container
     * support for this field is OPTIONAL.
     * <p/>
     * This value can not be set by the end user.
     *
     * @param postedTime the time at which this activity took place in milliseconds since the epoch
     */
    void setPostedTime(Long postedTime);

    /**
     * Get the priority, a number between 0 and 1 representing the relative priority of this activity
     * in relation to other activities from the same source. Container support for this field is
     * OPTIONAL.
     *
     * @return a number between 0 and 1 representing the relative priority of this activity in
     *         relation to other activities from the same source
     */
    Float getPriority();

    /**
     * Set the priority, a number between 0 and 1 representing the relative priority of this activity
     * in relation to other activities from the same source. Container support for this field is
     * OPTIONAL.
     *
     * @param priority a number between 0 and 1 representing the relative priority of this activity in
     *                 relation to other activities from the same source.
     */
    void setPriority(Float priority);

    /**
     * Get a string specifying the URL for the stream's favicon. Container support for this field is
     * OPTIONAL.
     *
     * @return a string specifying the URL for the stream's favicon.
     */
    String getStreamFaviconUrl();

    /**
     * Set a string specifying the URL for the stream's favicon. Container support for this field is
     * OPTIONAL.
     *
     * @param streamFaviconUrl a string specifying the URL for the stream's favicon.
     */
    void setStreamFaviconUrl(String streamFaviconUrl);

    /**
     * Get a string specifying the stream's source URL. Container support for this field is OPTIONAL.
     *
     * @return a string specifying the stream's source URL.
     */
    String getStreamSourceUrl();

    /**
     * Set a string specifying the stream's source URL. Container support for this field is OPTIONAL.
     *
     * @param streamSourceUrl a string specifying the stream's source URL.
     */
    void setStreamSourceUrl(String streamSourceUrl);

    /**
     * Get a string specifing the title of the stream. Container support for this field is OPTIONAL.
     *
     * @return a string specifing the title of the stream.
     */
    String getStreamTitle();

    /**
     * Set a string specifing the title of the stream. Container support for this field is OPTIONAL.
     *
     * @param streamTitle a string specifing the title of the stream.
     */
    void setStreamTitle(String streamTitle);

    /**
     * Get a string specifying the stream's URL. Container support for this field is OPTIONAL.
     *
     * @return a string specifying the stream's URL.
     */
    String getStreamUrl();

    /**
     * Set a string specifying the stream's URL. Container support for this field is OPTIONAL.
     *
     * @param streamUrl a string specifying the stream's URL.
     */
    void setStreamUrl(String streamUrl);

    /**
     * Get a map of custom key/value pairs associated with this activity. Container support for this
     * field is OPTIONAL.
     *
     * @return a map of custom key/value pairs associated with this activity.
     */
    Map<String, String> getTemplateParams();

    /**
     * Set a map of custom key/value pairs associated with this activity. The data has type
     * {@link Map<String, Object>}. The object may be either a String or an {@link Person}. When
     * passing in a person with key PersonKey, can use the following replacement variables in the
     * template:
     * <ul>
     * <li>PersonKey.DisplayName - Display name for the person</li>
     * <li>PersonKey.ProfileUrl. URL of the person's profile</li>
     * <li>PersonKey.Id - The ID of the person</li>
     * <li>PersonKey - Container may replace with DisplayName, but may also optionally link to the
     * user.</li>
     * </ul>
     * Container support for this field is OPTIONAL.
     *
     * @param templateParams a map of custom key/value pairs associated with this activity.
     */
    void setTemplateParams(Map<String, String> templateParams);

    /**
     * Get a string specifying the primary text of an activity. Container support for this field is
     * REQUIRED.
     * <p/>
     * Titles may only have the following HTML tags: &lt;b&gt; &lt;i&gt;, &lt;a&gt;, &lt;span&gt;. The
     * container may ignore this formatting when rendering the activity.
     *
     * @return a string specifying the primary text of an activity.
     */
    String getTitle();

    /**
     * Set a string specifying the primary text of an activity. Container support for this field is
     * REQUIRED.
     * <p/>
     * Titles may only have the following HTML tags: &lt;b&gt; &lt;i&gt;, &lt;a&gt;, &lt;span&gt;. The
     * container may ignore this formatting when rendering the activity.
     *
     * @param title a string specifying the primary text of an activity.
     */
    void setTitle(String title);

    /**
     * Get a string specifying the title template message ID in the gadget spec. Container support for
     * this field is REQUIRED.
     * <p/>
     * The title is the primary text of an activity. Titles may only have the following HTML tags:
     * <&lt;b&gt; &lt;i&gt;, &lt;a&gt;, &lt;span&gt;. The container may ignore this formatting when
     * rendering the activity.
     *
     * @return a string specifying the title template message ID in the gadget spec.
     */
    String getTitleId();

    /**
     * Set a string specifying the title template message ID in the gadget spec. Container support for
     * this field is REQUIRED.
     * <p/>
     * The title is the primary text of an activity. Titles may only have the following HTML tags:
     * <&lt;b&gt; &lt;i&gt;, &lt;a&gt;, &lt;span&gt;. The container may ignore this formatting when
     * rendering the activity.
     *
     * @param titleId a string specifying the title template message ID in the gadget spec.
     */
    void setTitleId(String titleId);

    /**
     * Get a string specifying the URL that represents this activity. Container support for this field
     * is OPTIONAL.
     *
     * @return a string specifying the URL that represents this activity.
     */
    String getUrl();

    /**
     * Set a string specifying the URL that represents this activity. Container support for this field
     * is OPTIONAL.
     *
     * @param url a string specifying the URL that represents this activity.
     */
    void setUrl(String url);

    /**
     * Get a string ID of the user who this activity is for. Container support for this field is
     * OPTIONAL.
     *
     * @return a string ID of the user who this activity is for.
     */
    String getUserId();

    /**
     * Get a string ID of the user who this activity is for. Container support for this field is
     * OPTIONAL.
     *
     * @param userId a string ID of the user who this activity is for.
     */
    void setUserId(String userId);
}
