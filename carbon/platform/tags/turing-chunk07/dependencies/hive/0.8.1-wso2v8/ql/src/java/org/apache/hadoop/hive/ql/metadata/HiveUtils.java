/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.hive.ql.metadata;

import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.common.JavaUtils;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.ql.index.HiveIndexHandler;
import org.apache.hadoop.hive.ql.parse.AbstractSemanticAnalyzerHook;
import org.apache.hadoop.hive.ql.security.HadoopDefaultAuthenticator;
import org.apache.hadoop.hive.ql.security.HiveAuthenticationProvider;
import org.apache.hadoop.hive.ql.security.authorization.DefaultHiveAuthorizationProvider;
import org.apache.hadoop.hive.ql.security.authorization.HiveAuthorizationProvider;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.hadoop.hive.metastore.api.FieldSchema;

/**
 * General collection of helper functions.
 * 
 */
public final class HiveUtils {

  public static final char QUOTE = '"';
  public static final char COLON = ':';
  public static final String LBRACKET = "[";
  public static final String RBRACKET = "]";
  public static final String LBRACE = "{";
  public static final String RBRACE = "}";
  public static final String LINE_SEP = System.getProperty("line.separator");

  public static String escapeString(String str) {
    int length = str.length();
    StringBuilder escape = new StringBuilder(length + 16);

    for (int i = 0; i < length; ++i) {
      char c = str.charAt(i);
      switch (c) {
      case '"':
      case '\\':
        escape.append('\\');
        escape.append(c);
        break;
      case '\b':
        escape.append('\\');
        escape.append('b');
        break;
      case '\f':
        escape.append('\\');
        escape.append('f');
        break;
      case '\n':
        escape.append('\\');
        escape.append('n');
        break;
      case '\r':
        escape.append('\\');
        escape.append('r');
        break;
      case '\t':
        escape.append('\\');
        escape.append('t');
        break;
      default:
        // Control characeters! According to JSON RFC u0020
        if (c < ' ') {
          String hex = Integer.toHexString(c);
          escape.append('\\');
          escape.append('u');
          for (int j = 4; j > hex.length(); --j) {
            escape.append('0');
          }
          escape.append(hex);
        } else {
          escape.append(c);
        }
        break;
      }
    }
    return (escape.toString());
  }

  public static String lightEscapeString(String str) {
    int length = str.length();
    StringBuilder escape = new StringBuilder(length + 16);

    for (int i = 0; i < length; ++i) {
      char c = str.charAt(i);
      switch (c) {
      case '\n':
        escape.append('\\');
        escape.append('n');
        break;
      case '\r':
        escape.append('\\');
        escape.append('r');
        break;
      case '\t':
        escape.append('\\');
        escape.append('t');
        break;
      default:
        escape.append(c);
        break;
      }
    }
    return (escape.toString());
  }

  /**
   * Regenerate an identifier as part of unparsing it back to SQL text.
   */
  public static String unparseIdentifier(String identifier) {
    // In the future, if we support arbitrary characters in
    // identifiers, then we'll need to escape any backticks
    // in identifier by doubling them up.
    return "`" + identifier + "`";
  }

  public static HiveStorageHandler getStorageHandler(
    Configuration conf, String className) throws HiveException {
    
    if (className == null) {
      return null;
    }
    try {
      Class<? extends HiveStorageHandler> handlerClass =
        (Class<? extends HiveStorageHandler>)
        Class.forName(className, true, JavaUtils.getClassLoader());
      HiveStorageHandler storageHandler = (HiveStorageHandler)
        ReflectionUtils.newInstance(handlerClass, conf);
      return storageHandler;
    } catch (ClassNotFoundException e) {
      throw new HiveException("Error in loading storage handler."
          + e.getMessage(), e);
    }
  }

  private HiveUtils() {
    // prevent instantiation
  }

  public static HiveIndexHandler getIndexHandler(HiveConf conf,
      String indexHandlerClass) throws HiveException {

    if (indexHandlerClass == null) {
      return null;
    }
    try {
      Class<? extends HiveIndexHandler> handlerClass =
        (Class<? extends HiveIndexHandler>)
        Class.forName(indexHandlerClass, true, JavaUtils.getClassLoader());
      HiveIndexHandler indexHandler = (HiveIndexHandler)
        ReflectionUtils.newInstance(handlerClass, conf);
      return indexHandler;
    } catch (ClassNotFoundException e) {
      throw new HiveException("Error in loading index handler."
          + e.getMessage(), e);
    }
  }
  
  @SuppressWarnings("unchecked")
  public static HiveAuthorizationProvider getAuthorizeProviderManager(
      Configuration conf, HiveAuthenticationProvider authenticator) throws HiveException {

    String clsStr = HiveConf.getVar(conf,
        HiveConf.ConfVars.HIVE_AUTHORIZATION_MANAGER);

    HiveAuthorizationProvider ret = null;
    try {
      Class<? extends HiveAuthorizationProvider> cls = null;
      if (clsStr == null || clsStr.trim().equals("")) {
        cls = DefaultHiveAuthorizationProvider.class;
      } else {
        cls = (Class<? extends HiveAuthorizationProvider>) Class.forName(
            clsStr, true, JavaUtils.getClassLoader());
      }
      if (cls != null) {
        ret = ReflectionUtils.newInstance(cls, conf);
      }
    } catch (Exception e) {
      throw new HiveException(e);
    }
    ret.setAuthenticator(authenticator);
    return ret;
  }

  @SuppressWarnings("unchecked")
  public static HiveAuthenticationProvider getAuthenticator(Configuration conf)
      throws HiveException {

    String clsStr = HiveConf.getVar(conf,
        HiveConf.ConfVars.HIVE_AUTHENTICATOR_MANAGER);

    HiveAuthenticationProvider ret = null;
    try {
      Class<? extends HiveAuthenticationProvider> cls = null;
      if (clsStr == null || clsStr.trim().equals("")) {
        cls = HadoopDefaultAuthenticator.class;
      } else {
        cls = (Class<? extends HiveAuthenticationProvider>) Class.forName(
            clsStr, true, JavaUtils.getClassLoader());
      }
      if (cls != null) {
        ret = ReflectionUtils.newInstance(cls, conf);
      }
    } catch (Exception e) {
      throw new HiveException(e);
    }
    return ret;
  }

  public static AbstractSemanticAnalyzerHook getSemanticAnalyzerHook(
      HiveConf conf, String hookName) throws HiveException{
    try {
      Class<? extends AbstractSemanticAnalyzerHook> hookClass =
        (Class<? extends AbstractSemanticAnalyzerHook>)
        Class.forName(hookName, true, JavaUtils.getClassLoader());
      return (AbstractSemanticAnalyzerHook) ReflectionUtils.newInstance(
          hookClass, conf);
    } catch (ClassNotFoundException e) {
      throw new HiveException("Error in loading semantic analyzer hook: "+
          hookName +e.getMessage(),e);
    }

  }


    /**
     * Convert FieldSchemas to columnNames with backticks around them.
     */
    public static String getUnparsedColumnNamesFromFieldSchema(
        List<FieldSchema> fieldSchemas) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < fieldSchemas.size(); i++) {
        if (i > 0) {
          sb.append(",");
        }
        sb.append(HiveUtils.unparseIdentifier(fieldSchemas.get(i).getName()));
      }
      return sb.toString();
    }
}
