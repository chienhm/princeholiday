//------------------------------------------------------------------------------------------------- (0)
// Basic
// 1. Trigger event in Firefox/Chrome
var evt = document.createEvent("MouseEvents");  
evt.initEvent("click", true, true);
document.getElementById('J_hb').dispatchEvent(evt);
//-------------------------------------
// 2. Time interval
function _click() {
	document.getElementById('J_hb').click();//for IE
}
var toHandle = setInterval(_click, 1000, null);
//-------------------------------------
// 3. Periodically run function
var toHandle = -1;
var interval = 5000;
var random = undefined;
function periodicRun(func) {
	var timeout = interval;
	if(toHandle!=-1) { clearInterval(toHandle); toHandle = -1; }
	if(random) { timeout += parseInt(Math.random()*random); }
	toHandle = setTimeout(function() { func(); periodicRun(func); }, timeout);
}
//-------------------------------------
// 4. Set time interval
if(typeof(toHandle)!="undefined"&&typeof(interval)!="undefined") {
	var input = prompt("toHandle=" + toHandle + ", interval=" + interval + "\nset interval:", interval);
	if(input==-1 && toHandle!=-1) {
		clearTimeout(toHandle);
	} else {
		interval = input;
	}
} else {
	console.log("var undefined.");
}
//-------------------------------------
// 5. clear timer
clearTimeout(toHandle);
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
dynamicJs("http://code.jquery.com/jquery-1.7.2.min.js");
//------------------------------------------------------------------------------------------------- (2)
// Run function at the next time slot, such as the 200th milliseconds
function futureRun(func, ms) {
	var t0 = new Date();
	setTimeout(func, 1000-t0.getMilliseconds() + ms);
}
// Run script at specified time
var toHandle = -1;
function at(atTime, func) {
	var left = atTime.getTime()-now.getTime();
	toHandle = setTimeout(func, left);
	console.log(Math.floor(left/1000) + "s " + left%1000 + "ms left.");
}
//Example:
var now = new Date();
var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), 14, 0, 0, 0);
at(atTime, function(){
	var date = new Date(); console.log(date + date.getMilliseconds());
	location.reload();
	//window.frames[0].location.reload();
});
//clearTimeout(toHandle);
//------------------------------------------------------------------------------------------------- (3)
/* fix the long time that may not be accurate. */
var toHandle = -1;
function at(time, func) {
	var now = new Date();
	var diff = time.getTime() - now.getTime(); 
	if(diff<0) {console.log("time expired");return;}
	var timeout = (diff>60000)?(diff-60000):(diff>10000)?(diff-10000):diff;
	toHandle = setTimeout(function(){
		if(timeout==diff)func();
		else at(time, func);
	}, timeout);
	console.log("[" + now + ", " + now.getMilliseconds() + "ms] " 
		+ ( (diff>60000) ? (parseInt(diff/60000)+"m "+parseInt(diff%60000/1000)+"s") : (parseInt(diff/1000)+"s") ) + " left.");
}

var now = new Date();
var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), 15, 0, 0, 0);
at(atTime, function(){
	location.reload();
});
//clearTimeout(toHandle);
//------------------------------------------------------------------------------------------------- (4)
// Ajax
//-------------------------------------------------------------------------------------------------
function fT(date) {
	return date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()+"."+date.getMilliseconds();
}

//------------------------------------------------------------------------------------------------- (5)
// Wrapped in iframe
//-------------------------------------------------------------------------------------------------
document.write("<iframe id='homevv' width='"+document.body.clientWidth+"' height='"+document.body.clientHeight+"' frameborder='0' src='"+ location.href +"'></iframe>");

//------------------------------------------------------------------------------------------------- (5)
// Format Time & Log
//-------------------------------------------------------------------------------------------------
/* Next second
var t0 = new Date();
t0.setMilliseconds(0);
t0.setTime(t0.getTime()+1000);
*/
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