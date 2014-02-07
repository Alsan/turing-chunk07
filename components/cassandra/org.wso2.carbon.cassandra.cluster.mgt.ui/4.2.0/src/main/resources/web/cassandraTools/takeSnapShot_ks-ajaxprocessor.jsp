<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@ page import="org.wso2.carbon.cassandra.cluster.mgt.ui.operation.ClusterKeyspaceOperationsAdminClient" %>
<%@ page import="org.wso2.carbon.cassandra.cluster.mgt.ui.constants.ClusterUIConstants" %>
<%
    JSONObject backendStatus = new JSONObject();
    backendStatus.put("success","no");
    try{
        String keyspace=request.getParameter(ClusterUIConstants.KEYSPACE);
        String tag=request.getParameter(ClusterUIConstants.SNAPSHOT_TAG);
        String hostAddress=request.getParameter(ClusterUIConstants.HOST_ADDRESS);
        ClusterKeyspaceOperationsAdminClient clusterKeyspaceOperationsAdminClient =new ClusterKeyspaceOperationsAdminClient(config.getServletContext(), session);
        clusterKeyspaceOperationsAdminClient.takeSnapshotKeyspace(hostAddress,tag,keyspace);
        backendStatus.put("success","yes");
    }catch (Exception e)
    {}
    out.print(backendStatus);
    out.flush();
%>
