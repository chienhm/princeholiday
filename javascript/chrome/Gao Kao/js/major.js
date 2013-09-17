function showMajors(code) {
	return JSON.parse(localStorage[code]);
}

function saveMajors(code, majors) {
	console.log("本地存储专业 " + code);
	localStorage[code] = JSON.stringify(majors);
	chrome.extension.sendRequest({"cmd": "SET_MAJOR", "code":code, "majors":majors}, function(response) {
		console.log("插件存储专业 " + code + ", " + response.success);
	});
}

function getMajors() {

	var schoolCode  = null;
	var re = /\w{2,}/ig.exec($(".report1_1_1").html());
	if(re!=null) {
		schoolCode = re[0];
	} else {
		console.error("No school code found.");
		return;
	}
	
	var majors = [];
	var rows = $("#report1 tr");
	var start = false;
	for(var i=0; i<rows.length; i++) {
		if($(rows[i]).text().indexOf("统计时间")!=-1){break;}
		if($(rows[i]).text().indexOf("专业代号")!=-1){start = true;continue;}
		if(!start)continue;
		var cols = $("td", rows[i]);
		var attr = [];
		for(var j=0; j<cols.length; j++) {
			attr[j] = $(cols[j]).text().trim();
		}
		var major = {code		: attr[0],
					  name		: attr[1],
					  plan		: attr[2],
					  score		: attr[3],
					  enrolled	: attr[4],
					  fee		: attr[5],
					  address	: attr[6],
					  remark	: attr[7],
					  url		: location.href};
		
		majors.push(major);
	}
	if(majors.length>0) {
		majors.sort(function(a, b){return a.score>b.score;});
		saveMajors(schoolCode, majors);
	}
}

var keyelement = $(".report1_2_1");
if(keyelement.length>0) {
	if(keyelement.html().indexOf("专业统计是")!=-1) {
		getMajors();
		if(location.search == "?chrome") {
			setTimeout(function(){window.close();}, 1000);		
		}
	}
}
