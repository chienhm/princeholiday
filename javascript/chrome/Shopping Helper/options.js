var b = chrome.extension.getBackgroundPage();
var scheduleList = ["toCart", "toOrder", "order"];

function save_options() {
	b.options.limit = $("#limit").val();
	b.options.username = $("#username").val();
	b.options.password = $("#password").val();
	b.options.schedule.enabled = $("#schedule").attr("checked") ? true : false;
	$(scheduleList).each(function(i, e) {
		b.options.schedule[e] = getTime(e);
	});
	
	$("#status").html("Options Saved.");
	setTimeout(function() {
		$("#status").html("");
	}, 1000);
}

function restore_options() {
	$("#limit").val(b.options.limit);
	if(b.options.username!="") {
		$("#username").val(b.options.username);
		$("#password").val(b.options.password);
	}
	$("#schedule").attr("checked", b.options.schedule.enabled);
	showSchedule(b.options.schedule.enabled);
	
	$(scheduleList).each(function(i, e) {
		setTime(e, b.options.schedule[e]);
	});
}

function showSchedule(enabled) {
	if(enabled) {
		$("#scheduleContent").show();
	} else {
		$("#scheduleContent").hide();
	}
}

function initSchedule(className) {
	var i = 0;
	var handle = $("#scheduleContent ." + className);
	//console.log(handle);
	handle.append("<select name=\"hour\" class=\"hour\"></select>:<select name=\"minute\" class=\"minute\"></select>:<select name=\"second\" class=\"second\"></select> <input type=\"text\" name=\"millisecond\" class=\"millisecond\" value=\"0\"/>");

	var millisecond = $("#scheduleContent ." + className + " .millisecond");
	
	var hour = $("#scheduleContent ." + className + " .hour");
	for(i=0; i<24; i++) {
		hour.append("<option value=\"" + i + "\">" + i + "</option>");
	}
	
	var minute = $("#scheduleContent ." + className + " .minute");
	for(i=0; i<=59; i++) {
		minute.append("<option value=\"" + i + "\">" + i + "</option>");
	}
	
	var second = $("#scheduleContent ." + className + " .second");
	for(i=0; i<=59; i++) {
		second.append("<option value=\"" + i + "\">" + i + "</option>");
	}
}

function setTime(className, time) {
	var date = time ? new Date(time) : new Date();
	$("#scheduleContent ." + className + " .hour").val(date.getHours());
	$("#scheduleContent ." + className + " .minute").val(date.getMinutes());
	$("#scheduleContent ." + className + " .second").val(time ? date.getSeconds() : 0);
	$("#scheduleContent ." + className + " .millisecond").val(time ? date.getMilliseconds():0);
}

function getTime(className) {
	var date = new Date();
	date.setHours($("#scheduleContent ." + className + " .hour").val());
	date.setMinutes($("#scheduleContent ." + className + " .minute").val());
	date.setSeconds($("#scheduleContent ." + className + " .second").val());
	date.setMilliseconds($("#scheduleContent ." + className + " .millisecond").val());
	return date.getTime();
}

document.addEventListener('DOMContentLoaded', function () {
	$(scheduleList).each(function(i, e) {
		initSchedule(e);
	});
	
	restore_options();	
		
	document.querySelector('button').addEventListener('click', save_options);
	$("#schedule").bind('change', function(){
		showSchedule(this.checked);
	});
});