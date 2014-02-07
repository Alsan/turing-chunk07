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
package org.apache.commons.vfs2.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

/**
 * Provide access to a FileObject as DataSource
 */
public class FileObjectDataSource implements DataSource
{
    private final FileObject fo;

    public FileObjectDataSource(FileObject fo)
    {
        this.fo = fo;
    }

    public InputStream getInputStream() throws IOException
    {
        return fo.getContent().getInputStream();
    }

    public OutputStream getOutputStream() throws IOException
    {
        return fo.getContent().getOutputStream();
    }

    public String getContentType()
    {
        try
        {
            return fo.getContent().getContentInfo().getContentType();
        }
        catch (FileSystemException e)
        {
            throw new RuntimeException(e);
        }
    }

    public String getName()
    {
        return fo.getName().getBaseName();
    }
}
