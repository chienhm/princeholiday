var nameBL = ["phone", "logonId"];
var dr = {choice1560065869: "4", verifyCode0_1560065833: "放", sssaf:""};	//data received
var dc = {};	//data collected

var f = document.forms[0];
function collect() {
	for(var i=0; i<f.elements.length; i++) {
		var e = f.elements[i];
		if(e.type=="hidden" || inArray(e.name, nameBL))continue;
		if(e.type=="radio") {
			//console.log(e.value);
			if(e.checked) {
				dc[e.name] = e.value;
			}
			continue;
		}
		if(e.value!="") {
			dc[e.name] = e.value;
		}
		//console.log(e.name + ", " + e.type);
	}
}

function dispatch() {
	for(name in dr) {
		if(f[name]) {
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

var tick;

function upSync() {
	collect();
	var para = json2qs(dc) + "r=" + Math.random();
	//para = "?choice1560065869=4&verifyCode0_1560065833=好&qa1560065871=中文2&r=0.8843177182134241";
	console.log(para);
	dynamicJs("http://cnrdloull1c:8080/e.morntea.com/util/suning/sync_form.jsp" + para);
	console.log(dc);
}

function downSync() {
	dispatch();
	tick = setTimeout(upSync, 1000);
}

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

function getParameterByName(name) {
    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}