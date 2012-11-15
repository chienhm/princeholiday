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
if(s>10) { m++; if(m==60)h++; }
var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), h, m, 10, 850);
at(atTime, function(){
	var date = new Date(); console.log(date + date.getMilliseconds());
	document.getElementById("J_authSubmit").click();
});
//=================================================================================================以上在下一个11点提交付款
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
var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), 23, 11, 10, 600);
at(atTime, function(){
	var date = new Date(); console.log(date + date.getMilliseconds());
	document.getElementById("J_authSubmit").click();
});
//=================================================================================================以上自定义时间付款