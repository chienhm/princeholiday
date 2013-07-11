var rows = $("#report1 tr");
var start = false;
var majors = [];
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
majors.sort(function(a, b){return a.score>b.score;});