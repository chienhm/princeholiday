<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="com.morntea.web.family.Member" %>
<%@ page import="com.morntea.web.family.MemberService" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>娄氏家族</title>
<script>
function genOptions(code) {
	var codes = [100,	101,	102,	103,	104,	105,	106,	107,	108,	109];
	var gens = ["A",	"B",	"万",	"世",	"永",	"昌",	"发",	"启",	"强",	"兴"];
	var options = "<select name=\"gen\"><option value=\"-1\">--</option>";
	for(var i=0; i<codes.length; i++) {
		options += "<option value=\"" + codes[i] + "\""
			+ ((code==codes[i])?" selected":"")
			+ ">"
			+ gens[i]
			+ "</option>";
	}
	options += "</select>";
	document.write(options);
}

</script>
<script>
	var mans = new Object();
<%
MemberService ms = new MemberService();
List<Member> members = ms.getMembers();
for(Member man : members) {
    if(man.isGender()) {
%>
	mans[<%=man.getId() %>] = {name:"<%=man.getName() %>", generation:<%=man.getGeneration()%>};
<%
    }
}
%>
function fidOptions(fid, gen) {
	var options = "<select name=\"fid\"><option value=\"-1\">--</option>";
	for(var key in mans) {
		if(gen==-1 || mans[key].generation==gen-1) {
			options += "<option value=\"" + key + "\""
				+ ((key==fid)?" selected":"")
				+ ">"
				+ mans[key].name
				+ "</option>";
		}
	}
	options += "</select>";
	document.write(options);
}
</script>
</head>
<body>
<a href="tree.jsp" target="_blank">娄氏家族族谱登记</a>
<table>
    <tr>
        <td>编号</td>
        <td>姓名</td>
        <td>字派</td>
        <td>性别</td>
        <td>生日</td>
        <td>农历生日</td>
        <td>母亲</td>
        <td>父亲</td>
        <td>电话</td>
        <td>地址</td>
        <td>备注</td>
        <td>Update</td>
    </tr>
<%
for(Member m : members) {
    String birthday = "";
    String lunarBirthday = "";
    String motherName = "";
    String phone = "";
    String address = "";
    if(m.getBirthday()!=null) {
        birthday = new SimpleDateFormat("yyyy-MM-dd").format(m.getBirthday());
    }
    if(m.getLunarBirthday()!=null) {
        lunarBirthday = new SimpleDateFormat("yyyy-MM-dd").format(m.getLunarBirthday());
    }
    if(m.getMotherName()!=null) {
        motherName = m.getMotherName();
    }
    if(m.getAddress()!=null) {
        address = m.getAddress();
    }
    if(m.getPhone()!=null) {
        phone = m.getPhone();
    }
%>
	<form method="post" action="/member">
    <tr>
         <td>
         	<% if(m.isGender()) { %>
         	<a href="tree.jsp?root=<%=m.getId() %>" target="_blank"><%=m.getId() %></a>
         	<% } else { %>
         	<%=m.getId() %>
         	<% } %>
         	</td>
         <td>
         	<input type="hidden" name="id" value="<%= m.getId() %>" />
         	<input type="text" name="name" value="<%= m.getName() %>" size="5" />
         </td>
         <td>
         	<script>genOptions(<%=m.getGeneration() %>);</script>
         </td>
         <td>
         <input type="radio" name="gender" value="1"<% if(m.isGender())out.print(" checked"); %> />
         <input type="radio" name="gender" value="0"<% if(!m.isGender())out.print(" checked"); %> />
         </td>
         <td><input type="text" name="birthday" value="<%= birthday %>" size="10" /></td>
         <td><input type="text" name="lunar" value="<%= lunarBirthday %>" size="10" /></td>
         <td><input type="text" name="mother" value="<%= motherName %>" size="5" /></td>
         <td>
         	<script>fidOptions(<%=m.getFatherId() %>, <%=m.getGeneration() %>);</script>
         </td>
         <td><input type="text" name="phone" value="<%= phone %>" /></td>
         <td><input type="text" name="address" value="<%= address %>" /></td>
         <td><input type="text" name="comment" value="<%= m.getComment() %>" /></td>
         <td><input type="submit" name="action" value="Update" /></td>
    </tr>
    </form>
<%
}
%>
	<form method="post" action="/member">
    <tr>
    	 <td>&nbsp;</td>
         <td>
         	<input type="text" name="name" value="" size="5" />
         </td>
         <td>
         	<script>genOptions(105);</script>
         </td>
         <td>
         <input type="radio" name="gender" value="1" checked />
         <input type="radio" name="gender" value="0" />
         </td>
         <td><input type="text" name="birthday" value="" size="10" /></td>
         <td><input type="text" name="lunar" value="" size="10" /></td>
         <td><input type="text" name="mother" value="" size="5" /></td>
         <td>
         	<script>fidOptions(-1, -1);</script>
         </td>
         <td><input type="text" name="phone" value="" /></td>
         <td><input type="text" name="address" value="" /></td>
         <td><input type="text" name="comment" value="" /></td>
         <td><input type="submit" name="action" value="Add" /></td>
    </tr>
    </form>

</table>
</body>
</html>