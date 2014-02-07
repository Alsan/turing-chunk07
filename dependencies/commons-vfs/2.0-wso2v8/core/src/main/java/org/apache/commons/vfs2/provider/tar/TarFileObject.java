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
package org.apache.commons.vfs2.provider.tar;

//TODO: Revert to [compress]
//import org.apache.commons.compress.tar.TarEntry;
import java.io.InputStream;
import java.util.HashSet;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.provider.AbstractFileName;
import org.apache.commons.vfs2.provider.AbstractFileObject;

/**
 * A file in a Tar file system.
 * @author <a href="http://commons.apache.org/vfs/team-list.html">Commons VFS team</a>
 */
public class TarFileObject extends AbstractFileObject implements FileObject
{
    /** The TarEntry */
    protected TarEntry entry;
    private final HashSet<String> children = new HashSet<String>();
    private final TarFileSystem fs;
    private FileType type;

    protected TarFileObject(AbstractFileName name,
                            TarEntry entry,
                            TarFileSystem fs,
                            boolean tarExists) throws FileSystemException
    {
        super(name, fs);
        this.fs = fs;
        setTarEntry(entry);
        if (!tarExists)
        {
            type = FileType.IMAGINARY;
        }
    }

    /**
     * Sets the details for this file object.
     */
    protected void setTarEntry(final TarEntry entry)
    {
        if (this.entry != null)
        {
            return;
        }

        if ((entry == null) || (entry.isDirectory()))
        {
            type = FileType.FOLDER;
        }
        else
        {
            type = FileType.FILE;
        }

        this.entry = entry;
    }

    /**
     * Attaches a child
     */
    protected void attachChild(FileName childName)
    {
        children.add(childName.getBaseName());
    }

    /**
     * Determines if this file can be written to.
     *
     * @return <code>true</code> if this file is writeable, <code>false</code> if not.
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public boolean isWriteable() throws FileSystemException
    {
        return false;
    }

    /**
     * Returns the file's type.
     */
    @Override
    protected FileType doGetType()
    {
        return type;
    }

    /**
     * Lists the children of the file.
     */
    @Override
    protected String[] doListChildren()
    {
        try
        {
            if (!getType().hasChildren())
            {
                return null;
            }
        }
        catch (FileSystemException e)
        {
            // should not happen as the type has already been cached.
            throw new RuntimeException(e);
        }

        return children.toArray(new String[children.size()]);
    }

    /**
     * Returns the size of the file content (in bytes).  Is only called if
     * {@link #doGetType} returns {@link FileType#FILE}.
     */
    @Override
    protected long doGetContentSize()
    {
        if (entry == null)
        {
            return 0;
        }

        return entry.getSize();
    }

    /**
     * Returns the last modified time of this file.
     */
    @Override
    protected long doGetLastModifiedTime() throws Exception
    {
        if (entry == null)
        {
            return 0;
        }

        return entry.getModTime().getTime();
    }

    /**
     * Creates an input stream to read the file content from.  Is only called
     * if  {@link #doGetType} returns {@link FileType#FILE}.  The input stream
     * returned by this method is guaranteed to be closed before this
     * method is called again.
     */
    @Override
    protected InputStream doGetInputStream() throws Exception
    {
        // VFS-210: zip allows to gather an input stream even from a directory and will
        // return -1 on the first read. getType should not be expensive and keeps the tests
        // running
        if (!getType().hasContent())
        {
            throw new FileSystemException("vfs.provider/read-not-file.error", getName());
        }

        return fs.getInputStream(entry);
    }
}
