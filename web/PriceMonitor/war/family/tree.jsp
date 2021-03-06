<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.morntea.web.family.MemberService" %>
<%
if(session.getAttribute("auth")==null) {
    session.setAttribute("referer", "tree.jsp");
    response.sendRedirect("auth.jsp");
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>娄氏家族族谱树</title>

<!-- CSS Files -->
<link type="text/css" href="tree/base.css" rel="stylesheet" />
<link type="text/css" href="tree/Spacetree.css" rel="stylesheet" />
<!-- http://thejit.org/static/v20/Docs/files/Options/Options-Canvas-js.html#Options.Canvas -->
<!--[if IE]><script language="javascript" type="text/javascript" src="tree/Extras/excanvas.js"></script><![endif]-->

<%
    MemberService ms = new MemberService();
	String _root = request.getParameter("root");
	Long root = null;
	if(_root!=null) {
	    root = Long.parseLong(_root);
	}
%>
<script>
	/* 兄弟姐妹大小排行未完全解决，顺序是以Node id为Hash的默认排序，暂时以数字从小到大作为Node的id */
    var json = <%=ms.getJson(root) %>;
</script>
<!-- JIT Library File -->
<script language="javascript" type="text/javascript" src="tree/jit-yc.js"></script>
<script language="javascript" type="text/javascript" src="tree/tree.js"></script>
</head>

<body onload="init();">
<div id="container">

<div id="center-container">
    <div id="infovis"></div>    
</div>

<div id="log"></div>
</div>
</body>
</html>
