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
package org.wso2.carbon.registry.social.api.people.userprofile.model;

public enum Smoker {
    /**
     * A heavy smoker.
     */
    HEAVILY("HEAVILY", "Heavily"),
    /**
     * Non smoker.
     */
    NO("NO", "No"),
    /**
     * Smokes occasionally.
     */
    OCCASIONALLY("OCCASIONALLY", "Ocasionally"),
    /**
     * Has quit smoking.
     */
    QUIT("QUIT", "Quit"),
    /**
     * in the process of quitting smoking.
     */
    QUITTING("QUITTING", "Quitting"),
    /**
     * regular smoker, but not a heavy smoker.
     */
    REGULARLY("REGULARLY", "Regularly"),
    /**
     * smokes socially.
     */
    SOCIALLY("SOCIALLY", "Socially"),
    /**
     * yes, a smoker.
     */
    YES("YES", "Yes");

    /**
     * The Json representation of the value.
     */
    private final String jsonString;

    /**
     * The value used for display purposes.
     */
    private final String displayValue;

    /**
     * Create a Smoker enumeration.
     *
     * @param jsonString   the json representation of the value.
     * @param displayValue the value used for display purposes.
     */
    private Smoker(String jsonString, String displayValue) {
        this.jsonString = jsonString;
        this.displayValue = displayValue;
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return this.jsonString;
    }

    /**
     *
     */
    public String getDisplayValue() {
        return displayValue;
    }
}
