var page = location.pathname;
var host = location.host;

//-----------------------------------------------------------------------------
if(host=="www.lamiu.com" && page=="/cart.php") {
	document.getElementById("to_checkout_btn").click();
}

if(host=="www.lamiu.com" && page=="/checkout.php") {
	//document.getElementById("pay2").click();
	$("#pay2").click();
}

//html js
if(false) {
	var toHander = -1;
	function buy() {
		$.ajax({type: "POST",url: "flow.php",data: $(document.forms.shoppingForm).serialize(),success: function(data) {
			console.log(data);
			var price = parseFloat(data.countCartRecords.price);
			console.log(price);
			if (data.lm_add_to_cart.error==0) {
				location.href = "/cart.php";
			} else {
				toHander = setTimeout(buy, 300);
			}
		},dataType: "json"});
	}

	var toHander = -1;
	function at(atTime, func) {
		var left = atTime.getTime()-now.getTime();
		toHander = setTimeout(func, left);
		console.log(Math.floor(left/1000) + "s " + left%1000 + "ms left.");
	}
	//Example:
	var now = new Date();
	var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), 15, 52, 30, 30);
	at(atTime, buy);
	clearTimeout(toHander);
}