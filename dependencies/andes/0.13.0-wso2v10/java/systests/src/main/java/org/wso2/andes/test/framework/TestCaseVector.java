/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
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
 *
 */
package org.wso2.andes.test.framework;

/**
 * <p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td>
 * </table>
 */
public class TestCaseVector
{
    /** The test case name. */
    private String testCase;

    /** The test cycle number within the test case. */
    private int testCycleNumber;

    public TestCaseVector(String testCase, int testCycleNumber)
    {
        this.testCase = testCase;
        this.testCycleNumber = testCycleNumber;
    }

    public String getTestCase()
    {
        return testCase;
    }

    public int getTestCycleNumber()
    {
        return testCycleNumber;
    }

    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }

        if ((o == null) || (getClass() != o.getClass()))
        {
            return false;
        }

        TestCaseVector that = (TestCaseVector) o;

        if (testCycleNumber != that.testCycleNumber)
        {
            return false;
        }

        if ((testCase != null) ? (!testCase.equals(that.testCase)) : (that.testCase != null))
        {
            return false;
        }

        return true;
    }

    public int hashCode()
    {
        int result;
        result = ((testCase != null) ? testCase.hashCode() : 0);
        result = (31 * result) + testCycleNumber;

        return result;
    }
}
