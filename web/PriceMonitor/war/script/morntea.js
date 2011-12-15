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
				+ "<a id=\"logout\" href=\"logout.html\">Logout</a>");		
	}
}

function updateMessage() {
	var message = getCookie("message");
	$("#messageField").append(message);
}
