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

//------------------------------------------------------------------------------------------------- 周期性领优惠券
if(!(jQuery && jQuery.ajax)) {console.error("jQuery not loaded.");}
var toHandle = -1;
var interval = 1000;
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

function start() {
	periodicGet("http://ecrm.taobao.com/shopbonusapply/buyer_apply.htm?spm=3.304798.277070.1&activity_id=24714203&seller_id=725677994", function(html){
		if(html.indexOf("很抱歉，该优惠券已经被领取完了，请选择其他优惠券")!=-1) {
			var date = new Date(); console.log(date + date.getMilliseconds() + ": fail.");
		}
		else clearInterval(toHandle);
	});
}

var now = new Date();
var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), 10, 0, 0, 0);
at(atTime, function(){
	var date = new Date(); console.log(date + date.getMilliseconds());
	start();
});
//clearTimeout(toHandle);