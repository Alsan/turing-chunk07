/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.wso2.carbon.governance.list.util;


public class StringComparatorUtil {

    private StringComparatorUtil() {}

    public static int compare(String versionString1, String versionString2) {

        versionString1 = versionString1.toLowerCase();
        versionString2 = versionString2.toLowerCase();

        int ia = 0, ib = 0;
        int nza, nzb;
        char ca, cb;
        int result;

        while (true)
        {
            // only count the number of zeroes leading the last number compared
            nza = nzb = 0;

            ca = charAt(versionString2, ia);
            cb = charAt(versionString1, ib);

            // skip over leading spaces or zeros
            while (Character.isSpaceChar(ca) || ca == '0')
            {
                if (ca == '0')
                {
                    nza++;
                }
                else
                {
                    // only count consecutive zeroes
                    nza = 0;
                }

                ca = charAt(versionString2, ++ia);
            }

            while (Character.isSpaceChar(cb) || cb == '0')
            {
                if (cb == '0')
                {
                    nzb++;
                }
                else
                {
                    // only count consecutive zeroes
                    nzb = 0;
                }

                cb = charAt(versionString1, ++ib);
            }

            // process run of digits
            if (Character.isDigit(ca) && Character.isDigit(cb))
            {
                if ((result = compareRight(versionString2.substring(ia), versionString1.substring(ib))) != 0)
                {
                    return result;
                }
            }

            if (ca == 0 && cb == 0)
            {
                // The strings compare the same. Perhaps the caller
                // will want to call strcmp to break the tie.
                return nza - nzb;
            }

            if (ca < cb)
            {
                return -1;
            }
            else if (ca > cb)
            {
                return +1;
            }

            ++ia;
            ++ib;
        }
    }

    private static char charAt(String s, int i)
    {
        if (i >= s.length())
        {
            return 0;
        }
        else
        {
            return s.charAt(i);
        }
    }

    private static int compareRight(String a, String b)
    {
        int bias = 0;
        int ia = 0;
        int ib = 0;

        // The longest run of digits wins. That aside, the greatest
        // value wins, but we can't know that it will until we've scanned
        // both numbers to know that they have the same magnitude, so we
        // remember it in BIAS.
        for (;; ia++, ib++)
        {
            char ca = charAt(a, ia);
            char cb = charAt(b, ib);

            if (!Character.isDigit(ca) && !Character.isDigit(cb))
            {
                return bias;
            }
            else if (!Character.isDigit(ca))
            {
                return -1;
            }
            else if (!Character.isDigit(cb))
            {
                return +1;
            }
            else if (ca < cb)
            {
                if (bias == 0)
                {
                    bias = -1;
                }
            }
            else if (ca > cb)
            {
                if (bias == 0)
                    bias = +1;
            }
            else if (ca == 0 && cb == 0)
            {
                return bias;
            }
        }
    }
}
