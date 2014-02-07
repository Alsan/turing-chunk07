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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.vfs2.Capability;
import org.apache.commons.vfs2.FileObject;

/**
 * URL test cases for providers.
 *
 * @author <a href="mailto:adammurdoch@apache.org">Adam Murdoch</a>
 * @version $Revision: 1040766 $ $Date: 2010-12-01 02:06:53 +0530 (Wed, 01 Dec 2010) $
 */
public class UrlTests
    extends AbstractProviderTestCase
{
    /**
     * Returns the capabilities required by the tests of this test case.  The
     * tests are not run if the provider being tested does not support all
     * the required capabilities.  Return null or an empty array to always
     * run the tests.
     * <p/>
     * <p>This implementation returns null.
     */
    @Override
    protected Capability[] getRequiredCaps()
    {
        return new Capability[]{Capability.URI};
    }

    /**
     * Tests url.
     */
    public void testURL() throws Exception
    {
        final FileObject file = getReadFolder().resolveFile("some-dir/");
        final URL url = file.getURL();

        assertEquals(file.getName().getURI(), url.toExternalForm());

        final URL parentURL;
        try
        {
            parentURL = new URL(url, "..");
        }
        catch (MalformedURLException e)
        {
            throw e;
        }
        assertEquals(file.getParent().getURL(), parentURL);

        final URL rootURL = new URL(url, "/");
        assertEquals(file.getFileSystem().getRoot().getURL(), rootURL);
    }

    /**
     * Tests content.
     */
    public void testURLContent() throws Exception
    {
        // Test non-empty file
        FileObject file = getReadFolder().resolveFile("file1.txt");
        assertTrue(file.exists());

        URLConnection urlCon = file.getURL().openConnection();
        assertSameURLContent(FILE1_CONTENT, urlCon);

        // Test empty file
        file = getReadFolder().resolveFile("empty.txt");
        assertTrue(file.exists());

        urlCon = file.getURL().openConnection();
        assertSameURLContent("", urlCon);
    }

    /**
     * Tests that unknown files have no content.
     */
    public void testUnknownURL() throws Exception
    {
        // Try getting the content of an unknown file
        final FileObject unknownFile = getReadFolder().resolveFile("unknown-file");
        assertFalse(unknownFile.exists());

        final URLConnection connection = unknownFile.getURL().openConnection();
        try
        {
            connection.getInputStream();
            fail();
        }
        catch (final IOException e)
        {
            assertSameMessage("vfs.provider/read-not-file.error", unknownFile, e);
        }
        assertEquals(-1, connection.getContentLength());
    }

}
