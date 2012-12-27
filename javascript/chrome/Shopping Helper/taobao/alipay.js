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
	var DES_PASSWORD = "";
	var SEASIDE_GOGO = "";	
	//-------------------------------------------------------------------------付款页面，是否自动付款
	if(host=="cashier.alipay.com" && page=="/standard/payment/cashier.htm") {
		var form = $("#balancePayForm")[0];
		if(!form) return;
		if(!response.options.autopay) {
			log("自动付款已关闭。");
			return;
		}
		
		log("自动付款...");
		var token = form["_form_token"].value;
		var order = form["orderId"].value;
		vp();
		
		function vp() {
			var url = "https://cashier.alipay.com/standard/payment/validatePayPassword.json";
			$.post(url, {
				J_aliedit_key_hidn:"payPassword",
				J_aliedit_using:"true",
				_input_charset:"utf-8",
				orderId:order,
				payPassword:DES_PASSWORD,
				secProdCheckStep:"STEP1"
			}, function(data){console.log(data);sc();});
		}

		function sc() {
			var url = "https://cashier.alipay.com/standard/step2SecProdCheck.json";
			$.post(url, {
				"J_aliedit_key_hidn" : "payPassword",
				"J_aliedit_uid_hidn" : "",
				"J_aliedit_using" : "true",
				"REMOTE_PCID_NAME" : "_seaside_gogo_pcid",
				"_form_token" : token,
				"_input_charset" : "utf-8",
				"_seaside_gogo_" : SEASIDE_GOGO,
				"_seaside_gogo_p" : "",
				"_seaside_gogo_pcid" : "",
				"orderId" : order,
				"payPassword" : DES_PASSWORD
			}, function(data){console.log(data);submit();});
		}

		function submit() {
			form["_seaside_gogo_"].value = SEASIDE_GOGO;
			form["payPassword"].value = DES_PASSWORD;
			form.submit();
		}
	}
});