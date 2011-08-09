//get cookie
function getCookie(sname) {
	var rex = new RegExp("(^| )" + sname + "=\"?([^;\"]*)(\"|;|$)");
	var arr = document.cookie.match(rex);
	if (arr != null) {
		return unescape(arr[2]);
	} else {
		return "";
	}
}

function updateUserInfo() {
	var username = getCookie("username");
	if (username == "") {
		$("#userInfoField").append("<a id=\"login\" href=\"login.html\">Login</a>"
				+ "<a id=\"register\" href=\"register.html\">Register</a>");
	} else {
		$("#userInfoField").append("<label>Welcome, " + username + "</label>"
				+ "<a id=\"logout\" href=\"logout\">Logout</a>");		
	}
}

function updateMessage() {
	var message = getCookie("message");
	$("#messageField").append(message);
}

function getErrorCode() {
	var errorCodeReg = new RegExp("(\&|\?)p=([^\&]*)(\&|$)");
	var arr = document.location.href.match(errorCodeReg);
	if (arr != null) {
		return unescape(arr[2]);
	} else {
		return null;
	}
}
function getErrorMessage(errorCode) {
	if (errorCode == null) {
		return "";
	}
	switch (errorCode) {
		case  0: return "Success";
		case  1: return "Error";
		case 10: return "Register successfully";
		case 11: return "Register error";
		case 12: return "log";
		default: return "";
	}
}
