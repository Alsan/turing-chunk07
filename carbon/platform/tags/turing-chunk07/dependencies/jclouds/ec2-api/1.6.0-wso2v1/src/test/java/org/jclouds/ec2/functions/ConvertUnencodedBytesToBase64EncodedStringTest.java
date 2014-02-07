/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.ec2.functions;

import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Tests behavior of {@code ConvertUnencodedBytesToBase64EncodedString}
 * 
 * @author Adrian Cole
 */
@Test(groups = "unit")
public class ConvertUnencodedBytesToBase64EncodedStringTest {
   Injector injector = Guice.createInjector();

   public void testDefault() throws IOException {
      ConvertUnencodedBytesToBase64EncodedString function = injector
               .getInstance(ConvertUnencodedBytesToBase64EncodedString.class);

      assertEquals("dGVzdA==", function.apply("test".getBytes()));
   }

}
