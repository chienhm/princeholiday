/*****************************************************************************
 * Taobao Secretary Plugin for Google Chrome
 * Copyright 2012 Morntea.com, All Rights Reserved.
 * Author: chrome@morntea.com
 *****************************************************************************/

chrome.extension.sendRequest({cmd: "GET_OPTIONS"}, function(response) {
	var config = response;
	if(!config.rate) config.rate = {enable:true, keyCode:191};
	if(!config.rate.enable) {
		console.debug("Auto rate is not enabled");
		return;
	}

	/*************************************************************************
	 * Rate only if user does not rate first (config.rate.remark used)
	 *************************************************************************/
	function rate() {
		/* sanity check, though legal in "http://trade.taobao.com/trade/trade_success.htm". */
		if(document.title.indexOf("评价宝贝")==-1) {
			console.debug("Rate may be send to wrong page:" + location.href);
			return;
		}
		
		var rated = false;
		var rates = [/*"good-rate", */"noraml-rate", "bad-rate"];
		var stars = ["description", "attitude", "delivery", "logistics"];
		
		out:for(var i=0; i<rates.length; i++) {
			var radios = $("input."+rates[i]);
			for(var j=0; j<radios.length; j++){
				var radio = $(radios[j]);
				if(radio.attr("checked")=="checked") {
					rated = true;
					break out;
				}
			}
		}
		
		if(!rated) {
			out:for(var i=0; i<stars.length; i++){
				var radios = $("input[type=radio][name="+stars[i]+"]");
				for(var j=0; j<radios.length; j++){
					var radio = $(radios[j]);
					if(radio.val()!=5 && radio.attr("checked")) {
						rated = true;
						break out;
					}
				}
			}
		}
		
		if(rated) {
			if(!confirm("您已经给出了自己的评价，是否继续“一键好评”？选择“是”将修改为全5分好评。")) {
				return;
			}
		}
		
		$("input.good-rate").attr("checked", "checked");
		
		$(".ks-simplestar img:last-child").click();
		/* The same effect with */
		$(stars).each(function(i, item){
			$("input[type=radio][name="+item+"][value=5]").attr("checked", "checked");
		});
		
		if($(".rate-msg").val().trim()=="") {
			$(".rate-msg").val(config.rate.remark);
		}
		
		console.debug("Auto Rate.");
		$("button.submit[type=submit]").click();
	}
	
	/*************************************************************************
	 * Triggers
	 *************************************************************************/
	chrome.extension.onRequest.addListener(
		function(request, sender, sendResponse) {
			if(sender.tab) {
				console.log(request.cmd);
				switch (request.cmd) {
				case "RATE":
					rate();
					break;
					
				default:
					console.log("invalid command: " + request.cmd);
				}
			}
		}
	);

	$("<button type=\"button\">一键好评</button>")
	.attr("style", "float: left; width: 132px;  height: 37px; background-color: #00ADFF; font-size: 20px; margin-right: 30px; color: white; font-weight: bold;")
	.attr("title", "点击此按钮将自动给出全5分好评(快捷键：Ctrl+/)。您可以在淘小蜜选项中关闭此功能。")
	.prependTo($("button.submit[type=submit]").parent())
	.click(rate);
	
	$(window).keyup(function(e){
		if(e.ctrlKey && e.keyCode==config.rate.keyCode) {
			rate();
		}
	});
});
