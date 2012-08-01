//=============================================================================
// Get server time and time diff between local and server
if(!(jQuery && jQuery.ajax)) {console.error("jQuery not loaded.");}

function futureRun(func, ms) {
	var t0 = new Date();
	setTimeout(func, 1000-t0.getMilliseconds() + ms);
}

function getServerTime() {
	var st = new Date();
	jQuery.ajax({
		url: location.href,
		success: function(data){
			//console.log(data);
		}, cache : false, 
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

//futureRun(getServerTime, 500);
//=============================================================================
// Periodically ajax
if(!($ && $.ajax)) {console.error("jQuery not loaded.");}
var toHandle = -1;
var interval = 5000;
var random = undefined;
function periodicRun(func) {
	var timeout = interval;
	if(toHandle!=-1) { clearInterval(toHandle); toHandle = -1; }
	if(random) { timeout += parseInt(Math.random()*random); }
	toHandle = setTimeout(function() { func(); periodicRun(func); }, timeout);
}

function periodicGet(url, callback, _interval, _random) {
	if(_interval)interval = _interval;
	if(_random)random = _random;
	periodicRun(function() {
		$.ajax({
			"url"     : url,
			"type"    : "get",
			"cache"   : false,
			"success" : callback
		})
	}, random);
}

function periodicPost(url, data, callback, _interval, _random) {
	if(_interval)interval = _interval;
	if(_random)random = _random;
	periodicRun(function() {
		$.ajax({
			"url"     : url,
			"type"    : "post",
			"data"    : data,
			"cache"   : false,
			"success" : callback
		})
	}, random);
}
//周期性检查是否到货
/*if(typeof(toHandle)!="undefined") {
	clearTimeout(toHandle);
	alert("clear timer, start again");
}
periodicGet(location.href, function(html){
	if(html.indexOf("noneBuySpan\" style=\"display:\">")==-1){
		goToAddCart();
		window.open("http://www.homevv.com/vvshopCartView.jhtml");
	} else {
		console.log(new Date() + "sold out.");
	}
});*/