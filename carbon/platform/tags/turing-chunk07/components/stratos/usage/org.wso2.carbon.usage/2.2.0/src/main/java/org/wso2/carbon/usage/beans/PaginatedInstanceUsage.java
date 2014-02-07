/*
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

package org.wso2.carbon.usage.beans;

import java.util.Arrays;

public class PaginatedInstanceUsage {
    private InstanceUsageStatics[] instanceUsages;
    private int pageNumber;
    private int numberOfPages;

    public InstanceUsageStatics[] getInstanceUsages() {
        if(instanceUsages != null) {
        return Arrays.copyOf(instanceUsages, instanceUsages.length);
        }

        return null;
    }

    public void setInstanceUsages(InstanceUsageStatics[] instanceUsages) {
        if(instanceUsages != null) {
            this.instanceUsages = Arrays.copyOf(instanceUsages, instanceUsages.length);
        }
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

}