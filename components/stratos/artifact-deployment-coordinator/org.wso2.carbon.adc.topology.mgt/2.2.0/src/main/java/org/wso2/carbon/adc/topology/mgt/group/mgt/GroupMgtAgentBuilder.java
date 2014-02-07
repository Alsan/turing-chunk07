/*
 * Copyright (c) 2005-2011, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.adc.topology.mgt.group.mgt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hazelcast.config.Config;
import com.hazelcast.core.DuplicateInstanceNameException;
import org.apache.axis2.clustering.ClusteringAgent;
import org.apache.axis2.clustering.ClusteringFault;
import org.apache.axis2.clustering.Member;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseException;
import org.wso2.carbon.adc.topology.mgt.util.GroupMgtAgentException;
import org.wso2.carbon.core.clustering.hazelcast.HazelcastClusteringAgent;
import org.wso2.carbon.core.clustering.hazelcast.HazelcastGroupManagementAgent;
import org.wso2.carbon.lb.common.conf.LoadBalancerConfiguration;
import org.wso2.carbon.lb.common.conf.util.HostContext;
import org.wso2.carbon.lb.common.conf.util.TenantDomainContext;
import org.wso2.carbon.adc.topology.mgt.util.ConfigHolder;

public class GroupMgtAgentBuilder {

    private static LoadBalancerConfiguration lbConfig;
	/**
     * Key - host name 
     * Value - {@link HostContext}
     */
    private static Map<String, HostContext> hostContexts = new HashMap<String, HostContext>();
    
    private static final Log log = LogFactory.getLog(GroupMgtAgentBuilder.class);
	
	public static void createGroupMgtAgents() {
		lbConfig = ConfigHolder.getInstance().getLbConfig();
		hostContexts = lbConfig.getHostContextMap();
		
		ClusteringAgent clusteringAgent = ConfigHolder.getInstance().getAxisConfiguration().getClusteringAgent();
        if (clusteringAgent == null) {
            throw new SynapseException("Axis2 ClusteringAgent not defined in axis2.xml");
        }

        // Add the Axis2 GroupManagement agents
        if (hostContexts != null) {
            // iterate through each host context
            for (HostContext hostCtxt : hostContexts.values()) {
                // each host can has multiple Tenant Contexts, iterate through them
                for (TenantDomainContext tenantCtxt : hostCtxt.getTenantDomainContexts()) {

                    String domain = tenantCtxt.getDomain();
                    String subDomain = tenantCtxt.getSubDomain();
                    int groupManagementPort=tenantCtxt.getGroupMgtPort();

                    if (clusteringAgent.getGroupManagementAgent(domain, subDomain) == null) {
                        clusteringAgent.addGroupManagementAgent(new SubDomainAwareGroupManagementAgent(
                                                                                                       subDomain),
                                                                domain, subDomain,groupManagementPort);
//                        if (log.isDebugEnabled()) {
                            log.info("Group management agent added to cluster domain: " +
                                      domain + " and sub domain: "+subDomain);
//                        }
                    }
                }
            }
        }
    }
	
	public static void createGroupMgtAgent(String domain, String subDomain, int groupMgtPort) throws GroupMgtAgentException {

        String scHost=System.getProperty("sc.host.name");
        String lbHost=System.getProperty("lb.host.name");

        List<String> scHosts=null;
        List<String> lbHosts=null;

        if(scHost!=null){
            scHosts=getHostNames(scHost);
        }

        if(lbHost!=null){
            lbHosts=getHostNames(lbHost);
        }

		ClusteringAgent clusteringAgent = ConfigHolder.getInstance().getAxisConfiguration().getClusteringAgent();
        if (clusteringAgent == null) {
            throw new SynapseException("Axis2 ClusteringAgent not defined in axis2.xml");
        }
		
		if (clusteringAgent.getGroupManagementAgent(domain, subDomain) == null) {

//            SubDomainAwareGroupManagementAgent agent = new SubDomainAwareGroupManagementAgent(subDomain);
            HazelcastGroupManagementAgent agent = new HazelcastGroupManagementAgent();
            clusteringAgent.addGroupManagementAgent(agent, domain, subDomain, groupMgtPort);


            //adding the wka sc members with group management port
            if(scHosts!=null){
                for(String host : scHosts){
                    Member scMember =  new Member(host,groupMgtPort);
                    agent.addMember(scMember);
                    log.info(" Added the sc Group Management Member : "+scMember.getHostName()+
                            " with group_mgt_port : "+scMember.getPort()+" for the new agent");
                }
            }

            //adding the wka lb members with group management port
            if(lbHosts!=null){
                for(String host : lbHosts){
                    Member lbMember =  new Member(host,groupMgtPort);
                    agent.addMember(lbMember);
                    log.info(" Added the lb Group Management Member : "+lbMember.getHostName()+
                            " with group_mgt_port : "+lbMember.getPort()+" for the new agent");
                }
            }

            if(clusteringAgent instanceof HazelcastClusteringAgent){
                Config config = null;
                try{
                    config = ((HazelcastClusteringAgent) clusteringAgent).getPrimaryHazelcastConfig();
                    agent.init(config, ConfigHolder.getInstance().getConfigCtxt());
                    log.info("Group management agent added to cluster domain: " +
                            domain + " and sub domain: " + subDomain);
                    if(log.isDebugEnabled()){
                        log.debug("domain : "+domain+", sub domain : "+subDomain+", groupManagementPort : "+groupMgtPort);

                    }
                }catch (Exception e) {
                    String message = "Cannot initialize the cluster from the new domain";
                    log.error(message,e);
                    throw new GroupMgtAgentException(message,e);
                }
            }
        }
	}

    private static List<String> getHostNames(String hostNames){
        List<String> hostNameList = new ArrayList<String>();
        for(String hostName : hostNames.split(",")){
            hostNameList.add(hostName);
        }
        return hostNameList;
    }

}
