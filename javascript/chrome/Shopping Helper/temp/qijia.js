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
			reloadToDo(function() {
				$("#UserName").val(response.options.username);
				$("#UserPsw").val(response.options.password);
				log(response.options.username + " login");
				$("#loginBtn").click();
			}, loginTime, 5*1000);
		}
	}
	
	if(host=="i.jia.com") {
		if(response.options.skmod && response.options.schedule.enabled) {
			chrome.extension.sendRequest({cmd:"GET_DATA"}, function(item_id) {
				if(item_id) {
					location.href = "http://mall.jia.com/item/" + item_id;
				}
			});
		}
	}

	//=========================================================================
	// product item page
	if(host=="mall.jia.com" && page.indexOf("/item/")==0) {
		var item_id = $("#item_id").val();
		chrome.extension.sendRequest({"cmd":"SET_DATA","data":item_id});
		if(response.options.skmod && response.options.schedule.enabled) {
			$(".detail_hd").children().append("<span class='price'>[秒杀已设定时间："+formatTime(orderTime)+"]["+item_id+"]</span>");
			reloadToDo(function() {
				log("Add ("+$("#item_id").val()+")"+$(".detail_hd").text().trim()+" to buy.");
				$('#cart_form').attr('action', '/order/order/get_web_order');
				$("#cart_form").submit();
			}, new Date(orderTime.getTime()-5*60*1000));
		}
	}
	
	//=========================================================================
	// web order checkout

function getItem(item_id, callback) {
	log("Ajax test item promotion status ...");
	$.get("/item/"+item_id, function(html) {
		//console.log(html);
		
		var promotion_price = 0;
		var promotion_start = false;
		var s, e;
		s = html.indexOf("<li>促销价：");
		if(s!=-1) { // promotion not start
			e = html.indexOf("元", s);
			promotion_price = html.substring(s+8, e).replace(/<.+?>/ig, "").trim();
			log("=> promotion not start. promotion price: " + promotion_price);
		}
		s = html.search("<label>\t+促 销 价：");//s = html.indexOf("<label>						促 销 价：");
		if(s!=-1) { // promotion starts
			s = html.indexOf("<strong class=\"price\">", s);
			e = html.indexOf("</strong>", s);
			promotion_price = html.substring(s+25, e).trim();
			promotion_start = true;
			log("=> promotion starts. promotion price: " + promotion_price);
		}
		if(promotion_price==0) {
			console.error("promotion_price not got!!!!!!");
		} else if(!/^[\d\.]+$/.test(promotion_price)) {
			console.error("promotion_price format error: " + promotion_price);
		}
		//**********************************
		var shop_id = getFormValue(html, "shop_id");
		/*
		buy_num:1
		instoreCount:3
		item_id:50151
		user_id:100231708
		promotion_id:19624
		shop_id:363
		qeekaPrice:54.50
		jifen:54
		*/
		var data = {
			"buy_num"      : 1,
			"instoreCount" : getFormValue(html, "instoreCount"),
			"item_id"      : item_id,
			"user_id"      : getFormValue(html, "user_id"),
			"promotion_id" : getFormValue(html, "promotion_id"),
			"shop_id"	   : shop_id,
			"qeekaPrice"   : getFormValue(html, "qeekaPrice"),
			"jifen"        : getFormValue(html, "jifen")
		};
		console.log(data);
		callback(data, promotion_price, promotion_start);
	}); // get item ajax get
} // end of function getItem

function getWebOrder(data, promotion_price, callback) {
	log("Ajax get web order.");
	$.ajax({
		"type":"post", 
		"url":"http://mall.jia.com/order/order/get_web_order", 
		"data":data,
		"success":function(html){
			var promotion_start = false;
			//console.log(html);
			//********************************** Test orderkey start
			var orderkey = getFormValue(html, "orderkey");
			
			if(orderkey=="") {
				log("get orderkey failed, try again.");
				console.log("html" + html);
				getWebOrder(data, promotion_price, callback);
				return;
			}
			log("get_web_order post complete, orderkey: " + orderkey);
			//********************************** Test orderkey end
			
			
			//********************************** Test address start
			var address_id = $("input[name=address_id]").val();
			if(address_id) {
				console.log("address id: " + address_id);
			} else {
				console.log("System error, address missing.");
				setTimeout(function() {
					location.href = "/item/" + item_id;
				}, 1000);
			}
			//********************************** Test address end
			//var allprice = parseFloat(getFormValue(html, "allprice"));
			//console.log(allprice);
			
			var s, e;					
			s = html.indexOf("promotion_"+data.item_id)+50;
			e = html.indexOf("</select>", s);
			var promotion = html.substring(s, e).trim();
			console.log(promotion);
			s = html.indexOf("不使用优惠", s);
			s = html.indexOf("<option", s);
			if(s!=-1 && s<e) {
				log("Promotion detected.");
				promotion_start = true;
				if(html.substring(s, e).indexOf(promotion_price)==-1) {
					// Assert. If promotion string does not contain promotion price
					console.log("Fatal error, promotion price not match!");
				}
			}
			
			var promotion_string = data["promotion_id"]+"_"+promotion_price;
			if(callback) {
				callback(orderkey, promotion_string, promotion_start);
			} else /*if(promotion_start && response.options.skmod)*/ {
				submitOrder(orderkey, promotion_string);
				/*if(response.options.schedule.enabled) {
					at(orderTime, function() {
						submitOrder(orderkey, promotion_string);	
					}, true); //force confirm order when timeout
				} else {
					submitOrder(orderkey, promotion_string);
				}*/
			} //if(promotion_start && skmod enabled)

		} // post success function
	}); // confirm order ajax post
} // end of function getWebOrder

function submitOrder(orderkey, promotion_string, promotion_start) {
	var shop_id = $("input[name=shop_id\\[\\]]").val();
	var item_id = $("input[name=item_"+shop_id+"\\[\\]]").val();

	var address_id = $("input[name=address_id]").val();
	var allprice = $("input[name=allprice]").val();
	var shop_fee = $("#select_"+shop_id).val();
	var sendtype = $("#sendtype_"+shop_id).val();
	var orderData = {
		"address_id"	: address_id,
		"user_name"		: "",
		"province"		: 0,
		"address"		: "",
		"zipcode"		: "",
		"mobile"		: "",
		"area_no"		: "",
		"phone_no"		: "",
		"sub_no"		: "",
		"deliveryType"	: 2, //weekend, holiday
		"shop_id[]"		: shop_id,
		"allprice"		: allprice,
		"orderkey"		: orderkey
	}
	orderData["item_"+shop_id+"[]"] 	= item_id;
	orderData["promotion_"+item_id] 	= promotion_string;
	orderData["aDoubleCredits_"+item_id] = 1;
	orderData["account_"+item_id] 		= 1;
	orderData["hasPreOrder_"+item_id] 	= 0;
	orderData["shop_fee_"+shop_id] 		= shop_fee;
	orderData["sendtype_"+shop_id] 		= sendtype;
	orderData["comment_"+shop_id] 		= "";
	orderData["integral_"+shop_id] 		= "";
	console.log(orderData);
	
	$("input[name=orderkey]").val(orderkey);

	/*
	address_id:86823
	user_name:
	province:0
	address:
	zipcode:
	mobile:
	area_no:
	phone_no:
	sub_no:
	deliveryType:2
	shop_id[]:16
	item_16[]:20085
	promotion_20085:19528_1999.00
	aDoubleCredits_20085:1
	account_20085:1
	hasPreOrder_20085:0
	shop_fee_16:2_50.00
	sendtype_16:2
	comment_16:
	integral_16:
	allprice:3998.00
	orderkey:5518eaff63fe36d5595ea9e8dd9e0721
	*/
						
	//$('#fromSubmit').submit();//$("#order_form_sub").click();
	function postOrder() {		
		log("Submit order.");
		$.post("http://mall.jia.com/order/order/add_cart_order", orderData, function(html){
			//console.log(html);
			log("Order submitted.");
			if(html!="") {
				var s = html.indexOf("checkup check_err");
				if(s!=-1) {
					log("Submit failure. Reason: " + html.substring(html.indexOf("<p", s)+16, html.indexOf("</p>", s)));
				}
			}
		});
	}
	var submitTimes = 1;
	if(promotion_start) {
		submitTimes = 3;
	}
	for(var i=0; i<submitTimes; i++) {
		setTimeout(postOrder, 200*i);
	}
	chrome.extension.sendRequest({cmd: "CLEAR_SKMOD"});
	/*$.getJSON(
		"http://mall.jia.com/order/order/get_item_count?itemid_list=49603",
		function(data) {
		   console.log(data);
		}
	);*/
} // end of function submitOrder

function buy(item_id) {
	var order = false;
	var itvHandle = -1;
	var timeAhead = new Date();
	var method = 3;
	var tryTimes = 0;
	log("Using method " + method);
	if(method==1) {
		//-----------------------------------------------------------------
		// Method 1. Periodically test item until promotion starts
		timeAhead.setTime(orderTime.getTime()-5*1000); // 5 seconds ahead
		at(timeAhead, function() {
			log("Start trying item status.");
			itvHandle = setInterval(function() {
				tryTimes++;
				if(tryTimes>20)clearInterval(itvHandle);
				getItem(item_id, function(data, price, start) {
					if(start) {
						clearInterval(itvHandle);
						if(!order) {
							order = true;
							getWebOrder(data, price);
						}
					}
				});
			}, 1000, null);
		}); // end at time
	} else if (method==2) {
		//-----------------------------------------------------------------
		// Method 2. Periodically get web order until promotion detected
		timeAhead.setTime(orderTime.getTime()-5*1000); // 5 seconds ahead
		at(timeAhead, function() {
			getItem(item_id, function(data, price, start) {
				//if(start) {
				//	getWebOrder(data, price);
				//} else {
					// may result to orderkey change
					itvHandle = setInterval(function() {
						tryTimes++;
						if(tryTimes>20)clearInterval(itvHandle);
						getWebOrder(data, price, function(orderkey, promotion, start) {
							if(start) {
								clearInterval(itvHandle);
								if(!order) {
									order = true;
									submitOrder(orderkey, promotion);
								}
							}
						});//getWebOrder
					}, 500, null); //setInterval
					
					/*
					function tryGetWebOrder() {
						tryTimes++;
						if(tryTimes>10)return;
						getWebOrder(data, price, function(orderkey, promotion, start) {
							if(start) {
								submitOrder(orderkey, promotion);
							} else {
								tryGetWebOrder();
							}
						});//getWebOrder
					} // end of tryGetWebOrder
					
					timeAhead.setTime(orderTime.getTime()-2*1000); // 2 seconds ahead
					at(timeAhead, tryGetWebOrder);
					*/
					
				//} // end if
			}); // getItem
		}); // end at
	} else if (method==3) {
		//-----------------------------------------------------------------
		// Method 3. Submit order directly with promotion price
		timeAhead.setTime(orderTime.getTime()-10*1000); // 5 seconds ahead
		at(timeAhead, function() {
			getItem(item_id, function(data, price, start) {
				//timeAhead.setTime(orderTime.getTime()-3*1000); // 3 seconds ahead
				//at(timeAhead, function() {
					getWebOrder(data, price, function(orderkey, promotion, start) {
						at(orderTime, function() {
							submitOrder(orderkey, promotion, true);
						}, true);
					});
				//}, true); // get web order ahead
			});
		}, true); // get item ahead
	}
}

	//=========================================================================
	// In checkout page
	if(inPage("mall.jia.com", "/order/order/get_web_order")) {
		if(!response.options.skmod && !response.options.schedule.enabled) {
			console.log("Order time not enabled.");
		} else {
			$("input[name=deliveryType][value=2]").click();
			var total = parseFloat($("#totle_cash").text());
			log("In web order checkout page. Total cost: " + total);
			var shop_id = $("input[name=shop_id\\[\\]]").val();
			var item_id = $("input[name=item_"+shop_id+"\\[\\]]").val();
			var promotion = $("select[name=promotion_"+item_id+"]").val();
			
			if(shop_id) {	
				//if(promotion.split("_")[1]!="0") {
				//	log("Promotion ("+promotion+") starts, submit order.");
					//$('#fromSubmit').submit();
				//} else {
					buy(item_id);
				//}
			} else {
				log("System error.");
				chrome.extension.sendRequest({cmd:"GET_DATA"}, function(item_id) {
					//console.log("[" + shop_id + "]get saved item_id:"+item_id);
					setTimeout(function() {
						location.href = "/item/" + item_id;
					}, 1000);
				});
			}
		}
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
			$("#UserName").val(name);
			$("#UserPsw").val(pass);
			if(name!="" && pass!="") {
				$("#loginBtn").click();
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