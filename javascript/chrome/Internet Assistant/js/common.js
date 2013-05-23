
function formatTime(time) {
	return time.getHours()+":"+time.getMinutes()+":"+time.getSeconds()+"."+time.getMilliseconds();
}

function log(string, time) {
	if(time == undefined) {
		time = new Date();
	}
	console.log("["+formatTime(time)+"] " + string);
}

function focusOrCreateTab(url, callback) {
	chrome.windows.getAll({
		"populate" : true
	}, function(windows) {
		var existing_tab = null;
		for ( var i in windows) {
			var tabs = windows[i].tabs;
			for ( var j in tabs) {
				var tab = tabs[j];
				if (tab.url == chrome.extension.getURL(url)) {
					existing_tab = tab;
					break;
				}
			}
		}
		if (existing_tab) {
			chrome.tabs.update(existing_tab.id, {
				"selected" : false
			}, callback);
		} else {
			chrome.tabs.create({
				"url" : url,
				"selected" : false
			}, callback);
		}
	});
}