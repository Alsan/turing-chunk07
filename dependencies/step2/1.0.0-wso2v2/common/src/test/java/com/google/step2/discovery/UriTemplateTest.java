/**
 * Copyright 2009 Google Inc.
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
 *
 */
package com.google.step2.discovery;

import junit.framework.TestCase;

import java.net.URI;
import java.net.URLEncoder;

public class UriTemplateTest extends TestCase {

  public void testMap() throws Exception {
    UriTemplate template = new UriTemplate("http://foo?uri={%uri}");
    String url = "http://bla.com";

    assertEquals("http://foo?uri=" + URLEncoder.encode(url, "UTF-8"),
        template.map(URI.create(url)).toString());


    template = new UriTemplate("{uri};about");
    url = "http://bla.com/";

    assertEquals("http://bla.com/;about",
        template.map(URI.create(url)).toString());
  }
}
