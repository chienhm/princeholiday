var collectMajor = true;

function showSchools() {
	return JSON.parse(localStorage["schools"]);
}

function saveSchools(schools) {
	console.log("本地存储");
	localStorage["schools"] = JSON.stringify(schools);
	chrome.extension.sendRequest({"cmd": "SET_SCHOOL", "schools":schools}, function(response) {
		console.log("插件存储" + response.success);
	});
}

function getSchools() {
	var schools = {};
	var rows = $("#report1 tr");
	var start = false;
	var count = 0;
	for(var i=0; i<rows.length; i++) {
		if($(rows[i]).text().indexOf("返回上一页")!=-1){break;}
		if($(rows[i]).text().indexOf("院校代号")!=-1){start = true;continue;}
		if(!start)continue;
		
		count++;
		var cols = $("td", rows[i]);
		var attr = [];
		for(var j=0; j<cols.length; j++) {
			attr[j] = $(cols[j]).text().trim();
		}
		var url = $("a", cols[1]).attr("href");
		var school = {code		: attr[0],
					  name		: attr[1],
					  plan		: attr[2],
					  score		: attr[3],
					  ratio		: attr[4],
					  rscore	: attr[5],
					  expected	: attr[6],
					  realnum	: attr[7],
					  remain	: attr[8],
					  "url"		: url};
		
		schools[school.name] = school;
		if(collectMajor) {
			setTimeout(function(link){return function(){window.open(link+"?chrome");}}(url), count*1000);
		}
	}
	console.log(schools);
	if(count>0) {
		saveSchools(schools);
	}
}

function clearAll() {
	for(var name in localStorage){
		if(name.length==3) {
			delete localStorage[name];
		}
	}
	chrome.extension.sendRequest({"cmd": "CLEAR_ALL"}, function(response) {
		console.log("Clear success: " + response.success);
	});
}
