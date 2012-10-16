
var b = chrome.extension.getBackgroundPage();

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
	var config = b.getConfig();
	var defaultUser = null;
	if(config && config.defaultUser) {
		defaultUser = config.defaultUser;
	}
	if(users) {
		$.each(users, function(n, p) {
			var name = n;
			if(n==defaultUser) {
				name = "<b>"+n+"</b>";
			}
			$("<li><a href=\"#\">"+name+"</a></li>").bind("click", function(){
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
	checkLoginPage();
}

function checkLoginPage() {
	chrome.tabs.query({windowId : chrome.windows.WINDOW_ID_CURRENT, highlighted : true}, function (tabs) {
		if(tabs.length==0) {
			console.error("Can't get current tab.");
			return;
		}
		chrome.tabs.executeScript(tabs[0].id, {file : "js/encrypt.js", allFrames : true});
		chrome.tabs.executeScript(tabs[0].id, {file : "res/login.js", allFrames : true}, 
			function(r){
				console.log(r);
				var loginPageFound = false;
				for(var i in r) {
					loginPageFound = loginPageFound || r[i];
				}
				if(!loginPageFound) {				
					b.createTabAndInject("http://login.taobao.com/member/logout.jhtml", [], ["js/encrypt.js", "res/login.js"]);//"https://login.taobao.com/member/login.jhtml"
				}
			}
		);
	});
}

$(function () {
	initUsers();
	$("#autoLogin").click(function(){login();});
	$("#more").click(showUserList);
	$("#goodRate").click(rate);
	$("#exchange").click(function(){
		openHelper('coin_exchange/coin_exchange.html');
	});
	$("#getCoin").click(function(){
		openHelper('get_coin/get_coin.html');
	});
	$("#options").click(function(){
		openHelper('options.html');
	});
	$("#about").click(function(){
		openHelper('about.html');
	});
});
