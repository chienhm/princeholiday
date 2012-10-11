function _login() {
	chrome.extension.sendRequest({cmd: "GET_OPTIONS"}, function(options) {
		console.log(options);
		if(options && options.autoLogin) {
			console.log("Auto login enabled.");
			chrome.extension.sendRequest({cmd: "GET_USERS"}, function(users) {
				//console.log(users);
				if(users) {
					var user = null;
					var pass = null;
					if(options.currentUser && users[options.currentUser]) {
						user = options.currentUser;
						pass = users[options.currentUser];
					} else if(options.defaultUser && users[options.defaultUser]) {
						user = options.defaultUser;
						pass = users[options.defaultUser];
					} else {
						for(var name in users) {
							user = name;
							pass = users[name];
							break;
						}
					}
					if(user) {
						document.getElementById("J_SafeLoginCheck").click();
						document.getElementById("TPL_username_1").value = user;
						document.getElementById("TPL_password_1").value = Base64.decode(pass);
						document.getElementById("J_SubmitStatic").click();
					}
				}
			});
		}
	});
}

function login() {
	var host = location.host;
	var page = location.pathname;
	//console.log("I'm in frame? "+((self==top)?"no":"yes")); //J_LoginPopup
	
	/* check if it's in login page. */
	if(host=="login.taobao.com" && page=="/member/login.jhtml") {
		console.log("login");
		_login();
		return true;
	}
	return false;
}

login();