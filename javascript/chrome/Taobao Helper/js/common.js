function showMsg(msg) {
	var notification = webkitNotifications.createNotification("logo_128_128.png", "淘小蜜友情提示", msg);
	notification.show();
	setTimeout(function(){notification.cancel();}, 5000);
}

function inPage(host, page) {
	return (host==location.host && page==location.pathname);
}