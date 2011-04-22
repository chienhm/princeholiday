var SEARCH_SERVER = "http://127.0.0.1:8080/e.morntea.com/"; //http://e.morntea.com/

function getHint(question) {
	var hint = null;
	var pl = question.lastIndexOf("（");
	var pr = question.lastIndexOf("）");	//parenthesis
	if(pl!=-1 && pr==question.length-1) {
		hint = question.substring(pl+1, question.lastIndexOf("）"));
	}
	return hint;
}

function getGivenAnswer(question) {
	var answer = null;
	var hint = getHint(question);
	if(hint!=null) {
		if(hint.indexOf("个")==-1 && hint.indexOf("字")==-1 && hint.indexOf("格式")==-1 && hint.indexOf("如")==-1){
			answer = hint.replace("答案是","").replace("答案","").replace("：","").replace(":","");
		}
	}
	return answer;
}

function searchAnswer(question, callback) {
	GM_xmlhttpRequest({
		method: "GET",
		url: SEARCH_SERVER + "search.jsp?onlyanswer&q="+question,
		onload: function(response) {
			callback(response.responseText);
		}
	});
}

function getLostWord(question, callback) {
	var lp = question.indexOf("：");
	var word = question.substring(lp+1).trim();
	GM_xmlhttpRequest({
		method: "GET",
		url: "http://www.baidu.com/s?wd="+word,//baidu version
		onload: function(response) {
			var pattern = new RegExp("<th><a href=[^>]+>" + word.replace("_", "(.)") +"<\/a>");
			console.log(word);
			console.log(pattern);
			var html = response.responseText;
			//console.log(html);
			var matches = null, found = false;
			if ((matches = pattern.exec(html)) != null) {
				callback(matches[1]);
				found = true;
			}
			if(!found) {
				pattern = new RegExp("<th><a href=[^>]+>.*?" + word.replace("_", "(.)") +".*?<\/a>");
				console.log(pattern);
				if ((matches = pattern.exec(html)) != null) {
					callback(matches[1]);
					found = true;
				}
			}
		}
	});
}

function getCity(question) {
	var provCapi = [];
	provCapi["北京"] = "北京";
	provCapi["上海"] = "上海";
	provCapi["天津"] = "天津";
	provCapi["重庆"] = "重庆";
	provCapi["黑龙江"] = "哈尔滨";
	provCapi["吉林"] = "长春";
	provCapi["辽宁"] = "沈阳";
	provCapi["内蒙古"] = "呼和浩特";
	provCapi["河北"] = "石家庄";
	provCapi["新疆"] = "乌鲁木齐";
	provCapi["甘肃"] = "兰州";
	provCapi["青海"] = "西宁";
	provCapi["陕西"] = "西安";
	provCapi["宁夏"] = "银川";
	provCapi["河南"] = "郑州";
	provCapi["山东"] = "济南";
	provCapi["山西"] = "太原";
	provCapi["安徽"] = "合肥";
	provCapi["湖北"] = "武汉";
	provCapi["湖南"] = "长沙";
	provCapi["江苏"] = "南京";
	provCapi["四川"] = "成都";
	provCapi["贵州"] = "贵阳";
	provCapi["云南"] = "昆明";
	provCapi["广西"] = "南宁";
	provCapi["西藏"] = "拉萨";
	provCapi["浙江"] = "杭州";
	provCapi["江西"] = "南昌";
	provCapi["广东"] = "广州";
	provCapi["福建"] = "福州";
	provCapi["台湾"] = "台北";
	provCapi["海南"] = "海口";
	provCapi["香港"] = "香港";
	provCapi["澳门"] = "澳门";
	var provAbbr = [];
	provAbbr["北京"] = "京";
	provAbbr["上海"] = "沪";
	provAbbr["天津"] = "津";
	provAbbr["重庆"] = "渝";
	provAbbr["黑龙江"] = "黑";
	provAbbr["吉林"] = "吉";
	provAbbr["辽宁"] = "辽";
	provAbbr["内蒙古"] = "蒙";
	provAbbr["河北"] = "冀";
	provAbbr["新疆"] = "新";
	provAbbr["甘肃"] = "甘";
	provAbbr["青海"] = "青";
	provAbbr["陕西"] = "陕";
	provAbbr["宁夏"] = "宁";
	provAbbr["河南"] = "豫";
	provAbbr["山东"] = "鲁";
	provAbbr["山西"] = "晋";
	provAbbr["安徽"] = "皖";
	provAbbr["湖北"] = "鄂";
	provAbbr["湖南"] = "湘";
	provAbbr["江苏"] = "苏";
	provAbbr["四川"] = "川";
	provAbbr["贵州"] = "黔";
	provAbbr["云南"] = "滇";
	provAbbr["广西"] = "桂";
	provAbbr["西藏"] = "藏";
	provAbbr["浙江"] = "浙";
	provAbbr["江西"] = "赣";
	provAbbr["广东"] = "粤";
	provAbbr["福建"] = "闽";
	provAbbr["台湾"] = "台";
	provAbbr["海南"] = "琼";
	provAbbr["香港"] = "港";
	provAbbr["澳门"] = "澳";
	var provCity = [
		["北京", "京", "北京"],
		["上海", "沪", "上海"],
		["天津", "津", "天津"],
		["重庆", "渝", "重庆"],
		["黑龙江", "黑", "哈尔滨"],
		["吉林", "吉", "长春"],
		["辽宁", "辽", "沈阳"],
		["内蒙古", "蒙", "呼和浩特"],
		["河北", "冀", "石家庄"],
		["新疆", "新", "乌鲁木齐"],
		["甘肃", "甘", "兰州"],
		["青海", "青", "西宁"],
		["陕西", "陕", "西安"],
		["宁夏", "宁", "银川"],
		["河南", "豫", "郑州"],
		["山东", "鲁", "济南"],
		["山西", "晋", "太原"],
		["安徽", "皖", "合肥"],
		["湖北", "鄂", "武汉"],
		["湖南", "湘", "长沙"],
		["江苏", "苏", "南京"],
		["四川", "川", "成都"],
		["贵州", "黔", "贵阳"],
		["云南", "滇", "昆明"],
		["广西", "桂", "南宁"],
		["西藏", "藏", "拉萨"],
		["浙江", "浙", "杭州"],
		["江西", "赣", "南昌"],
		["广东", "粤", "广州"],
		["福建", "闽", "福州"],
		["台湾", "台", "台北"],
		["海南", "琼", "海口"],
		["香港", "港", "香港"],
		["澳门", "澳", "澳门"] 
	];
	var answer = null;
	if(question.indexOf("省会") !=-1 ) {
		for(var i=0; i<provCity.length; i++) {
			if(question.indexOf(provCity[i][0]) != -1) {
				answer = provCity[i][2];
				break;
			}
			if(question.indexOf(provCity[i][2]) != -1) {
				answer = provCity[i][0];
				break;
			}
		}
	} else if(question.indexOf("简称") !=-1 || question.indexOf("缩写") !=-1) {
		for(var i=0; i<provCity.length; i++) {
			if(question.indexOf(provCity[i][0]) != -1) {
				answer = provCity[i][1];
				break;
			}
			if(question.indexOf(provCity[i][1]) != -1) {
				answer = provCity[i][0];
				break;
			}
		}
	}
	return answer;
}