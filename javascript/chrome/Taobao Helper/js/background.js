
var user = null;
var token = null;

function getConfig() {
	var config = null;
	var _config = localStorage["config"];
	try{
		config = JSON.parse(_config);
	}catch(err){
		console.error(err);
	}
	if(!config || typeof config != "object") {
		config = {};
	}
	return config;
}

function saveConfig(config) {
	localStorage["config"] = JSON.stringify(config);
}

function focusOrCreateTab(url) {
	chrome.windows.getAll({
		"populate" : true
	}, function(windows) {
		var existing_tab = null;
		for ( var i in windows) {
			var tabs = windows[i].tabs;
			for ( var j in tabs) {
				var tab = tabs[j];
				if (tab.url == url) {
					existing_tab = tab;
					break;
				}
			}
		}
		if (existing_tab) {
			chrome.tabs.update(existing_tab.id, {
				"selected" : true
			});
		} else {
			chrome.tabs.create({
				"url" : url,
				"selected" : true
			});
		}
	});
}

function openHelper(url) {
	var manager_url = chrome.extension.getURL(url);
	focusOrCreateTab(manager_url);
}

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

//==============================================================================
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

function log(str) {
	console.log(str);
}
/*
 * chrome.browserAction.onClicked.addListener(function(tab) { var manager_url =
 * chrome.extension.getURL("manager.html"); focusOrCreateTab(manager_url); });
 */
function createTabAndInject(url, cssFiles, jsFiles) {
	chrome.tabs.create({"url":url}, function (oTab) {
		console.log(oTab);
		
		function updateTab(tabId, changeInfo, tab) {
			if(oTab.id==tabId && changeInfo.status=="complete") {
				chrome.tabs.onUpdated.removeListener(updateTab);
				for(var index in cssFiles) {
					chrome.tabs.insertCSS(tabId, {file : cssFiles[index]});				
				}
				for(var index in jsFiles) {
					chrome.tabs.executeScript(tabId, {file : jsFiles[index]});
				}
			}
		}
		chrome.tabs.onUpdated.addListener(updateTab);
	});
}

chrome.extension.onRequest.addListener(
	function(request, sender, sendResponse) {
		if(sender.tab) {
			console.log(request.cmd);
			switch (request.cmd) {
			case "GET_OPTIONS":
				sendResponse(getConfig());
				break;
			case "GET_USERS":
				sendResponse(getUser());
				break;
				
			default:
				chrome.tabs.sendRequest(sender.tab.id, request, sendResponse);
			}
		}
	}
);