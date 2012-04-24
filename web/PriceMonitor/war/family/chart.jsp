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
<link rel="stylesheet" href="orgchart/jquery.orgchart.css" />
<style>
body {
	font-family: Ubuntu, SansSerif
}

h1 {
	padding-bottom: 10px;
	text-align: center;
}

h2 {
	margin-top: 0px;
	margin-bottom: 8px;
}

p {
	margin: 0px;
	padding: 0px;
	line-height: 1.5em;
}

div#text {
	padding: 10px;
}
</style>
<script src="../script/jquery.min.js"></script>
<script src="orgchart/jquery.textchildren.js"></script>
<script src="orgchart/jquery.orgchart.js"></script>
<script>
	$(function() {
		$("#org").orgChart({
			levels : -1,
			stack : false,
			nodeText : function($node) {
				return $node.textChildren();
			}
		}, $("#main"));
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

	<h1>JQuery/CSS Organization Chart</h1>

	<div id="main"></div>
	<ul id="org" style="display: none;">
<%= ms.getDescendatList(members, root) %>
	</ul>

</body>
</html>
