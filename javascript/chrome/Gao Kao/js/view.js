var b = chrome.extension.getBackgroundPage();

function loadSchools() {
	if(localStorage["schools"]) {
		return JSON.parse(localStorage["schools"]);
	}
	return {};
}

function loadExpected() {
	if(localStorage["expected"]) {
		return JSON.parse(localStorage["expected"]);
	}
	return {};
}

function loadOrders() {
	if(localStorage["order"]) {
		return JSON.parse(localStorage["order"]);
	}
	return {};
}

function loadMajors(name) {
	if(localStorage[name]) {
		return JSON.parse(localStorage[name]);
	}
	return {};
}

function persist() {
	var schools = loadSchools();
	var schoolSelected = {};
	
	for(var name in schools){
		var school = schools[name];
		var majors = loadMajors(name);
		
		var schoolContainer = $("#"+school.code);
		/* This school not found, go to next */
		if(schoolContainer.length==0) continue;
		
		var selected = $("#"+school.code+" input[name=school]").attr("checked")=="checked";
		/* This school not selected, go to next */
		if(!selected) continue;
		
		var majorSelected = {};
		for(var i=0; i<majors.length; i++) {
			var major = majors[i];
			var majorContainer = $("#"+school.code+"-"+major.code);
			/* This major not found, go to next */
			if(majorContainer.length==0)continue;
			
			var checked = $("#"+school.code+"-"+major.code+" input[name=major]").attr("checked")=="checked";
			var order = $("#"+school.code+"-"+major.code+" input[name=order]").val();
			/* This major selected, added */
			if(checked) {
				majorSelected[major.code] = order;
			}
		}
		schoolSelected[school.code] = majorSelected;
	}
	localStorage["expected"] = JSON.stringify(schoolSelected);
	console.log(schoolSelected);
}

function isExpected(expects, majorCode) {
	if(!expects)return false;
	for(var i=0; i<expects.length; i++) {
		if(majorCode==expects[i].code) {
			return true;
		}
	}
	return false;
}

function init() {
	var schools = loadSchools();
	var expected = loadExpected();
	var savedOrders = loadOrders();
	
	var index = 1;
	for(var name in schools){
		var school = schools[name];
		var majors = loadMajors(name);
		var expectSchool = expected[school.code];
		var savedOrder = savedOrders[school.code];
		
		var html = "<li id='"+school.code+"' style='color: red;'>["+(index++)+"]";
		html += "<a href='"+(school.url?"http://www.nm.zsks.cn"+school.url.replace("?chrome", ""):"")+"' target='_blank'>"+name+"</a>";
		html += "[已报"+school.realnum+"/计划"+school.expected+"/最低分"+school.score+"]";
		html += "<input type='checkbox' name='school' value='"+school.code+"' "+(expectSchool?"checked":"")+" />";
		html += "<input type='button' name='fill' value='"+school.code+"' />";
		html += savedOrder?("[当前排名：<strong>" + savedOrder.paihang + "</strong>"+(savedOrder.pingxing?"(平行分人数："+savedOrder.pingxing+")":"") + "]["+savedOrder.time+"]") : "";
		html += "</li>";
		var list = "";
		for(var i=0; i<majors.length; i++) {
			var major = majors[i];
			var order = "";
			var checked = "";
			if(expectSchool && expectSchool[major.code]) {
				checked = "checked";
				order = expectSchool[major.code];
			}
			
			list += "<li id='"+school.code+"-"+major.code+"' >("+major.code+")"+major.name+"["+major.enrolled+"/"+major.plan+"/"+major.score+"]<input type='checkbox' name='major' value='"+major.code+"' "+checked+" /><input type='text' name='order' value='"+order+"' /></li>";
		}
		if(list!="") {
			list = "<ul id='"+school.code+"-major'>"+list+"</ul>"
		}
		$("#schools").append(html + list);
	}
}

function initEvent() {
	var buttons = $("input[name=fill]");
	for(var i=0; i<buttons.length; i++) {
		var button = $(buttons[i]);
		var schoolCode = button.val();
		button.click(function(code) {
			return function(){fillSchool(code);}
		}(schoolCode));
	}
}

function fillSchool(code) {
	console.log("填报" + code);
	var school = {"code":code, majors:[]};
	var majorsContainer = $("#"+code+"-major > li");
	for(var i=0; i<majorsContainer.length; i++) {
		var li = $(majorsContainer[i]);
		var majorCheckbox = $("input[name=major]", li);
		var selected = majorCheckbox.attr("checked")=="checked";
		if(!selected)continue;
		var order = $("input[name=order]", li);
		school.majors.push({"code":majorCheckbox.val(), "order":order.val()});
	}
	school.majors.sort(function(a,b){return a.order>b.order;});
	console.log(school);
	/* 暂存并开启自动 */
	b.option.auto = true;
	b.option.fillSchool = school;
	chrome.tabs.query({currentWindow: true, url:"http://www1.nm.zsks.cn/kscx/*"}, function (tabs) {
		//console.log(tabs);
		chrome.tabs.sendRequest(tabs[0].id, {"cmd": "FILL", "school":school});
	});
}

function showMajor(show) {
	var schools = loadSchools();
	for(var name in schools){
		var school = schools[name];
		var code = school.code;
		if($("#"+code).is(":visible")) {
			if(show) {
				$("#"+code+"-major").show();
			} else {
				$("#"+code+"-major").hide();
			}
		}
	}
}
$(function() {
	init();
	initEvent();
	$("#persist").click(function(){
		persist();
	});
	$("#all").click(function(){
		$("ul").show();
		$("li").show();
	});
	$("#major_show").click(function(){
		showMajor(true);
	});
	$("#major_hide").click(function(){
		showMajor(false);
	});
	$("#stop").click(function(){
		b.option.auto = false;
	});
	$("#start").click(function(){
		b.option.auto = true;
	});
	
	$("#part").click(function(){
		var lists = $("#schools>li");
		for(var i=0; i<lists.length; i++) {
			var list = $(lists[i]);
			if($("input[type=checkbox]", list).attr("checked")!="checked") {
				var id = list.attr("id");
				list.hide();
				$("#"+id+"-major").hide();
			}
		}
	});
});