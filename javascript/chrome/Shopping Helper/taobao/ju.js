var page = location.pathname;
var host = location.host;

//-----------------------------------------------------------------------------
if(host=="ju.taobao.com" && page=="/tg/home.htm") {
	chrome.extension.sendRequest({cmd: "GET_SKMOD"}, function(response) {
		if(response.skmod) {
			var buyButton = $(".buy:first"); //buy J_BuySubmit
			console.log(buyButton[0]);
			//setTimeout(function(){buyButton[0].click();}, 100);
			setInterval(function(){buyButton[0].click();}, 100, null);
		}
	});
}

