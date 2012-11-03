
	chrome.extension.sendRequest({cmd: "GET_OPTIONS"}, function(options) {
		console.log(options);
		if(options && options.workaround) {
			console.log("Workaround enabled.");
			setTimeout(function() {
				document.getElementById("J_CoinGrantBtn").click();
			}, 10000);
			
			//window.close();
		}
	});
	