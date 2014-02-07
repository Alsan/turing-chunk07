package org.wso2.carbon.apacheds.impl;

import org.apache.commons.io.FileUtils;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;

/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Date: Sep 19, 2010 Time: 9:41:09 PM
 */

public class CarbonSchemaLdifExtractorTest extends TestCase {

    private File tempDirectory;

    public void testExtractAndCopy() throws Exception {

        createTmpDirectory();
        
        File f = new File("./src/test/resources/is-default-schema.zip");
        System.out.println(f.getAbsolutePath());

        CarbonSchemaLdifExtractor extractor = new CarbonSchemaLdifExtractor(tempDirectory, f);
        assertFalse(extractor.isExtracted());

        extractor.extractOrCopy();

        File extractedFile = new File(tempDirectory.getAbsolutePath(), "schema" + File.separatorChar + "ou=schema.ldif");
        assertTrue(extractedFile.exists());

        assertTrue(extractor.isExtracted());
        
    }

    private void createTmpDirectory() {
        String temDir = "tmp";
            File file = new File(".");
            String temDirectory = file.getAbsolutePath() + File.separator + temDir;
            tempDirectory = new File(temDirectory);

            if (tempDirectory.exists()) {
                try {
                    FileUtils.deleteDirectory(tempDirectory);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            tempDirectory.mkdir();
    }

}
