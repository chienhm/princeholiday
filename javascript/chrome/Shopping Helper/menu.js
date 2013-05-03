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
	document.getElementById("sk").innerHTML = (b.options.skmod?"关闭":"开启") + "抢购";
}

$(function () {
	init();
	$("#keepAlive").click(function(){
		chrome.tabs.executeScript({"file" : "script/jquery.min.js"}, function(){
			chrome.tabs.executeScript({"file" : "taobao/inject/keepAlive.js"});
		});
	});
	$("#bangpai").click(function(){
		openHelper('taobao/bangpai.html');
	});
	$("#sk").click(setAuto);
});