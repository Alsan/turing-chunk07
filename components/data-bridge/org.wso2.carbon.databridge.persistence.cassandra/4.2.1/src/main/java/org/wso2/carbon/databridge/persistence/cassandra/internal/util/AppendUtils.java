package org.wso2.carbon.databridge.persistence.cassandra.internal.util;

import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * Copyright (c) WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public final class AppendUtils {

    public static void appendToFile(final InputStream in, final File f) throws IOException {
        OutputStream stream = null;
        try {
            stream = outStream(f);
            IOUtils.copy(in, stream);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    public static void appendToFile(final String in, final File f) throws IOException {
        InputStream stream = null;
        try {
            stream = IOUtils.toInputStream(in);
            appendToFile(stream, f);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    private static OutputStream outStream(final File f) throws IOException {
        return new BufferedOutputStream(new FileOutputStream(f, true));
    }

    private AppendUtils() {}
}
