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
package org.apache.hadoop.tools.rumen;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

/**
 * Simple wrapper around {@link JsonGenerator} to write objects in JSON format.
 * @param <T> The type of the objects to be written.
 */
public class JsonObjectMapperWriter<T> implements Closeable {
  private JsonGenerator writer;
  
  public JsonObjectMapperWriter(OutputStream output, boolean prettyPrint) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(
        SerializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS, true);
    mapper.getJsonFactory();
    writer = mapper.getJsonFactory().createJsonGenerator(
        output, JsonEncoding.UTF8);
    if (prettyPrint) {
      writer.useDefaultPrettyPrinter();
    }
  }
  
  public void write(T object) throws IOException {
    writer.writeObject(object);
  }
  
  @Override
  public void close() throws IOException {
    writer.close();
  }
}
