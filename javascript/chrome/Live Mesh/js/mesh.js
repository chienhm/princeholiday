var url = location.href;
console.log(url);
if(url.indexOf("https://devices.live.com/Sync/Summary")==0) {
	console.log("start page.");
	var tables = $(".dax_sh_folderitemtable"); //$(".dax_sh_primaryText");

	for(var i=0; i<tables.length; i++) {
		var table = $(tables[i]);
		
		if(table.text().indexOf("Shared by")==-1) {
			var url = table.find("tr td.dax_sh_folderImagePadding").attr("onclick").replace(/window.location='(.+?)'/ig, "$1");
			console.log(url);
			window.open(url);
		}
	}
} else if(url.indexOf("https://devices.live.com/Sync/FolderSelf")==0
	|| url.indexOf("https://devices.live.com/Sync/BrowseFolder")==0) {
	var files = $("#dax_brf_contents a");
	if(files.length==1) {
		files[0].click();
	} else {
		for(var i=0; i<files.length; i++) {
			var file = $(files[i]);
			var url = file.attr("href");
			if(url.indexOf("https://devices.live.com/Sync/BrowseFolder")==0) {
				console.log(url);
				if(i==0)window.open(url);
			} else {
				files[i].click();
			}
		}
	}
}