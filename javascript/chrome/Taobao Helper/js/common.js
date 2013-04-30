function showMsg(msg) {
	var notification = webkitNotifications.createNotification("logo/logo.png", "淘小蜜友情提示", msg);
	notification.show();
	setTimeout(function(){notification.cancel();}, 5000);
}

function inPage(host, page) {
	return (host==location.host && page==location.pathname);
}

function formatTime(time) {
	return time.getHours()+":"+time.getMinutes()+":"+time.getSeconds()+"."+time.getMilliseconds();
}

function log(string, time) {
	if(time == undefined) {
		time = new Date();
	}
	console.log("["+formatTime(time)+"] " + string);
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
	var manager_url = url;
	if(url.indexOf("http")!=0) {
		manager_url = chrome.extension.getURL(url);
	}
	focusOrCreateTab(manager_url);
}
