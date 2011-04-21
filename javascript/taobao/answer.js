var SEARCH_SERVER = "http://127.0.0.1:8080/e.morntea.com/"; //http://e.morntea.com/

function getHint(question) {
	var hint = null;
	var pos = question.lastIndexOf("（");
	if(pos!=-1) {
		hint = question.substring(pos+1, question.lastIndexOf("）"));
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
