<%@ taglib uri="http://wso2.org/projects/carbon/taglibs/carbontags.jar" prefix="carbon" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page import="org.wso2.carbon.event.stream.manager.stub.EventStreamAdminServiceStub" %>
<%@ page import="org.wso2.carbon.event.stream.manager.stub.types.EventStreamInfoDto" %>
<%@ page import="org.wso2.carbon.event.stream.manager.ui.EventStreamUIUtils" %>

<%@ page
        import="org.wso2.carbon.event.stream.manager.stub.EventStreamAdminServiceStub" %>
<%@ page
        import="org.wso2.carbon.event.formatter.stub.types.EventFormatterConfigurationFileDto" %>
<%@ page import="org.wso2.carbon.event.formatter.stub.types.EventFormatterConfigurationInfoDto" %>


<fmt:bundle basename="org.wso2.carbon.event.stream.manager.ui.i18n.Resources">

<carbon:breadcrumb
        label="eventstream.list"
        resourceBundle="org.wso2.carbon.event.stream.manager.ui.i18n.Resources"
        topPage="false"
        request="<%=request%>"/>

<script type="text/javascript" src="../admin/js/breadcrumbs.js"></script>
<script type="text/javascript" src="../admin/js/cookies.js"></script>
<script type="text/javascript" src="../admin/js/main.js"></script>

    <%
        String eventStreamWithVersion = request.getParameter("eventStreamWithVersion");
        String loadingCondition = "exportedStreams";
    %>

    <div id="middle">
        <h2><fmt:message key="title.event.out.flow"/></h2>

        <div id="workArea">

                <table style="width:100%" id="outFlowDetails" class="styledLeft">
                    <%--<thead>--%>
                    <%--<tr>--%>
                        <%--<th><fmt:message key="title.event.stream.details"/></th>--%>
                    <%--</tr>--%>
                    <%--</thead>--%>
                    <tbody>

                    <tr>
                        <td class="formRaw">
                            <jsp:include page="../eventstream/event_formatter_outFlows.jsp" flush="true">
                                <jsp:param name="eventStreamWithVersion" value="<%=eventStreamWithVersion%>"/>
                            </jsp:include>

                            <jsp:include page="../eventprocessor/inner_index.jsp" flush="true">
                                <jsp:param name="eventStreamWithVersion" value="<%=eventStreamWithVersion%>"/>
                                <jsp:param name="loadingCondition" value="<%=loadingCondition%>"/>
                            </jsp:include>

                        </td>
                    </tr>
                    <tr>
                        <td class="buttonRow">

                        </td>
                    </tr>
                    </tbody>
                </table>
        </div>
    </div>

</fmt:bundle>
