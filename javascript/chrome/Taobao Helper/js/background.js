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
//-----------------------------------------------------------------------------

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

//-----------------------------------------------------------------------------
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

function getVersion() {
	return localStorage["ver"];
}
function setVersion(ver) {
	localStorage["ver"] = ver;
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

window.addEventListener("load", function() {
	var ver = chrome.app.getDetails().version;
	if (localStorage.ver != ver) {
		var notification = webkitNotifications.createHTMLNotification("update.html");
		notification.show();
		setTimeout(function(){notification.cancel();}, 5000);
	}
}, false);