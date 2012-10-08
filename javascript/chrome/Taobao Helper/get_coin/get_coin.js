var b = chrome.extension.getBackgroundPage();
var tasks = [];

function Task(id, name, func, option) {
	this.name = name;
	this.id = id;
	this.func = func;
	this.tips = name;
	this.timeout = 2000;
	this.url = null;
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
	}
	
	this.count = 0;
	this.finish = false;
	this.success = false;
	this.sTime = 0; //millisecond
	this.eTime = 0; //millisecond
}

function initTask() {
	tasks = [
		new Task("everyday", 	"领取当日淘金币", 	getEveryDayCoins, 
				{tips:"每天5枚，连续7天以上每天40枚，如中断则又会从5开始", 
				 url:"http://taojinbi.taobao.com/record/my_coin_detail.htm"}),
				 
		new Task("friend", 		"帮好友领淘金币", 	helpGetCoins, 
				{tips:"每帮领一个好友，即可得5个奖励！（15个封顶）", timeout:3000}),
				
		new Task("task", 		"任务盒子", 		taskBoxCoins, 
				{tips:"做任务领取淘金币，部分任务需您亲自完成", timeout:3000, 
				 url:"http://mission.jianghu.taobao.com/umission_list.htm"}),
				 
		new Task("ju", 			"聚划算签到", 		signeJu, 
				{url:"http://i.ju.taobao.com/subscribe/keyword_items.htm"}),
				
		new Task("alipay", 		"支付宝签到", 		signAlipay,
				{url:"https://jfb.alipay.com/activity/earn.htm"}),
		
		new Task("try", 		"试用中心签到", 	signTryCenter, 
				{url:"http://try.taobao.com/item/my_try_item.htm"}),
				
		new Task("etao", 		"一淘签到", 		signeTao, 
				{tips:"签到6秒钟之后才能进行下一个签到", timeout:6000,
				 url:"http://jf.etao.com/"}),
				
		new Task("aiguangjie", 	"爱逛街签到", 		signAiGuangJie, 
				{tips:"签到5秒钟之后才能进行下一个签到", timeout:5000,
				 url:"http://love.taobao.com"}),
				
		new Task("wangwang", 	"旺旺签到", 		signWangWang, 
				{tips:"签到5秒钟之后才能进行下一个签到"})
	];
	
	for(var i=0; i<tasks.length; i++) {
		var task = tasks[i];
		var html = task.name;
		if(task.url) {
			html = "<a href='"+task.url+"' target='_blank'>"+task.name+"</a>";
		}
		html = "<li class='task' id='"+task.id+"' title='"+task.tips+"'>"+html+"</li>";
		var taskObj = $(html);
		$("#tasks").append(taskObj);
	}
}

function resetTask() {
	for(var i=0; i<tasks.length; i++) {
		var task = tasks[i];
		task.count = 0;
		task.finish = false;
		task.success = false;
		task.sTime = 0;
		task.eTime = 0;
		$("#"+task.id).css("background-color", "whiteSmoke");
	}
}

function startTask(task) {
	task.sTime = new Date().getTime();
	$("#"+task.id).css("background-color", "lightblue");
	log("Task [" + task.name + "] starts.");
	task.func(task);
	setTimeout(schedule, task.timeout);
}

function finishTask(task) {
	task.eTime = new Date().getTime();
	task.finish = true;
	$("#"+task.id).css("background-color", task.success ? "lightgreen" : "darkGray");
	log("Task [" + task.name + "] finished, spend "+(task.eTime-task.sTime)+"ms.");
	schedule();
}

function schedule() {
	for(var i=0; i<tasks.length; i++) {
		var task = tasks[i];
		if(!task.finish) { //not finish
			if(task.sTime==0) { // not start
				startTask(task);
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

function autoGetCoin() {
	resetTask();
	var selected = $("#userlist option:selected");
	user = selected.html();
	pass =  selected.val();
	
	if(user!="" && pass!="") {
		b.logout();
		$.get("http://login.etao.com/logout.html?spm=1002.1.0.1.da48c5");
		appendLog(user + "自动登录淘宝网");
		b.login(user, pass, function(isLogin) {
			if(isLogin) {
				appendLog(user + "登录成功，开始领取淘金币");
				b.initUser(getCoin);
			} else {
				appendLog(user + "登录失败");
				needLogin();
			}
		});
	} else {
		b.checkLogin(function(isLogin) {
			if(isLogin) {
				b.initUser(getCoin); //Get user info from cookie, maybe cookie is null
			} else {
				needLogin();
			}
		});
	}
}

function getCoin() {
	if(b.token==null) {
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
function getEveryDayCoins(task) {
	var time = new Date().getTime();
	
	//"http://taojinbi.taobao.com//home/award_exchange_home.htm?auto_take=true&tracelog=newmytb_kelingjinbi";
	var url = "http://taojinbi.taobao.com/home/grant_everyday_coin.htm?t="+time+"&_tb_token_="+b.token;

	time = new Date().getTime();
	$.post(url, {enter_time:time, ran:Math.random()},
		function(json){
			//{"code":1,"coinOld":4471,"coinNew":4501,"daysTomorrow":6,"coinTomorrow":"35","auth":true,"isTake":"false","takeAmount":"","friendNum":"","switcher":"true"}
			//{"code":2,"coinOld":4501,"coinNew":4501,"daysTomorrow":1,"coinTomorrow":"10","auth":true,"isTake":"","takeAmount":"","friendNum":"0","switcher":"true"}
			//{"code":4,"coinOld":-1,"coinNew":-1,"daysTomorrow":1,"coinTomorrow":"10","auth":true,"isTake":"","takeAmount":"","friendNum":"","switcher":"true"}
			//{"code":6,"coinOld":1418,"coinNew":1418,"daysTomorrow":1,"coinTomorrow":"10","auth":true,"isTake":"","takeAmount":"","friendNum":"0","switcher":"true"}
			console.log(json);
			if(json.code==1) {
				appendLog("成功领取"+json.coinTomorrow+"个淘金币，已连领"+json.daysTomorrow+"天，当前金币数量"+json.coinNew);
				task.success = true;
			} else if(json.code==4) {
				appendLog("需要输入验证码，领淘金币越来越麻烦啦！");
			} else if(json.code==5) {
				appendLog("验证码错误！");
			} else if(json.code==6) {
				appendLog("亲，有5个好友的用户才能天天领金币，当前淘金币数量"+json.coinNew);
			} else {
				appendLog("今天可能已经领取过淘金币，当前淘金币数量"+json.coinNew);
			}
			finishTask(task);
		}, "json"
	);
	console.log("XHR: " + url);
}

//==========================================================================
//任务盒子
var taskCount = 0;
var successCount = 0;
function taskBoxCoins(task) {
	function doTask(taskBox, index) {
		function completeTask() {
			if(index==taskCount-1) {
				if(successCount!=0) {
					task.success = true;
				}
				if(taskCount>successCount) {
					appendLog("还有"+(taskCount-successCount)+"个任务尚未完成，前往<a href='http://mission.jianghu.taobao.com/umission_list.htm' target='_blank'>任务中心</a>");
				}
				finishTask(task);
				taskCount = 0;
				successCount = 0;
			} else {
				doTask(taskBox, index+1);
			}
		} // end of completeTask()
		var mid = taskBox[index];
		var url = "http://mission.jianghu.taobao.com/ajax/mission_oper.do?t="+Math.random();
		//console.log("XHR: " + url);
		$.ajax({
			"url": url,
			"type": "POST",
			"data": {"missionId":mid, "oper":"f", "_tb_token_":b.token},
			"dataType": "json"
		}).done(function(json) {
			//{"result":{"con":"快打开页面看特价机票酒店吧，低至2折哦","img":"http://img01.taobaocdn.com/tps/i1/T1u9vQXataXXXBqKDv-150-120.png","msg":"假期出游贵？错峰游2折起","num":"2","oldnum":"1506","sharemsg":{"client_id":"73","comment":"我刚完成任务，做任务还能有金币拿，靠谱！","isShowFriend":"","pic":"http://img01.taobaocdn.com/tps/i1/T1u9vQXataXXXBqKDv-150-120.png","title":"分享来自任务盒子"}},"status":3,"success":true}
			console.log(json);
			if(json.success && json.status==3) {
				appendLog("完成任务["+json.result.msg+"]，获得"+json.result.num+"枚金币");
				successCount++;
			}
			completeTask();
		}).fail(function(jqXHR, textStatus){
			console.error(task.name + ": " + textStatus);
			completeTask();
		}); // end of ajax
	} // end of doTask()
	
	var url = "http://mission.jianghu.taobao.com/umission_list.htm?spm=a1z01.0.1000710.11.e9da7f&tracelog=Tcoin_mission"
	$.get(url, function(html) {
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
			finishTask(task);
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
function helpGetCoins(task) {
	function getFriends() {
		var url = "http://jianghu.taobao.com/admin/follow/json/getUserFollowing.htm?twoway=true&exceptIds="; //callback=?&
		$.getJSON(url,
			function(data){
				/*jsonp1339590822981({"users":[{"id":012345678,"g":[1],"n":"boy","fn":"boy"},{"id":87654321,"g":[1,2],"pt":"girl.jpg","n":"girl","fn":"girl"}],"groups":[{"id":1,"n":"\u597d\u53cb","ct":1},{"id":2,"n":"\u631a\u4ea4\u597d\u53cb","ct":1},{"id":3,"n":"\u672a\u5206\u7ec4","ct":0}]});*/
				console.log(data);
				if(data.users.length==0) {
					console.log("No friends."); // already checked by check_take.htm
					finishTask(task);
				} else {
					if(data.users.length<3) {
						appendLog("您的好友太少，至少帮3个好友领取才能得到15个淘金币的奖励");
					}
					$.each(data.users, 
						function(i, user){
							console.log(user.n + ", id:" + user.id);
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
					finishTask(task);
				}
				takeIds = "";
				takeNum = userNum = 0;
			}
		});
	} // end of checkTake()
	
	function takeCoins(ids) {
		var time = new Date().getTime();
		var url = "http://taojinbi.taobao.com/ajax/take/coin_take.htm";

		$.post(url, {takeIds:ids, _tb_token_:b.token, t:time},
			function(json){
				//{"result": {"status":"false","failNames":"","msg":"<h4>会话过期，非法请求！</h4>重新刷新页操作。"}}
				//{"result": {"status":"true","successNames":"张三,李四,王五","failNames":"","takeCoin":"15"}}
				console.log(json);	
				if(json.result.status=="true"){
					appendLog("帮"+json.result.successNames+"领取成功，奖励"+json.result.takeCoin+"个淘金币");
					task.success = true;
				} else {
					appendLog(json.result.msg);
				}
				finishTask(task);
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
			finishTask(task);
		}
	});
	console.log("XHR: " + url);
}

//==========================================================================
// 聚划算签到
function signeJu(task) {
	var url = "http://i.ju.taobao.com/json/my/checkInAction.htm?callback=jsonp69";
	$.get(url, function(code){
		if(code.indexOf("success")!=-1) {
			appendLog(task.name + "成功。");
			task.success = true;
		} else { //"error"
			appendLog(task.name + "今日已签到。");
		}
		finishTask(task);
	});
}
//==========================================================================
// 一淘签到
function signeTao(task) {
	jfb(task, 2);
	// jifenbao(task, 2);
}

function jifenbao(task, src) {
	$.get("http://jf.etao.com/getCredit.htm?jfSource="+src+"&t="+Math.random(), function(html){
		var r = /<p class="news">([\s\S]+?)</ig.exec(html);
		if(r) {
			/* 
			(2) 亲，您今天已经领过了，看看自己的“战绩”吧！
			(-4) 亲，您不是支付宝实名认证用户，无法签到！赶快去认证吧 !
			(-6) 抢的人太多了，今天的积分发完了，明天再来吧
			(-8) 亲，您的操作太频繁了哦！
			*/
			var msg = r[1].trim();
			appendLog(msg);
		} else {
			appendLog(task.name + "失败。");
		}
		finishTask(task);
	});
}

function jfb(task, src) {
	$.ajax({
		url : "http://jf.etao.com/ajax/getCreditForSrp.htm?jfSource=" + src, 
		dataType : "json",
		type: "GET",
		success : function(json){
			console.log(json);
			if(json.status==-4) { //{"status":-4}
				appendLog("非实名认证用户，" + task.name + "失败。");
			} else if (json.status==2) {
				appendLog("今日已经完成"+task.name+"。");
			} else if (json.status==-6) {
				appendLog(task.name + "：抢的人太多了，今天的积分发完了，明天再来吧。");
			} else if (json.amount) { //{"amount":1,"days":1,"status":10}
				appendLog(task.name+"，连续签到"+json.days+"天，领取"+json.amount+"个积分宝。");
				task.success = true;
			} else {
				if(json.status==-8) {
					console.error("亲，您的操作太频繁了哦！");
				}
				appendLog(task.name + "失败。");
			}
			// make this task sync-alike to avoid quick access
			var elapse = new Date().getTime()-task.sTime;
			if(elapse<(task.timeout-100)) {
				setTimeout(function(){finishTask(task);}, task.timeout-100-elapse);
			} else {
				finishTask(task);
			}
		}
	});
}
// 爱逛街签到
function signAiGuangJie(task) {
	//http://jf.etao.com/?spm=1001.1000502.0.478.9f3e01&tb_lm_id=t_aiguangjie&signIn&jfSource=7
	jfb(task, 7);
	//jifenbao(task, 7);
}
// 登陆旺旺签到
function signWangWang(task) {
	jfb(task, 8);
}
//==========================================================================
// 支付宝签到
function signAlipay(task) {
	var url = "https://hi.alipay.com/campaign/normal_campaign.htm?campInfo=f8TFC%2B0iCwshcQr4%2BKQCH7zMoy1VtWKh&from=jfb&sign_from=3000";
	$.get(url, function(html) {
		var r = /<span class="jfb triJfb">(\d+)<\/span>/ig.exec(html);
		if(r) {
			appendLog("成功签到支付宝，领取"+r[1]+"个积分宝。");
			task.success = true;
		} else {
			r = /lukyLevelErr">(.+?)<\/p>/ig.exec(html);
			if(r) {
				appendLog(r[1]);
			} else {
				r = /window.location.href = "(.+?)"/ig.exec(html);
				if(r) {
					log("支付宝签到登录。");
					$.get(r[1], function(html) {
						signAlipay(task);
					});
					return;
				} else {
					console.log(html);
				}
			}
		}
		//$.get("https://auth.alipay.com/login/logout.htm"); //logout Alipay
		finishTask(task);
	}).fail(function(e){console.error(e);});
	//frameLoad("https://hi.alipay.com/campaign/normal_campaign.htm?campInfo=f8TFC%2B0iCwshcQr4%2BKQCH7zMoy1VtWKh&from=jfb&sign_from=3000");
}

function frameLoad(url) {
	if($("#frm").length>0) {
		$("#frm").attr("src", url);
	} else {
		$(document.body).append("<iframe id='frm' width='"+document.body.clientWidth+"' height='"+document.body.clientHeight+"' frameborder='0' src='"+ url +"'></iframe>");
	}
}
//==========================================================================
// 试用中心签到
function signTryCenter(task) {
	var url = "http://try.taobao.com/json/popInfo.htm?t=" + Math.random();
	$.getJSON(url, function(json) {
		console.log(json);
		if(json.bean) {
			var t = json.bean.split(",");
			var bean = parseInt(t[1]);
			var total = parseInt(t[3]);
			appendLog("成功领取"+bean+"颗试用豆，共有"+total+"颗。");
			task.success = true;
		} else {
			appendLog("已经登录试用中心并领取过试用豆。");
		}
		finishTask(task);
	});
}
//==========================================================================
function appendLog(logs) {
	log(logs);
	$("#logs").append(logs+"<br/>");
}

function needLogin() {
	appendLog("尚未登录，请前往淘宝<a href='http://login.taobao.com' target='_blank'>登录</a>。");
	$("#userlist option[index='0']").attr("selected", true);
}

//==========================================================================

function loadUsers() {
	var users = b.getUser();
	if(users) {
		$.each(users, function(n, p) {
			$('#userlist').append( new Option(n, p) );
		})
	}
}

function init() {
	var config = b.getConfig();
	if(config!=null && config.autoLogin) {
		loadUsers();
	} else {
		$("#auto").hide();
	}
}
		
$(function() {
	init();
	initTask();
	$("#get_coin").bind('click', autoGetCoin);
});