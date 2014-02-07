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
package org.apache.synapse.mediators.template;

import org.apache.synapse.Mediator;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.mediators.Value;
import org.apache.synapse.mediators.eip.EIPUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class handles invocation of a synapse function template.
 * <invoke target="">
 * <parameter name="p1" value="{expr} | {{expr}} | value" />*
 * ..
 * </invoke>
 */
public class InvokeMediator extends AbstractMediator {
    /**
     * refers to the target template this is going to invoke
     * this is a read only attribute of the mediator
     */
    private String targetTemplate;
    
    /**
     * Refers to the parent package qualified reference
     * 
     */
    private String packageName;

    /**
     * maps each parameter name to a Expression/Value
     * this is a read only attribute of the mediator
     */
    private Map<String, Value> pName2ExpressionMap;
    
    private boolean dynamicMediator =false;
    
    
    /** The local registry key which is used to pick a sequence definition*/
    private Value key = null;

    public InvokeMediator() {
        //LinkedHashMap is used to preserve tag order
        pName2ExpressionMap = new LinkedHashMap<String, Value>();
    }

    public boolean mediate(MessageContext synCtx) {
        SynapseLog synLog = getLog(synCtx);

        if (synLog.isTraceOrDebugEnabled()) {
            synLog.traceOrDebug("Invoking Target EIP Sequence " + targetTemplate + " paramNames : " +
                                pName2ExpressionMap.keySet());
            if (synLog.isTraceTraceEnabled()) {
                synLog.traceTrace("Message : " + synCtx.getEnvelope());
            }
        }
        //get the target function template and invoke by passing populated parameters
        Mediator mediator = synCtx.getSequenceTemplate(targetTemplate);

        //executing key reference if found defined at configuration.
		if (key != null) {
			String sequenceKey = key.evaluateValue(synCtx);
			Mediator m = synCtx.getSequence(sequenceKey);
			if (m == null) {
				handleException("Sequence named " + key + " cannot be found", synCtx);

			} else {
				if (synLog.isTraceOrDebugEnabled()) {
					synLog.traceOrDebug("Executing with key " + key);
				}

				boolean result = m.mediate(synCtx);

				if(!result){
					handleException("Error while executig target reference  " + key, synCtx);
				}
			}
		}
        
        
        if (mediator != null && mediator instanceof TemplateMediator) {
            populateParameters(synCtx, ((TemplateMediator) mediator).getName());
            return mediator.mediate(synCtx);
        }
        return false;
    }

    /**
     * poplulate declared parameters on temp synapse properties
     * @param synCtx
     * @param templateQualifiedName
     */
    private void populateParameters(MessageContext synCtx, String templateQualifiedName) {
        Iterator<String> params = pName2ExpressionMap.keySet().iterator();
        while (params.hasNext()) {
            String parameter = params.next();
            if (!"".equals(parameter)) {
                Value expression = pName2ExpressionMap.get(parameter);
                if (expression != null) {
                    EIPUtils.createSynapseEIPTemplateProperty(synCtx, templateQualifiedName, parameter, expression);
                }
            }
        }
    }

    public String getTargetTemplate() {
        return targetTemplate;
    }

    public void setTargetTemplate(String targetTemplate) {
        this.targetTemplate = targetTemplate;
    }

    public Map<String, Value> getpName2ExpressionMap() {
        return pName2ExpressionMap;
    }

    public void addExpressionForParamName(String pName, Value expr) {
        pName2ExpressionMap.put(pName, expr);
    }

	public boolean isDynamicMediator() {
    	return dynamicMediator;
    }

	public void setDynamicMediator(boolean dynamicMediator) {
    	this.dynamicMediator = dynamicMediator;
    }

	public Value getKey() {
		return key;
	}

	public void setKey(Value key) {
		this.key = key;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
  
	
    
}
