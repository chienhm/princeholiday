
//---------------------------------------------------------------------------天猫下单 Begin
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
var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), 23, 47, 59, 0);
at(atTime, function(){
	var date = new Date(); console.log(date + date.getMilliseconds());
	var btn = document.getElementById("J_Go");
	if(btn==null) btn = document.getElementById("J_go");
	btn.click();
});
//---------------------------------------------------------------------------天猫下单 End

//=================================================================================================支付宝付款 Begin
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
var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), 9, 59, 59, 550);
at(atTime, function(){
	var date = new Date(); console.log(date + date.getMilliseconds());
	document.getElementById("J_authSubmit").click();
});
//=================================================================================================支付宝付款 End

/*-------------------------------------------------------------------------------------------------
  配合网速测试，服务器时间减200-300ms最佳，400ms出过一次错误
  -------------------------------------------------------------------------------------------------*/
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
var h = now.getHours();
var m = now.getMinutes();
var s = now.getSeconds();
if(s>10) { m++; if(m==60)h++; } /* Work Around */
var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), h, m, 10, 850);
at(atTime, function(){
	var date = new Date(); console.log(date + date.getMilliseconds());
	document.getElementById("J_authSubmit").click();
});
//=================================================================================================以上在下一个11点提交付款，供测试