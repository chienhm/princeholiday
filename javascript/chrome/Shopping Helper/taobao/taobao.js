
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
			if(callback)callback();
		})
	});
}

//==============================================================================
// User Login

function checkLogin(callback) {
	$.ajax({
		url: "http://i.taobao.com/my_taobao.htm",
		success: function(data){
			console.log(data);
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

//==============================================================================
// http://www.json.org/js.html
function saveUser(n, p) {
	var _users = localStorage["users"];
	var users = (_users==null) ? {} : JSON.parse(_users);
	users[n] = p;
	localStorage["users"] = JSON.stringify(users);
}

function delUser(n) {
	var _users = localStorage["users"];
	if(_users!=null && n!="") {
		var users = JSON.parse(_users);
		delete users[n];
		localStorage["users"] = JSON.stringify(users);
	}
}

function getUser() {
	var _users = localStorage["users"];
	if(_users!=null) {
		return JSON.parse(_users);
	}
	return null;
}

function logout() {
	$.get("http://login.taobao.com/member/logout.jhtml?spm=1.1000386.0.4&f=top");
}

//==============================================================================
//Tmall Shop
var tp_id = -1;

function getTpId() {
	$.get("http://chaoshi.tmall.com/", function(html){
		var regExp = /tp_id=(\d+)/ig;
		var result = null;
		if((result = regExp.exec(html)) != null) {
			tp_id = result[1];
			console.log("tp_id:" + tp_id);
		}
	});
}

function tmallBuy(item_id) {
	addToCart(item_id, function(data){
		var cart_id = "";
		for(var i in data.items) {
			cart_id += (cart_id==""?"":",") + data.items[i].cart_id;
		}
		console.log(cart_id);
		chrome.tabs.create({
			"url" : "http://chaoshi.tmall.com/order/confirmOrder.htm?cart_id="+cart_id+"&tp_id="+tp_id,
			"selected" : true
		});
	});
}

function addToCart(item_id, callback) {
	if(tp_id==-1) {
		console.log("No tp_id.");
		return;
	}
	if(token==null) {
		console.log("No token.");
		return;
	}
	$.post(
		"http://chaoshi.tmall.com/cart/add_cart_item.htm",
		{
			"item_id"		: item_id,
			"outer_id"		: item_id,
			"outer_id_type"	: "1",
			"quantity"		: "1",
			"key"			: "",
			"addfrom"		: "sku",
			"from"			: "miniCart",
			"tp_id"			: tp_id,
			"_tb_token_"	: token,
		},
		function(data){
			console.log(data);
			/*if(data.maxQuantity!=0) {
				setTimeout(function(){addToCart(item_id, null);}, 500);
			}*/
			if(callback)callback(data);
		},
		"json"
	);
}

function showCart() {
	var rnd = new Date().getTime();
	var url = "http://chaoshi.tmall.com/cart/mini_cart.htm?from=miniCart&tp_id="+tp_id+"&_tb_token_="+token+"&vvjxsm"+rnd+"="+rnd;
	$.getJSON(url, function(json){
		console.log(json);
	});
}