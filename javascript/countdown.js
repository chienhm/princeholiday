function invoke() {
	alert(0);
}
var headObj = document.getElementsByTagName('head')[0];
var scriptObj = document.createElement('script');
scriptObj.type = 'text/javascript';
scriptObj.src = 'http://localhost:8888/invoke';
headObj.appendChild(scriptObj);