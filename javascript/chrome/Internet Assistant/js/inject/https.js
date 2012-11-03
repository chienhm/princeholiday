var url = location.href;
if(url.indexOf("http:")==0) {
	url = url.replace(/^http/i, "https");
	location.href = url;
}