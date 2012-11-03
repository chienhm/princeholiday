var b = chrome.extension.getBackgroundPage();

function save_options() {
	var engmode = $("#engmode").attr("checked")=="checked";
	
	b.setConfig("engmode", engmode);
	
	var status = document.getElementById("status");
	status.innerHTML = "Options Saved.";
	setTimeout(function() {
		status.innerHTML = "";
	}, 1000);
}

function restore_options() {
	var engmode = b.getConfig("engmode");
	if(engmode=="true") {
		$("#engmode").attr("checked", true);
	}
}

$(function () {
	restore_options();
	$("#oSave").click(save_options);
});