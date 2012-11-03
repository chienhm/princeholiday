
function formatTime(time) {
	return time.getHours()+":"+time.getMinutes()+":"+time.getSeconds()+"."+time.getMilliseconds();
}

function futureRun(func, ms) {
	var t0 = new Date();
	setTimeout(func, 1000-t0.getMilliseconds() + ms);
}

function getServerTime() {
	var st = new Date();
	var href=location.href;
	href = href + ((href.indexOf("?")==-1)?"?":"&") + Math.random();
	
	var xhr = new XMLHttpRequest();
	xhr.onreadystatechange = handleStateChange; // Implemented elsewhere.
	xhr.open("GET", href, true);
	xhr.send();
	
	function handleStateChange() {
		if (xhr.readyState == 4) {
			var rt = new Date();
			var elapse = rt.getTime()-st.getTime();
			console.log(
			  "[Send    : " + formatTime(st) + "]\n" 
			+ "[Receive : " + formatTime(rt) + "]\n"
			+ "[Server  : " + formatTime(new Date(xhr.getResponseHeader("Date"))) + "]\n"
			+ "[Time    : " + elapse + "ms]");
			
			var r = document.getElementById("_r_");
			if(r) {
				r.innerHTML = "[Send: "+formatTime(st)+"] => [Server: "+formatTime(new Date(xhr.getResponseHeader("Date")))+"] => [Receive:"+formatTime(rt) + "], Time: "+elapse+"ms";
			}
		}
	}
}

var speedObj = document.getElementById("_speed_");
if(speedObj) {
	speedObj.style.display = "block";
} else {
	document.body.insertAdjacentHTML("afterBegin", "<div id='_speed_'><input type='text' id='_ms_' value='0'><input type='button' id='_bt_' value='speed'/><input type='button' id='_close_' value='close'/><span id=_r_></span></div>");
	document.getElementById("_close_").onclick=function() {
		document.getElementById("_speed_").style.display = "none";
	};
	document.getElementById("_bt_").onclick=function(){
		var ms = document.getElementById("_ms_").value;
		if(ms!="") {
			ms = parseInt(ms);
			futureRun(getServerTime, ms);
		}
	};
}

futureRun(getServerTime, 500);