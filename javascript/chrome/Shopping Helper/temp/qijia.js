var page = location.pathname;
var host = location.host;
console.log("In page "+location.href);
//-----------------------------------------------------------------------------
if(host=="mall.jia.com" && page=="/order/order/get_c_car_order_info") {
	$("input[name=deliveryType][value=2]").click();
}

var promotion_start = false;

chrome.extension.sendRequest({cmd: "GET_SKMOD"}, function(response) {
	var now = new Date();
	var orderTime = null;
	if(response.options.schedule.order) {
		orderTime = new Date(response.options.schedule.order);
	} else {
		orderTime = new Date();
		orderTime.setTime(orderTime.getTime() + 24*60*60*100); //work-around, next day
		console.log("Order time not set.");
		//return;
	}

	//=========================================================================
	// Login
	if(host=="passport.jia.com" && page=="/cas/login") {
		if(response.options.skmod && response.options.schedule.enabled) {
			if(response.options.username=="") {
				console.log("Username not set.");
				return;
			}
			var loginTime = new Date(orderTime.getTime()-6*60*1000);
			at(loginTime, function() {
				$("#UserName").val(response.options.username);
				$("#UserPsw").val(response.options.password);
				log(response.options.username + " login");
				$("#loginBtn").click();
			}, true);
		}
	}
	
	if(host=="i.jia.com") {
		if(response.options.skmod && response.options.schedule.enabled) {
			chrome.extension.sendRequest({cmd:"GET_DATA"}, function(data) {
				if(data) {
					location.href = "http://mall.jia.com/item/" + data.item_id;
				}
			});
		}
	}

	//=========================================================================
	// product item page
	if((host=="mall.jia.com" || host=="jiaju.jia.com") && page.indexOf("/item/")==0) {
		var item_id = $("#item_id").val();
		var promotion = false;
		var promotion_id = $("#promotion_id").val();
		var promotion_price = 0;
		var promotionObj = $(".cx_box li:last-child span:first-child");
		if(promotionObj.length==0) {
			if($(".sell_price label").text().replace(/\s+/ig, "").indexOf("促销价")==-1) {
				console.log("No promotion detected.");
				return;
			}
			promotion_price = $(".sell_price .price").text().trim();
			promotion = true;
		} else {
			promotion_price = promotionObj.text().trim();
		}
		
		var data = {"item_id":item_id, "promotion":promotion, "promotion_id":promotion_id, "promotion_price":promotion_price};
		console.log(data);
		
		chrome.extension.sendRequest({"cmd":"SET_DATA","data":data});
		if(response.options.skmod && response.options.schedule.enabled) {
			$(".detail_hd").children().append("<span class='price'>[秒杀已设定时间："+formatTime(orderTime)+"]["+item_id+"]</span>");
			reloadToDo(function() {
				log("Add ("+item_id+")"+$(".detail_hd").text().trim()+" to buy.");
				$('#cart_form').attr('action', '/order/order/get_web_order');
				$("#cart_form").submit();
			}, new Date(orderTime.getTime()-5*60*1000));
		}
	}
	
	//=========================================================================
	// In checkout page
	if((host=="mall.jia.com" || host=="jiaju.jia.com") && page=="/order/order/get_web_order") {
		if(!response.options.skmod || !response.options.schedule.enabled) {
			console.log("skmod is not on or order time is not enabled.");
		} else {			
			chrome.extension.sendRequest({cmd:"GET_DATA"}, function(data) {
				var addr = $("input[name=address_id]");
				if(addr.length==0) {
					console.log("Address missing.");
					setTimeout(function(){
						addr = $("input[name=address_id]");
						if(addr.length==0) {
							window.location = "http://mall.jia.com/item/"+data.item_id;
						}
					}, 3000);
				}
			
				$("input[name=deliveryType][value=2]").click();
				var total = parseFloat($("#totle_cash").text());
				log("In web order checkout page. Total cost: " + total);
				var shop_id = $("input[name=shop_id\\[\\]]").val();
				var promotionObj = $("select[name=promotion_"+data.item_id+"]");
				var promotion_string = data.promotion_id+"_"+data.promotion_price;
				var promotion_option = new Option("黄金八点档", promotion_string);
				$(promotion_option).attr("selected", true).appendTo(promotionObj);
				if(shop_id) {
					$(".wrap:last")
						.append($("<iframe id='abc' name='abc' width='100%'/>"))
						//.append($("<iframe id='def' name='def' width='100%'/>"))
						//.append($("<iframe id='ghi' name='ghi' width='100%'/>"))
						;
					at(orderTime, function(){
						chrome.extension.sendRequest({cmd: "CLEAR_SKMOD"});
						log("submit order");
						$("#fromSubmit").attr("target", "abc");
						$("#fromSubmit").submit();
						/*
						$("#fromSubmit").attr("target", "def");
						$("#fromSubmit").submit();
						$("#fromSubmit").attr("target", "ghi");
						$("#fromSubmit").submit();
						*/
					});
				} else {
					log("System error.");
					window.location = "http://mall.jia.com/item/"+data.item_id;
				}
			});
		}
		/*$.getJSON(
			"http://mall.jia.com/order/order/get_item_count?itemid_list=49603",
			function(data) {
			   console.log(data);
			}
		);*/
	} // in confirm order page	
}); // end of extension send request

/******************************************************************
 * For login and lucky draw 
 ******************************************************************/
var users = [

];
if(inPage("passport.jia.com", "/cas/login")) {
	$("#UserPsw").parent().after($("<p class=\"item\"><label for=\"AutoUser\">账&nbsp;&nbsp;&nbsp;户：</label><select id=\"AutoUser\" name=\"AutoUser\" class=\"form_text\"><option/></select></p>"));
	for(var i in users) {
		$(new Option(users[i][0], users[i][1])).appendTo($("#AutoUser"));
		$("#AutoUser").change(function(){
			var name = $("#AutoUser").find("option:selected").text();
			var pass = $("#AutoUser").val();
			$("#UserName1").val(name);
			$("#UserPsw").val(pass);
			if(name!="" && pass!="") {
				$("#loginBtn1").click();
			}
		});
	}
} else if (inPage("mall.jia.com", "/activity/promotion_active/get_sweepstakes")) {
	var myIndex = -1;
	var userText = $(".top_info").text();
	for(var i in users) {
		if(userText.indexOf(users[i][0])!=-1) {
			myIndex = i;
			break;
		}
	}
	console.log(myIndex);
	$(".d-outer").parent().css("visibility", "visible");
	if(myIndex!=-1) {
		if($("#active_no").val()!=1) {
			$(".taste-btn")[0].click();
			$("#nameVali").val(users[myIndex][3]);
			$("#mobileVali").val(users[myIndex][2]);
		}
	}
} else if (inPage("i.jia.com", "/order/order_list.htm")) {
	/*
	chrome.extension.sendRequest({cmd: "GET_SKMOD"}, function(response) {
		var orderTime = null;
		if(response.options.schedule.order) {
			orderTime = response.options.schedule.order;
		}
	});
	*/
		$(".trade_time").each(function(i, e){
			var nodeValue = e.parentNode.childNodes[0].nodeValue.trim();
			if(nodeValue=="") {
				nodeValue = e.parentNode.childNodes[2].nodeValue.trim();
			}
			$(e).append("."+nodeValue.substring(17, 20));
			var time = parseInt(nodeValue.substring(7, 20));
			var date = new Date(time);
			var diff = 0;
			if(date.getSeconds()>=0 && date.getSeconds()<=2) {
				diff = 1000 * date.getSeconds() + date.getMilliseconds();
			}
			if(diff > 700) {
				diff = diff - 500;
				$(e).append("[-"+diff+"]");
			}
			if(date.getSeconds()>=58 && date.getSeconds()<=59) {
				diff = 1000 * (60-date.getSeconds()) - date.getMilliseconds() + 200;
				$(e).append("[+"+diff+"]");
			}
			//console.log(time-orderTime);
		});
}