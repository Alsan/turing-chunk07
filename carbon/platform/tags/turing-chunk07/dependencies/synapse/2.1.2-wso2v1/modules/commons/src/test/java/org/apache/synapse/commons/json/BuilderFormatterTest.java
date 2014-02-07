/**
 *  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.synapse.commons.json;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.builder.Builder;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.MessageFormatter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static junit.framework.Assert.assertTrue;

public class BuilderFormatterTest {
    private static final String jsonRequest = "{\"value\":12.4,\"12X12\":\"http://localhost/images/id/img_0_24_24.png\",\"0\":{\"date\":\"19/12/12\",\"venue\":\"NONVEG\"},\"1\":[1,2,{\"newArray\":[\"one-Element\"]}]}";

    private static String xmlPayload = "<jsonObject><value>12.4</value><_JsonReader_PD_12X12>http://localhost/images/id/img_0_24_24.png</_JsonReader_PD_12X12><_JsonReader_PD_0><date>19/12/12</date><venue>NONVEG</venue></_JsonReader_PD_0><?xml-multiple  1?><_JsonReader_PD_1>1</_JsonReader_PD_1><_JsonReader_PD_1>2</_JsonReader_PD_1><_JsonReader_PD_1><?xml-multiple  newArray?><newArray>one-Element</newArray></_JsonReader_PD_1></jsonObject>";

    public void testCase1() {
        try {
            MessageContext message = Util.newMessageContext();
            Builder jsonBuilder = Util.newJsonBuilder();
            InputStream inputStream = Util.newInputStream(jsonRequest.getBytes());
            OMElement element  = jsonBuilder.processDocument(inputStream, "application/json", message);
            message.getEnvelope().getBody().addChild(element);
            assertTrue(xmlPayload.equals(element.toString()));

            OutputStream out = Util.newOutputStream();
            MessageFormatter formatter = Util.newJsonFormatter();
            formatter.writeTo(message, null, out, false);
            String outStr = new String(((ByteArrayOutputStream) out).toByteArray());
            assertTrue(jsonRequest.equals(outStr));
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
            assertTrue(false);
        }
    }
}
