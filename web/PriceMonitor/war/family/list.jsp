<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="com.morntea.web.family.Member" %>
<%@ page import="com.morntea.web.family.MemberService" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<a href="/member">Add Member</a>
<%
MemberService ms = new MemberService();
List<Member> members = ms.getMembers();
for(Member m : members) {
%>
<%= m.getName() %>, <%= m.getBirthday() %> <br/>
<%
}
%>
</body>
</html>