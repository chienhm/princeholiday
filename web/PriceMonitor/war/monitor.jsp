<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="com.morntea.web.pricemonitor.data.Condition" %>
<%@ page import="com.morntea.web.pricemonitor.data.ProductItem" %>
<%@ page import="com.morntea.web.pricemonitor.service.*" %>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>Add Product Item</title>

</head>

<body>
	<form method="post" action="/additem">
		Url:
		<input type="text" name="url" id="url" /> <br />
		条件:
		当
		<select name="type">
			<option value="1">价格低于</option>
			<option value="2">价格下降</option>
			<option value="3">价格上涨</option>
			<option value="4">到货</option>
			<option value="5">库存数量低于</option>
			<option value="0">网页出现字符串</option>
		</select>
		<input type="text" name="para" id="para" />时，
		发邮件到
		<input type="text" name="email" id="email" />
		通知我<br/>
		<input type="submit" name="btn_sbm" value="Add Item" />
	</form>
<%
ProductService ps = new ProductService();
for(ProductItem item : ps.getAllItem()) {
%>
<ul>
<%= item.getUrl() + " (" + item.getPrice() + ")" %>
	<%
	for(Condition condition : item.getConditions()) {
	%>
	<li><%= condition.getType() + ", " + condition.getParameter() + " (" + condition.isMeet() + ")" %></li>
	<%
	}
	%>
</ul>
<%
}
ps.close();
%>
</body>
</html>
