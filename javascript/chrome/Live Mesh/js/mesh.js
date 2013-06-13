var url = location.href;
console.log(url);

function mesh() {

	var email = null;
	var regex = /"email":"(.+?)"/ig;
	var script = $("script:contains('email')").html();
	var result=regex.exec(script);
	if(result != null) {
		email = result[1].replace("\\u0040", "@");
	} else {
		console.error("email account missing.");
		return;
	}

	var tables = $(".dax_sh_folderitemtable"); //$(".dax_sh_primaryText");

	for(var i=0; i<tables.length; i++) {
		var table = $(tables[i]);
		
		if(table.text().indexOf("Shared by")==-1) {
			var url = table.find("tr td.dax_sh_folderImagePadding").attr("onclick").replace(/window.location='(.+?)'/ig, "$1");
			console.log(url);
			window.open(url);
		}
	}
}

function traverse() {
	var navs = $(".t_lnkpi li");
	var path = "";
	for(var i=0; i<navs.length; i++) {
		if(i<3)continue;
		path += "/" + $(navs[i]).text().trim();
	}
	
	var files = $("#dax_brf_contents a");

	for(var i=0; i<files.length; i++) {
		var file = $(files[i]);
		var url = file.attr("href");
		
		if(url.indexOf("https://devices.live.com/Sync/BrowseFolder")==0) {/* folder */
			console.log(url);
			window.open(url);
		} else { /* file: https://devices.live.com/Sync/Download */
			console.log(path + "/" + file.text().trim());
			savePath({"local" : path + "/" + file.text().trim(), "link" : url});
		}
	}
	
	/* close window to save memory */
	window.close();
}

/*!**************************************************************/
if(url.indexOf("https://devices.live.com/Sync/Summary")==0 ||
	url.indexOf("https://devices.live.com/Devices/SkyDriveSyncedStorage")==0) {
	console.log("live mesh home.");
	mesh();
} else if(url.indexOf("https://devices.live.com/Sync/FolderSelf")==0
	|| url.indexOf("https://devices.live.com/Sync/BrowseFolder")==0) {
	traverse();
}

/*!**************************************************************/
function saveEnv(env) {
	chrome.extension.sendRequest({"cmd": "SAVE_ENV", "env" : env}, function(response) {
		console.log(response.msg);
	});
}

function savePath(filePath) {
	chrome.extension.sendRequest({"cmd": "SAVE_PATH", "path" : filePath}, function(response) {
		console.log(response.msg);
	});
}