	var b = chrome.extension.getBackgroundPage();
	
	var DEFAULT_INTERVAL = 3000;
	var currentTask = null;
	var taskIndex = 0;
	
	function Task(name, timeout, interval, func) {
		this.name = name;
		this.timeout = timeout; // -1 - No wait, concurrently; 0 - Sync, Wait until this task done; N - timeout
		this.clearTimeout = -1;
		this.interval = interval; // 0 - Immediately run after this task done; N - Wait N seconds
		this.func = func;
		this.finish = false;
		this.sTime = 0; //millisecond
		this.eTime = 0; //millisecond
	}
	
	var collectCoins = new Task("领取当日淘金币", 1000, DEFAULT_INTERVAL, function(){
		console.log(new Date() + "start t1");
		setTimeout(function(){
			console.log(new Date() + "finish t1");
			collectCoins.finish = true;
			schedule();
		}, 5000);
	});
	var helpCollectCoins = new Task("帮好友领取淘金币", -1, DEFAULT_INTERVAL, function(){
		console.log(new Date() + "start t2");
		setTimeout(function(){
			console.log(new Date() + "finish t2");
			helpCollectCoins.finish = true;
			schedule();
		}, 2000);
	});
	var doTaskCoins = new Task("完成任务获取淘金币", 0, DEFAULT_INTERVAL, function(){
		console.log(new Date() + "start t3");
		setTimeout(function(){
			console.log(new Date() + "finish t3");
			schedule();
		}, 1000);
	});
	var tasks = [collectCoins, helpCollectCoins, doTaskCoins];

	function schedule() {
		var newTask = nextTask = null;
		if(taskIndex > tasks.length-1) {
			console.log("All tasks finished.");
		}
		
		if(currentTask==null) {
			if(tasks.length>0) {
				nextTask = tasks[0];
				taskIndex = 1; // point to the second task
			} else {
				return;
			}
		} else {
			// If current task doesn't finish
			if(currentTask.finish==false) {
				return;
			}
		}
		
		if(nextTask!=null) {
			currentTask = nextTask;
			if(currentTask.timeout!=-1) {
				if(taskIndex<tasks.length) {
					nextTask = tasks[taskIndex];
					taskIndex++;
					console.log("cuncurrently run task " + nextTask.name);
				} else {
					nextTask = null;
				}
			} else if(currentTask.timeout>0) {
				currentTask.clearTimeout = setTimeout(schedule, currentTask.timeout);
				nextTask = null;
			} else {
				nextTask = null;
			}
			console.log(new Date() + "ready to start " + currentTask.name);
			setTimeout(function(task) {
				return function(){
					task.sTime = new Date().getTime();
					task.func();
				};
			}(currentTask), 0);
		}
	}
		
	function startTask(task) {
		task.sTime = new Date().getTime();
		console.log(task.name + ":" + task.sTime);
		task.func();
	}
	
	function autoGetCoin() {
		var selected = $("#userlist option:selected");
		var user = $('#user').val();
		var pass = $('#pass').val(); 
		if(user=="" || pass=="") {
			user = selected.html();
			pass =  selected.val();
			$('#user').val("");
		}
		$('#pass').val("");
		
		if(user!="" && pass!="") {
			b.logout();
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
				if(html.indexOf("明天可领")!=-1) {
					appendLog("今天已经领取过淘金币。");
					helpGetCoins(function() {
						getTaskCoins(0);
					});
				} else {
					var r = /<a href=.+?>(.+?)<\/a>已帮你领了<em>(\d+)<\/em>淘金币！<\/div>/ig.exec(html);
					if(r!=null) {
						appendLog(r[1]+"已帮你领了"+r[2]+"个淘金币");
					}
					getEveryDayCoins(function(){
						helpGetCoins(function() {
							getTaskCoins(0);
						});
					});
				}
			}
		});
		console.log("XHR: " + url);
	}
	
	function getEveryDayCoins(nextAction) {
		var time = new Date().getTime();
		//"http://taojinbi.taobao.com//home/award_exchange_home.htm?auto_take=true&tracelog=newmytb_kelingjinbi";
		var url = "http://taojinbi.taobao.com/home/grant_everyday_coin.htm?t="+time+"&_tb_token_="+b.token;

		time = new Date().getTime();
		$.post(url, {enter_time:time, ran:Math.random()},
			function(json){
				//{"code":1,"coinOld":4471,"coinNew":4501,"daysTomorrow":6,"coinTomorrow":"35","auth":true,"isTake":"false","takeAmount":"","friendNum":"","switcher":"true"}
				//{"code":2,"coinOld":4501,"coinNew":4501,"daysTomorrow":1,"coinTomorrow":"10","auth":true,"isTake":"","takeAmount":"","friendNum":"0","switcher":"true"}
				//{"code":6,"coinOld":1418,"coinNew":1418,"daysTomorrow":1,"coinTomorrow":"10","auth":true,"isTake":"","takeAmount":"","friendNum":"0","switcher":"true"}
				console.log(json);	
				if(json.code=="1"){
					appendLog("成功领取"+json.coinTomorrow+"个淘金币，已连续领取"+json.daysTomorrow+"天，当前淘金币数量"+json.coinNew);
				} else if(json.code=="6"){
					appendLog("亲，有5个好友的用户才能天天领金币，当前淘金币数量"+json.coinNew);
				} else {
					appendLog("今天已经领取过淘金币，当前淘金币数量"+json.coinNew);
				}
				nextAction();
			}, "json"
		);
		console.log("XHR: " + url);
	}

	//==========================================================================
	var taskCount = 0;
	var finishCount = 0;
	function getTaskCoins(stage) {
		var url = "http://mission.jianghu.taobao.com/umissionList.htm#url"
		$.get(url, function(html) {
			var regExp = /detail\(this,'(\d+)'\)/ig;
			var result = null;
			var tasks = [];
			while ((result = regExp.exec(html)) != null)  {
				tasks.push(result[1]);
			}
			if(stage==0) {
				taskCount = tasks.length;
				if(taskCount==0) {
					getTaskCoins(1);
				} else {
					for(var i in tasks) {
						doTask(tasks[i]);
					}
				}
			} else if (stage==1) {
				var regExp = /getScore\(this,'(\d+)','(\d+)'\)/ig;
				while ((result = regExp.exec(html)) != null)  {
					finishTask(result[1], result[2]);
				}
				if(tasks.length!=0) {
					appendLog("还有"+tasks.length+"个任务尚未完成，前往<a href='"+url+"' target='_blank'>任务中心</a>");
				}
				taskCount = 0;
				finishCount = 0;
			}
		});
		console.log("XHR: " + url);
	}
	
	function doTask(mid) {
		var url = "http://mission.jianghu.taobao.com/missionExecutor.htm?mid="+mid+"&operation=p&ran="+Math.random()+"&tracelog=MISSION003&_tb_token_=" + b.token;
		$.get(url, function(json){
			//({"result":{"auth":true,"defaultModel":null,"defaultModelKey":"_defaultModel","missionExecutException":false,"missionHasFinished":false,"models":{},"resultCode":0,"success":true,"totalScore":0}})
			finishCount++;
			if(finishCount==taskCount) {
				getTaskCoins(1);
			}
		});
		console.log("XHR: " + url);
	}
	
	function finishTask(mid, muid) {
		var url = "http://mission.jianghu.taobao.com/missionExecutor.htm?mid="+mid+"&ran="+Math.random()+"&tracelog=MISSION003&_tb_token_="+b.token+"&operation=f&muid="+muid;
		$.get(url, function(json){
			appendLog("完成一项任务");
		});
		console.log("XHR: " + url);
	}

	//==========================================================================
	function helpGetCoins(nextAction) {
		var time = new Date().getTime();
		var url = "http://taojinbi.taobao.com/ajax/take/check_take.htm?method=checkTakeUser&takenNum=1&t="+time+"&tracelog=bljb01";
		$.getJSON(url, function(json){
			//{"result": {"status":"true","msg":""}}
			if(json.result.status=="true"){
				getFriends(nextAction);
			} else {
				appendLog(json.result.msg);
				nextAction();
			}
		});
		console.log("XHR: " + url);
	}
	
	function getFriends(nextAction) {
		var url = "http://jianghu.taobao.com/admin/follow/json/getUserFollowing.htm?twoway=true&exceptIds="; //callback=?&
		$.getJSON(url,
				function(data){
					/*jsonp1339590822981({"users":[{"id":012345678,"g":[1],"n":"boy","fn":"boy"},{"id":87654321,"g":[1,2],"pt":"girl.jpg","n":"girl","fn":"girl"}],"groups":[{"id":1,"n":"\u597d\u53cb","ct":1},{"id":2,"n":"\u631a\u4ea4\u597d\u53cb","ct":1},{"id":3,"n":"\u672a\u5206\u7ec4","ct":0}]});*/
					console.log(data);
					if(data.users.length==0) {
						nextAction();
					} else {
						if(data.users.length<3) {
							appendLog("您的好友太少，至少帮3个好友领取才能得到15个淘金币的奖励");
						}
						$.each(data.users, 
							function(i, user){
								console.log(user.n + ", id:" + user.id);
								checkTake(user, data.users.length, nextAction);
							}
						);
					}
				}
		);
	}

	var HELP_USER_MAX = 10;
	var takeIds = "";
	var takeNum = userNum = 0;
	function checkTake(user, max, nextAction) {
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
				appendLog(user.n + json.result.msg);
			}
			if((takeNum==HELP_USER_MAX) || (userNum==max)) {
				if(takeIds!="") {
					console.log("Max number reached, ready to get coins.");
					takeCoins(takeIds, nextAction);
				} else {
					nextAction();
				}
				takeIds = "";
				takeNum = userNum = 0;
			}
		});
	}
	
	function takeCoins(ids, nextAction) {
		var time = new Date().getTime();
		var url = "http://taojinbi.taobao.com/ajax/take/coin_take.htm";

		$.post(url, {takeIds:ids, _tb_token_:b.token, t:time},
			function(json){
				//{"result": {"status":"false","failNames":"","msg":"<h4>会话过期，非法请求！</h4>重新刷新页操作。"}}
				//{"result": {"status":"true","successNames":"张三,李四,王五","failNames":"","takeCoin":"15"}}
				console.log(json);	
				if(json.result.status=="true"){
					appendLog("帮"+json.result.successNames+"领取成功，奖励"+json.result.takeCoin+"个淘金币");
				} else {
					appendLog(json.result.msg);
				}
				nextAction();
			}, "json"
		);
		console.log("XHR: " + url);
	}
	
	//==========================================================================
	// 一淘积分
	function getCredit() {
		$.get("http://jf.etao.com/getCredit.htm", function(html) {
			var regExp = /lukyLevel">(.+?)<\/p>/ig;
			var r = regExp.exec(html);
			appendLog(r[1]);
		});
		//http://jf.etao.com/?signIn=https://hi.alipay.com/campaign/normal_campaign.htm?campInfo=f8TFC%2B0iCwshcQr4%2BKQCH7zMoy1VtWKh&from=jfb&sign_from=3000#
		$.get("https://hi.alipay.com/campaign/normal_campaign.htm?campInfo=f8TFC%2B0iCwshcQr4%2BKQCH7zMoy1VtWKh&from=jfb&sign_from=3000", function(html) {
			var regExp = /lukyLevel">(.+?)<\/p>/ig;
			var r = regExp.exec(html);
			appendLog(r[1]);
		});
	}

	//==========================================================================
	function appendLog(log) {
		console.log(log);
		$("#logs").append(log+"<br/>");
	}
	
	function needLogin() {
		console.log("您还没有登录。");
		appendLog("尚未登录，请前往淘宝<a href='http://login.taobao.com' target='_blank'>登录</a>。");
		$("#userlist option[index='0']").attr("selected", true);
	}

	//==========================================================================
	function save() {
		var user = $('#user').val(); //$('#user').val("");
		var pass = $('#pass').val(); $('#pass').val("");
		if(user!="" && pass!="") {
			b.saveUser(user, pass);
			var foundUsers = $('#userlist option').filter(function(index){return $(this).text()==user;});
			if(foundUsers) {
				if(foundUsers.length==0) {
					$('#userlist').append( new Option(user, pass) );
				} else {
					foundUsers.val(pass);
				}
			}
		}
	}
	
	function del() {
		var selected = $("#userlist option:selected");
		b.delUser(selected.html());
		selected.remove();
	}
	
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
		if(config!=null && config.autoGetCoinEnabled) {
			loadUsers();
		} else {
			$("#auto").hide();
		}
	}
	
		
document.addEventListener('DOMContentLoaded', function () {
	init();
	$("#del").bind('click', del);
	$("#add").bind('click', save);
	$("#get_coin").bind('click', autoGetCoin);
});