
var b = chrome.extension.getBackgroundPage();

function openHelper(url) {
	b.openHelper(url);
}
	
document.addEventListener('DOMContentLoaded', function () {
	document.getElementById("exchange").addEventListener('click', function(){
		openHelper('coin_exchange/coin_exchange.html');
	});
	document.getElementById("getCoin").addEventListener('click', function(){
		openHelper('get_coin/get_coin.html');
	});
});