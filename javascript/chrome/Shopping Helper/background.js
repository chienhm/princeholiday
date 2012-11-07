var retry = 0;
var options = {
	"skmod"    : false, 
	"limit"    : 0, 
	"username" : "",
	"password" : "",
	"schedule" : {
		"enabled" : false,
		"toCart"  : null,
		"toOrder" : null,
		"order"   : null
	}
};
var data = null;

//content script proxy
chrome.extension.onRequest.addListener(
	function(request, sender, sendResponse) {
		if(sender.tab) {
			console.log(request.cmd);
			var response = {"options" : options, "retry" : retry};
			switch (request.cmd) {
			case "SET_SKMOD":
				options.skmod = true;
				break;
			case "GET_SKMOD":
				sendResponse(response);
				break;
			case "CLEAR_SKMOD":
				options.skmod = false;
				retry = 0;
				break;				
			case "RETRY":
				retry++;
				break;
				
			case "SET_DATA":
				data = request.data;
				break;
			case "GET_DATA":
				sendResponse(data);
				break;
				
			default:
				chrome.tabs.sendRequest(sender.tab.id, request, sendResponse);
			}
		}
		setState();
	}
);

function setState() {
	chrome.browserAction.setBadgeText({text : options.skmod ? "S" : ""});
}
	
function getConfig() {
	var _config = localStorage["config"];
	if(_config!=null) {
		return JSON.parse(_config);
	}
	return {};
}

function saveOption() {
	localStorage["option"] = JSON.stringify(options);
}
	
function loadOption() { /*private*/
	var _option = localStorage["option"];
	var old = options;
	if(_option) {
		try {
			options = JSON.parse(_option);
		} catch(e) {
			console.error(e);
			options = old;
		}
	}
	options.skmod = false;
}

function open(url) {
	chrome.tabs.create({
		"url" : url,
		"selected" : true
	});
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

loadOption(); /* load option to memory */