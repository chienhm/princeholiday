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

/*!**************************************************************/
if(url.indexOf("http://www1.nm.zsks.cn/kscx/pm_skf.jsp")==0) {
	location.href = "http://www1.nm.zsks.cn/kscx/preMm_skf.do?pcdm=1";
} else if(url.indexOf("http://www1.nm.zsks.cn/kscx/zy/prePm.jsp")==0) {
	realRank();
} else if($("form[name=mmFormForm]").length>0) { /* 仅密码框，自动输入并提交 */
	autoInput(submit);
} 

/*!**************************************************************/
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