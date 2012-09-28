function addProduct(url) {
	//"http://www.yihaodian.com/product/2120996_4227";
	var p = url.lastIndexOf("/");
	var s = url.substring(p+1).split("_");
	addToCart(s[0], s[1]);
}

function addToCart(productID, merchantID) {
	$.getJSON("http://www.yihaodian.com/product/addGlobalProduct2.do?productID="+productID+"&merchantId="+merchantID+"&productNum=1&callback=?", function(data){
		console.log(data);
	});
}

function yihaodian() {
	$.get("http://www.yihaodian.com/product/cart.do?action=view", function(html){
		//console.log(html);
		var url = "http://www.yihaodian.com/checkoutV3/index.do";
		b.open(url);
		//http://www.yihaodian.com/checkoutV3/payment/savePayment.do?paymentMethodID=50&paymentGatewayID=2&rd=0.20977296447381377
		/* $.get(url, function(html){
			console.log(html);
		}); */
	});
	
}