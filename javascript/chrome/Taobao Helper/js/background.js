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

/* New version is set by update.js */
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
		var mainVer = ver.substring(0, ver.lastIndexOf("."));
		var localMainVer = localStorage.ver.substring(0, localStorage.ver.lastIndexOf("."));
		
		if (localMainVer != mainVer) { /* Big change, notificate user */
			var notification = webkitNotifications.createHTMLNotification("update.html");
			notification.show();
			setTimeout(function(){notification.cancel();}, 10000);
		} else { /* Small change, update quietly */
			
		}
		setVersion(ver);
	}
}, false);