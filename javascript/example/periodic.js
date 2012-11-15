//============================================================================= ������ִ��
// Periodically ajax
if(!(jQuery && jQuery.ajax)) {console.error("jQuery not loaded.");}
var toHandle = -1;
var interval = 5000;
var random = undefined;
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
			"success" : callback //, "error" : function(){console.log("login time out.");clearInterval(toHandle);}
		})
	}, random);
}

function periodicPost(url, data, callback, _interval, _random) {
	if(_interval)interval = _interval;
	if(_random)random = _random;
	periodicRun(function() {
		jQuery.ajax({
			"url"     : url,
			"type"    : "post",
			"data"    : data,
			"cache"   : false,
			"success" : callback
		})
	}, random);
}
//�����Լ���Ƿ񵽻�
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
