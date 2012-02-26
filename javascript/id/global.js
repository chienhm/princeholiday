// Global Javascript
var http_request = false;
function makeRequest(url, functionName, httpType, sendData) {

 http_request = false;
 if (!httpType) httpType = "GET";

 if (window.XMLHttpRequest) { 
  http_request = new XMLHttpRequest();
  if (http_request.overrideMimeType) {
   http_request.overrideMimeType('text/plain');
  }
 } else if (window.ActiveXObject) {
  try {
   http_request = new ActiveXObject("Msxml2.XMLHTTP");
  } catch (e) {
   try {
    http_request = new ActiveXObject("Microsoft.XMLHTTP");
   } catch (e) {}
  }
 }

 if (!http_request) {
  alert('Cannot send an XMLHTTP request');
  return false;
 }

 var changefunc="http_request.onreadystatechange = "+functionName;
 eval (changefunc);
 //http_request.onreadystatechange = alertContents;
 http_request.open(httpType, url, true);
 http_request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
 http_request.send(sendData);
}

function getReturnedText () {
	document.getElementById("show").style.display = "block";
 if (http_request.readyState == 4) {
  if (http_request.status == 200) {
		document.getElementById("show").innerHTML = http_request.responseText;
  }
 }else{
		document.getElementById("show").innerHTML = "<img src=/images/loading.gif>";
 }
}
var t;
function show(){
	clearTimeout(t);
	document.getElementById("morebox").style.display="block";
}
function kill(){
	t=setTimeout(closebox,300);
}
function closebox(){
	document.getElementById("morebox").style.display="none";
}