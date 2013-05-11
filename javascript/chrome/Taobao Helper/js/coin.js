/*****************************************************************************
 * Taobao Secretary Plugin for Google Chrome
 * Copyright 2012 Morntea.com, All Rights Reserved.
 * Author: chrome@morntea.com
 *****************************************************************************/
var tasks = [];

function Task(id, name, func, option) {
	this.name = name;
	this.id = id;
	this.func = func;
	this.tips = name;
	this.timeout = 2000;
	this.url = "http://www.morntea.com";
	this.skip = false;
	this.standalone = false;
	if(option) {
		if(option.tips) {
			this.tips = option.tips;
		}
		if(option.url) {
			this.url = option.url;
		}
		if(option.timeout) {
			this.timeout = option.timeout;
		}
		if(option.skip) {
			this.skip = option.skip;
		}
	}
	
	this.gain = 0;
	this.finish = false;
	this.success = false;
	this.sTime = 0; //millisecond
	this.eTime = 0; //millisecond
}

Task.prototype = {
	start: function() {
		this.sTime = new Date().getTime();
		$("#"+this.id).css("background-color", "lightblue");
		log("Task [" + this.name + "] starts.");
		this.func();
		if(!this.standalone) {
			setTimeout(schedule, this.timeout);
		}
	},
	
	complete: function() {
		this.eTime = new Date().getTime();
		this.finish = true;
		$("#"+this.id).css("background-color", this.success ? "lightgreen" : "darkGray");
		$("#"+this.id+" a").text(this.name+"("+this.gain+")");
		log("Task [" + this.name + "] finished, spend "+(this.eTime-this.sTime)+"ms.");
		if(!this.standalone) {
			schedule();
		}
	},
	
	reset: function() {	
		this.gain = 0;
		this.finish = false;
		this.success = false;
		this.sTime = 0;
		this.eTime = 0;
		this.standalone = false;
		$("#"+this.id).css("background-color", "whiteSmoke");
		$("#"+this.id+" a").text(this.name+"(0)");
	}
};

function initTask() {
	tasks = [
		new Task("everyday", 	"领取当日淘金币", 	getEveryDayCoins, 
				{tips:"每天5枚，连续7天以上每天40枚，如中断则又会从5开始", 
				 url:"http://vip.taobao.com/vip_home.htm"}),
				 
		new Task("friend", 		"帮好友领淘金币", 	helpGetCoins, 
				{tips:"每帮领一个好友，即可得5个奖励！（15个封顶）", timeout:3000}),
				
		new Task("task", 		"任务盒子", 		taskBoxCoins, 
				{tips:"做任务领取淘金币，部分任务需您亲自完成", timeout:3000, 
				 url:"http://mission.jianghu.taobao.com/umission_list.htm", skip:true}),
				 
		new Task("ju", 			"聚划算签到", 		signeJu, 
				{url:"http://i.ju.taobao.com/subscribe/keyword_items.htm"}),
				
		new Task("ttl", 		"太太乐",			ttl, 
				{tips:"太太乐每天点亮5个淘金币（2013.1.1-2013.12.31）", timeout:2000,
				 url:"http://ttl.taobao.com"}),
		
		new Task("try", 		"试用中心签到", 	signTryCenter, 
				{url:"http://try.taobao.com/item/my_try_item.htm"}),
				 
		new Task("favorite", 	"店铺收藏", 		favorite, 
				{tips:"每天首次收藏新店铺领5个淘金币", timeout:2000,
				 url:"http://dongtai.taobao.com/square.htm"})
	];
	
	function enclosure(task) {
		return function(){
			task.standalone=true;
			initUser(function(){task.start();}); //need to get token first
		}
	}
	function enclSkip(task, checkbox) {
		return function(){
			task.skip = (checkbox.attr("checked")=="checked");
		}
	}
	for(var i=0; i<tasks.length; i++) {
		var task = tasks[i];
		var checkbox = $("<input type='checkbox' " + (task.skip?"checked":"") + ">跳过</input>");
		checkbox.click(enclSkip(task, checkbox));
		var button = $("<input type='button' value='单独执行'>");
		button.click(enclosure(task));
		var html = "<a href='"+task.url+"' target='_blank'>"+task.name+"(0)</a><br>";
		var taskDiv = $("<div class='task' id='"+task.id+"' title='"+task.tips+"'></div>");
		taskDiv.append(html).append(checkbox).append(button);
		$("#tasks").append(taskDiv);
	}
}

function resetTask() {
	for(var i=0; i<tasks.length; i++) {
		tasks[i].reset();
	}
}

function schedule() {
	for(var i=0; i<tasks.length; i++) {
		var task = tasks[i];
		if(task.skip) continue;
		if(!task.finish) { //not finish
			if(task.sTime==0) { // not start
				task.start();
				break;
			} else { // start
				var elapse = new Date().getTime()-task.sTime;
				if(elapse < task.timeout) { // task in progress
					break;
				} else { // else schedule next task
					log("Task [" + task.name + "] timeout.");
				}
			}
		} 
		/*else {
			if(task.sync) { //already finished but need sync
				var elapse = new Date().getTime()-task.sTime;
				if(elapse < task.timeout) { // wait until timeout
					break;
				}
			}
		}*/
	}
}

function inLoginPage(html){
	if(html.indexOf("标准登录框")!=-1) {
		needLogin();
		return true;
	}
	return false;
}

function autoGetCoin() {
	resetTask();
	var selected = $("#userlist option:selected");
	user = selected.html();
	pass =  selected.val();
	
	if(user!="" && pass!="") {
		//logout();
		$.get("http://login.etao.com/logout.html?spm=1002.1.0.1.da48c5"); //logout etao and taobao?
		$.get("https://auth.alipay.com/login/logout.htm"); //logout Alipay
		
		pass = decrypt(user, pass);
		appendLog(user + "自动登录淘宝网");
		login(user, pass, function(isLogin) {
			if(isLogin) {
				appendLog(user + "登录成功，开始领取淘金币");
				initUser(getCoin);
			} else {
				appendLog(user + "登录失败");
				needLogin();
			}
		});
		
		$("#userlist").val("");
	} else {
		checkLogin(function(isLogin) {
			if(isLogin) {
				initUser(getCoin); //Get user info from cookie, maybe cookie is null
			} else {
				needLogin();
			}
		});
	}
}

function getCoin() {
	if(token==null) {
		needLogin();
		return;
	}
	var url = "http://taojinbi.taobao.com/record/my_coin_detail.htm"; //"http://i.taobao.com/my_taobao.htm";
	$.get(url, function(html) {
		if(html=="" || html.indexOf("标准登录框")!=-1) {
			needLogin();
		} else {
			schedule();
		}
	});
	console.log("XHR: " + url);
}

//==========================================================================每日领金币
function getEveryDayCoins() {
	var task = this;
	var time = new Date().getTime();
	
	//"http://taojinbi.taobao.com/home/home_new.htm?spm=a1z02.1.1000294.d1000326.9izYL4&auto_take=true&tracelog=newmytb_kelingjinbi";
	//http://vip.taobao.com/home/grant_everyday_coin.htm?t=1367301763907&_tb_token_=3e17e033b7e65&checkCode=null&enter_time=1367301752893
	var url = "http://vip.taobao.com/home/grant_everyday_coin.htm?t="+time+"&_tb_token_="+token+"&checkCode=null&enter_time="+(time-12345);
/*checkCode:okrm
enter_time:1351642860468
ua:249uhjHGDhYGGhIKIhoOHgIWNo=|uhiIxycrLCfa|uijHuic5iyz721snxygoxyd6m+tL+3w8J9ra|uhiIxyc8myfa|ujjHugjHCMcYSCg4xxhICHjHGEhICMeYCAjHGEhICMeIaAja2g==|uojHJyfa|upjHJyfa|uhgoxycYOFgYaEgoiGg4eAhYqAjnaAgoiEg4WHiYGDh4aJh4iCfa|uhiIxyebSyfa|uhiIxyd7yzz7J9o=|uhgIxye6KChoSAjHyicYOEhICHgomAg4OAg4zAjneBhoGGiYiCg4SChoeFg4OMonxxjaJ9o=|uhiIxyfbOyfa|uljHuicnx7pYiBjHKFhI2scIxycnx3gYaCja2g==|uhiIxye7PCfa|umjHuicnx3iYxwjHiHhoWNra|uhiIxye7PCfa|umjHuicnx3hYxwjHmAgYKNra|uhiIxye7PCfa|umjHuicnx4goxwjHmDg4ONra|uhiIxyfbDCfa|ukjHuicnx1hYGMc4OJjHGAhYWDja2g==|uhiIxyfbOyfa|uljHuicnx7pYaFjHOFiI2scoxycnxxhYWJhY2to=|uhiIxyfbOyfa|uljHuicnx7oYGDhYx1g4CNrHCMcnJ8cYeCgoeNra|uhiIxyfbDCfa|ukjHuicnx5h4aMdIeHjHGJg4GEja2g==|uhiIxyfbDCfa|ukjHuicnxyiYCMdYCDjHKAhIOEja2g==
ran:0.13288874388672411 */
	time = new Date().getTime();
	$.getJSON(url, function(json){
		/* 	{"currentLevel":"v0","nextLevel":"v4","nextMaxCoin":30,"code":1,"coinOld":1110,"coinNew":1115,"daysTomorrow":1,"coinTomorrow":"10","auth":true,"isTake":"false","takeAmount":"","friendNum":"","switcher":"true"}
		*/
			//{"code":1,"coinOld":4471,"coinNew":4501,"daysTomorrow":6,"coinTomorrow":"35","auth":true,"isTake":"false","takeAmount":"","friendNum":"","switcher":"true"}
			//{"code":2,"coinOld":4501,"coinNew":4501,"daysTomorrow":1,"coinTomorrow":"10","auth":true,"isTake":"","takeAmount":"","friendNum":"0","switcher":"true"}
			//{"code":4,"coinOld":-1,"coinNew":-1,"daysTomorrow":1,"coinTomorrow":"10","auth":true,"isTake":"","takeAmount":"","friendNum":"","switcher":"true"}
			//{"code":6,"coinOld":1418,"coinNew":1418,"daysTomorrow":1,"coinTomorrow":"10","auth":true,"isTake":"","takeAmount":"","friendNum":"0","switcher":"true"}
			console.log(json);
			if(json.code==1) {
				var coinGot = json.coinNew-json.coinOld;
				appendLog("成功领取"+coinGot+"个淘金币，已连领"+json.daysTomorrow+"天，当前金币数量"+json.coinNew);
				task.gain = coinGot;
				task.success = true;
			} else if(json.code==4) {
				appendLog("需要输入验证码，领淘金币越来越麻烦啦！");
				/*if($("#workaround").attr("checked")=="checked") {
					var b = chrome.extension.getBackgroundPage();
					b.createTabAndInject(task.url, [], ["res/coin.js"]);
				}*/
			} else if(json.code==5) {
				appendLog("验证码错误！");
			} else if(json.code==6) {
				appendLog("亲，有5个好友的用户才能天天领金币，当前淘金币数量"+json.coinNew);
			} else {
				appendLog("今天可能已经领取过淘金币，当前淘金币数量"+json.coinNew);
			}
			updateCheckCoin();
			task.complete();
		}, "json"
	).fail(function(xhr, e) {
		inLoginPage(xhr.responseText);
		updateCheckCoin();
		task.complete();
	});
	console.log("XHR: " + url);
}

//==========================================================================
//任务盒子
function taskBoxCoins() {
	var task = this;
	var taskCount = 0;
	var successCount = 0;
	var coins = 0;
	function doTask(taskBox, index) {
		function completeTaskBox() {
			if(index==taskCount-1) {
				if(successCount!=0) {
					task.success = true;
				}
				if(taskCount>successCount) {
					appendLog("还有"+(taskCount-successCount)+"个任务尚未完成，前往<a href='http://mission.jianghu.taobao.com/umission_list.htm' target='_blank'>任务中心</a>");
				}
				task.gain = coins;
				task.complete();
				taskCount = 0;
				successCount = 0;
			} else {
				doTask(taskBox, index+1);
			}
		} // end of completeTaskBox()
		var mid = taskBox[index];
		var url = "http://mission.jianghu.taobao.com/ajax/mission_oper.do?t="+new Date().getTime();
		//console.log("XHR: " + url);
		$.ajax({
			"url": url,
			"type": "POST",
			"data": {"missionId":mid, "oper":"f", "_tb_token_":token},
			"dataType": "json"
		}).done(function(json) {
			//{"result":{"con":"快打开页面看特价机票酒店吧，低至2折哦","img":"http://img01.taobaocdn.com/tps/i1/T1u9vQXataXXXBqKDv-150-120.png","msg":"假期出游贵？错峰游2折起","num":"2","oldnum":"1506","sharemsg":{"client_id":"73","comment":"我刚完成任务，做任务还能有金币拿，靠谱！","isShowFriend":"","pic":"http://img01.taobaocdn.com/tps/i1/T1u9vQXataXXXBqKDv-150-120.png","title":"分享来自任务盒子"}},"status":3,"success":true}
			console.log(json);
			if(json.success && json.status==3) {
				appendLog("完成任务["+json.result.msg+"]，获得"+json.result.num+"枚金币");
				coins += parseInt(json.result.num);
				successCount++;
			}
			completeTaskBox();
		}).fail(function(jqXHR, textStatus){
			console.error(task.name + ": " + textStatus);
			completeTaskBox();
		}); // end of ajax
	} // end of doTask()
	
	var url = "http://mission.jianghu.taobao.com/umission_list.htm?spm=a1z01.0.1000710.11.e9da7f&tracelog=Tcoin_mission"
	$.get(url, function(html) {
		if(inLoginPage(html)) {
			task.complete();
			return;
		}
		var regExp = /data-missionId="(\d+)"/ig;
		var result = null;
		var taskBox = [];
		while ((result = regExp.exec(html)) != null)  {
			var missionId = result[1];
			var exist = false;
			for(var i in taskBox) {
				if(taskBox[i]==missionId) {
					exist = true;
					break;
				}
			}
			if(!exist) {
				taskBox.push(missionId);
			}
		}
		taskCount = taskBox.length;
		if(taskCount==0) {
			appendLog("没有任务可做。");
			task.complete();
		} else {
			log("发现"+taskCount+"个任务。");
			console.log(taskBox);
			doTask(taskBox, 0);
		}
	});
	console.log("XHR: " + url);
}

//==========================================================================
//帮好友领淘金币
function helpGetCoins() {
	var task = this;
	function getFriends() {
		var url = "http://jianghu.taobao.com/admin/follow/json/getUserFollowing.htm?twoway=true&exceptIds="; //callback=?&
		$.getJSON(url,
			function(data){
				/*jsonp1339590822981({"users":[{"id":012345678,"g":[1],"n":"boy","fn":"boy"},{"id":87654321,"g":[1,2],"pt":"girl.jpg","n":"girl","fn":"girl"}],"groups":[{"id":1,"n":"\u597d\u53cb","ct":1},{"id":2,"n":"\u631a\u4ea4\u597d\u53cb","ct":1},{"id":3,"n":"\u672a\u5206\u7ec4","ct":0}]});*/
				console.log(data);
				if(data.users.length==0) {
					console.log("No friends."); // already checked by check_take.htm
					task.complete();
				} else {
					$.each(data.users, 
						function(i, user){
							//console.log(user.n + ", id:" + user.id);
							checkTake(user, data.users.length);
						}
					);
				}
			}
		);
	}

	var HELP_USER_MAX = 10;
	var takeIds = "";
	var takeNum = userNum = 0;
	function checkTake(user, max) {
		if(takeNum>=HELP_USER_MAX) {
			return;
		}
		var time = new Date().getTime();
		var url = "http://taojinbi.taobao.com/ajax/take/check_take.htm?method=checkTakenUser&takenUserId="+user.id+"&takenNum=1&t="+time;
		$.getJSON(url, function(json){
			//{"result": {"status":"true","msg":""}}
			//{"result": {"status":"false","msg":"已经被别的好友帮领过了/好友今天已经领过了金币了"}}
			userNum++;
			if(json.result.status=="true") {
				console.log(user.n + "还没被领过");
				if(takeNum==0) {
					takeIds = user.id;
				} else {
					takeIds += "," + user.id;
				}
				takeNum++;
			} else {
				console.log(user.n + json.result.msg);
			}
			if((takeNum==HELP_USER_MAX) || (userNum==max)) {
				if(takeIds!="") {
					log("Max number reached, ready to get coins.");
					takeCoins(takeIds);
				} else {
					task.complete();
				}
				takeIds = "";
				takeNum = userNum = 0;
			}
		});
	} // end of checkTake()
	
	function takeCoins(ids) {
		var time = new Date().getTime();
		var url = "http://taojinbi.taobao.com/ajax/take/coin_take.htm";

		$.post(url, {takeIds:ids, _tb_token_:token, t:time},
			function(json){
				//{"result": {"status":"false","failNames":"","msg":"<h4>会话过期，非法请求！</h4>重新刷新页操作。"}}
				//{"result": {"status":"true","successNames":"张三,李四,王五","failNames":"","takeCoin":"15"}}
				console.log(json);	
				if(json.result.status=="true"){
					appendLog("帮"+json.result.successNames+"领取成功，奖励"+json.result.takeCoin+"个淘金币");
					task.gain = json.result.takeCoin;
					task.success = true;
				} else {
					appendLog(json.result.msg);
				}
				task.complete();
			}, "json"
		);
		console.log("XHR: " + url);
	} // end of takeCoins()
		
	var time = new Date().getTime();
	var url = "http://taojinbi.taobao.com/ajax/take/check_take.htm?method=checkTakeUser&takenNum=1&t="+time+"&tracelog=bljb01";
	$.getJSON(url, function(json){
		//{"result": {"status":"true","msg":""}}
		if(json.result.status=="true"){
			getFriends();
		} else {
			appendLog(json.result.msg);
			task.complete();
		}
	}).fail(function(xhr, e) {
		inLoginPage(xhr.responseText);
		task.complete();
	});
	console.log("XHR: " + url);
}
//==========================================================================
// 聚划算签到
function signeJu() {
	var task = this;
	var url = "http://i.ju.taobao.com/json/my/checkInAction.htm?callback=jsonp69";
	$.get(url, function(code){
		if(inLoginPage(code)) {
			task.complete();
			return;
		}
		if(code.indexOf("success")!=-1) {
			appendLog(task.name + "成功。");
			task.success = true;
		} else { //"error"
			appendLog(task.name + "今日已签到。");
		}
		task.complete();
	});
}
/*
一键评价快捷键
商城一键评价
http://jf.etao.com/getCredit.htm?jfSource=11 
http://bangpai.taobao.com/group/thread/14569742-274664841.htm
*/
/*****************************************************************************
 * 试用中心签到（仅获得试用豆）
 *****************************************************************************/
function signTryCenter() {
	var task = this;
	var url = "http://try.taobao.com/json/popInfo.htm?t=" + Math.random();
	$.getJSON(url, function(json) {
		console.log(json);
		if(json.bean) {
			var t = json.bean.split(",");
			var bean = parseInt(t[1]);
			var total = parseInt(t[3]);
			appendLog("成功领取"+bean+"颗试用豆，共有"+total+"颗。");
			task.gain = bean;
			task.success = true;
		} else if(json.login) {
			appendLog("已经领取过试用豆。");
		} else {
			needLogin();
		}
		task.complete();
	});
}

/*****************************************************************************
 * 太太乐活动（2013.1.1-2013.12.31）
 *****************************************************************************/
function ttl() {
	var task = this;
	var url = "http://taojinbi.taobao.com/detail/activity_executor.htm?activity_id=2012122601&callback=jsonp19";
	$.get(url, function(code){
		if(inLoginPage(code)) {
			task.complete();
			return;
		}
		/* 
		jsonp19({"success":true,"code":1,"message":"恭喜！你成功了！","error":"0","coinUserSum":5,"extra":{},"id":"0"})		jsonp19({"success":false,"code":3,"message":"发淘金币失败，你已经领取过淘金币了，不能够再领取","error":"1","coinUserSum":5,"extra":{},"id":"0"})
		*/
		if(code.indexOf("\"success\":true")!=-1) {
			appendLog(task.name + "点亮成功，获得5个金币。");
			task.gain = 5;
			task.success = true;
		} else { //"error"
			var r = /"message":"(.+?)"/ig.exec(code);
			var msg = (r!=null) ? r[1] : "点亮失败。";
			appendLog(task.name + msg);
		}
		task.complete();
	});
}

/*****************************************************************************
 * 店铺收藏（必须先手动收藏一家店铺）
 *****************************************************************************/
function favorite() {
	var task = this;
	var shop_ids = [];
	function collect(index) {
		if(index>=shop_ids.length) {
			task.complete();
			return;
		}
		/*
		20130503 - http://shuo.taobao.com/microshop/shop_middle_page.htm?shopOwnerId=160562238&flag=true&_tb_token_=SVoccvfGyU1x
		20130508 - http://dongtai.taobao.com/hub/new_arrival.htm?shopOwnerId=231920032&_tb_token_=55e9e073be56b
		*/
		/*var url = "http://shuo.taobao.com/microshop/shop_middle_page.htm?shopOwnerId="+shop_ids[index]+"&flag=true&_tb_token_="+token;*/
		var url = "http://dongtai.taobao.com/hub/new_arrival.htm?shopOwnerId="+shop_ids[index]+"&_tb_token_="+token;
		log("Try to collect shop: " + url);
		$.get(url, function(html) {
			if(inLoginPage(html)) {
				task.complete();
				return;
			}
			if(html.indexOf("淘金币已到账")!=-1) {
				appendLog(task.name + "获得5个淘金币。");
				task.gain = 5;
				task.success = true;
				$("#"+task.id+" a").attr("href", "http://store.taobao.com/shop/view_shop.htm?user_number_id="+shop_ids[index]);
				task.complete();
			} else if(html.indexOf("店铺收藏成功")!=-1) {
				appendLog("今日已收藏店铺并领取过淘金币。");
				task.complete();
			} else {
				if(index>=5) {
					appendLog("尝试收藏次数过多，收藏失败。(请至少手动收藏一家店铺)");
					task.complete();
				} else {
					/*console.log(html);*/
					log("This shop does not contain any coins.");
					collect(index+1);
				}
			} /* end of favorite test */
		});
	} /* end of collect */
	var url = "http://dongtai.taobao.com/highqualityshop/high_quality_shop.htm?spm=0.0.0.0.hx2FCg";
	$.get(url, function(html){
		var regExp = /"sellerId":"(\d+)"/ig;
		var r = null, last=null;
		while((r=regExp.exec(html))!=null) {
			if(last==r[1])continue;
			last = r[1];
			shop_ids.push(r[1]);
		}
		log(shop_ids);
		collect(0);
	}).fail(function(){
		appendLog("收藏失败，动态广场改版或程序错误。");
		task.complete();
	});
}

//==========================================================================

function appendLog(logs) {
	log(logs);
	$("#logs").append(logs+"<br/>");
}

function needLogin() {
	appendLog("尚未登录，请前往淘宝<a href='https://login.taobao.com/member/login.jhtml' target='_blank'>登录</a>。");
	$("#userlist option[index='0']").attr("selected", true);
}

//==========================================================================
function updateCheckCoin() {
	var b = chrome.extension.getBackgroundPage();
	b.localStorage["lastCoinTime"] = new Date().getTime();
	b.everydayCheck();
}

function loadUsers() {
	var b = chrome.extension.getBackgroundPage();
	var users = b.getUser();
	if(users) {
		$.each(users, function(n, p) {
			$('#userlist').append( new Option(n, p) );
		})
	}
}

function init() {
	var b = chrome.extension.getBackgroundPage();
	var config = b.getConfig();
	if(config.autoLogin) {
		loadUsers();
	} else {
		$("#auto").hide();
	}
	$("#workaround").attr("checked", config.workaround);
}

function saveWorkaround() {
	var b = chrome.extension.getBackgroundPage();
	var config = b.getConfig();
	config.workaround = $("#workaround").attr("checked")=="checked";
	b.saveConfig(config);
}

$(function() {
	init();
	initTask();
	$("#get_coin").click(autoGetCoin);
	$("#workaround").click(saveWorkaround);
});
