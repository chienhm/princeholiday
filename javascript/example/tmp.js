var toHandle = -1;
function at(atTime, func, force) {
	var left = atTime.getTime()-now.getTime();
	if(left<0 || left>=2147483648) {
		if(force) func();
		else console.log("expired.");
		return;
	}
	toHandle = setTimeout(func, left);
	console.log(Math.floor(left/1000) + "s " + left%1000 + "ms left.");
}
//Example:
var now = new Date();
var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), 19, 59, 59, 900);
at(atTime, function(){
	var date = new Date(); console.log(date + date.getMilliseconds());
	document.getElementsByClassName("J_RefreshStatus")[0].click();
});
//---------------------------------------------------------------------------点击查看秒杀问题 End

var toHandle = -1;
function at(atTime, func, force) {
	var left = atTime.getTime()-now.getTime();
	if(left<0 || left>=2147483648) {
		if(force) func();
		else console.log("expired.");
		return;
	}
	toHandle = setTimeout(func, left);
	console.log(Math.floor(left/1000) + "s " + left%1000 + "ms left.");
}
//Example:
var now = new Date();
var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), 19, 59, 59, 900);
at(atTime, function(){
	var date = new Date(); console.log(date + date.getMilliseconds());
	document.getElementById("J_PerformSubmit").click();
});
//---------------------------------------------------------------------------充值下单 End
//----------------------------------------------云服务器提交
var toHandle = -1;
function at(atTime, func, force) {
	var left = atTime.getTime()-now.getTime();
	if(left<0 || left>=2147483648) {
		if(force) func();
		else console.log("expired.");
		return;
	}
	toHandle = setTimeout(func, left);
	console.log(Math.floor(left/1000) + "s " + left%1000 + "ms left.");
}
//Example:
var now = new Date();
var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), 22, 59, 59, 950);
at(atTime, function(){
	var date = new Date(); console.log(date + date.getMilliseconds());
	$("#fmsubmit").click()
});

//---------------------------------------------- 捉猫猫
var EACH_INTERVAL = 200;
var start = new Date();
function encl(theCat, index) {
	return function(){
		theCat.click();
		console.log("捉第"+index+"只猫。");
	}
}
function loopCat() {
	var now = new Date();
	if(now.getTime()-start.getTime()>60000)return;
	var cats = document.getElementsByClassName("vol-cat");
	console.log("发现"+cats.length+"只猫，包括躲起来的。");
	var interval = EACH_INTERVAL * cats.length;
	var n = 0;
	for(var i=0; i<cats.length; i++) {
		var cat = cats[i];
		if(cat.parentNode.style.display=="block") {
			n++;
			setTimeout(encl(cat, n), n * EACH_INTERVAL);
		}
	}
	setTimeout(loopCat, interval);
}
loopCat();
//---------------------------------------------- 自动充值付款
var form = $("#cartoonConfirmForm")[0];
var token = form["_form_token"].value;
var order = form["orderId"].value;
vp();
function vp() {
	var url = "https://cashier.alipay.com/standard/payment/validatePayPassword.json";
	$.post(url, {
		J_aliedit_key_hidn:"payPassword",
		J_aliedit_using:"true",
		_input_charset:"utf-8",
		orderId:order,
		payPassword:"",
		secProdCheckStep:"STEP1"
	}, function(data){console.log(data);sc();});
}

function sc() {
	var url = "https://cashier.alipay.com/standard/step2SecProdCheck.json";
	$.post(url, {
		"J_aliedit_key_hidn" : "payPassword",
		"J_aliedit_uid_hidn" : "",
		"J_aliedit_using" : "true",
		"REMOTE_PCID_NAME" : "_seaside_gogo_pcid",
		"_form_token" : token,
		"_input_charset" : "utf-8",
		"_seaside_gogo_" : "",
		"_seaside_gogo_p" : "",
		"_seaside_gogo_pcid" : "",
		"action" : "doPay",
		"depositAmount" : "1",
		"orderId" : order,
		"payPassword" : "",
		"secProdCheckStep" : "STEP1"
	}, function(data){console.log(data);submit();});
}

function submit() {
	form["_seaside_gogo_"].value="";
	form["payPassword"].value="";
	form.submit();
}

//---------------------------------------------- 