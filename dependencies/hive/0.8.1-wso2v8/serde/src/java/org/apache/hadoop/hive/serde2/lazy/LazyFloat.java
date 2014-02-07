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
package org.apache.hadoop.hive.serde2.lazy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.serde2.lazy.objectinspector.primitive.LazyFloatObjectInspector;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;

/**
 * LazyObject for storing a value of Double.
 * 
 */
public class LazyFloat extends
    LazyPrimitive<LazyFloatObjectInspector, FloatWritable> {

  private static final Log LOG = LogFactory.getLog(LazyFloat.class);
  public LazyFloat(LazyFloatObjectInspector oi) {
    super(oi);
    data = new FloatWritable();
  }

  public LazyFloat(LazyFloat copy) {
    super(copy);
    data = new FloatWritable(copy.data.get());
  }

  @Override
  public void init(ByteArrayRef bytes, int start, int length) {
    String byteData = null;
    try {
      byteData = Text.decode(bytes.getData(), start, length);
      data.set(Float.parseFloat(byteData));
      isNull = false;
    } catch (NumberFormatException e) {
      float value = ByteBuffer.wrap(bytes.getData(), start, length).asFloatBuffer().get();
      data.set(value);
      isNull = false;
      LOG.debug("Handling number format exception", e);
    } catch (CharacterCodingException e) {
      isNull = true;
      LOG.debug("Data not in the Float data type range so converted to null.", e);
    }
  }

}
