var b = chrome.extension.getBackgroundPage();
var email = null;
var paths = null;
var stuck = [];
var files = [];

function loadData() {
	email = $("#email").val();
	if($("#email").val()!="") {
		paths = b.getLocalObj(email);
	} else if(b.env!=null) {
		email = b.env.email;
		paths = b.paths;
	}
}

function filterFiles() {
	var fileName = null;
	for(var key in paths) {
		fileName = key.substring(key.lastIndexOf("/")+1);
		if($.inArray(fileName.toLowerCase(), files)==-1) {
			console.log(key);
		} else {
			delete paths[key];
		}
	}
	listFiles();
}

function listFiles() {
	if(email!="") {
		$("#user").html(email);
		
		var dom = $("#content ul");
		dom.empty();
		var i = 0;
		for(var key in paths) {
			dom.append("<li>["+(i++)+"] <a href=\""+paths[key]+"\">"+key+"</a></li>");
		}
	}
}

var index = -1; //150
var timeHdl = -1;
var stop = false;

function saveFailure(itemIdx) {
	stuck.push(itemIdx);
	localStorage["stuck"] = JSON.stringify(stuck);
}

function openAll() {
	var links = $("#content ul li a");

	download();
	
	function download() {
		if(stop)return;
		index++;
		if(index>=links.length)return;
		
		var link = $(links[index]).attr("href");
		//console.log("Download " + link);
		//window.open(link);
		chrome.tabs.create({"url" : link}, function(tab) {
			tabListener(tab, index);
		});
	}
	
	function tabListener(tab, curIdx) {
		/* monitor timeout (3 min) */
		timeHdl = setTimeout(detectDownload(tab.id, curIdx), 3*60*1000);
		
		/* file download starts */
		chrome.tabs.onRemoved.addListener(tabClosed(curIdx));
		
		function tabClosed(itemIdx) {
			return function(tabId, removeInfo) {
				if(tab.id==tabId) {
					var i = $.inArray(itemIdx, stuck);
					console.log("["+itemIdx+"]"+tab.url + " starts, try next.");
					if(i!=-1) {
						console.log("["+itemIdx+"] download too late, remove from stuck list.");
						stuck.splice(i, 1);
					} else {
						download();
					}
				}
			}
		}
		
		/* enclosure function for monitor */
		function detectDownload(tabId, itemIdx) {
			return function() {
				chrome.tabs.get(tabId, function (t){
					if(!!t == true) { /* window not closed */
						console.log("["+itemIdx+"]" + " download stuck.");
						saveFailure(itemIdx);
						download();
					}
				});
			}
		}
	}
}

function init() {
	for(var key in b.localStorage) {
		$("#email").append("<option value=\""+key+"\">"+key+"</option>");
	}
}

function showFiles() {
	loadData();
	listFiles();
}

$(function() {
	init();
	showFiles();
	
	$("#persist").click(function() {
		b.persist();
	});
	
	$("#open").click(function() {
		if(confirm("Yes or No?")) {
			openAll();
		}
	});
	
	$("#email").change(showFiles);
});

/*
var nodes = document.getElementsByClassName("download");
for(var i=0; i<nodes.length; i++){
	if(nodes[i].children[1].style.display=="none") {
		console.log(nodes[i].children[2].children[0]);
		nodes[i].children[2].children[1].click();
	}
}

var controls = document.getElementsByClassName("controls");
for(var i=0; i<controls.length; i++){
	var nodes = controls[i].childNodes;
	for(var j=0; j<nodes.length; j++){
		if(nodes[j].style.display=="inline" && nodes[j].innerText=="Remove from list") {
			nodes[j].click();
		}
	}
}
*/