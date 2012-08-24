//------------------------------------------------------------------------------------------------- 定时刷新
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
//clearTimeout(toHandle);//--------------------------------------------------------------
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
var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), 8, 59, 58, 0);
at(atTime, function(){
	var date = new Date(); console.log(date + date.getMilliseconds());
	window.frames[0].location.reload();
});
//------------------------------------------------------------------------------------------------- 定时登录
if(!(jQuery && jQuery.ajax)) {console.error("jQuery not loaded.");}
function postLoginData(postData, callback) {
	jQuery.ajax({
		type: "post",
		url: "http://login.taobao.com/member/login.jhtml",
		data: postData,
		success: function(data, textStatus, jqXHR) {
			if(data.indexOf("验证码错误")!=-1 || data.indexOf("请输入验证码")!=-1) {
				console.log("需要验证码");//为了您的账号安全，请输入验证码//验证码错误，请重新输入。
			} else if (data.indexOf("密码和账户名不匹配")!=-1) {
				console.log("用户名密码错误");//您输入的密码和账户名不匹配，请重新输入。
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
});

//------------------------------------------------------------------------------------------------- 保持在线
if(!(jQuery && jQuery.ajax)) {console.error("jQuery not loaded.");}
var toHandle = -1;
var interval = 4 * 60 * 1000;
var random = 1 * 60 * 1000;
function periodicRun(func) {
	var timeout = interval;
	if(toHandle!=-1) { clearInterval(toHandle); toHandle = -1; }
	if(random) { timeout += parseInt(Math.random()*random); }
	toHandle = setTimeout(function() { 
		if(func) func(); 
		periodicRun(func); 
	}, timeout);
}

function periodicGet(url, callback, _interval, _random) {
	if(_interval)interval = _interval;
	if(_random)random = _random;
	periodicRun(function() {
		jQuery.ajax({
			"url"     : url,
			"type"    : "get",
			"cache"   : false,
			"success" : callback, 
			"error"   : function(){console.log("login time out.");clearInterval(toHandle);}
		})
	}, random);
}
periodicGet(location.href, function(data){console.log(new Date());})