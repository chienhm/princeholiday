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
			if(html.indexOf("��������ȷ����֤��")!=-1) {
				$("#img_rrand_code", frame).attr("src", "https://dynamic.12306.cn/otsweb/passCodeAction.do?rand=sjrand" + Math.random());
				input.val("").focus().blur(function(e){
					if($(this).val().length!=4){
						$(this).focus();
					} else {
						randCode = $(this).val();
						login();
					}
				});
			} else if(html.indexOf("��ǰ�����û�����")!=-1) {
				console.log("�û����࣬��¼ʧ�ܣ�");
				toHandle = setTimeout(login, 2000);
			} else if(html.indexOf("�����������")!=-1) {
				console.log("�����������?????????????????????");
				toHandle = setTimeout(login, 2000);
			} else if(html.indexOf("�Ѿ�������")!=-1) {
				console.log("ϵͳ��ʾ�������û��Ѿ�������,����ʱ��Ϊ20 ����,���Ժ�����!\n���ָ�̾��������¼���ϻ������˺ţ�����P����ô���");
			} else {
				$("#main").attr("src", "https://dynamic.12306.cn/otsweb/loginAction.do?method=initForMy12306");
				console.log(html);
			}
		});
	})
}
login();