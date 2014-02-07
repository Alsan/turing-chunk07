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
package org.apache.commons.vfs2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The main entry point for the VFS.  Used to create {@link FileSystemManager}
 * instances.
 *
 * @author <a href="http://commons.apache.org/vfs/team-list.html">Commons VFS team</a>
 * @version $Revision: 1035158 $ $Date: 2010-11-15 12:18:47 +0530 (Mon, 15 Nov 2010) $
 */
public final class VFS
{
    /** The URI style */
    private static Boolean uriStyle;

    /** The FileSystemManager */
    private static FileSystemManager instance;

    private VFS()
    {
    }

    /**
     * Returns the default {@link FileSystemManager} instance.
     * @return The FileSystemManager.
     * @throws FileSystemException if an error occurs creating the manager.
     */
    public static synchronized FileSystemManager getManager()
        throws FileSystemException
    {
        if (instance == null)
        {
            instance = createManager("org.apache.commons.vfs2.impl.StandardFileSystemManager");
        }
        return instance;
    }

    /**
     * Creates a file system manager instance.
     * @param managerClassName The specific manager impelmentation class name.
     * @return The FileSystemManager.
     * @throws FileSystemException if an error occurs creating the manager.
     */
    private static FileSystemManager createManager(final String managerClassName)
        throws FileSystemException
    {
        try
        {
            // Create instance
            final Class<?> mgrClass = Class.forName(managerClassName);
            final FileSystemManager mgr = (FileSystemManager) mgrClass.newInstance();

            /*
            try
            {
                // Set the logger
                final Method setLogMethod = mgrClass.getMethod("setLogger", new Class[]{Log.class});
                final Log logger = LogFactory.getLog(VFS.class);
                setLogMethod.invoke(mgr, new Object[]{logger});
            }
            catch (final NoSuchMethodException e)
            {
                // Ignore; don't set the logger
            }
            */

            try
            {
                // Initialise
                final Method initMethod = mgrClass.getMethod("init", (Class[]) null);
                initMethod.invoke(mgr, (Object[]) null);
            }
            catch (final NoSuchMethodException e)
            {
                // Ignore; don't initialize
            }

            return mgr;
        }
        catch (final InvocationTargetException e)
        {
            throw new FileSystemException("vfs/create-manager.error",
                managerClassName,
                e.getTargetException());
        }
        catch (final Exception e)
        {
            throw new FileSystemException("vfs/create-manager.error",
                managerClassName,
                e);
        }
    }

    public static boolean isUriStyle()
    {
        if (uriStyle == null)
        {
            uriStyle = Boolean.FALSE;
        }
        return uriStyle.booleanValue();
    }

    public static void setUriStyle(boolean uriStyle)
    {
        if (VFS.uriStyle != null && VFS.uriStyle.booleanValue() != uriStyle)
        {
            throw new IllegalStateException("URI STYLE ALREADY SET TO");
        }
        VFS.uriStyle = uriStyle ? Boolean.TRUE : Boolean.FALSE;
    }
}
