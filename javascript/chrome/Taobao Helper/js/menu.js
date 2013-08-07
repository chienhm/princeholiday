
var b = chrome.extension.getBackgroundPage();

function rate() {
	var config = b.getConfig();
	if(config.rate && !config.rate.enable) {
		showMsg("一键评价功能已关闭，请至淘小蜜选项中开启。");
		return;
	}
	function rateAll(tabs) {
		/* maybe it's a bug, if trade and rate page opened at the same time */
		if(!config.rate || !config.rate.batRate) {
			if(tabs.length>1) {
				showMsg("您打开了多个评价页面，批量评价功能尚未启用，请至淘小蜜选项中开启。");
				return;
			}
		}
		if(tabs.length>0){
			for(var i=0; i<tabs.length; i++) {				
				chrome.tabs.sendRequest(tabs[i].id, {cmd: "RATE"});
			}
			window.close();
		}
	}
	
	chrome.tabs.query({currentWindow: true, url:"http://rate.taobao.com/remark_seller.jhtml*"}, function (tabs) {
		rateAll(tabs);
	});
	
	/* will send to trade page and rate frame page */
	/* only available for rate page, ignore http://trade.taobao.com/trade/trade_success.htm 
	chrome.tabs.query({currentWindow: true, url:"http://trade.taobao.com/trade/trade_success.htm*"}, function (tabs) {
		rateAll(tabs);
	});
	*/
}

function showUserList() {
	if($("#userList").is(":hidden")) {
		$("#userList").show("normal");
		$("#more").html("&#60;&#60;");
	} else {
		$("#userList").hide("normal");
		$("#more").html("&#62;&#62;");
	}
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
	$("#getCoin").click(function(){
		openHelper('index.html#coin');
	});
	$("#options").click(function(){
		openHelper('index.html');
	});
	$("#about").click(function(){
		openHelper('index.html#feedback');
	});
});
