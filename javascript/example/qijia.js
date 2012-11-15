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
