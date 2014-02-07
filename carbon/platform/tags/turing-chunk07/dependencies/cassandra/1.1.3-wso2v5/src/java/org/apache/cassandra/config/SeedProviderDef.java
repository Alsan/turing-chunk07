package org.apache.cassandra.config;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


public class SeedProviderDef
{
    public String class_name;
    public Map<String, String> parameters;

    public SeedProviderDef(LinkedHashMap p)
    {
        class_name = (String)p.get("class_name");
        parameters = (Map<String, String>)((List)p.get("parameters")).get(0);
    }
}
