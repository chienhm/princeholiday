/*****************************************************************************
 * Taobao Secretary Plugin for Google Chrome
 * Copyright 2012 Morntea.com, All Rights Reserved.
 * Author: chrome@morntea.com
 *****************************************************************************/

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

/*****************************************************************************
 * Get value from localStorage, return object expected.
 *****************************************************************************/
function getConfig(cfgName) {
	var name = "config";
	var config = null;
	if(cfgName) name = cfgName;
	var _config = localStorage[name];
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

function saveConfig(config, cfgName) {
	var name = "config";
	if(cfgName) name = cfgName;
	localStorage[name] = JSON.stringify(config);
}

/*****************************************************************************
 * User operation
 *****************************************************************************/
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

function getVersion() {
	return localStorage["ver"];
}

/* New version is set by update.js */
function setVersion(ver) {
	localStorage["ver"] = ver;
}

/*****************************************************************************
 * Message passing from content script
 *****************************************************************************/
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

/*****************************************************************************
 * Everyday notification for getting coins
 *****************************************************************************/
var timeout = -1;
function everydayCheck() {
	var config = getConfig();
	if(config.everydayCheck!=null && !config.everydayCheck) {
		console.log("Everyday get coin check disabled.");
		chrome.browserAction.setTitle({title:""})
		chrome.browserAction.setBadgeText({text:""});
		return;
	}

	var lastCoinTime = 0;
	if(localStorage["lastCoinTime"]){
		lastCoinTime = localStorage["lastCoinTime"];
	}
	var now = new Date();
	var last = new Date();
	last.setTime(lastCoinTime);
	if(now.getFullYear()==last.getFullYear() && now.getMonth()==last.getMonth() && now.getDate()==last.getDate()) {
		/* today already get coins */
		chrome.browserAction.setTitle({title:"感谢您今天通过淘小蜜领取淘金币！\n您可以通过淘小蜜选项关闭此提示。"})
		chrome.browserAction.setBadgeText({text:""});
	} else {
		last.setMilliseconds(0);last.setSeconds(0);last.setMinutes(0);last.setHours(0)
		var days = Math.floor((now.getTime()-last.getTime())/(24*60*60*1000));
		chrome.browserAction.setTitle({title:"您已经连续"+days+"天没通过淘小蜜领金币啦。\n您可以通过淘小蜜选项关闭此提示。"})
		chrome.browserAction.setBadgeText({text:""+days});
	}
	
	/* next check time is next day */
	if(timeout!=-1) {
		clearTimeout(timeout);
	}
	var tonight = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 23, 59, 59, 999);
	nextTime = tonight.getTime()-now.getTime()+1001;
	timeout = setTimeout(everydayCheck, nextTime);
}

/*****************************************************************************
 * Main()
 *****************************************************************************/
window.addEventListener("load", function() {
	var ver = chrome.app.getDetails().version;
	if (localStorage.ver != ver) {
		var mainVer = ver.substring(0, ver.lastIndexOf("."));
		var localMainVer = localStorage.ver.substring(0, localStorage.ver.lastIndexOf("."));
		
		if (localMainVer != mainVer) { /* Big change, notify user */
			var notification = webkitNotifications.createHTMLNotification("update.html");
			notification.show();
			setTimeout(function(){notification.cancel();}, 10000);
		} else { /* Small change, update quietly */
			
		}
		setVersion(ver);
	}
	everydayCheck();
}, false);