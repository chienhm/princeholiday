<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.morntea.web.family.MemberService"%>
<%@ page import="com.morntea.web.family.Member"%>
<%@ page import="java.util.List"%>
<%
if(session.getAttribute("auth")==null) {
    session.setAttribute("referer", "chart.jsp");
    response.sendRedirect("auth.jsp");
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>娄氏家族族谱树</title>

    <link rel="stylesheet" href="chart/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="chart/css/jquery.jOrgChart.css"/>
    <link rel="stylesheet" href="chart/css/custom.css"/>
    
    <script src="../script/jquery.min.js"></script>
    <script type="text/javascript" src="chart/jquery-ui-1.8.19.custom.min.js"></script>
    
    <script src="chart/jquery.jOrgChart.js"></script>

    <script>
    //https://github.com/wesnolte/jOrgChart
    jQuery(document).ready(function() {
        $("#org").jOrgChart({
            chartElement : '#chart',
            dragAndDrop  : false,
            nodeCursor   : "pointer",
            fade         : true
        });

    	$(".jOrgChart").draggable();
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
                <a class="brand" href="#">娄姓家族族谱树</a>
                <ul class="nav">
                    <!-- <li><a href="list.jsp">List</a></li> -->
                    <li><a href="tree.jsp">Tree</a></li>                  
                    <li><a href="chart.jsp">Chart</a></li>
                    <li><a href="auth.jsp?auth=logout">退出</a></li>
                </ul>
                <div class="note">(背景灰色表示已故，名字橙色为男性，红色为女性)</div>
	            <div class="pull-right">
	                <div class="alert-message info" id="reset">Reset</div>
	            </div>
            </div>
        </div>
    </div>

	<div id="chart" class="orgChart"></div>
	<ul id="org" style="display: none;">
<%= ms.getDescendantList(members, root) %>
	</ul>
    <script>
    $("#reset").click(function(e){
        e.preventDefault();        
        $(".jOrgChart").css("left", "0px").css("top", "0px");
    });
    </script>
</body>
</html>
