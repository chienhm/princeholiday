var b = chrome.extension.getBackgroundPage();
var ver = chrome.app.getDetails().version;
var localVer = b.getVersion();

console.log("Local version: " + localVer + ", New version: " + ver);
if(!localVer || localVer<"1.6.0") {
	var users = b.getUser();
	if(users && users.length>0) {
		$.each(users, function(n, p) {		
			//b.saveUser(n, encrypt(n, p));
		})
	}
}
b.setVersion(ver);
$(function(){$("#ver").text(ver);});