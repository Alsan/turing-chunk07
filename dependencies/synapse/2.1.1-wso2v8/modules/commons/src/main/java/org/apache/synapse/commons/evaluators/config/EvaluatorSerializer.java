/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.synapse.commons.evaluators.config;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.commons.evaluators.Evaluator;
import org.apache.synapse.commons.evaluators.EvaluatorException;

/**
 * This interface should be implemented by the classes to serialize the {@link Evaluator}
 * object model in to XML configuration.
 */
public interface EvaluatorSerializer {

    /**
     * Serialze an Evaluator configuration to a XML element.
     * @param parent if not null the serialize element will be added to the parent
     * @param evaluator The <code>Evaluator</code> object to be serialized
     * 
     * @return <code>OMElement</code> containing the configuration
     * @throws org.apache.synapse.commons.evaluators.EvaluatorException if an error
     * occurs while serializing
     */
    OMElement serialize(OMElement parent, Evaluator evaluator) throws EvaluatorException;
}
