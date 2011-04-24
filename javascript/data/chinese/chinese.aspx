<%@ Page Language="C#" ContentType="text/html"%>
<%@ Import Namespace="System.Data" %>
<%@ Import NameSpace="System.Data.OleDb" %>
<script runat="server">
void mGetRandContent(){ //������10��������ҳ��ʾ��

string strDb=Server.MapPath("db-access/chinesedict.mdb");//�����ֵ�λ�ã����Ҫ��ֹ���أ���.mdb��׺���������������޸�
//Response.Write(strDb);
string strAuth="PROVIDER=Microsoft.Jet.OLEDB.4.0;DATA Source=";
OleDbConnection db=new OleDbConnection(strAuth+strDb);

//strKey=Regex.Replace(strKey,"[=\\s\"\'!,.����~@#��%����&*(){}`+����<>?/]","",RegexOptions.IgnoreCase).Trim();//��ֹSQLע��
int iDate=System.DateTime.Now.Day;
string strSql="SELECT TOP 10 * FROM louhome ORDER BY rnd("+iDate.ToString()+"-id) DESC "; 


OleDbCommand cmd=new OleDbCommand(strSql,db);
try{
db.Open();

dl.DataSource=cmd.ExecuteReader();
dl.DataBind();


db.Close();

cmd.Dispose();
db.Dispose();

labCount.Text=dl.Items.Count.ToString();
if(dl.Items.Count==0)
labError.Text="û���ҵ��κ�������ݣ������������ٲ�ѯ��";
else labError.Text="";
}
catch(Exception ee){
labError.Text=ee.ToString();
}
}


void mGetContent(string strKey){

string strDb=Server.MapPath("db-access/chinesedict.mdb");//�����ֵ�λ�ã����Ҫ��ֹ���أ���.mdb��׺���������������޸�
//Response.Write(strDb);
string strAuth="PROVIDER=Microsoft.Jet.OLEDB.4.0;DATA Source=";
OleDbConnection db=new OleDbConnection(strAuth+strDb);

strKey=Regex.Replace(strKey,"[=\\s\"\'!,.����~@#��%����&*(){}`+?/]","",RegexOptions.IgnoreCase).Trim();//��ֹSQLע��

string strSql="SELECT TOP 990 Name,Content FROM louhome WHERE Key LIKE '%"+strKey+"%' OR Name LIKE '%"+strKey+"%'"; 


OleDbCommand cmd=new OleDbCommand(strSql,db);
try{
db.Open();

dl.DataSource=cmd.ExecuteReader();
dl.DataBind();

db.Close();

cmd.Dispose();
db.Dispose();

labCount.Text=dl.Items.Count.ToString();
if(dl.Items.Count==0)
labError.Text="û���ҵ��κ�������ݣ������������ٲ�ѯ��";
else labError.Text="";
}
catch(Exception ee){
labError.Text=ee.ToString();
}
}

string strTitle(string strInput){
string strResult=strInput;

string strRex;
if(tbKey.Text.Trim()=="")
strRex="DDXXTX";
else strRex=tbKey.Text.Trim();

strResult=strResult.Replace(strRex,"<FONT STYLE='background:yellow;color:red'>"+strRex+"</FONT>");
//strResult=Regex.Replace(strResult,strRex,"<FONT STYLE='background:yellow;color:red'>"+strRex+"</FONT>", RegexOptions.IgnoreCase);

return strResult;
}


void mSearch(object sender,EventArgs e){
labTopMsg.Visible=false;
mGetContent(tbKey.Text.Trim());

}
void Page_Load(){
if(!Page.IsPostBack){
mGetRandContent();

}
}
</script>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>¥����-������ֵ�-.NET��Դվ��������</title>
</head>
<body>
<form runat="server" style="width:90%; margin:0px auto;">
  <img src="images/logo.gif" alt="¥����" border="0"/> <br />
  <asp:Label ID="labError" runat="server" Font-Size="8pt" ForeColor="#999999"/>
  <br />
  &nbsp;ʫ �ʣ�
  <asp:TextBox ID="tbKey" runat="server" Width="160px" AutoPostBack="false" MaxLength="8"/>    
  <asp:RequiredFieldValidator ID="rfv" runat="server" ControlToValidate="tbKey" ErrorMessage="*" Display="Dynamic"/>  
  <asp:Button ID="btn" runat="server" OnClick="mSearch" Text="�鿴"/><font color="#CCCCCC">��������ʫ�ʣ����ߣ�����ȵ�����ؼ���</font>
  <br />
  <br style="height:8px"/>
  <div style="background:#75CBFF; color:#666666; width:67%; height:20px; padding-top:4px; float:left; text-align:left; vertical-align:middle">&nbsp;&nbsp;��<a href="javascript:window.external.AddFavorite('http://www.louhome.com', '¥����')">¥����</a>�����ղ�</div>
  <div style="background:#75CBFF; color:#666666; width:30%; height:20px; padding-top:4px; float:left; text-align:right">����
      <asp:Label ID="labCount" runat="server" Text="0" ForeColor="#FF0000"/>  
    ����ؽ��&nbsp;&nbsp;</div>
  <br style="height:8px"/><br />
  <asp:Label ID="labTopMsg" runat="server" Font-Bold="true" Font-Size="15px" ForeColor="#FF0000" Visible="true" Text="��10���ִʡ�"/>
  <br style="height:8px"/>
  <asp:DataList ID="dl" runat="server" Width="98%">
    <itemtemplate>
      <div style="background:#016392; color:#FFFFFF; width:80px; clear:both;"><i>&middot;��� <%#(Container.ItemIndex+1).ToString()%></i></div>
      <div style="width:99%; border:1px solid #016392;">
        <div style="background:#E2E3D7; font-weight:bold; padding:2px;">
          <asp:Label ID="labName" runat="server" Text=<%#(DataBinder.Eval(Container.DataItem,"Name").ToString()) %> />
        </div>
        <div style=" padding:10px;">
		  <asp:Label id="labContent2" runat="server" Text=<%#"<font style='color:#0000ff;'>"+strTitle(DataBinder.Eval(Container.DataItem,"Content").ToString().Replace("{BR}","<br>"))+"</font>"%> />   <br style="height:4px"/> <br style="height:6px"/>
        </div>
      </div>
	<br/>
    </itemtemplate>
  </asp:DataList>
  <div style="background:#E1FAFF; width:97%; text-align:center;">
  <!--��Ȩ��־�������ٱ���һ��¥����www.louhome.com��λ�ã�-->
  <a href="http://www.louhome.com" target="_blank">¥����</a> ��Ȩ���� LouHome.com All right Reserved. Copyright 2009-2012&copy; ��ICP��09048681</div>
</form>
    <!--վ��ͳ�ƿ�ʼ�����޸Ļ�ɾ��-->
<div style="display:none"><script src="http://s96.cnzz.com/stat.php?id=1994368&web_id=1994368" language="JavaScript"></script></div>
<!--վ��ͳ�ƽ���-->
</body>
</html>
