// ==UserScript==
// @name           CheckTime
// @namespace      http://www.morntea.com/
// @include        http://www.baidu.com/*
// ==/UserScript==

function $(id) {return document.getElementById(id);}

function send() {
	var sendTime = new Date();
	GM_xmlhttpRequest({
		method: "GET",
		url: "http://item.tmall.com/item.htm?id=9050791957",//"http://auction1.paipai.com/1D24863A00000000001D3A7B070A25FA",//
		onload: function(response) {
			var receiveTime = new Date();
			var headerString = response.responseHeaders;
			var arr, serverTime=new Date();
			if ((arr = /([1-9]?\d):(\d\d):(\d\d)/g.exec(headerString)) != null) {
				//shop_name = arr[1];
				serverTime.setHours(parseInt(arr[1])+8);
				serverTime.setMinutes(arr[2]);
				serverTime.setSeconds(arr[3]);
				alert(sendTime+"["+sendTime.getMilliseconds()+"] 0\n"
				+serverTime+"[???]\n"
				+receiveTime+"["+receiveTime.getMilliseconds()+"] "+(receiveTime.getTime()-sendTime.getTime()));
			}
			//alert(receiveTime.getTime()-sendTime.getTime());
		}
	});
}

function wait() {
	var THRESHOLD = 700;
	var now = new Date();
	var millisec = now.getMilliseconds();
	if(millisec>=THRESHOLD) {
		setTimeout(function(){send();}, THRESHOLD-millisec+1000);
	} else {
		setTimeout(function(){send();}, THRESHOLD-millisec);
	}
}

wait();