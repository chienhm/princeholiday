var b = chrome.extension.getBackgroundPage();

var menus = [
	{id:"copy", name:"网页解禁", click:copyClick, eng:false}, 
	{id:"image", name:"提炼图片", click:imageClick, eng:false}, 
	{id:"frame", name:"肢解网页", click:frameClick, eng:false},  
	{id:"https", name:"编辑网页", click:editWeb, eng:false},  
	{id:"https", name:"安全加载", click:httpsClick, eng:true}, 
	{id:"speed", name:"网速测试", click:speedClick, eng:true}
];

function execJs(js) {
	chrome.tabs.executeScript({"allFrames" : true, "file" : js});
	window.close();
}

function execJsWorkAround(js) {
	chrome.tabs.update({"url" : "javascript:"+js});
}

function initMenu() {
	var showEng = false;
	var engModeEnabled = b.getConfig("engmode");
	if(engModeEnabled=="true") {
		showEng = true;
	}
	$.each(menus, function(i, e) {
		if(!e.eng || showEng){		
			var li = $("<div class='item' id='"+e.id+"'>"+e.name+"</div>");
			li.click(e.click);
			$("#menu").append(li);
		}
	});
}

function copyClick(){
	execJs("js/inject/copy.js");
	b.showMsg("恭喜您，可以选择、复制、点击右键啦！");
};

function imageClick(){
	focusOrCreateTab("pic.html", function(tab) {
		execJs("js/inject/image.js");
		chrome.tabs.update(tab.id, {
			"selected" : true
		});
	});
};

function frameClick(){
	execJs("js/inject/frame.js");
}

function editWeb(){
	execJs("js/inject/edit.js");
}

function speedClick(){
	chrome.tabs.executeScript({"file" : "js/inject/speed.js"});
}

function httpsClick(){
	chrome.tabs.executeScript({"runAt" : "document_start", "file" : "js/inject/https.js"});
}

$(function() {
	initMenu();
});