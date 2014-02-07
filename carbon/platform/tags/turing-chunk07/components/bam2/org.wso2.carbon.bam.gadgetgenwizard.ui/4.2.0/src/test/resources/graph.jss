<% include("common.jss"); %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <link rel="stylesheet" type="text/gadgetgen.css" href="jquery.jqChart.gadgetgen.css" />
    <script src="jquery-1.5.1.min.js" type="text/javascript"></script>
    <script src="jquery.jqChart.min.js" type="text/javascript"></script>
    <!--[if IE]><script lang="javascript" type="text/javascript" src="excanvas.js"></script><![endif]-->
    
    <script lang="javascript" type="text/javascript">
    	<%
    	var result = db.query("select monthsPassed, tag, SUM(count) as count from CONTENT_ACCESS_BY_MONTH where org=\'WSO2\' group by monthsPassed, tag order by monthsPassed, SUM(count) desc");
		var variableList = wrtieDataDefinitions(result, "TAG", "COUNT"); 
   		 %>
        $(document).ready(function () {
            $('#jqChart1').jqChart({
                title: 'Content Accessed group by Month', legend: { title: 'Legend' },
                border: { strokeStyle: '#6ba851' },
                series: <% print(variableList);%>
            });
        });
    </script>
</head>
<body>
        <div id="jqChart1" style="width: 500px; height: 300px;"/>
</body>
</html>