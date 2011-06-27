var nameBL = ["phone", "logonId"];
var dr = {};	//data received
var dc = {};	//data collected

var f = document.forms[0];
function collect() {
	dc = {};
	for(var i=0; i<f.elements.length; i++) {
		var e = f.elements[i];
		if(e.type=="hidden" || inArray(e.name, nameBL))continue;
		if(e.type=="radio") {
			if(e.checked && e.value!=dr[e.name]) {
				dc[e.name] = e.value;
			}
			continue;
		}
		if(e.value!="" && e.value!=dr[e.name]) {
			dc[e.name] = e.value;
		}
	}
}

function dispatch() {
	for(name in dr) {
		if(f[name]) {
			if(dc[name]==dr[name]) {
				//if received value equals to input value, ignore
			} else {
				if(f[name].length && f[name][0].type=="radio") {
					for(var i=0; i<f[name].length; i++) {
						if(f[name][i].value==dr[name]) {
							f[name][i].checked = true;
							break;
						}
					}
				} else {
					f[name].value = dr[name];
				}
			}
		}
	}
}

function downSync() {
	dispatch();
	tick = setTimeout(upSync, 1000);
}

function upSync() {
	collect();
	var para = json2qs(dc) + "r=" + Math.random();
	console.log(para);
	dynamicJs("http://cnrdloull1c:8080/e.morntea.com/util/suning/sync_form.jsp" + para);
	//console.log(dc);
}

var tick;
upSync();

function inArray(s, a) {
	for(var i=0; i<a.length; i++) {
		if(s==a[i])return true;
	}
	return false;
}

function dynamicJs(src) {
	var head = document.getElementsByTagName('head')[0];
	var script = document.createElement('script');
	script.type = 'text/javascript';
	script.src= src;
	head.appendChild(script);
}

function json2qs(json) {
	var qs = "?";
	for(name in json) {
		qs += name + "=" + json[name] + "&";
	}
	return qs;
}