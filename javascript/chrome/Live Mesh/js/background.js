var options = {
	"redirect"     : ""
};

//content script proxy
chrome.extension.onRequest.addListener(
	function(request, sender, sendResponse) {
		if(sender.tab) {
			console.log(request.cmd);
			var response = {"options" : options};
			switch (request.cmd) {
			case "GET_OPTIONS":
				sendResponse(options);
				break;
			case "SET_OPTIONS":
				options = request.options;
				sendResponse(options);
				break;
				
			default:
				chrome.tabs.sendRequest(sender.tab.id, request, sendResponse);
			}
		}
	}
);

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
/*
chrome.browserAction.onClicked.addListener(function(tab) {
	var manager_url = chrome.extension.getURL("index.html");
	focusOrCreateTab(manager_url); 
});
 */