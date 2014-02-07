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

package org.apache.hadoop.hive.ql.udf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.hive.serde2.io.TimestampWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

/**
 * UDFDateSub.
 *
 */
@Description(name = "date_sub",
    value = "_FUNC_(start_date, num_days) - Returns the date that is num_days before start_date.",
    extended = "start_date is a string in the format 'yyyy-MM-dd HH:mm:ss' or"
    + " 'yyyy-MM-dd'. num_days is a number. The time part of start_date is "
    + "ignored.\n"
    + "Example:\n "
    + "  > SELECT _FUNC_('2009-30-07', 1) FROM src LIMIT 1;\n"
    + "  '2009-29-07'")
public class UDFDateSub extends UDF {
  private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
  private final Calendar calendar = Calendar.getInstance();

  private Text result = new Text();

  public UDFDateSub() {
  }

  /**
   * Subtract a number of days to the date. The time part of the string will be
   * ignored.
   *
   * NOTE: This is a subset of what MySQL offers as:
   * http://dev.mysql.com/doc/refman
   * /5.1/en/date-and-time-functions.html#function_date-sub
   *
   * @param dateString1
   *          the date string in the format of "yyyy-MM-dd HH:mm:ss" or
   *          "yyyy-MM-dd".
   * @param days
   *          the number of days to subtract.
   * @return the date in the format of "yyyy-MM-dd".
   */
  public Text evaluate(Text dateString1, IntWritable days) {

    if (dateString1 == null || days == null) {
      return null;
    }

    try {
      calendar.setTime(formatter.parse(dateString1.toString()));
      calendar.add(Calendar.DAY_OF_MONTH, -days.get());
      Date newDate = calendar.getTime();
      result.set(formatter.format(newDate));
      return result;
    } catch (ParseException e) {
      return null;
    }
  }

  public Text evaluate(TimestampWritable t, IntWritable days) {
    if (t == null || days == null) {
      return null;
    }
    calendar.setTime(t.getTimestamp());
    calendar.add(Calendar.DAY_OF_MONTH, -days.get());
    Date newDate = calendar.getTime();
    result.set(formatter.format(newDate));
    return result;
  }

}
