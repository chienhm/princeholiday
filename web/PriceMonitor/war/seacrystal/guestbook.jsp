<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.morntea.web.guestbook.Guestbook" %>
<%@ page import="com.morntea.web.guestbook.PMF" %>

<html>
  <head>
    <link type="text/css" rel="stylesheet" href="main.css" />
    <link rel="shortcut icon" href="favicon.png" />
    <title>说说您对书声天堂的看法</title>
  </head>

  <body>

<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
%>
<p>您好， <%= user.getNickname() %>！ 
(<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">退出</a>)</p>
<%
    } else {
%>
<p>您好！从这里<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">登录</a> 以发表您的留言。</p>
<%
    }
%>

<%
    PersistenceManager pm = PMF.get().getPersistenceManager();
    String query = "select from " + Guestbook.class.getName() + " order by date desc range 0,50";
    List<Guestbook> greetings = (List<Guestbook>) pm.newQuery(query).execute();
    if (greetings.isEmpty()) {
%>
<p>尚无人发表留言。</p>
<%
    } else {
        for (Guestbook g : greetings) {
            if (g.getAuthor() == null) {
%>
<p>游客
<%
            } else {
            	String username = g.getAuthor().getNickname();
%>
<p><b><%= username %></b>
<%
            }
%>
(<%= g.getDate() %>)：</p>
<blockquote><%= g.getContent() %></blockquote>
<%
        }
    }
    pm.close();
%>

    <form action="/soundbookpost" method="post">
      <div><textarea name="content" rows="3" cols="60"></textarea></div>
      <div><input type="submit" value="提交留言" /></div>
    </form>

  </body>
</html>
