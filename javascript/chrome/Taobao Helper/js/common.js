function showMsg(msg) {
	var notification = webkitNotifications.createNotification("logo.png", "淘小蜜友情提示", msg);
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