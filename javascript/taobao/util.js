
/*-------------------------- util functions begin -------------------------- */
function $(id) {
	return document.getElementById(id);
}

var ij = 1;
function log(str) {
	switch(LOG_CONSOLE) {
		case 0: document.title = "(" + (ij++) + ")" + str + " " + document.title;break;
		case 1: GM_log(str);break;
		case 2: console.log(str);break;
		default: alert(str);
	}
}

function getFormElementByName(name) {
	return document.evaluate("//input[@name = '"+name+"']", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;
}

/* add answer candidate link, when click this link, the answer will be set to the link's content */
function addChoice(obj, input, choice){
	var link = document.createElement("a");
	link.href = "javascript:var f=document.forms['mainform'];var ai=f['"+input+"'];var ci=f['J_checkCodeInput'];" +
		"if(ai.value=='"+choice+"'){" +
			"if(ci.value.length==4){f.submit();}else{ci.focus();}" + 
		"}else{ai.value='"+choice+"';}void(0);";
	link.setAttribute("style", "margin-right:1cm");
	link.innerHTML = choice;
	obj.appendChild(link);
}

function fT(date) {
	//return (date.getMonth()+1)+"/"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()+":"+date.getMilliseconds();
	//return date.getDate()+"日"+date.getHours()+"点"+date.getMinutes()+"分"+date.getSeconds()+"秒"+date.getMilliseconds()+"毫秒";
	return date.getHours()+"点"+date.getMinutes()+"分"+date.getSeconds()+"秒"+date.getMilliseconds()+"毫秒";
}

String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
}

function getQuotedString(str) {
	var lp = str.indexOf("“");
	var rp = str.indexOf("”");
	var word = str.substring(lp+1, rp);
	if(word) word = word.trim();
	return word;
}

function getNumRange(question) {
	var matches = null;
	if ((matches = /(\d)+[-到](\d+)/g.exec(question)) != null) {
		return {from : matches[1], to : matches[2]};
	}
	return null;
}

function getBracketString(str) {
	var lp = str.indexOf("（");
	var rp = str.indexOf("）");
	var word = str.substring(lp+1, rp);
	if(word) word = word.trim();
	return word;
}

function shuzi2hanzi(n) {
	var hanzi = "";
	switch(n) {
		case 0: hanzi = "零"; break;
		case 1: hanzi = "一"; break;
		case 2: hanzi = "二"; break;
		case 3: hanzi = "三"; break;
		case 4: hanzi = "四"; break;
		case 5: hanzi = "五"; break;
		case 6: hanzi = "六"; break;
		case 7: hanzi = "七"; break;
		case 8: hanzi = "八"; break;
		case 9: hanzi = "九"; break;
	}
	return hanzi;
}

function hanzi2shuzi(w) {
	var shuzi = -1;
	switch(w) {
		case "零":
		case "〇": shuzi = 0; break;
		case "一": shuzi = 1; break;
		case "二": 
		case "两": shuzi = 2; break;
		case "三": shuzi = 3; break;
		case "四": shuzi = 4; break;
		case "五": shuzi = 5; break;
		case "六": shuzi = 6; break;
		case "七": shuzi = 7; break;
		case "八": shuzi = 8; break;
		case "九": shuzi = 9; break;
	}
	return shuzi;
}

/*--------------------------  util functions end  -------------------------- */
