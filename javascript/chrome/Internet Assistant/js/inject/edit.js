if(window.document.designMode == "on") {
	window.document.designMode = "off";
	window.document.body.contentEditable = "false";
} else {
	window.document.designMode = "on";
	window.document.body.contentEditable = "true";
}
window.focus();