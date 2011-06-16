var head = document.getElementsByTagName('head')[0];
var script = document.createElement('script');
script.type = 'text/javascript';
script.src= 'http://code.jquery.com/jquery-1.5.2.min.js';
head.appendChild(script);


//var jdprice={"P":"\uFFE5598.00","I":332171,"M":"\uFFE5598.00"};
var link = "http://price.360buy.com/price-b-PAFF7209820A7B0B41F5E3D631D29F143.html";
var lastprice = "";
var tick;
function loulin(){
	$.ajax({url: link, dataType: "text",
		success: function (text) {
			console.log(text);
			eval(text);
			if(lastprice!="" && lastprice!=jdprice.P) {
				clearTimeout(tick);
				window.open("http://jd2008.360buy.com/purchase/InitCart.aspx?pid="+jdprice.I+"&pcount=1&ptype=1");
				alert("price changed!");
			} else {
				lastprice = jdprice.P;
				tick = setTimeout(loulin, 1000);
			}
		},
		error: function(xhr, errmsg) {
			alert(errmsg);
		}
	});
}
loulin(); 