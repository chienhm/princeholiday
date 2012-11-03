var images = new Array();
var count = 0;

function mergeImages(imgs) {
	for(var j in imgs) {
		var exist = false;
		for(var i in images) {
			if(imgs[j].src==images[i].src) {
				exist = true;
				break;
			}
		}
		if(!exist) {
			images.push(imgs[j]);
			showImage(imgs[j]);
		}
	}
}

function showImage(img) {
	count++;
	$(document.body).append(
		"<div id=\"i"+count+"\" class=\"pic\">"
		+ "<a href=\""+img.src+"\" target=\"_blank\">"
		+ "<img src=\""+img.src+"\" title=\""+img.width+"×"+img.height+"\" />"
		+ "</a>"
		+"</div>");
}

function listener(request, sender, sendResponse) {
	if(request.cmd == "SHOW_IMAGES") {
		mergeImages(request.images);
	}
}

chrome.extension.onMessage.addListener(listener);