package org.wso2.carbon.lb.endpoint.group.mgt;

import com.hazelcast.config.Config;
import org.apache.axis2.clustering.ClusteringAgent;
import org.apache.axis2.clustering.management.GroupManagementAgent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseException;
import org.wso2.carbon.core.clustering.hazelcast.HazelcastClusteringAgent;
import org.wso2.carbon.core.clustering.hazelcast.HazelcastGroupManagementAgent;
import org.wso2.carbon.lb.endpoint.util.ConfigHolder;
import org.wso2.carbon.lb.endpoint.util.GroupMgtAgentException;

/**
 * Responsible for building {@link GroupManagementAgent}s.
 */
public class GroupMgtAgentBuilder {
    
    private static final Log log = LogFactory.getLog(GroupMgtAgentBuilder.class);
	
    /**
     * Creates a {@link HazelcastGroupManagementAgent} corresponds to the given
     * parameters, if and only if there's no existing agent.
     * @param domain clustering domain.
     * @param subDomain clustering sub domain.
     */
    public static void createGroupMgtAgent(String domain, String subDomain, int groupMgtPort)
            throws GroupMgtAgentException {

        ClusteringAgent clusteringAgent =
            ConfigHolder.getInstance().getAxisConfiguration().getClusteringAgent();
        
        if (clusteringAgent == null) {
            throw new SynapseException("Axis2 Clustering Agent not defined in axis2.xml");
        }

        // checks the existence. 
        if (clusteringAgent.getGroupManagementAgent(domain, subDomain) == null) {

            HazelcastGroupManagementAgent agent = new HazelcastGroupManagementAgent();
            clusteringAgent.addGroupManagementAgent(agent, domain, subDomain,groupMgtPort);
            if(clusteringAgent instanceof HazelcastClusteringAgent){
                Config config = null;
                try{
                    config = ((HazelcastClusteringAgent) clusteringAgent).getPrimaryHazelcastConfig();
                    agent.init(config, ConfigHolder.getInstance().getConfigCtxt());
                    log.info("Group management agent added to cluster domain: " +
                            domain + " and sub domain: " + subDomain);
                }catch (Exception e) {
                    String message = "Cannot initialize the cluster from the new domain";
                    log.error(message,e);
                    throw new GroupMgtAgentException(message,e);
                }
            }
        }
    }
    
    public static void resetGroupMgtAgent(String domain, String subDomain) {

        ClusteringAgent clusteringAgent =
            ConfigHolder.getInstance().getAxisConfiguration().getClusteringAgent();
        
        if (clusteringAgent == null) {
            throw new SynapseException("Axis2 Clustering Agent not defined in axis2.xml");
        }

        // checks the existence. 
        if (clusteringAgent.getGroupManagementAgent(domain, subDomain) != null) {
            
            clusteringAgent.resetGroupManagementAgent(domain, subDomain);
            
            log.info("Group management agent of cluster domain: " +
                domain + " and sub domain: " + subDomain+" is removed.");
        }
    }
}
