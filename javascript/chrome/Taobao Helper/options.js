var b = chrome.extension.getBackgroundPage();

function save_options() {
	var config = b.getConfig();
	var autolog = document.getElementById("autolog");
	if(config) {
		config.autoGetCoinEnabled = autolog.checked;
	} else {
		config = {autoGetCoinEnabled : autolog.checked};
	}
	b.saveConfig(config);
	
	var status = document.getElementById("status");
	status.innerHTML = "Options Saved.";
	setTimeout(function() {
		status.innerHTML = "";
	}, 1000);
}

function restore_options() {
	var config = b.getConfig();
	if(config!=null) {
		document.getElementById("autolog").checked = config.autoGetCoinEnabled;
	}
}

document.addEventListener('DOMContentLoaded', function () {
	restore_options();
	document.querySelector('button').addEventListener('click', save_options);
});