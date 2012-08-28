//15s

//------------------------------------------------------------------------------------------------- 测试抢购延迟
if(!(jQuery && jQuery.ajax)) {console.error("jQuery not loaded.");}

function getServerTime() {
	var st = new Date();
	jQuery.ajax({
		url: window.frames[0].location.href,
		success: function(data){
			//console.log(data);
		}, 
		cache: false,
		complete: function(jqXHR){
			var rt = new Date();
			console.log(
			  "[Send    : " + st + " " + st.getMilliseconds() + "]\n" 
			+ "[Receive : " + rt + " " + rt.getMilliseconds() + "]\n"
			+ "[Server  : " + jqXHR.getResponseHeader("Date") + "]\n"
			+ "[Time    : " + (rt.getTime()-st.getTime()) + "ms]");
		}
	});
}

var toHandle = -1;
function at(atTime, func) {
	var left = atTime.getTime()-now.getTime();
	toHandle = setTimeout(func, left);
	console.log(Math.floor(left/1000) + "s " + left%1000 + "ms left.");
}
//Example:
var now = new Date();
at(new Date(now.getFullYear(),now.getMonth(),now.getDate(), 14, 0, 0, 0), getServerTime);
at(new Date(now.getFullYear(),now.getMonth(),now.getDate(), 14, 0, 0, 500), getServerTime);
at(new Date(now.getFullYear(),now.getMonth(),now.getDate(), 14, 0, 1, 0), getServerTime);
at(new Date(now.getFullYear(),now.getMonth(),now.getDate(), 14, 0, 1, 500), getServerTime);

//------------------------------------------------------------------------------------------------- 齐家网登录
function login(u, p) {
	$("#UserName").val(u);
	$("#UserPsw").val(p);
	$("#loginBtn").click();
}
var toHandle = -1;
function at(time, func) {
	var now = new Date();
	var diff = time.getTime() - now.getTime(); 
	if(diff<0) {console.log("time expired");return;}
	var timeout = (diff>60000)?(diff-60000):(diff>10000)?(diff-10000):diff;
	toHandle = setTimeout(function(){
		if(timeout==diff)func();
		else at(time, func);
	}, timeout);
	console.log("[" + now + ", " + now.getMilliseconds() + "ms] " 
		+ ( (diff>60000) ? (parseInt(diff/60000)+"m "+parseInt(diff%60000/1000)+"s") : (parseInt(diff/1000)+"s") ) + " left.");
}

var now = new Date();
var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), 13, 56, 0, 0);
at(atTime, function(){
	login("lleoeo", "epcc..."); 
});
//------------------------------------------------------------------------------------------------- 齐家网自动订单
function getFormValue(html, name) {
	var regExp = new RegExp("name=\""+name+"\"[^><]+value=\"(.*?)\"", "ig");
	var r = regExp.exec(html);
	if(r!=null) {
		return r[1];
	}
	return "";
}

if(!(jQuery && jQuery.ajax)) {console.error("jQuery not loaded.");}
var toHandle = -1;
var interval = 100;
var random = 200;
function periodicRun(func) {
	var timeout = interval;
	if(toHandle!=-1) { clearInterval(toHandle); toHandle = -1; }
	if(random) { timeout += parseInt(Math.random()*random); }
	toHandle = setTimeout(function() { 
		if(func) func(); 
		periodicRun(func); 
	}, timeout);
}

var item_id = 30288;
var user_id = 100218274;
var shop_id = 243;
var r_instore_count = 188;
var canGoCart = 1;
var count = 1;

var data = {
	"order_item[]" : item_id,
	"item_id"      : item_id,
	"user_id"      : user_id,
};
data[item_id + "_shop_id"] = shop_id;
data[item_id + "_r_instore_count"] = r_instore_count;
data[item_id + "_canGoCart"] = canGoCart;
data[item_id + "_count"] = count;
var lastTryTime = 0;

function order() {
	var now = new Date().getTime();
	var timeDiff = now-lastTryTime;
	if(timeDiff<3000) {
		console.log("Connection alive?" + timeDiff);
		return; //too quick
	} //timeout
	lastTryTime = now;
	$.ajax({
		"type":"post", 
		"url":"http://mall.jia.com/order/order/get_cart_order", 
		"data":data,
		"success":function(html){
			//console.log(html);
			var s, e;
			s = html.indexOf("orderkey");
			s = html.indexOf("value=\"", s) + 7;
			e = html.indexOf("\"/", s);
			var orderkey = html.substring(s, e);
			console.log(orderkey);
			$("input[name=orderkey]").val(orderkey);
			
			s = html.indexOf("totle_cash");
			s = html.indexOf(">", s) + 1;
			e = html.indexOf("<", s);
			var total = parseFloat(html.substring(s, e));
			console.log(total);
			if(total < 500) {
				$("#order_form_sub").click();
			}
		}
	});
}
periodicRun(order);

//------------------------------------------------------------------------------------------------- 齐家网自动预约
if(!(jQuery && jQuery.ajax)) {console.error("jQuery not loaded.");}
var toHandle = -1;
var interval = 100;
var random = 200;
function periodicRun(func) {
	var timeout = interval;
	if(toHandle!=-1) { clearInterval(toHandle); toHandle = -1; }
	if(random) { timeout += parseInt(Math.random()*random); }
	toHandle = setTimeout(function() { 
		if(func) func(); 
		periodicRun(func); 
	}, timeout);
}

var data = {
	"item_id"      : 2660,
	"user_id"      : 100218250,
	"shop_id"      : 258,
	"instoreCount" : 994,
	"qeekaPrice"   : 3840.00,
};
var lastTryTime = 0;

function order() {
	var now = new Date().getTime();
	var timeDiff = now-lastTryTime;
	if(timeDiff<3000) {
		console.log("Connection alive?" + timeDiff);
		return; //too quick
	} //timeout
	lastTryTime = now;
	$.ajax({
		"type"    : "post", 
		"url"     : "http://mall.jia.com/order/order/get_c_car_order_info", 
		"data"    : data,
		"success" : function(html){
			//console.log(html);
			var s, e;
			s = html.indexOf("orderkey");
			s = html.indexOf("value=\"", s) + 7;
			e = html.indexOf("\"/", s);
			var orderkey = html.substring(s, e);
			console.log(orderkey);
			$("input[name=orderkey]").val(orderkey);
			
			s = html.indexOf("product_name");
			s = html.indexOf("<td>", s) + 4;
			e = html.indexOf("</td>", s);
			var price = parseFloat(html.substring(s, e));
			console.log(price);
			if(price < 5000) {
				clearInterval(toHandle);
				$("#order_form_sub").click();
			} else {
				lastTryTime = 0;
			}
		}
	});
}
periodicRun(order);
//------------------------------------------------------------------------------------------------- 周期性领优惠券
if(!(jQuery && jQuery.ajax)) {console.error("jQuery not loaded.");}
var toHandle = -1;
var interval = 1000;
var random = 2000;
function periodicRun(func) {
	var timeout = interval;
	if(toHandle!=-1) { clearInterval(toHandle); toHandle = -1; }
	if(random) { timeout += parseInt(Math.random()*random); }
	toHandle = setTimeout(function() { 
		if(func) func(); 
		periodicRun(func); 
	}, timeout);
}

function periodicGet(url, callback, _interval, _random) {
	if(_interval)interval = _interval;
	if(_random)random = _random;
	periodicRun(function() {
		jQuery.ajax({
			"url"     : url,
			"type"    : "get",
			"cache"   : false,
			"success" : callback
		})
	}, random);
}
function at(time, func) {
	var now = new Date();
	var diff = time.getTime() - now.getTime(); 
	if(diff<0) {console.log("time expired");return;}
	var timeout = (diff>60000)?(diff-60000):(diff>10000)?(diff-10000):diff;
	toHandle = setTimeout(function(){
		if(timeout==diff)func();
		else at(time, func);
	}, timeout);
	console.log("[" + now + ", " + now.getMilliseconds() + "ms] " 
		+ ( (diff>60000) ? (parseInt(diff/60000)+"m "+parseInt(diff%60000/1000)+"s") : (parseInt(diff/1000)+"s") ) + " left.");
}

var now = new Date();
var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), 9, 30, 0, 0);
var endTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), 10, 30, 0, 0).getTime();

function start() {
	periodicGet("http://ecrm.taobao.com/shopbonusapply/buyer_apply.htm?spm=3.304798.277070.1&activity_id=24714203&seller_id=725677994", function(html){
		if(html.indexOf("很抱歉，该优惠券已经被领取完了，请选择其他优惠券")!=-1) {
			var date = new Date(); console.log(date + date.getMilliseconds() + ": fail.");
			if(date.getTime()>endTime) clearInterval(toHandle);
		}
		else clearInterval(toHandle);
	});
}

at(atTime, function(){
	var date = new Date(); console.log(date + date.getMilliseconds());
	start();
});
//clearTimeout(toHandle);