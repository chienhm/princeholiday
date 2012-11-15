var frame = $("#main")[0].contentWindow.document;
var input = $("input[name=randCode]", frame);
var randCode = input.val();
var toHandle = -1;

function login() {
	$.getJSON("loginAction.do?method=loginAysnSuggest", function(data){
		$.post("https://dynamic.12306.cn/otsweb/loginAction.do?method=login", {
			"loginRand" : data.loginRand,
			"refundLogin" : "N",
			"refundFlag" : "Y",
			"loginUser.user_name" : "ots00a",
			"nameErrorFocus" : "",
			"user.password" : "ots12306",
			"passwordErrorFocus" : "",
			"randCode" : randCode,
			"randErrorFocus" : ""
		}, function(html) {
			if(html.indexOf("请输入正确的验证码")!=-1) {
				$("#img_rrand_code", frame).attr("src", "https://dynamic.12306.cn/otsweb/passCodeAction.do?rand=sjrand" + Math.random());
				input.val("").focus().blur(function(e){
					if($(this).val().length!=4){
						$(this).focus();
					} else {
						randCode = $(this).val();
						login();
					}
				});
			} else if(html.indexOf("当前访问用户过多")!=-1) {
				console.log("用户过多，登录失败！");
				toHandle = setTimeout(login, 2000);
			} else if(html.indexOf("密码输入错误")!=-1) {
				console.log("密码输入错误?????????????????????");
				toHandle = setTimeout(login, 2000);
			} else if(html.indexOf("已经被锁定")!=-1) {
				console.log("系统提示：您的用户已经被锁定,锁定时间为20 分钟,请稍后再试!\n助手感叹：泥马，登录不上还锁定账号，你让P民怎么活啊！");
			} else {
				$("#main").attr("src", "https://dynamic.12306.cn/otsweb/loginAction.do?method=initForMy12306");
				console.log(html);
			}
		});
	})
}
login();