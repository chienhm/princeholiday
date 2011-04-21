//祝我的妈妈和老婆三八节快乐，祝你们永远健康幸福！
var jQuery;
var timerHandle;
var countdown = false;
var TARGET_FLOOR = 338;

function loadJQuery() {
	if(!jQuery) {
		console.log("load jQuery...");
		var head= document.getElementsByTagName('head')[0];
		var script= document.createElement('script');
		script.type= 'text/javascript';
		script.src= 'http://code.jquery.com/jquery-1.5.1.min.js';
		head.appendChild(script);
	}
}

loadJQuery();

function postReply() {
	var elements = document.forms["frmAnnounce"].getElementsByTagName("input");
	for(var i=0; i<elements.length; i++){
		if(elements[i].type=="image" && elements[i].src.indexOf("btn15")!=-1) {
			console.log(elements[i]);
			elements[i].click();
			alert("post msg!");
			break;
		}
	}
}

function checkFloor(data){
	var matches = null;
	var floor = 0;
	while((matches=/(\d+)楼/g.exec(data))!=null){
		floor = matches[1];
	}
	console.log("current floor: " + floor);
	if(floor>TARGET_FLOOR-10 && floor<TARGET_FLOOR-1){
		if(!countdown) {
			countdown = true;
			window.clearInterval(timerHandle);
			timerHandle = setInterval(ajaxCheck, 1000);
		}
	} else if(floor==TARGET_FLOOR-1) {
		postReply();
	} else if(floor>=TARGET_FLOOR) {
		window.clearInterval(timerHandle);
	} else {
		console.log("waiting 5 seconds...");
	}
}

function ajaxCheck() {
	if(jQuery)jQuery.ajax({url:"http://shbbs.soufun.com/esf~14~2555/120446458_120548779_100.htm", success:checkFloor});
}

timerHandle = setInterval(ajaxCheck, 5000);