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
package org.apache.commons.vfs2.provider;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;

/**
 * Description.
 *
 * @author <a href="http://commons.apache.org/vfs/team-list.html">Commons VFS team</a>
 * @version $Revision: 1035190 $ $Date: 2010-11-15 14:28:37 +0530 (Mon, 15 Nov 2010) $
 */
public abstract class CompositeFileProvider extends AbstractFileProvider
{
    private static final int INITIAL_BUFSZ = 80;

    public CompositeFileProvider()
    {
        super();
    }

    /**
     * The schemes to use for resolve
     */
    protected abstract String[] getSchemes();

    /**
     * Locates a file object, by absolute URI.
     * @param baseFile The base FileObject.
     * @param uri The file to find.
     * @param fileSystemOptions The options for the FileSystem.
     * @return A FileObject for the located file.
     * @throws FileSystemException if an error occurs.
     */
    public FileObject findFile(final FileObject baseFile,
                               final String uri,
                               final FileSystemOptions fileSystemOptions)
        throws FileSystemException
    {
        StringBuilder buf = new StringBuilder(INITIAL_BUFSZ);

        UriParser.extractScheme(uri, buf);

        String[] schemes = getSchemes();
        for (int iterSchemes = 0; iterSchemes < schemes.length; iterSchemes++)
        {
            buf.insert(0, ":");
            buf.insert(0, schemes[iterSchemes]);
        }

        FileObject fo = getContext().getFileSystemManager().resolveFile(buf.toString(), fileSystemOptions);
        return fo;
    }
}
