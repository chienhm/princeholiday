chrome.extension.sendRequest({cmd: "GET_OPTIONS"}, function(options) {
	console.log(options);
	if(options && options.autoLogin) {
		console.log("Auto login enabled.");
		chrome.extension.sendRequest({cmd: "GET_USERS"}, function(users) {
			console.log(users);
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
					$("#J_SafeLoginCheck").click();
					$("#TPL_username_1").val(user);
					$("#TPL_password_1").val(pass);
					$("#J_SubmitStatic").click();
				}
			}
		});
	}
});
