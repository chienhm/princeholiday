<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.morntea.web.family.MemberService"%>
<%@ page import="com.morntea.web.family.Member"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>娄氏家族族谱树</title>

    <link rel="stylesheet" href="orgchart/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="orgchart/css/jquery.jOrgChart.css"/>
    <link rel="stylesheet" href="orgchart/css/custom.css"/>
    
    <script src="../script/jquery.min.js"></script>
    <script type="text/javascript" src="orgchart/jquery-ui-1.8.19.custom.min.js"></script>
    
    <script src="orgchart/jquery.jOrgChart.js"></script>

    <script>
    //https://github.com/wesnolte/jOrgChart
    jQuery(document).ready(function() {
    	$("#chart").draggable();
        $("#org").jOrgChart({
            chartElement : '#chart',
            dragAndDrop  : false,
            nodeCursor   : "pointer"
        });
    });
    </script>
<%
    MemberService ms = new MemberService();
	String _root = request.getParameter("root");
	Long lroot = null;
	if(_root!=null) {
	    lroot = Long.parseLong(_root);
	}
	Member root = ms.getRoot(lroot);
	List<Member> members = ms.getAllMembers();
%>
</head>

<body>
    <div class="topbar">
        <div class="topbar-inner">
            <div class="container">
                <a class="brand" href="#">家谱树</a>
                <ul class="nav">
                    <li><a href="list.jsp">List</a></li>
                    <li><a href="tree.jsp">Tree</a></li>                  
                    <li><a href="chart.jsp">Chart</a></li>      
                </ul>  
            </div>
        </div>
    </div>

	<div id="chart" class="orgChart"></div>
	<ul id="org" style="display: none;">
<%= ms.getDescendatList(members, root) %>
	</ul>

</body>
</html>
