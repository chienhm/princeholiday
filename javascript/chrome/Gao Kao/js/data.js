var rows = $("#report1 tr");
var start = false;
var schools = {};
for(var i=0; i<rows.length; i++) {
	if($(rows[i]).text().indexOf("返回上一页")!=-1){break;}
	if($(rows[i]).text().indexOf("院校代号")!=-1){start = true;continue;}
	if(!start)continue;
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
				  major		: null};
	
	getMajorDelay(school, url, i*500);
	schools[school.name] = school;
}
console.log(schools);

function getMajorDelay(school, url, delay) {
	setTimeout(function(){getMajor(school, url);}, delay);
}

function getMajor(school, url) {
	console.log(url);
	$.ajax({"url": url, 
		dataType: "text/html", 
		cache	: false,
		headers	: {
            "Accept" 		: "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
            "Cache-Control"	: "max-age=0",
            "Content-Type"	: "text/html; charset=GB2312"
        }, 
		complete: function(jqXHR){
			var doc = $("<body/>");
			try{doc.html(jqXHR.responseText);}catch(e){console.error(e.message);}
			var rows = $("#report1 tr", doc);
			var start = false;
			var majors = [];
			for(var i=0; i<rows.length; i++) {
				if($(rows[i]).text().indexOf("统计时间")!=-1){break;}
				if($(rows[i]).text().indexOf("专业代号")!=-1){start = true;continue;}
				if(!start)continue;
				var cols = $("td", rows[i]);
				var attr = [];
				for(var j=0; j<cols.length; j++) {
					attr[j] = $(cols[j]).text();
				}
				majors.push(attr);
			}
			school.major = majors;
		}
	});
}