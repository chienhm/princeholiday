var imgs = new Array();
for(var i=0; i<document.images.length; i++){
	var src = document.images[i].src;
	var exist = false;
	for(var j in imgs) {
		if(imgs[j].src==src) {
			exist = true;
			break;
		}
	}
	if (!exist) {
		var img = new Image();
		img.src = src;	
		imgs.push({
			"src" 	: src, 
			"width" : img.width, 
			"height": img.height, 
			"from" 	: location.host+location.pathname
		});
	}
}

chrome.extension.sendMessage({"cmd" : "SHOW_IMAGES", "images" : imgs, "from" : location.href});