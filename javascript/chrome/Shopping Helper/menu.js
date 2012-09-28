var b = chrome.extension.getBackgroundPage();

function openHelper(url) {
	b.openHelper(url);
}

function setAuto() {
	b.options.skmod = !b.options.skmod;
	b.setState();
	init();
}

function init() {
	document.getElementById("sk").innerHTML = (b.options.skmod?"关闭":"开启") + "通用抢购";
}

document.addEventListener('DOMContentLoaded', function () {
	init();
	document.getElementById("chaoshi").addEventListener('click', function(){
		openHelper('taobao/tmall.html');
	});
	document.getElementById("yihaodian").addEventListener('click', function(){
		openHelper('yihaodian/index.html');
	});
	document.getElementById("sk").addEventListener('click', setAuto);
});