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
package org.apache.hive.pdk;

import org.apache.hadoop.hive.ql.exec.Description;

public class FunctionExtractor {
  public static void main(String [] args) throws Exception {
    System.out.println("<ClassList>");
    for (String arg : args) {
      Class<?> c = Class.forName(arg);
      Description d = c.getAnnotation(Description.class);
      if (d == null) {
        continue;
      }
      System.out.print("    <Class javaname=\"");
      System.out.print(c.getName());
      System.out.print("\" sqlname=\"");
      System.out.print(d.name());
      System.out.println("\" />");
    }
    System.out.println("</ClassList>");
  }
}
