//------------------------------------------------------------------------------------------------- 淘宝新版抢购
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
function kill() {
	console.log(new Date()+"提交");
	document.getElementsByClassName("J_Submit submit")[0].click();
}
var now = new Date();
var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), 21, 12, 0, 0);
at(atTime, function(){
	var date = new Date(); console.log(date + date.getMilliseconds());
	function fresh(){
		var answer = document.getElementsByClassName("answer-input");
		if(answer.length!=0) {
			console.log("聚焦");
			answer[0].focus();
			answer[0].onkeyup = function(e){
				//console.log(e.keyCode);
				//------------------------------------------------ 数值计算
				var v = answer[0].value;
				if(48<v.charCodeAt(0) && v.charCodeAt(0)<=57) {
					var op = v.indexOf(" ");
					if(op==-1) op = v.indexOf("+");
					if(v.length==6) {
						if(op==-1) {
							answer[0].value = eval(v.substring(0,4)+"-"+v.substring(4));
							kill();
						}
					} else if (v.length>6) {
						answer[0].value = eval(v.substring(0,op)+"+"+v.substring(op+1));
						kill();
					}
					return;
				}
				//------------------------------------------------四个汉字的首字母
				/*if(v.length==4) {
					var allchar = true;
					for(var i=0; i<4; i++) {
						var ch = v.charCodeAt(i);
						if(ch<97 || ch>122) {
							allchar = false;break;
						}
					}
					if(allchar) {
						kill();
						return;
					}
				}*/
				//------------------------------------------------反转
				if(e.keyCode>=37 && e.keyCode<=40) {
					console.log("反转");
					answer[0].value = answer[0].value.split("").reverse().join("");
					kill();
				} else if (e.keyCode==13) {
					kill();
				}
				//------------------------------------------------
			}
			answer[0].onblur = function() {
				kill();
			};
		} else {
			var button = document.getElementsByClassName("J_RefreshStatus");
			if(button.length!=0) {
				console.log("点击刷新");
				button[0].click();
				setTimeout(fresh, 300);
			}
		}
	}
	fresh();
});