// Dynamic load javascript
var head = document.getElementsByTagName('head')[0];
var script = document.createElement('script');
script.type = 'text/javascript';
script.src= 'http://code.jquery.com/jquery-1.5.2.min.js';
head.appendChild(script);
void(0);

function dynamicJs(src) {
	var head = document.getElementsByTagName('head')[0];
	var script = document.createElement('script');
	script.type = 'text/javascript';
	script.src= src;
	head.appendChild(script);
}

//javascript:var head=document.getElementsByTagName('head')[0];var script=document.createElement('script');script.type='text/javascript';script.src='http://localhost:8888/';head.appendChild(script);void(0);
