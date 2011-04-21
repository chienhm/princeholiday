// ==UserScript==
// @name           GetOrderNo
// @namespace      http://www.cdntaobao.com
// @include        http://trade.taobao.com/trade/itemlist/list_bought_items.htm*
// ==/UserScript==

/* 1. 异步获取订单页面，待付款http://trade.taobao.com/trade/itemlist/listBoughtItems.htm?action=itemlist/QueryAction&event_submit_do_query=1&_fmt.q._0.au=NOT_PAID
 * 2. 如果成功获取订单号，则转3，否则转1
 * 3. 设置订单号码，显示在指定FAKE_ID的订单项目上
 * 4. 迅速对该订单项目截图（记住必须将截图快手打开，清空指定目录下的命名图片，并修改好该订单项目的内容）
 */
var now = new Date();
var atTime = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 16, 0, 0, 100);
var FAKE_ID = "cb63785615887605";
var tryTimes = 0;

function setNo(orderNo) {
	var label = document.getElementById(FAKE_ID);
	label.nextSibling.nextSibling.innerHTML = orderNo;
}

function getNo() {
	tryTimes++;
	console.log("Try getting the order number " + tryTimes + " times");
	GM_xmlhttpRequest({
		method: "GET",
		url: "http://trade.taobao.com/trade/itemlist/listBoughtItems.htm?action=itemlist/QueryAction&event_submit_do_query=1&_fmt.q._0.au=NOT_PAID",//"http://trade.taobao.com/trade/itemlist/list_bought_items.htm",
		onload: function(response) {
			//console.log(response.responseText);
			var matchs = null;
			if ((matchs = /<span class="order-num">(\d+)<\/span>/g.exec(response.responseText)) != null) {
				var orderNo = matchs[1];
				console.log(orderNo + "["+(new Date())+"]");
				setNo(orderNo);
			} else {
				if(tryTimes>=10) {
					console.log("Something wrong? have tried 5 times.");
				} else {
					getNo();
				}
			}
		}
	});
}

//setNo("");
//setTimeout(function(){getNo();}, atTime.getTime()-now.getTime());
//document.getElementById("tradeStatus_63785615887605").parentNode.nextSibling.nextSibling.innerHTML = "成交时间：2011-02-14 16:00";
var t = document.getElementsByTagName("tbody");
for(var i=0; i<t.length; i++) {
	console.log(t[i].className);
	t[i].className = "close-order";//"xcard";
}