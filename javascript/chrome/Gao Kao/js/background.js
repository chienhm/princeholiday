
var option = {
	user : "",
	pass : "",
	auto : false,
	fillSchool : null,
	schools : {}
};

function persist() {
	localStorage["schools"] = JSON.stringify(option.schools);
}

function getLocalObj(key) {
	var content = localStorage[key];
	if(content!=null) {
		return JSON.parse(content);
	}
	return {};
}

//content script proxy
chrome.extension.onRequest.addListener(
	function(request, sender, sendResponse) {
		if(sender.tab) {
			switch (request.cmd) {
			case "GET_OPT":
				sendResponse(option);
				break;
				
			case "SET_SCHOOL":
				option.schools = request.schools;
				persist();
				sendResponse({"success":true});
				break;
				
			case "SET_MAJOR":
				setMajor(request.code, request.majors);
				sendResponse({"success":true});
				break;
				
			case "CLEAR_ALL":
				clearAll();
				sendResponse({"success":true});
				break;
				
			case "AUTO_DISABLE":
				option.auto = false;
				sendResponse({"success":true});
				break;
				
			case "SAVE_ORDER":
				saveOrder(request.code, request.order);
				sendResponse({"success":true});
				break;
				
			default:
				chrome.tabs.sendRequest(sender.tab.id, request, sendResponse);
			}
		}
	}
);

function saveOrder(code, order) {
	var orders = null;
	if(localStorage["order"]) {
		orders = JSON.parse(localStorage["order"]);
	} else {
		orders = {};
	}
	orders[code] = order;
	localStorage["order"] = JSON.stringify(orders);
}

function setMajor(code, majors) {
	var schools = option.schools;
	for(var name in schools){
		if(schools[name].code==code) {
			localStorage[name] = JSON.stringify(majors);
			console.log("[" + schools[name].code + "]" + name + " => " + majors.length + " majors");
			break;
		}
	}
}

function clearAll() {
	for(var name in localStorage){
		if(name.length==3) {
			delete localStorage[name];
		}
	}
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

function open(url) {
	var manager_url = chrome.extension.getURL(url);
	focusOrCreateTab(manager_url);
}

chrome.browserAction.onClicked.addListener(function(tab) {
	var manager_url = chrome.extension.getURL("view.html");
	focusOrCreateTab(manager_url); 
});