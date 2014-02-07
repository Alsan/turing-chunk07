/**
 * Copyright (c) 2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.hive.metastore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.hooks.JDOConnectionURLHook;

// WSO2 Fix. Holds multi tenant Hive configurations
public class HiveContext {

    private static final Log log = LogFactory.getLog(HiveContext.class);

    private static final String DATABASE_WAREHOUSE_SUFFIX = ".db";

    private static final ThreadLocal<Integer> tenantIdThreadLocal = new ThreadLocal<Integer>();

    private static Map<Integer, HiveContext> contextMap =
            new ConcurrentHashMap<Integer, HiveContext>();

    private static Map<Integer, Map<String, String>> tenantPropertyMap =
            new ConcurrentHashMap<Integer, Map<String, String>>();

    private static JDOConnectionURLHook urlHook = null;
    private static String urlHookClassName = "";

    private static ClassLoader classLoader;

    static {
        classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = HiveConf.class.getClassLoader();
        }
    }

    private final HiveConf conf; // Tenant specific Hive Configuration. Holds tenant meta store database
    // information

    private HiveContext(HiveConf conf) {
        this.conf = conf;

        int tenantId = getTenantId();
        String whRootString = HiveConf.getVar(conf, HiveConf.ConfVars.METASTOREWAREHOUSE);
        whRootString = whRootString + tenantId;

        HiveConf.setVar(conf, HiveConf.ConfVars.METASTOREWAREHOUSE, whRootString);

    }

    public static void startTenantFlow(int tenantId) {
        tenantIdThreadLocal.set(tenantId);
    }

    public static void endTenantFlow() {
        tenantIdThreadLocal.remove();
    }

    public static HiveContext getCurrentContext() {
        int tenantId = tenantIdThreadLocal.get();
        HiveContext context = contextMap.get(tenantId);
        if (context == null) {
            HiveConf conf = new HiveConf();
            conf.setInt(HiveConf.ConfVars.CURRENTTENANT.varname, tenantId);
            setPropertiesToConf(tenantId, conf);
            context = new HiveContext(conf);
            contextMap.put(tenantId, context);
            return context;
        }
        setPropertiesToConf(tenantId, context.getConf());
        return context;
    }

    public HiveConf getConf() {
        return conf;
    }

    public static void startTenantFlow(int tenantId, HiveConf hiveConf) {
        startTenantFlow(tenantId);
        HiveContext context = contextMap.get(tenantId);
        if (context == null) {
            hiveConf.setInt(HiveConf.ConfVars.CURRENTTENANT.varname, tenantId);
            setPropertiesToConf(tenantId, hiveConf);
            context = new HiveContext(hiveConf);
            contextMap.put(tenantId, context);
        }
    }

    public int getTenantId() {
        return tenantIdThreadLocal.get();
    }

    public void setProperty(String key, String value) {
        Map<String, String> properties = tenantPropertyMap.get(getTenantId());
        if (properties == null) {
            properties = new ConcurrentHashMap<String, String>();
            tenantPropertyMap.put(getTenantId(), properties);
        }
        if (null == value) {
            properties.remove(key);
            getConf().set(key, "");
        } else {
            properties.put(key, value);
        }
    }

    public String getProperty(String key) {
        Map<String, String> properties = tenantPropertyMap.get(getTenantId());
        if (properties == null) {
            return properties.get(key);
        }
        return null;
    }

    private static void setPropertiesToConf(int tenantId, HiveConf conf) {
        Map<String, String> propertyMap = tenantPropertyMap.get(tenantId);
        if (propertyMap != null) {
            for (Map.Entry<String, String> entry : propertyMap.entrySet()) {
                conf.set(entry.getKey(), entry.getValue());
            }
        }
    }

}
