/*
 * Copyright 2011 LMAX Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lmax.disruptor;

import org.junit.Assert;

public abstract class AbstractPerfTestQueueVsDisruptor
{
    public static final int RUNS = 20;

    protected void testImplementations()
        throws Exception
    {
        final int availableProcessors = Runtime.getRuntime().availableProcessors();
        if (getRequiredProcessorCount() > availableProcessors)
        {
            System.out.print("*** Warning ***: your system has insufficient processors to execute the test efficiently. ");
            System.out.println("Processors required = " + getRequiredProcessorCount() + " available = " + availableProcessors);
        }

        long[] queueOps = new long[RUNS];
        long[] disruptorOps = new long[RUNS];

        if ("true".equalsIgnoreCase(System.getProperty("com.lmax.runQueueTests", "false")))
        {
            System.out.println("Starting Queue tests");
            for (int i = 0; i < RUNS; i++)
            {
                System.gc();
                queueOps[i] = runQueuePass();
                System.out.format("Run %d, BlockingQueue=%,d ops/sec%n", i, Long.valueOf(queueOps[i]));
            }
        }
        else
        {
            System.out.println("Skipping Queue tests");
        }

        System.out.println("Starting Disruptor tests");
        for (int i = 0; i < RUNS; i++)
        {
            System.gc();
            disruptorOps[i] = runDisruptorPass();
            System.out.format("Run %d, Disruptor=%,d ops/sec%n", i, Long.valueOf(disruptorOps[i]));
        }

//        printResults(getClass().getSimpleName(), disruptorOps, queueOps);

        for (int i = 0; i < RUNS; i++)
        {
            Assert.assertTrue("Performance degraded", disruptorOps[i] > queueOps[i]);
        }
    }

    public static void printResults(final String className, final long[] disruptorOps, final long[] queueOps)
    {
        for (int i = 0; i < RUNS; i++)
        {
            System.out.format("%s run %d: BlockingQueue=%,d Disruptor=%,d ops/sec\n",
                              className, Integer.valueOf(i), Long.valueOf(queueOps[i]), Long.valueOf(disruptorOps[i]));
        }
    }

    protected abstract int getRequiredProcessorCount();

    protected abstract long runQueuePass() throws Exception;

    protected abstract long runDisruptorPass() throws Exception;

    protected abstract void shouldCompareDisruptorVsQueues() throws Exception;
}
