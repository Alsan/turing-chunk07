/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.vfs2.test;


/**
 * The suite of tests for a file system.
 *
 * @author <a href="mailto:adammurdoch@apache.org">Adam Murdoch</a>
 * @author Gary D. Gregory
 * @version $Id: CacheTestSuite.java 1034665 2010-11-13 05:50:50Z rgoers $
 */
public class CacheTestSuite
    extends AbstractTestSuite
{
    /**
     * Adds the tests for a file system to this suite.
     */
    public CacheTestSuite(final ProviderTestConfig providerConfig) throws Exception
    {
        this(providerConfig, "", false);
    }

    protected CacheTestSuite(final ProviderTestConfig providerConfig,
                             final String prefix,
                             final boolean nested)
        throws Exception
    {
        super(providerConfig, prefix, nested);
    }
}
