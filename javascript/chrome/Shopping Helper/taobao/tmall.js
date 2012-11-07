var page = location.pathname;
var host = location.host;

//=============================================================================chaoshi.tmall.com
if(host=="chaoshi.tmall.com") {
if(page=="/order/confirmOrder.htm") {
// top
	function setInvoice() {
		var invoice_radio = $("input[type=radio][name=needInvoice][value=1]");
		var invoice_title = $("input[type=text][name=invoice_title]");
		//var invoice_select = $("select[name=invoice_content]");
		invoice_title.val("个人");
		$("select option[value=家居用品]").attr("selected", "selected");
		invoice_radio.click(); //invoice_radio.attr("checked", "checked");
		//invoice_select.trigger("change")
	}

	console.log("top add listener");
	chrome.extension.onRequest.addListener(
		function(request, sender, sendResponse) {
			if(request.cmd == "testReady") {
				//setInvoice();
				$("html, body").animate({ scrollTop: $(document).height() }, "fast");
				sendResponse({output: "top says I'm ready."});
			}
		}
	);
}
//-----------------------------------------------------------------------------
if(page=="/order/confirmOrderInfo.htm") {
// frame
	/*
	$("#J_PointsToUse").val("2000");
	$("#J_PointsToUse").focus();
	$("#J_PointsToUse").blur();
	*/
	//console.log("frame send testReady");
	
	chrome.extension.sendRequest({cmd: "testReady"}, function(response) {
		//console.log(response.output);
		var total = parseFloat($("#J_Total").text());
		console.log("Total cost: " + total);
		//console.log($("#J_go"));
		chrome.extension.sendRequest({cmd: "GET_SKMOD"}, function(response) {
			var date = new Date();
			console.log("[" + date + date.getMilliseconds() + "]SK MOD: " + (response.options.skmod?"ON":"OFF") + ". Cost limit: " + response.options.limit);
			if(response.options.limit==0) {
				console.log("========= cost limit is not set ==========");
				return;
			}
			if(response.options.skmod) {
				if(total<response.options.limit) {
					$("#J_go").click();
					chrome.extension.sendRequest({cmd: "CLEAR_SKMOD"});
				} else if (response.retry<600){ //~10min
					var interval = response.retry*100;
					if(interval>1000) {
						interval = 1000;
					}
					chrome.extension.sendRequest({cmd: "RETRY"}); //increase retry counter
					setTimeout(function(){
						var date = new Date();
						console.log("[" + date + date.getMilliseconds() + "]Retry: " + response.retry + ". Last Interval: " + interval);
						location.reload();
					}, interval);
				}
			}
		});
	});
}
//-----------------------------------------------------------------------------
// Shopping Cart
if(page=="/cart/my_cart.htm") {
	var url = "http://chaoshi.tmall.com/order/confirmOrder.htm";
	var cart_id = ""; //5192281,5191843
	var tp_id = location.search; //"?tp_id=725677994"
	
	if(tp_id.charAt(0)!="?") {
		tp_id = "?";
	}
	
	$(".uncod").each(function(i, e){
		cart_id += ((i==0)?"":",") + $(e).attr("data-cartid");
	});
	if(cart_id!="") {
		url += tp_id+"&cart_id="+cart_id;
		console.log(url);
		$("#J_Go").prev().attr("class", "btn J_MakePoint box-shadow").attr("href", url).text("全部购买");
	}
}
}