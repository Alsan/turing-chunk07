/*
 * Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.bpel.ui.bpel2svg.impl;

import org.wso2.carbon.bpel.ui.bpel2svg.BPEL2SVGFactory;
import org.wso2.carbon.bpel.ui.bpel2svg.ActivityInterface;
import org.apache.axiom.om.OMElement;
import org.wso2.carbon.bpel.ui.bpel2svg.FaultHandlerInterface;

/**
 * FaultHandler tag UI impl
 */
public class FaultHandlerImpl extends EventHandlerImpl implements FaultHandlerInterface {

    public FaultHandlerImpl(String token) {
        super(token);

        if (name == null) {
            name = "FAULTHANDLER" + System.currentTimeMillis();
        }
        displayName = "Fault Handlers";
        // Set Icon and Size
        startIconPath = BPEL2SVGFactory.getInstance().getIconPath(this.getClass().getName());
        endIconPath = BPEL2SVGFactory.getInstance().getEndIconPath(this.getClass().getName());
        // Set Layout
        setVerticalChildLayout(false);
    }

    public FaultHandlerImpl(OMElement omElement) {
        super(omElement);

        if (name == null) {
            name = "FAULTHANDLER" + System.currentTimeMillis();
        }
        displayName = "Fault Handlers";
        // Set Icon and Size
        startIconPath = BPEL2SVGFactory.getInstance().getIconPath(this.getClass().getName());
        endIconPath = BPEL2SVGFactory.getInstance().getEndIconPath(this.getClass().getName());
        // Set Layout
        setVerticalChildLayout(false);
    }

    public FaultHandlerImpl(OMElement omElement, ActivityInterface parent) {
        super(omElement);
        setParent(parent);
        if (name == null) {
            name = "FAULTHANDLER" + System.currentTimeMillis();
        }
        displayName = "Fault Handlers";
        // Set Icon and Size
        startIconPath = BPEL2SVGFactory.getInstance().getIconPath(this.getClass().getName());
        endIconPath = BPEL2SVGFactory.getInstance().getEndIconPath(this.getClass().getName());
        // Set Layout
        setVerticalChildLayout(false);
    }

    @Override
    public String getEndTag() {
        return BPEL2SVGFactory.FAULTHANDLER_END_TAG;
    }
}
