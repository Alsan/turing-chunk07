/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.wso2.carbon.cassandra.search.engine;

import org.wso2.carbon.cassandra.search.exception.CassandraSearchException;
import org.wso2.carbon.cassandra.search.utils.OperationType;
import org.wso2.carbon.cassandra.search.utils.SearchConstants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StatementParser {
    private Map<String, List<Filter>> streamFilters;

    public StatementParser() {
        this.streamFilters = new LinkedHashMap<String, List<Filter>>();
    }

    public Map<String, List<Filter>> extractFilters(String searchQuery) throws CassandraSearchException {
        if(searchQuery == null || searchQuery.isEmpty()) {
            throw new CassandraSearchException("Null or Empty search query..");
        }

        String query;

        query = searchQuery.replaceAll(" AND | and ", "|AND|");
        query = query.replaceAll(" OR | or ", "|OR|");

        String[] conditions = query.split("\\|");
        try {
            for (int i = 0; i < conditions.length; i++) {
                OperationType joinOp = null;

                if (i != 0) {
                    try {
                        joinOp = OperationType.valueOf(conditions[i++]);
                    } catch (Exception e) {
                        --i;
                    }
                }

                String operator = getOperator(conditions[i]);

                if(operator == null) {
                    throw new CassandraSearchException("Unable to extract Filters from the search query..");
                }


                String[] fragment = conditions[i].split(operator);

                int streamSeparatorPos = fragment[0].lastIndexOf('.');
                String streamName = fragment[0].substring(0, streamSeparatorPos).trim();
                String property = fragment[0].substring(streamSeparatorPos + 1).trim();
                String value    = fragment[1].trim();

                Filter searchFilter = new Filter(property, operator, value, joinOp);

                List<Filter> filtersList = null;
                if(!streamFilters.containsKey(streamName)) {
                    filtersList = new ArrayList<Filter>();
                    filtersList.add(searchFilter);
                    streamFilters.put(streamName, filtersList);
                } else {
                    streamFilters.get(streamName).add(searchFilter);
                }
            }
        } catch (Exception e) {
            throw new CassandraSearchException("Unable to extract Filters from the search query: " + e.getMessage());
        }

        return streamFilters;
    }

    public String getOperator(String criteria) {
        if(criteria.indexOf(SearchConstants.GE) > 0) {
            return SearchConstants.GE;
        }
        if(criteria.indexOf(SearchConstants.LE) > 0) {
            return SearchConstants.LE;
        }
        if(criteria.indexOf(SearchConstants.EQ) > 0) {
            return SearchConstants.EQ;
        }
        if(criteria.indexOf(SearchConstants.LT) > 0) {
            return SearchConstants.LT;
        }
        if(criteria.indexOf(SearchConstants.GT) > 0) {
            return SearchConstants.GT;
        }

        return null;

    }
}
