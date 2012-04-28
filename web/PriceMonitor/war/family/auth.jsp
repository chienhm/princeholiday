<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String referer = null;
if(session.getAttribute("referer")!=null) {
    referer = (String)session.getAttribute("referer");
}

String auth = request.getParameter("auth");
if(auth!=null) {
    if(auth.equals("logout")) {
        session.removeAttribute("auth");
    } else {
        String password = request.getParameter("password");
        if(password!=null && password.equals("15121036201")) {
            session.setAttribute("auth", true);
            out.println("登录成功！前往<a href='chart.jsp'>家谱图</a>");
            if(referer!=null) {
                session.removeAttribute("referer");
                response.sendRedirect(referer);
            }
        } else {
            out.println(password + "密码错误！");
        }
    }
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login</title>
</head>
<body>

<form action="?auth" method="post">
请输入本人的手机号码（不知道请联系QQ: 5794560）：<input type="password" name="password" />
<br/>
你的姓名：<input type="text" name="name" />
<br/>
你的父亲：<input type="text" name="fathername" />
<input type="submit" name="submit" value="进入" />
</form>

</body>
</html>