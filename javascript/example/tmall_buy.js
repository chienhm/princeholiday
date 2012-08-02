//------------------------------------------------------------------------------------------------- ��ʱˢ��
var toHandle = -1;
function at(atTime, func) {
	var left = atTime.getTime()-now.getTime();
	toHandle = setTimeout(func, left);
	console.log(Math.floor(left/1000) + "s " + left%1000 + "ms left.");
}
//Example:
var now = new Date();
var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), 14, 0, 0, 0);
at(atTime, function(){
	var date = new Date(); console.log(date + date.getMilliseconds());
	window.frames[0].location.reload();
});
//clearTimeout(toHandle);
function at(time, func) {
	var now = new Date();
	var diff = time.getTime() - now.getTime(); 
	if(diff<0) {console.log("time expired");return;}
	var timeout = (diff>60000)?(diff-60000):(diff>10000)?(diff-10000):diff;
	toHandle = setTimeout(function(){
		if(timeout==diff)func();
		else at(time, func);
	}, timeout);
	console.log("[" + now + ", " + now.getMilliseconds() + "ms] " 
		+ ( (diff>60000) ? (parseInt(diff/60000)+"m "+parseInt(diff%60000/1000)+"s") : (parseInt(diff/1000)+"s") ) + " left.");
}
//------------------------------------------------------------------------------------------------- ���������ӳ�
if(!(jQuery && jQuery.ajax)) {console.error("jQuery not loaded.");}

function getServerTime() {
	var st = new Date();
	jQuery.ajax({
		url: window.frames[0].location.href,
		success: function(data){
			//console.log(data);
		},
		complete: function(jqXHR){
			var rt = new Date();
			console.log(
			  "[Send    : " + st + " " + st.getMilliseconds() + "]\n" 
			+ "[Receive : " + rt + " " + rt.getMilliseconds() + "]\n"
			+ "[Server  : " + jqXHR.getResponseHeader("Date") + "]\n"
			+ "[Time    : " + (rt.getTime()-st.getTime()) + "ms]");
		}
	});
}

var toHandle = -1;
function at(atTime, func) {
	var left = atTime.getTime()-now.getTime();
	toHandle = setTimeout(func, left);
	console.log(Math.floor(left/1000) + "s " + left%1000 + "ms left.");
}
//Example:
var now = new Date();
at(new Date(now.getFullYear(),now.getMonth(),now.getDate(), 14, 0, 0, 0), getServerTime);
at(new Date(now.getFullYear(),now.getMonth(),now.getDate(), 14, 0, 0, 500), getServerTime);
at(new Date(now.getFullYear(),now.getMonth(),now.getDate(), 14, 0, 1, 0), getServerTime);
at(new Date(now.getFullYear(),now.getMonth(),now.getDate(), 14, 0, 1, 500), getServerTime);

//------------------------------------------------------------------------------------------------- ��ʱ��¼
if(!(jQuery && jQuery.ajax)) {console.error("jQuery not loaded.");}
function postLoginData(postData, callback) {
	jQuery.ajax({
		type: "post",
		url: "http://login.taobao.com/member/login.jhtml",
		data: postData,
		success: function(data, textStatus, jqXHR) {
			if(data.indexOf("��֤�����")!=-1 || data.indexOf("��������֤��")!=-1) {
				console.log("��Ҫ��֤��");//Ϊ�������˺Ű�ȫ����������֤��//��֤��������������롣
			} else if (data.indexOf("������˻�����ƥ��")!=-1) {
				console.log("�û����������");//�������������˻�����ƥ�䣬���������롣
			} else {
				console.log("login success");
				//checkLogin(callback);
				if(callback)callback();
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			console.log("login success. [If return Status Code is 302 Found]");
			if(callback)callback();
		},
		complete: function(jqXHR){
			console.log("login complete.");
		},
	});
}

function getFormValue(html, name) {
	var regExp = new RegExp("name=\""+name+"\"[^><]+value=\"(.*?)\"", "ig");
	var r = regExp.exec(html);
	if(r!=null) {
		return r[1];
	}
	return "";
}

function login(user, pass, callback) {
	jQuery.get("http://login.taobao.com/member/login.jhtml",
		function(html) {
			//console.log(data);
			var data = {	
				"TPL_username"		: user,
				"TPL_password"		: pass,
				"TPL_checkcode"		: "",
				"need_check_code"	: "",
				"action"			: "Authenticator",
				"event_submit_do_login" : "anything",
				"TPL_redirect_url"	: "",
				"from"				: "tb",
				"fc"				: "default",
				"style"				: "default",
				"css_style"			: "",
				"tid"				: getFormValue(html, "tid"),
				"support"			: getFormValue(html, "support"),
				"CtrlVersion"		: getFormValue(html, "CtrlVersion"),
				"loginType"			: 3,
				"minititle"			: "",
				"minipara"			: "",
				"umto"				: getFormValue(html, "umto"),
				"pstrong"			: 2,
				"llnick"			: "",
				"sign"				: "",
				"need_sign"			: "",
				"isIgnore"			: "",
				"full_redirect"		: "",
				"popid"				: "",
				"callback"			: "",
				"guf"				: "",
				"not_duplite_str"	: "",
				"need_user_id"		: "",
				"poy"				: "",
				"gvfdcname"			: 10,
				"gvfdcre"			: getFormValue(html, "gvfdcre"),
				"from_encoding"		: ""
			};
			console.log(data);
			postLoginData(data, callback);
		}
	);
}

var toHandle = -1;
function at(time, func) {
	var now = new Date();
	var diff = time.getTime() - now.getTime(); 
	if(diff<0) {console.log("time expired");return;}
	var timeout = (diff>60000)?(diff-60000):(diff>10000)?(diff-10000):diff;
	toHandle = setTimeout(function(){
		if(timeout==diff)func();
		else at(time, func);
	}, timeout);
	console.log("[" + now + ", " + now.getMilliseconds() + "ms] " 
		+ ( (diff>60000) ? (parseInt(diff/60000)+"m "+parseInt(diff%60000/1000)+"s") : (parseInt(diff/1000)+"s") ) + " left.");
}

var now = new Date();
var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), 13, 56, 0, 0);
at(atTime, function(){
	login("lleoeo", "epcc123456");
});