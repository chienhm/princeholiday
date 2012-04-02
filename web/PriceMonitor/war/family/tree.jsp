<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.morntea.web.family.MemberService" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>娄氏家族族谱树</title>

<!-- CSS Files -->
<link type="text/css" href="base.css" rel="stylesheet" />
<link type="text/css" href="Spacetree.css" rel="stylesheet" />
<!-- http://thejit.org/static/v20/Docs/files/Options/Options-Canvas-js.html#Options.Canvas -->
<!--[if IE]><script language="javascript" type="text/javascript" src="Extras/excanvas.js"></script><![endif]-->

<%
    MemberService ms = new MemberService();
	String _root = request.getParameter("root");
	Long root = null;
	if(_root!=null) {
	    root = Long.parseLong(_root);
	}
%>
<script>
    var json = <%=ms.getJson(root) %>;
</script>
<!-- JIT Library File -->
<script language="javascript" type="text/javascript" src="jit-yc.js"></script>
<script language="javascript" type="text/javascript" src="tree.js"></script>
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
