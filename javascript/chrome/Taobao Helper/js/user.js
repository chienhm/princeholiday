/*
1. get_coin.js - login
2. res/login.js - decrypt
3. options.js - save
4. update.js - update
*/
var user = null;
var token = null;

function initUser(callback) {
	chrome.cookies.get({
		url : "http://*.taobao.com",
		name : "tracknick"
	}, function(cookie) {
		if(cookie!=null) {
			user = cookie.value;
		}
		
		chrome.cookies.get({
			url : "http://*.taobao.com",
			name : "_tb_token_"
		}, function(cookie) {
			if(cookie!=null) {
				token = cookie.value;
			}
			console.log(user + ", token:" + token);
			callback();
		})
	});
}

// User Login
function getFormValue(html, name) {
	var regExp = new RegExp("name=\""+name+"\"[^><]+value=\"(.*?)\"", "ig");
	var r = regExp.exec(html);
	if(r!=null) {
		return r[1];
	}
	return "";
}

function checkLogin(callback) {
	$.ajax({
		url: "http://i.taobao.com/my_taobao.htm",
		cache: false,
		success: function(data){
			//console.log(data);
			if(data.indexOf("我的淘宝")!=-1) {
				console.log("Already login.");
				callback(true);
			} else { //data.indexOf("标准登录框")!=-1)
				console.log("Not login.");
				callback(false);
			}
		},
		error: function() {
			console.log("Error: Not login.");
			callback(false);
		}
	});
}

// private
function postLoginData(postData, callback) {
	$.ajax({
		type: "post",
		url: "http://login.taobao.com/member/login.jhtml",
		data: postData,
		complete: function(jqXHR){
			checkLogin(callback);
		},
	});
}

function login(user, pass, callback) {
	$.get("http://login.taobao.com/member/login.jhtml",
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

function logout() {
	$.get("http://login.taobao.com/member/logout.jhtml?spm=1.1000386.0.4&f=top");
}