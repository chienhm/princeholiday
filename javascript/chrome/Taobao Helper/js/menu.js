
var b = chrome.extension.getBackgroundPage();

function openHelper(url) {
	b.openHelper(url);
}

function rate() {
	chrome.tabs.query({currentWindow : true, highlighted : true}, function (tabs) {
		var rateUrl = "http://rate.taobao.com/remark_seller.jhtml";
		var rateSuccess = "http://trade.taobao.com/trade/trade_success.htm";
		if(tabs.length>0){
			var tab = tabs[0];
			if(tab.url.indexOf(rateUrl)==0 || tab.url.indexOf(rateSuccess)==0) {
				chrome.tabs.executeScript(tab.id, {file:"res/rate.js", allFrames:true});
			} else {
				showMsg("请进入宝贝评价页面。");
			}
		}
	});
}

function showUserList() {
	$("#userList").is(":hidden") ? 
		$("#userList").show("normal") : $("#userList").hide("normal");
}

var hasUser = false;
function initUsers() {
	var users = b.getUser();
	if(users) {
		$.each(users, function(n, p) {
			//$("<li/>").append($("<a/>").html(n).bind("click", function(){
			//	login(n);
			//})).appendTo($('#userList'));
			$("<li><a href=\"#\">"+n+"</a></li>").bind("click", function(){
				login(n);
			}).appendTo($('#userList'));
			hasUser = true;
		})
	}
	if(!hasUser) {
		$("<li/>").html("尚无保存的用户").appendTo($('#userList'));
	}
}

function login(username) {
	var config = b.getConfig();
	if(!config || !config.autoLogin) {
		showMsg("自动登录已禁用，请到选项中开启。");
		return;
	};
	if(username) {
		config.currentUser = username;
		b.saveConfig(config);
	} else {
		if(!hasUser) {
			showMsg("没有已保存的用户。");
			return;
		}
		config.currentUser = null;
		b.saveConfig(config);
	}
	b.createTabAndInject("http://login.taobao.com/member/logout.jhtml", [], ["res/login.js"]);//"https://login.taobao.com/member/login.jhtml"
}

$(function () {
	initUsers();
	$("#autoLogin").bind('click', function(){login();});
	$("#more").bind('click', showUserList);
	$("#goodRate").bind('click', rate);
	$("#exchange").bind('click', function(){
		openHelper('coin_exchange/coin_exchange.html');
	});
	$("#getCoin").bind('click', function(){
		openHelper('get_coin/get_coin.html');
	});
});
