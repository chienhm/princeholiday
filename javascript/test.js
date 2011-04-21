var body = document.body;
while(body.firstChild!=null)body.removeChild(body.firstChild);

var head = document.getElementsByTagName('head')[0];
var scripts = head.getElementsByTagName('script');
for(var i=0; i<scripts.length; i++) {
	head.removeChild(scripts[i]);
}

var script = document.createElement('script');
script.type = 'text/javascript';
script.src= 'http://code.jquery.com/jquery-1.5.2.min.js';
head.appendChild(script);

function loadPage() {
	jQuery("<iframe src='http://www.baidu.com' id='page' name='page'></iframe>").bind("load", function(){alert("loaded");}).appendTo(body);
}

script.onload = loadPage;
void(0);