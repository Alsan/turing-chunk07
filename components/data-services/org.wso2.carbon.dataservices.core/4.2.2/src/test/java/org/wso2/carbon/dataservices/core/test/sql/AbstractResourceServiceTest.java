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
package org.wso2.carbon.dataservices.core.test.sql;

import org.apache.axiom.om.OMElement;
import org.wso2.carbon.dataservices.core.test.DataServiceBaseTestCase;
import org.wso2.carbon.dataservices.core.test.util.TestUtils;

/**
 * Class to represent resource request test cases.
 */
public abstract class AbstractResourceServiceTest extends
		DataServiceBaseTestCase {

	private String epr = null;

	public AbstractResourceServiceTest(String testName, String serviceName) {
		super(testName);
		this.epr = this.baseEpr + serviceName;
	}

	/**
	 * Test a resource call with HTTP GET.
	 */
	protected void resourceGET() {
		TestUtils.showMessage(this.epr + " - resourceGET");
		try {
			OMElement result = TestUtils.getAsResource(this.epr, "customers1",
					null, "GET");
			assertTrue(TestUtils.validateResultStructure(result,
					TestUtils.CUSTOMER_XSD_PATH));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test a resource call with HTTP POST.
	 */
	protected void resourcePOST() {
		TestUtils.showMessage(this.epr + " - resourcePOST");
		try {
			OMElement result = TestUtils.getAsResource(this.epr, "customers2",
					null, "POST");
			assertTrue(TestUtils.validateResultStructure(result,
					TestUtils.CUSTOMER_XSD_PATH));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		assertTrue(true);
	}

}