package org.apache.cassandra.gms;
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


import static org.junit.Assert.*;

import org.junit.Test;

public class ArrivalWindowTest
{

    @Test
    public void test()
    {
        ArrivalWindow window = new ArrivalWindow(4);
        //base readings
        window.add(111);
        window.add(222);
        window.add(333);
        window.add(444);
        window.add(555);

        //all good
        assertEquals(0.4342, window.phi(666), 0.01);

        //oh noes, a much higher timestamp, something went wrong!
        assertEquals(9.566, window.phi(3000), 0.01);
    }


}
