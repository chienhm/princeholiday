function inPage(host, page) {
	return (host==location.host && page==location.pathname);
}

function formatTime(time) {
	return time.getHours()+":"+time.getMinutes()+":"+time.getSeconds()+"."+time.getMilliseconds();
}

function log(string, time) {
	if(time == undefined) {
		time = new Date();
	}
	// +time.getFullYear()+"-"+((time.getMonth()+1))+"-"+time.getDate()
	console.log("["+formatTime(time)+"] " + string);
}

/**
 * Run function at specified time, if force given, run function even timeout.
 */
function at(time, func, force) {
	var now = new Date();
	var diff = time.getTime() - now.getTime(); 
	if(diff<0) {
		log("timeout exceeded.");
		if(force) func();
		return;
	}
	var timeout = (diff>60000)?(diff-60000):(diff>10000)?(diff-10000):diff;
	setTimeout(function(){
		if(timeout==diff)func();
		else at(time, func);
	}, timeout);
	log(( (diff>60000) ? (parseInt(diff/60000)+"m "+parseInt(diff%60000/1000)+"s") : (parseInt(diff/1000)+"s") ) + " left.");
}

function getFormValue(html, name) {
	var regExp = new RegExp("name=\""+name+"\"[^><]+value=\"(.*?)\"", "ig");
	var r = regExp.exec(html);
	if(r!=null) {
		return r[1];
	}
	return "";
}
/*!****************************************************************************
 * @brief
 *     Reload the page before calling the function
 * @param
 *     func - The function to be called
 *     targetTime - The target time when the function is called
 *     intReloadAhead (Optional) - The time ahead when the page is reloaded (milliseconds)
 *                                 If not specified, use default 15 seconds.
 * @return
 *     None
 *****************************************************************************/
function reloadToDo(func, targetTime, intReloadAhead) {
	var now = new Date();
	if(intReloadAhead==undefined) intReloadAhead = 15*1000;
	if(targetTime) {
		var reloadTime = targetTime.getTime()-intReloadAhead;
		if(now.getTime() < reloadTime) {
			var waitTime = reloadTime - now.getTime();
			console.log("Wait until " + formatTime(new Date(reloadTime)) + "(" + waitTime/1000 + "s) to reload.");
			setTimeout(function(){
				log("Reload");
				location.reload();
			}, waitTime);
		} else {
			console.log("Wait until " + formatTime(targetTime));
			at(targetTime, func, true);
		}
	}
}