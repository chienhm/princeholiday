if(inPage("login.taobao.com", "/member/login.jhtml")) {
	chrome.extension.sendRequest({cmd: "GET_OPTIONS"}, function(response) {
		console.log(response.options);
		if(response.options && response.options.autoLogin) {
		}
	});
}