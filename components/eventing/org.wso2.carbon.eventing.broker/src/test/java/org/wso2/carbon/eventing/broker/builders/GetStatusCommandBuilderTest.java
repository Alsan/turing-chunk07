/*
 *  Copyright (c) 2005-2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.wso2.carbon.eventing.broker.builders;

import junit.framework.TestCase;
import org.apache.axis2.context.MessageContext;
import org.apache.axiom.om.OMElement;
import org.wso2.carbon.eventing.broker.utils.CommandBuilderTestUtils;
import org.wso2.eventing.Subscription;

import java.util.Calendar;

public class GetStatusCommandBuilderTest extends TestCase {

    /**
     * The Get Status request payload according to the WS-Eventing specification. 
     * http://www.w3.org/Submission/WS-Eventing/#Table8
     */
    protected final String REQUEST_PAYLOAD_SOAP12 =
            "<s12:Envelope\n" +
            "    xmlns:s12=\"http://www.w3.org/2003/05/soap-envelope\"\n" +
            "    xmlns:wsa=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\"\n" +
            "    xmlns:wse=\"http://schemas.xmlsoap.org/ws/2004/08/eventing\"\n" +
            "    xmlns:ow=\"http://www.example.org/oceanwatch\" >\n" +
            "  <s12:Header>\n" +
            "    <wsa:Action>\n" +
            "      http://schemas.xmlsoap.org/ws/2004/08/eventing/GetStatus\n" +
            "    </wsa:Action>\n" +
            "    <wsa:MessageID>\n" +
            "      uuid:bd88b3df-5db4-4392-9621-aee9160721f6\n" +
            "    </wsa:MessageID>\n" +
            "    <wsa:ReplyTo>\n" +
            "      <wsa:Address>http://www.example.com/MyEventSink</wsa:Address>\n" +
            "    </wsa:ReplyTo>\n" +
            "    <wsa:To>\n" +
            "      http://www.example.org/oceanwatch/SubscriptionManager\n" +
            "    </wsa:To>\n" +
            "    <wse:Identifier>\n" +
            "      uuid:22e8a584-0d18-4228-b2a8-3716fa2097fa\n" +
            "    </wse:Identifier>\n" +
            "  </s12:Header>\n" +
            "  <s12:Body>\n" +
            "    <wse:GetStatus />\n" +
            "  </s12:Body>\n" +
            "</s12:Envelope>";

    protected final String REQUEST_PAYLOAD_SOAP11 =
            "<soapenv:Envelope\n" +
            "    xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
            "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "    xmlns:wsa=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\"\n" +
            "    xmlns:wse=\"http://schemas.xmlsoap.org/ws/2004/08/eventing\"\n" +
            "    xmlns:ow=\"http://www.example.org/oceanwatch\" >\n" +
            "    xsi:schemaLocation=\"http://schemas.xmlsoap.org/soap/envelope/\n" +
            "                        http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soapenv:Header>\n" +
            "    <wsa:Action>\n" +
            "      http://schemas.xmlsoap.org/ws/2004/08/eventing/GetStatus\n" +
            "    </wsa:Action>\n" +
            "    <wsa:MessageID>\n" +
            "      uuid:bd88b3df-5db4-4392-9621-aee9160721f6\n" +
            "    </wsa:MessageID>\n" +
            "    <wsa:ReplyTo>\n" +
            "      <wsa:Address>http://www.example.com/MyEventSink</wsa:Address>\n" +
            "    </wsa:ReplyTo>\n" +
            "    <wsa:To>\n" +
            "      http://www.example.org/oceanwatch/SubscriptionManager\n" +
            "    </wsa:To>\n" +
            "    <wse:Identifier>\n" +
            "      uuid:22e8a584-0d18-4228-b2a8-3716fa2097fa\n" +
            "    </wse:Identifier>\n" +
            "  </soapenv:Header>\n" +
            "  <soapenv:Body>\n" +
            "    <wse:GetStatus />\n" +
            "  </soapenv:Body>\n" +
            "</soapenv:Envelope>\n" +
            "";

    protected final String RESPONSE_PAYLOAD_SOAP11 = "<?xml version='1.0' encoding='utf-8'?>" +
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
            "<soapenv:Body><wse:GetStatusResponse xmlns:wse=\"http://schemas.xmlsoap.org/ws/2004/08/eventing\">" +
            "<wse:Expires>*</wse:Expires></wse:GetStatusResponse></soapenv:Body>" +
            "</soapenv:Envelope>";

    protected final String RESPONSE_PAYLOAD_SOAP12 = "<?xml version='1.0' encoding='utf-8'?>" +
            "<soapenv:Envelope xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\">" +
            "<soapenv:Body><wse:GetStatusResponse xmlns:wse=\"http://schemas.xmlsoap.org/ws/2004/08/eventing\">" +
            "<wse:Expires>*</wse:Expires></wse:GetStatusResponse></soapenv:Body>" +
            "</soapenv:Envelope>";

    protected final String RESPONSE_PAYLOAD_SOAP11_WITH_TIMESTAMP = "<?xml version='1.0' encoding='utf-8'?>" +
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
            "<soapenv:Body><wse:GetStatusResponse xmlns:wse=\"http://schemas.xmlsoap.org/ws/2004/08/eventing\">" +
            "<wse:Expires>1970-01-01T08:55:45.678+05:30</wse:Expires></wse:GetStatusResponse></soapenv:Body>" +
            "</soapenv:Envelope>";

    protected final String RESPONSE_PAYLOAD_SOAP12_WITH_TIMESTAMP = "<?xml version='1.0' encoding='utf-8'?>" +
            "<soapenv:Envelope xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\">" +
            "<soapenv:Body><wse:GetStatusResponse xmlns:wse=\"http://schemas.xmlsoap.org/ws/2004/08/eventing\">" +
            "<wse:Expires>1970-01-01T08:55:45.678+05:30</wse:Expires></wse:GetStatusResponse></soapenv:Body>" +
            "</soapenv:Envelope>";

    public void testSOAP12EnvelopeToSubscription() throws Exception {
        MessageContext mc = CommandBuilderTestUtils.getMCWithSOAP12Envelope();
        GetStatusCommandBuilder builder = new GetStatusCommandBuilder(mc);
        
        Subscription subscription = builder.toSubscription(
                CommandBuilderTestUtils.payloadToSOAP12Envelope(REQUEST_PAYLOAD_SOAP12));

        assertNotNull("The subscription object is null", subscription);
        assertEquals("Invalid subscription id", "uuid:22e8a584-0d18-4228-b2a8-3716fa2097fa",
                subscription.getId());
    }

    public void testSOAP11EnvelopeToSubscription() throws Exception {
        MessageContext mc = CommandBuilderTestUtils.getMCWithSOAP11Envelope();
        GetStatusCommandBuilder builder = new GetStatusCommandBuilder(mc);

        Subscription subscription = builder.toSubscription(
                CommandBuilderTestUtils.payloadToSOAP11Envelope(REQUEST_PAYLOAD_SOAP11));

        assertNotNull("The subscription object is null", subscription);
        assertEquals("Invalid subscription id", "uuid:22e8a584-0d18-4228-b2a8-3716fa2097fa",
                subscription.getId());
    }

    public void testSubscriptionToSOAP12Envelope() throws Exception {
        Subscription subscription = new Subscription();
        MessageContext mc = CommandBuilderTestUtils.getMCWithSOAP12Envelope();
        GetStatusCommandBuilder builder = new  GetStatusCommandBuilder(mc);

        OMElement payload = builder.fromSubscription(subscription);

        assertEquals("Invalid response for the get status request", RESPONSE_PAYLOAD_SOAP12,
                payload.toString());
    }

    public void testSubscriptionToSOAP11Envelope() throws Exception {
        Subscription subscription = new Subscription();
        MessageContext mc = CommandBuilderTestUtils.getMCWithSOAP11Envelope();
        GetStatusCommandBuilder builder = new  GetStatusCommandBuilder(mc);

        OMElement payload = builder.fromSubscription(subscription);

        assertEquals("Invalid response for the get status request", RESPONSE_PAYLOAD_SOAP11,
                payload.toString());
    }

    public void testSubscriptionToSOAP12EnvelopeWithTimeStamp() throws Exception {
        Subscription subscription = new Subscription();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(12345678);
        subscription.setExpires(calendar);
        MessageContext mc = CommandBuilderTestUtils.getMCWithSOAP12Envelope();
        GetStatusCommandBuilder builder = new  GetStatusCommandBuilder(mc);

        OMElement payload = builder.fromSubscription(subscription);

        assertEquals("Invalid response for the get status request", RESPONSE_PAYLOAD_SOAP12_WITH_TIMESTAMP,
                payload.toString());
    }

    public void testSubscriptionToSOAP11EnvelopeWithTimeStamp() throws Exception {
        Subscription subscription = new Subscription();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(12345678);
        subscription.setExpires(calendar);
        MessageContext mc = CommandBuilderTestUtils.getMCWithSOAP11Envelope();
        GetStatusCommandBuilder builder = new  GetStatusCommandBuilder(mc);

        OMElement payload = builder.fromSubscription(subscription);

        assertEquals("Invalid response for the get status request", RESPONSE_PAYLOAD_SOAP11_WITH_TIMESTAMP,
                payload.toString());
    }

}