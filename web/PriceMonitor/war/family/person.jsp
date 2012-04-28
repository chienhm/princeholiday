<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.morntea.web.family.MemberService"%>
<%@ page import="com.morntea.web.family.Member"%>
<%@ page import="com.morntea.web.family.Lunar"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Date"%>
<%
String _id = request.getParameter("id");
Long id = null;
if(_id!=null) {
    id = Long.parseLong(_id);
} else {
    return;
}
%>

<%
if(session.getAttribute("auth")==null) {
    session.setAttribute("referer", "person.jsp?id="+_id);
    response.sendRedirect("auth.jsp");
}
%>
<%

MemberService ms = new MemberService();
Member person = ms.getMember(id);
Member father = ms.getMember(person.getFatherId());
Member grandfather = null;
if(father!=null) {
    grandfather = ms.getMember(father.getFatherId());
}
String motherName = person.getMotherName();
if(motherName==null || motherName.isEmpty()) {
    motherName = "？";
}

List<Member> siblings = ms.getSiblings(person);
List<Member> desendants = ms.getDescendant(person);

int birthdiff = -1;
int deathdiff = -9999;
String birthdayStr = null;
String deathdayStr = null;
if(person.getBirthday()!=null) {
    birthdayStr = Lunar.chineseDateFormat.format(person.getBirthday());
    birthdiff = Lunar.diffYear(person.getBirthday(), new Date());
}
String lunarBirthdayStr = null;
if(person.getLunarBirthday()!=null) {
    lunarBirthdayStr = "农历" + Lunar.toChineseString(person.getLunarBirthday());
    //--------------------------------------------------------------------------特别处理
    if(lunarBirthdayStr.endsWith("一月初一")) {
        lunarBirthdayStr += "(日期有误)";
    }
}
if(birthdayStr==null) {
    if(lunarBirthdayStr!=null) {
        birthdayStr = lunarBirthdayStr;
    }
} else if(lunarBirthdayStr!=null) {
    birthdayStr += "(" + lunarBirthdayStr + ")";
}
if(birthdayStr==null) {
    birthdayStr = "？";
}

if(person.getDeathday()!=null) {
    deathdiff = Lunar.diffYear(person.getDeathday(), new Date());
    deathdayStr = Lunar.chineseDateFormat.format(person.getDeathday());
    //--------------------------------------------------------------------------特别处理
    if(deathdayStr.endsWith("01月01日")) {
        deathdayStr = "？";
        deathdiff = 9999;
    }
}
int codes[] = {100, 101, 102, 103, 104, 105, 106, 107, 108, 109};
String gens[] = {"A", "B", "万", "世", "永", "昌", "发", "启", "强", "兴"};
String gen = gens[person.getGeneration()-100];
List<String> spouses = ms.getSpouse(person);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%= person.getName() %></title>
<link rel="stylesheet" href="css/style.css"/>
</head>
<body>
<div id="container">
	<div id="usernav">		
		<ul>
			<li><a href="chart.jsp" title=""><span>家谱全图</span></a></li>
			<li><a href="auth.jsp?auth=logout" title="退出系统"><span>退出</span></a></li>
		</ul>
	</div>
	<div style="clear:both;"></div>
	<div id="main">
		<div id="userleft">
			<div id="information">
				<ul>
					<li>
						<a href="chart.jsp?root=<%=person.getId() %>" target="_blank" title="查看家谱图">
						<img class="block" src="image/<%=person.isGender()?"male.jpg":"female.jpg" %>" width="140" border="0" />
						</a>
					</li>
					<li>
						<p>  生辰至今：<%=((birthdiff==-1)?"？":birthdiff) %>年</p>
						<% if(deathdiff>0) { %>
						<p>  逝世至今：<%=((deathdiff==9999)?"？":deathdiff) %>年</p>
						<% } %>
						<p>  纪念日: </p>
						<p>  兄弟姐妹: <%=siblings.size() %>人</p>
						<% if(person.isGender()) { %>
						<p>  子女数目: <%=desendants.size() %>人</p>
						<% } %>
						<p>  人生信仰: </p>
					</li>
				</ul>
			</div>
		</div>
		<div id="usercenter">
			<div>
				<ul style="line-height:180%;">
					<li>姓名：<%=person.getName() %></li>
					<li>字派：<%=gen %></li>
					<li>性别：<%=person.isGender()?"男":"女" %></li>
					<li>出生：<%=birthdayStr %></li>
					<% if(deathdayStr!=null) { %>
					<li>逝世：<%=deathdayStr %></li>
					<% } %>
					<li>曾用名：</li>
					<% if(person.isGender() && spouses.size()>0) { %>
					<li>配偶：
					<% for(String name : spouses) { %>
					<%=name %> 
					<% }} %>
					</li>
					<li>
						父亲：
						<% if(father!=null){%><a href="person.jsp?id=<%=father.getId() %>"><%=father.getName() %></a><%} else {%>？<%} %>&nbsp;&nbsp;
					</li>
					<li>母亲：
						<%=motherName %>
					</li>
					<li>爷爷：
						<% if(grandfather!=null){%><a href="person.jsp?id=<%=grandfather.getId() %>"><%=grandfather.getName() %></a><%} else {%>？<%} %>&nbsp;&nbsp;
					</li>
					<li>
						奶奶：
						<% 
							String grandMotherName = null;
						    if(grandfather!=null){
						        grandMotherName = grandfather.getMotherName();
						    }
						    if(grandMotherName==null || grandMotherName.isEmpty()){
						    	out.println("？");
						    } else {
						        out.println(grandMotherName);
						    }
						%>
					</li>
					<li>子女：
					<%
						for(Member m : desendants) {
					%>
					<a href="person.jsp?id=<%=m.getId() %>"><%=m.getName() %></a> 
					<%
						}
					%>
					</li>
					<li>兄弟：
					<%
						for(Member m : siblings) {
						    if(m.isGender()) {
					%>
					<a href="person.jsp?id=<%=m.getId() %>"><%=m.getName() %></a> 
					<%
						    }
						}
					%>
					<li>姐妹：
					<%
						for(Member m : siblings) {
						    if(!m.isGender()) {
					%>
					<a href="person.jsp?id=<%=m.getId() %>"><%=m.getName() %></a> 
					<%
						    }
						}
					%>
					</li>
					<li>籍贯：湖北省随州市天河口乡</li>
					<li>居住城市：</li>
					<li>通讯地址：
						<%=person.getAddress() %>
					</li>
					<li>联系电话：
						<%=person.getPhone() %>
					</li>
					<li>E-mail/QQ：</li>
					<li>简介：<br /></li>
				</ul>
			</div>			
		</div>
		<div id="userright">
			<div id="message">
				<h3><span>重要日历</span></h3>
				<ul>
					<li style="color:#999;">暂无</li>
				</ul>
				<a href="#">更多</a>
			</div>
		</div>
	</div>
	<div id="footer" style="margin-bottom:20px;margin-top:20px;line-height:150%;font-family:Arial;">
		<ul id="copyright" style="margin-left:0px;">
			<li><span style="font-family:Arial;">&copy;</span>&nbsp;2012-2100&nbsp;&nbsp;联系人：娄林&nbsp;&nbsp;QQ：5794560&nbsp;&nbsp;</li>
			<li><a href="http://www.morntea.com" target="_blank">MornTea.com</a> 提供技术支持┊<span class="help">欢迎为娄氏家谱提供更多准确信息</span> Email：<a href="mailto:5794560@qq.com">5794560@qq.com</a></li>
		</ul>
	</div>
</div>
</body>
</html>