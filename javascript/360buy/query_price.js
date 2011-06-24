var head = document.getElementsByTagName('head')[0];
var script = document.createElement('script');
script.type = 'text/javascript';
script.src= 'http://code.jquery.com/jquery-1.5.2.min.js';
head.appendChild(script);


//var jdprice={"P":"\uFFE5598.00","I":332171,"M":"\uFFE5598.00"};
//http://price.360buy.com/price-b-P5274995D0619B85DBA9C300E0625D990.html
var link = "http://price.360buy.com/price-b-PABC324C9EEB720AB3E530099C8265D40.html";
var lastprice = "";
var tick;
var count = 0;
function loulin(){
	$.ajax({url: link, dataType: "text",
		success: function (text) {
			console.log((count++) + ". " + text + "[" + (new Date()) + "]");
			eval(text);
			if(lastprice!="" && lastprice!=jdprice.P) {
				clearTimeout(tick);
				window.open("http://jd2008.360buy.com/purchase/InitCart.aspx?pid="+jdprice.I+"&pcount=1&ptype=1");
				setTimeout(function(){
					window.open("http://jd2008.360buy.com/purchase/shoppingcartselect.aspx");
				}, 1000);
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