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

import org.apache.commons.codec.binary.Base64;
import org.apache.hadoop.hive.serde.Constants;
import org.apache.hadoop.hive.serde2.SerDeException;
import org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe.SerDeParameters;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.*;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

/**
 * LazyUtils.
 *
 */
public final class LazyUtils {

  /**
   * Returns the digit represented by character b.
   *
   * @param b
   *          The ascii code of the character
   * @param radix
   *          The radix
   * @return -1 if it's invalid
   */
  public static int digit(int b, int radix) {
    int r = -1;
    if (b >= '0' && b <= '9') {
      r = b - '0';
    } else if (b >= 'A' && b <= 'Z') {
      r = b - 'A' + 10;
    } else if (b >= 'a' && b <= 'z') {
      r = b - 'a' + 10;
    }
    if (r >= radix) {
      r = -1;
    }
    return r;
  }

  /**
   * Returns -1 if the first byte sequence is lexicographically less than the
   * second; returns +1 if the second byte sequence is lexicographically less
   * than the first; otherwise return 0.
   */
  public static int compare(byte[] b1, int start1, int length1, byte[] b2,
      int start2, int length2) {

    int min = Math.min(length1, length2);

    for (int i = 0; i < min; i++) {
      if (b1[start1 + i] == b2[start2 + i]) {
        continue;
      }
      if (b1[start1 + i] < b2[start2 + i]) {
        return -1;
      } else {
        return 1;
      }
    }

    if (length1 < length2) {
      return -1;
    }
    if (length1 > length2) {
      return 1;
    }
    return 0;
  }

  /**
   * Convert a UTF-8 byte array to String.
   *
   * @param bytes
   *          The byte[] containing the UTF-8 String.
   * @param start
   *          The start position inside the bytes.
   * @param length
   *          The length of the data, starting from "start"
   * @return The unicode String
   */
  public static String convertToString(byte[] bytes, int start, int length) {
    try {
      return Text.decode(bytes, start, length);
    } catch (CharacterCodingException e) {
      return null;
    }
  }

  private static byte[] trueBytes = {(byte) 't', 'r', 'u', 'e'};
  private static byte[] falseBytes = {(byte) 'f', 'a', 'l', 's', 'e'};

  /**
   * Write the bytes with special characters escaped.
   *
   * @param escaped
   *          Whether the data should be written out in an escaped way.
   * @param escapeChar
   *          if escaped, the char for prefixing special characters.
   * @param needsEscape
   *          if escaped, whether a specific character needs escaping. This
   *          array should have size of 128.
   */
  private static void writeEscaped(OutputStream out, byte[] bytes, int start,
      int len, boolean escaped, byte escapeChar, boolean[] needsEscape)
      throws IOException {
    if (escaped) {
      int end = start + len;
      for (int i = start; i <= end; i++) {
        if (i == end || (bytes[i] >= 0 && needsEscape[bytes[i]])) {
          if (i > start) {
            out.write(bytes, start, i - start);
          }
          start = i;
          if (i < len) {
            out.write(escapeChar);
            // the current char will be written out later.
          }
        }
      }
    } else {
      out.write(bytes, 0, len);
    }
  }

  /**
   * Write out the text representation of a Primitive Object to a UTF8 byte
   * stream.
   *
   * @param out
   *          The UTF8 byte OutputStream
   * @param o
   *          The primitive Object
   * @param needsEscape
   *          Whether a character needs escaping. This array should have size of
   *          128.
   */
  public static void writePrimitiveUTF8(OutputStream out, Object o,
      PrimitiveObjectInspector oi, boolean escaped, byte escapeChar,
      boolean[] needsEscape) throws IOException {

    switch (oi.getPrimitiveCategory()) {
    case BOOLEAN: {
      boolean b = ((BooleanObjectInspector) oi).get(o);
      if (b) {
        out.write(trueBytes, 0, trueBytes.length);
      } else {
        out.write(falseBytes, 0, falseBytes.length);
      }
      break;
    }
    case BYTE: {
      LazyInteger.writeUTF8(out, ((ByteObjectInspector) oi).get(o));
      break;
    }
    case SHORT: {
      LazyInteger.writeUTF8(out, ((ShortObjectInspector) oi).get(o));
      break;
    }
    case INT: {
      LazyInteger.writeUTF8(out, ((IntObjectInspector) oi).get(o));
      break;
    }
    case LONG: {
      LazyLong.writeUTF8(out, ((LongObjectInspector) oi).get(o));
      break;
    }
    case FLOAT: {
      float f = ((FloatObjectInspector) oi).get(o);
      ByteBuffer b = Text.encode(String.valueOf(f));
      out.write(b.array(), 0, b.limit());
      break;
    }
    case DOUBLE: {
      double d = ((DoubleObjectInspector) oi).get(o);
      ByteBuffer b = Text.encode(String.valueOf(d));
      out.write(b.array(), 0, b.limit());
      break;
    }
    case STRING: {
      Text t = ((StringObjectInspector) oi).getPrimitiveWritableObject(o);
      writeEscaped(out, t.getBytes(), 0, t.getLength(), escaped, escapeChar,
          needsEscape);
      break;
    }

    case BINARY: {
      BytesWritable bw = ((BinaryObjectInspector) oi).getPrimitiveWritableObject(o);
      byte[] toEncode = new byte[bw.getLength()];
      System.arraycopy(bw.getBytes(), 0,toEncode, 0, bw.getLength());
      byte[] toWrite = Base64.encodeBase64(toEncode);
      out.write(toWrite, 0, toWrite.length);
      break;
    }
    case TIMESTAMP: {
      LazyTimestamp.writeUTF8(out,
          ((TimestampObjectInspector) oi).getPrimitiveWritableObject(o));
      break;
    }
    default: {
      throw new RuntimeException("Hive internal error.");
    }
    }
  }



  public static void writeToCassandra(OutputStream out, Object o,
                                          PrimitiveObjectInspector oi, boolean escaped, byte escapeChar,
                                          boolean[] needsEscape) throws IOException {

        switch (oi.getPrimitiveCategory()) {
            case BOOLEAN: {
                boolean b = ((BooleanObjectInspector) oi).get(o);
                byte[] bytes = new byte[1];
                bytes[0] = b ? (byte) 1 : (byte) 0;
                ByteBuffer.wrap(bytes);
                out.write(bytes, 0, bytes.length);
                break;
            }
            case BYTE: {
                byte byteVal = ((ByteObjectInspector) oi).get(o);
                byte[] bytes = new byte[1];
                ByteBuffer.wrap(bytes).put(0,byteVal);
                out.write(bytes, 0, bytes.length);
                break;
            }
            case SHORT: {
                short s = ((ShortObjectInspector) oi).get(o);
                byte[] bytes = new byte[2];
                ByteBuffer.wrap(bytes).putShort(0,s);
                out.write(bytes, 0, bytes.length);
                break;
            }
            case INT: {
                int i = ((IntObjectInspector) oi).get(o);
                byte[] bytes = new byte[4];
                ByteBuffer.wrap(bytes).putInt(0,i);
                out.write(bytes, 0, bytes.length);
                break;
            }
            case LONG: {
                Long longVal = ((LongObjectInspector) oi).get(o);
                byte[] bytes = new byte[8];
                ByteBuffer.wrap(bytes).putLong(0,longVal);
                out.write(bytes, 0, bytes.length);
                break;
            }
            case FLOAT: {
                float f = ((FloatObjectInspector) oi).get(o);
                byte[] bytes = new byte[4];
                ByteBuffer.wrap(bytes).putFloat(0,f);
                out.write(bytes, 0, bytes.length);
                break;
            }
            case DOUBLE: {
                double d = ((DoubleObjectInspector) oi).get(o);
                byte[] bytes = new byte[8];
                ByteBuffer.wrap(bytes).putDouble(0,d);
                out.write(bytes, 0, bytes.length);
                break;
            }
            case STRING: {
                Text t = ((StringObjectInspector) oi).getPrimitiveWritableObject(o);
                writeEscaped(out, t.getBytes(), 0, t.getLength(), escaped, escapeChar,
                        needsEscape);
                break;
            }

            case BINARY: {
                BytesWritable bw = ((BinaryObjectInspector) oi).getPrimitiveWritableObject(o);
                byte[] toEncode = new byte[bw.getLength()];
                System.arraycopy(bw.getBytes(), 0,toEncode, 0, bw.getLength());
                out.write(toEncode, 0, toEncode.length);
                break;
            }
            case TIMESTAMP: {
                LazyTimestamp.writeUTF8(out,
                        ((TimestampObjectInspector) oi).getPrimitiveWritableObject(o));
                break;
            }
            default: {
                throw new RuntimeException("Hive internal error.");
            }
        }
    }

  public static int hashBytes(byte[] data, int start, int len) {
    int hash = 1;
    for (int i = start; i < len; i++) {
      hash = (31 * hash) + data[i];
    }
    return hash;
  }

  public static void extractColumnInfo(Properties tbl, SerDeParameters serdeParams,
      String serdeName) throws SerDeException {
    // Read the configuration parameters
    String columnNameProperty = tbl.getProperty(Constants.LIST_COLUMNS);
    // NOTE: if "columns.types" is missing, all columns will be of String type
    String columnTypeProperty = tbl.getProperty(Constants.LIST_COLUMN_TYPES);

    // Parse the configuration parameters

    if (columnNameProperty != null && columnNameProperty.length() > 0) {
      serdeParams.columnNames = Arrays.asList(columnNameProperty.split(","));
    } else {
      serdeParams.columnNames = new ArrayList<String>();
    }
    if (columnTypeProperty == null) {
      // Default type: all string
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < serdeParams.columnNames.size(); i++) {
        if (i > 0) {
          sb.append(":");
        }
        sb.append(Constants.STRING_TYPE_NAME);
      }
      columnTypeProperty = sb.toString();
    }

    serdeParams.columnTypes = TypeInfoUtils
        .getTypeInfosFromTypeString(columnTypeProperty);

    if (serdeParams.columnNames.size() != serdeParams.columnTypes.size()) {
      throw new SerDeException(serdeName + ": columns has "
          + serdeParams.columnNames.size()
          + " elements while columns.types has "
          + serdeParams.columnTypes.size() + " elements!");
    }
  }

  private LazyUtils() {
    // prevent instantiation
  }

}
