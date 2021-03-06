﻿var page = location.pathname;
var host = location.host;
log("In page "+location.href);
//-----------------------------------------------------------------------------
var taobao = false;
var tmall = false;
var promotion_start = false;

if(host.indexOf("taobao")!=-1) {
	taobao = true;
} else if(host.indexOf("tmall")!=-1) {
	tmall = true;
}

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

	//=============================================================================login.taobao.com
	if(host=="login.taobao.com" && page=="/member/login.jhtml") {
		if(response.options.username) {
			at(orderTime, function() {
				$("#J_SafeLoginCheck").click();
				$("#TPL_username_1").val(response.options.username);
				$("#TPL_password_1").val(response.options.password);
				$("#J_SubmitStatic").click();
			}, true);
		}
	}
	//=========================================================================	// product item page
	function addOrder() {
		log("Go to checkout.");
		//-------------------------------- select group
		function select() {
			var dd = $("dd");
			$.each(dd, function(i, e){
				var choices = $("ul li a[href=#] span", e);
				if(choices.length>0) {
					//console.log(choices);
					var selected = $("ul li.tb-selected", e);
					if(selected.length==0) {
						choices[0].click();
					}
				}
			});//end of each
		}
		select();
		//-------------------------------- select group
		
		if($("#J_FrmBid").attr("action")=="") { // for tmall
			console.log("Set action for tmall.");
			$("#J_FrmBid").attr("action", "http://buy.tmall.com/order/confirm_order.htm");
		}
		//-------------------------------- form hijack
		var form = $("#J_FrmBid")[0];
		var submit = form.submit;
		form.submit = function(){
			var data = {};
			for (var i = 0; i<this.elements.length; i++) {
				data[this.elements[i].name] = this.elements[i].value;
			}
			console.log(data);
			console.log("Action: " + this.action);
			chrome.extension.sendRequest({"cmd" : "SET_DATA", "data" : data});
			//$.post(this.action, data, function(html){console.log(html);});
			//submit.apply(this);
		};
		//-------------------------------- form hijack
		//$("#J_FrmBid")[0].submit();
		
		function linkBuy(){
			select();
			log("点击提交按钮.");
			var buy = null;
			if(tmall) buy = $("#J_LinkBuy")[0];
			if(taobao) buy = $("a.J_LinkBuy")[0];
			if(buy) buy.click();else console.log("Can't find buy button!!!!!!!!!!!!!!!!!!!!!!!!!");
			setTimeout(linkBuy, 200);
		}
		linkBuy();
	} // end of addOrder()
	
	if(inPage("item.taobao.com", "/item.htm") 
		|| inPage("detail.tmall.com", "/item.htm") || $("#bd").length>0
		|| inPage("localhost:8888", "/taobao/item.htm")) {
		
		console.log("Order time set to " + formatTime(orderTime));
		if(response.options.skmod && response.options.schedule.enabled) {
			log("Reload to buy.");
			reloadToDo(addOrder, orderTime, 300);
		}
	}
	//=========================================================================	// confirm order page
	if(inPage("buy.taobao.com", "/auction/buy_now.jhtml") 
		|| inPage("buy.taobao.com", "/auction/buy_now.htm")
		|| inPage("localhost:8888", "/taobao/buy_now.jhtml")
		|| inPage("localhost:8888", "/taobao/buy_now.htm")
		|| inPage("buy.tmall.com", "/order/confirm_order.htm")) {
		
		function trySubmit() {
			console.log($("#verify-code"));
			if($("#verify-code").length!=0) {
				log("出现验证码");
				$("#verify-code").focus();
				$("#verify-code").keyup(function(e){
					if(e.keyCode==13) submitOrder();
				});
			} else {
				submitOrder();
				setTimeout(trySubmit, 500);
			}
		}
		function submitOrder() {
			var btn = null;
			if(tmall) btn = $("#J_go")[0];
			if(taobao) btn = $("#J_Go")[0];
			log("点击提交订单按钮");
			if(btn)btn.click();else console.log("Can't find submit button!!!!!!!!!!!!!!!!!!!!!!!!!");
		}
		// Test for phone fee
		if($("#J_phone").length>0) {
			$("#J_phoneRepeat").val("13764084288");
			$("#J_phone").focus();
			$("#J_phone").val("13764084288");
			$("#J_phone").blur();
		}
		if($("#J_Form").length > 0) {
			trySubmit();
			//chrome.extension.sendRequest({cmd: "CLEAR_SKMOD"});
		} else if ($("#J_OrderForm").length > 0) {
			console.log("old look");
			if($("#checkCodeImg").length==0) {
				log("No check code, submit order");
				$("#performSubmit").click();
				$("#J_PerformSubmit").click(); //for old mobile charge
			}
			/*$.get("http://item.taobao.com/item.htm?id=14361087260", function(html){
				console.log(html);
				chrome.extension.sendRequest({cmd: "GET_DATA"}, function(data) {
					console.log(data);
					for(var name in data) {
						var v = getFormValue(html, name);
						if(v!="") {
							data[name] = v;
						}
						//console.log(name + ": " + data[name]);
					}
					console.log(data);*/
					log("Input checkcode to buy, action: " + $("#J_OrderForm").attr("action"));
					$("#checkCodeImg").css("height", "100px");
					$("#J_checkCodeInput").focus().keyup(function() {
						if(this.value.length==4) {
							log("Submit order");
							$("#J_PerformSubmit")[0].click();
						}
					}); //checkcode
				/*}); // end chrome request
			});// end $.get
			*/
		}
	} /* end if submit order */
	
	/* 领取淘金币 */
	if(inPage("trade.taobao.com", "/trade/pay_success.htm")) {
		$(".J_MakePoint").filter(function(index) {
			return $(this).text()== "点此领取";
		})[0].click();
	}
	
}); // end of extension send request
