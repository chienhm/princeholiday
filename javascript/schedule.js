javascript:var now = new Date();var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), 16, 0, 0, 0);document.title=atTime.toString();setTimeout(function(){document.getElementById("submitBtn").click();}, atTime.getTime()-now.getTime());void(0);

javascript:var now = new Date();var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), 16, 0, 0, 0);document.title=atTime.toString();setTimeout(function(){document.getElementById("performSubmit").click();}, atTime.getTime()-now.getTime());void(0);

javascript:var now = new Date();var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), 16, 0, 0, 0);document.title=atTime.toString();setTimeout(function(){location.reload();}, atTime.getTime()-now.getTime());void(0);

/**
 * At time execute func
 * time: Date
 * func: Function
 */
function log(str) {
	console.log(str);
}
function at(time, func) {
	var now = new Date();
	var diff = time.getTime() - now.getTime(); 
	if(diff<0) {log("time expired");return;}
	var timeout = (diff>10000)?(diff-10000):(diff>1000)?(diff-1000):(diff>200)?(diff-200):diff;
	setTimeout(function(){if(timeout==diff)func();else at(time, func);}, timeout);
	log(now + ", " + now.getMilliseconds() + "ms; " + diff + "ms left.");
}

var time = new Date(2011,0,5,15,56,10);
time.setMilliseconds(800);
at(time, function(){alert(new Date() + "," + new Date().getMilliseconds());});

javascript:function showActEnd(){};showInaction();function enableSecKill(){};function disableSecKill(){};void(0);

javascript:function log(str){document.title=str;}function at(time, func){var now=new Date();var diff= time.getTime()-now.getTime();if(diff<0){log("time expired");return;}var timeout=(diff>10000)?(diff-10000):(diff>1000)?(diff-1000):(diff>200)?(diff-200):diff;setTimeout(function(){if(timeout==diff)func();else at(time,func);},timeout);log(now+","+now.getMilliseconds()+"ms;"+diff+"ms left.");}var time=new Date(2011,0,5,18,45,50);time.setMilliseconds(800);at(time,function(){document.forms["bidForm"].submit();});void(0);

javascript:function log(str){document.title=str;}function at(time, func){var now=new Date();var diff= time.getTime()-now.getTime();if(diff<0){log("time expired");return;}var timeout=(diff>10000)?(diff-10000):(diff>1000)?(diff-1000):(diff>200)?(diff-200):diff;setTimeout(function(){if(timeout==diff)func();else at(time,func);},timeout);log(now+","+now.getMilliseconds()+"ms;"+diff+"ms left.");}var time=new Date(2011,1,21,13,55,50);time.setMilliseconds(800);at(time,function(){document.forms[0].submit();});void(0);
