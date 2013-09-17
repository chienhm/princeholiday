var url = location.href;
console.log(url);

var callback = null;
checkLogin();
autoInput();

function submit() {
	$("input[name=ok]").click();
}

function checkLogin() {
	if($("form[name=loginForm]").length>0) {
		callback = submit;
	}
}

function autoInput(callback) {
	chrome.extension.sendRequest({"cmd": "GET_OPT"}, function(response) {
		var user = response.user;
		var pass = response.pass;
		
		if($("input[name=ksh]").length>0) {
			$("input[name=ksh]").val(user);
		}
		if($("input[name=mm]").length>0) {
			$("input[name=mm]").val(pass);
		}
		$("font strong").each(function(index, obj){
			var yzm = $(obj).text().trim(); 
			if($.isNumeric(yzm)){
				console.log(yzm);
				$("input[name=yzm]").val(yzm);
			}
		});
		if(callback) {
			callback();
		}
	});
}

function realRank() {
	console.log("实考分自动输入");
	autoInput(submit);
}

function nmRank() {
	console.log("投档分自动输入");
	realRank();
}

function fill(school) {
	var main = $("frame#main");
	console.log(location.href + "=>自动填报学校"+school.code);
	if(main.length==0)return;
	main.attr("src", "http://www1.nm.zsks.cn/kscx/zy/xgzy1.jsp");
}
/*!**************************************************************/
if(url.indexOf("http://www1.nm.zsks.cn/kscx/pm_skf.jsp")==0) {
	location.href = "http://www1.nm.zsks.cn/kscx/preMm_skf.do?pcdm=1";
} else if(url.indexOf("http://www1.nm.zsks.cn/kscx/zy/prePm.jsp")==0) {
	realRank();
} else if($("form[name=mmFormForm]").length>0) { /* 仅密码框，自动输入并提交 */
	autoInput(submit);
} else {
	autoFill();
}
/* 随时监听自动请求 */
if(url.indexOf("http://www1.nm.zsks.cn/kscx/")==0) {
	chrome.extension.onRequest.addListener(
		function(request, sender, sendResponse) {
			if(sender.tab) {
				console.log(request.cmd);
				switch (request.cmd) {
				case "FILL":
					fill(request.school);
					break;
					
				default:
					console.log("invalid command: " + request.cmd);
				}
			}
		}
	);
}
/*!**************************************************************/
function autoFill() {
	chrome.extension.sendRequest({"cmd": "GET_OPT"}, function(option) {
		console.log("Auto fill school: " + option.auto);
		console.log(option.fillSchool);
		if(!option.auto)return;
		
		if(url.indexOf("http://www1.nm.zsks.cn/kscx/zy/xgzy1.jsp")==0) {
			$("input[name=ok]").click();
		} else if(url.indexOf("http://www1.nm.zsks.cn/kscx/xgzy1.do")==0) {
			$("input[name=ok]").click();
		} else if(url.indexOf("http://www1.nm.zsks.cn/kscx/xgzy2.do")==0) {
			if(!option.fillSchool) return;
			$("input[name=yxdh]").val(option.fillSchool.code);
			for(var i=1; i<=6; i++) {
				$("input[name=zydh"+i+"]").val("");
			}
			for(var i=0; i<option.fillSchool.majors.length; i++) {
				$("input[name=zydh"+(i+1)+"]").val(option.fillSchool.majors[i].code);
			}
			if(option.fillSchool.majors.length>0) {
				$("input[name=ok]").click();
			} else {
				console.log("没有选择专业！");
			}
		} else if(url.indexOf("http://www1.nm.zsks.cn/kscx/xgzy3.do")==0) {
			$("input[name=ok]").click();
		} else if(url.indexOf("http://www1.nm.zsks.cn/kscx/xgzy4.do")==0) {
			var error = $("#error");
			if(error.length>0 && error.html().indexOf("成功修改")!=-1) {
				console.log("跳转到院校排名");
				location.href = "http://www1.nm.zsks.cn/kscx/zy/prePm.jsp";
			} else {
				if(error.length>0) console.log(error.html());
				else console.log("出现错误" + location.href);
				autoDisable();
			}
		} else if(url.indexOf("http://www1.nm.zsks.cn/kscx/pm.do")==0) {
			console.log("院校排名，关闭自动");
			
			autoDisable();
			/*-------------- 存储名次 --------------*/
			var re = /\d+/ig.exec($("strong font").html());
			var paihang = -1;
			if(re!=null) {
				paihang = re[0];
			}
			var pingxing = $("td[align=center]>font[color=#000099]").html();
			if(paihang!=-1) {
				chrome.extension.sendRequest({"cmd": "SAVE_ORDER", "code":option.fillSchool.code, "order":{"paihang":paihang, "pingxing":pingxing,"time":new Date()}}, function(response) {
					console.log("SAVE_ORDER " + response.success);
				});
			}			
			/*-------------- 存储名次 --------------*/
		} 
	});
}

function autoDisable() {
	chrome.extension.sendRequest({"cmd": "AUTO_DISABLE",}, function(response) {
		console.log("AUTO disabled " + response.success);
	});
}

function saveEnv(env) {
	chrome.extension.sendRequest({"cmd": "SAVE_ENV", "env" : env}, function(response) {
		console.log(response.msg);
	});
}

function savePath(filePath) {
	chrome.extension.sendRequest({"cmd": "SAVE_PATH", "path" : filePath}, function(response) {
		console.log(response.msg);
	});
}