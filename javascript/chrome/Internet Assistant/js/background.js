
function showMsg(msg) {
	var notification = webkitNotifications.createNotification("images/logo_19.png", "冲浪助手", msg);
	notification.show();
	setTimeout(function(){notification.cancel();}, 3000);
}

function getConfig(name) {
	return localStorage[name];
}

function setConfig(name, value) {
	localStorage[name] = value;
}

/*
chrome.extension.onRequest.addListener(
	function(request, sender, sendResponse) {
		if(sender.tab) {
			switch (request.cmd) {
			default:
				chrome.tabs.sendRequest(sender.tab.id, request, sendResponse);
			}
		}
	}
);
*/
