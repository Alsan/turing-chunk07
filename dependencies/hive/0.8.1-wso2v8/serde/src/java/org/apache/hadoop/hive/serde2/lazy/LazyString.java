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

import org.apache.hadoop.hive.serde2.lazy.objectinspector.primitive.LazyStringObjectInspector;
import org.apache.hadoop.io.Text;

/**
 * LazyObject for storing a value of String.
 */
public class LazyString extends LazyPrimitive<LazyStringObjectInspector, Text> {

  public LazyString(LazyStringObjectInspector oi) {
    super(oi);
    data = new Text();
  }

  public LazyString(LazyString copy) {
    super(copy);
    data = new Text(copy.data);
  }

  @Override
  public void init(ByteArrayRef bytes, int start, int length) {
    if (oi.isEscaped()) {
      byte escapeChar = oi.getEscapeChar();
      byte[] inputBytes = bytes.getData();

      // First calculate the length of the output string
      int outputLength = 0;
      for (int i = 0; i < length; i++) {
        if (inputBytes[start + i] != escapeChar) {
          outputLength++;
        } else {
          outputLength++;
          i++;
        }
      }

      // Copy the data over, so that the internal state of Text will be set to
      // the required outputLength.
      data.set(bytes.getData(), start, outputLength);

      // We need to copy the data byte by byte only in case the
      // "outputLength < length" (which means there is at least one escaped
      // byte.
      if (outputLength < length) {
        int k = 0;
        byte[] outputBytes = data.getBytes();
        for (int i = 0; i < length; i++) {
          byte b = inputBytes[start + i];
          if (b != escapeChar || i == length - 1) {
            outputBytes[k++] = b;
          } else {
            // get the next byte
            i++;
            outputBytes[k++] = inputBytes[start + i];
          }
        }
        assert (k == outputLength);
      }
    } else {
      // if the data is not escaped, simply copy the data.
      data.set(bytes.getData(), start, length);
    }
  }

}
