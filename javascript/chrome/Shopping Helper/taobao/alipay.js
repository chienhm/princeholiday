var page = location.pathname;
var host = location.host;
log("In page "+location.href);
//-----------------------------------------------------------------------------
chrome.extension.sendRequest({cmd: "GET_SKMOD"}, function(response) {
	if(!response.options.skmod)return;
	//=========================================================================	// return if not in skmod
	var now = new Date();
	var orderTime = null;
	if(response.options.schedule.order) {
		orderTime = new Date(response.options.schedule.order);
		console.log("Order time set to " + orderTime);
	} else {
		orderTime = new Date();
		orderTime.setTime(orderTime.getTime() + 24*60*60*100); //work-around, next day
		console.log("Order time not set.");
		//return;
	}
	//-------------------------------------------------------------------------付款页面，是否自动付款
	if(host=="cashier.alipay.com" && page=="/standard/payment/cashier.htm") {
		var form = $("#balancePayForm")[0];
		if(!form) return;
		if(!response.options.autopay) {
			log("自动付款已关闭。");
			return;
		}
		
		log("自动付款...");
		document.getElementById("payPassword_input").value = "";
		document.getElementById("J_authSubmit").click();
	}
});