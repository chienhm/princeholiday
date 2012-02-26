//------------------------------------------------------------------------------------------------- (0)
// Basic
// 1. Trigger event in Firefox/Chrome
var evt = document.createEvent("MouseEvents");  
evt.initEvent("click", true, true);
document.getElementById('J_hb').dispatchEvent(evt);
// 2. Time interval
function _click() {
	document.getElementById('J_hb').click();//for IE
}
var interval = setInterval(_click, 1000, null);
//clearInterval(interval);
//------------------------------------------------------------------------------------------------- (1)
// Dynamic load javascript
//-------------------------------------------------------------------------------------------------
function dynamicJs(src) {
	var head = document.getElementsByTagName('head')[0];
	var script = document.createElement('script');
	script.type = 'text/javascript';
	script.src= src;
	head.appendChild(script);
}
dynamicJs("http://code.jquery.com/jquery-1.5.2.min.js");
//-------------------------------------------------------------------------------------------------
//javascript:var head=document.getElementsByTagName('head')[0];var script=document.createElement('script');script.type='text/javascript';script.src='http://localhost:8888/';head.appendChild(script);void(0);
//------------------------------------------------------------------------------------------------- (2)
/**
 * Run script at specified time
 */
function at(atTime, func) {
	var left = atTime.getTime()-now.getTime();
	setTimeout(func, left);
	console.log(Math.floor(left/1000) + "s " + left%1000 + "ms left.");
}
//Example:
var now = new Date();
var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), 15, 52, 30, 30);
at(atTime, function(){location.reload();});
//-------------------------------------------------------------------------------------------------
//javascript:var now = new Date();var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), 16, 0, 0, 0);document.title=atTime.toString();setTimeout(function(){location.reload();}, atTime.getTime()-now.getTime());void(0);
//------------------------------------------------------------------------------------------------- (3)
/**
 * At time execute func
 * time: Date
 * func: Function
 */
function log(str) {
	console.log(str);
}
/* precise time, but no sense. */
function at(time, func) {
	var now = new Date();
	var diff = time.getTime() - now.getTime(); 
	if(diff<0) {log("time expired");return;}
	var timeout = (diff>10000)?(diff-10000):(diff>1000)?(diff-1000):(diff>200)?(diff-200):diff;
	setTimeout(function(){if(timeout==diff)func();else at(time, func);}, timeout);
	log("[" + now + ", " + now.getMilliseconds() + "ms] " + diff + "ms left.");
}

var time = new Date(2011,0,5,15,56,10);
time.setMilliseconds(800);
at(time, function(){alert(new Date() + "," + new Date().getMilliseconds());});
//------------------------------------------------------------------------------------------------- (4)
// Ajax
//-------------------------------------------------------------------------------------------------
function fT(date) {
	return date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()+"."+date.getMilliseconds();
}
var st = new Date();
jQuery.ajax({
	url: "http://www.baidu.com",
	success: function(data){
		console.log(data);
	},
	complete: function(jqXHR){
		var rt = new Date();
		console.log("[Send\t:" + fT(st) + "]\n" 
		+ "[Receive:" + fT(rt) + "]\n"
		+ "[Server:" + jqXHR.getResponseHeader("Date") + "]");
	}
});
//------------------------------------------------------------------------------------------------- (5)
// Wrapped in iframe
//-------------------------------------------------------------------------------------------------
document.write("<iframe id='homevv' width='"+document.body.clientWidth+"' height='"+document.body.clientHeight+"' frameborder='0' src='"+ location.href +"'></iframe>");

//------------------------------------------------------------------------------------------------- (5)
// Format Time & Log
//-------------------------------------------------------------------------------------------------
Date.prototype.getFullMonth = function() {return (this.getMonth()<9?"0":"") + (this.getMonth()+1);};
Date.prototype.getFullDate = function() {return (this.getDate()<10?"0":"") + this.getDate();};
Date.prototype.getFullHours = function() {return (this.getHours()<10?"0":"") + this.getHours();};
Date.prototype.getFullMinutes = function() {return (this.getMinutes()<10?"0":"") + this.getMinutes();};
Date.prototype.getFullSeconds = function() {return (this.getSeconds()<10?"0":"") + this.getSeconds();};
Date.prototype.getFullMilliseconds = function() {return (this.getMilliseconds()<10?"00":(this.getMilliseconds()<100?"0":"")) + this.getMilliseconds();};
function log(string) {
	var date = new Date();
	var fTime = "["+date.getFullYear()+"."+date.getFullMonth()+"."+date.getFullDate()+" "+date.getFullHours()+":"+date.getFullMinutes()+":"+date.getFullSeconds()+"."+date.getFullMilliseconds()+"] ";
	console.log(fTime + string);
}