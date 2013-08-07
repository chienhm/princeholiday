/*****************************************************************************
 * Taobao Secretary Plugin for Google Chrome
 * Copyright 2012 Morntea.com, All Rights Reserved.
 * Author: chrome@morntea.com
 *****************************************************************************/
var b = chrome.extension.getBackgroundPage();

function save_options() {
	var config = b.getConfig();
	
	config.autoLogin = $("#cfg_autolog").attr("checked")=="checked";
	config.batDelete = $("#cfg_batdelete").attr("checked")=="checked";
	config.everydayCheck = $("#cfg_everyday").attr("checked")=="checked";
	
	var rate = config.rate;
	if(!rate) rate = {};
	rate.enable = $("#cfg_rate").attr("checked")=="checked";
	rate.batRate = $("#cfg_batRate").attr("checked")=="checked";
	rate.remark = $("#cfg_remark").val();
	rate.keyCode = 191;
	config.rate = rate;
	
	b.saveConfig(config);
	b.everydayCheck();
	
	var status = document.getElementById("status");
	status.innerHTML = "Options Saved.";
	setTimeout(function() {
		status.innerHTML = "";
	}, 1000);
}

function restore_options() {
	var config = b.getConfig();
	if(config!=null) {
		$("#cfg_autolog").attr("checked", config.autoLogin);
		$("#cfg_batdelete").attr("checked", config.batDelete);
		$("#cfg_everyday").attr("checked", config.everydayCheck || config.everydayCheck==null);
		var rate = config.rate;
		if(!rate)rate = {enable:true, batRate:false, keyCode:191};
		$("#cfg_rate").attr("checked", rate.enable);
		$("#cfg_batRate").attr("checked", rate.batRate);
		$("#cfg_remark").val(rate.remark);
	}
	var users = b.getUser();
	if(users) {
		$.each(users, function(n, p) {
			addUserItem(n);
		})
	}
}

/*****************************************************************************
 * User operation
 *****************************************************************************/
function findRow(user) {
	var tbody = $("#usertable tbody");
	var rows = $("tr", tbody)
						.filter(function(index){
							return $("td:nth-child(1)", $(this)).text()==user;
						});
	if(rows.length==0) {
		return null;
	} else {
		return rows;
	}
}

function addUserItem(user) {
	var tbody = $("#usertable tbody");
	var row = $("<tr><td>"+user+"</td><td>设为默认</td><td>修改</td><td>删除</td></tr>");
	var config = b.getConfig();
	if(config.defaultUser==user) {
		$("td:eq(1)", row).html("默认").css("cursor", "default")
	} else {
		setDefault($("td:eq(1)", row), user);
	}
	$("td:eq(2)", row).css("cursor", "pointer").bind("click", function(){
		$('#user').val(user);
	});
	$("td:eq(3)", row).css("cursor", "pointer").bind("click", function(){
		if(confirm("确定删除？")) {
			$(this).parent().remove();			
			b.delUser(user);
		}
	});
	row.appendTo(tbody);
}

function addUser() {
	var user = $('#user').val(); $('#user').val("");
	var pass = $('#pass').val(); $('#pass').val("");
	insertUser(user, pass);
}

function insertUser(user, pass) {
	if(user!="" && pass!="") {
		b.saveUser(user, encrypt(user, pass));
		var userRow = findRow(user);
		if(userRow) {
			//userRow.attr("pass", pass);
		} else {
			addUserItem(user);
		}
	}
}

function setDefault(td, user) {
	td.css("cursor", "pointer").html("设为默认").bind("click", function(){
		var config = b.getConfig();
		var defaultRow = findRow(config.defaultUser);
		if(defaultRow) {
			setDefault($("td:eq(1)", defaultRow), $("td:eq(0)", defaultRow).text());
		}
		td.html("默认").css("cursor", "default").unbind("click");
		config.defaultUser = user;
		b.saveConfig(config);
	});
}

function exportUser() {
	var users = b.getUser();
	if(users) {
		prompt("请复制并妥善保管以下用户数据：", Base64.encode(JSON.stringify(users)));
	} else {
		alert("没有保存的用户。");
	}
}

function importUser() {
	var str = prompt("请输入导出的用户数据（不会覆盖现有数据）：").trim();
	if(str=="") {
		return;
	}
	var json = null;
	try {
		json = JSON.parse(Base64.decode(str));
	} catch (e) {
		alert("数据错误！");
		return;
	}
	if(json!=null) {
		for(var key in json) {
			insertUser(key, json[key]);
		}
	}
}

/*****************************************************************************
 * Main
 *****************************************************************************/
function initTab(tab) {
	var CONTAINER = ".navlinks";
	var FOCUS_CLASS = "onpage";
	var items = [
		{name : "通用选项", tab : "general"},
		{name : "帐户设置", tab : "account"},
		{name : "领取金币", tab : "coin"},
		{name : "反馈", tab : "feedback", callback:loadFrame},
		{name : "广告", tab : "adv"}
	];
	$.each(items, function(i, e){
		var item = $("<a href='#"+e.tab+"'><span>"+e.name+"</span></a>");
		var target = $("#_"+e.tab);
		item.click(function(){
			$.each(items, function(i, e2){
				$("#_"+e2.tab).hide();
			});
			target.show("normal", e.callback);
			
			$(CONTAINER+" ."+FOCUS_CLASS).removeClass(FOCUS_CLASS);
			$(this).addClass(FOCUS_CLASS);
		});
		
		$(CONTAINER).append(item);
		target.hide();
		if(e.tab==tab) {
			item.addClass(FOCUS_CLASS);
			target.show("normal", e.callback);
		}
	});
}

function loadFrame() {
	$("#fbIfr").attr("src", "http://blog.morntea.com/lab/taobao-secretary/#ds-sync-checkbox");
}

$(function () {
	restore_options();
	$("#oSave").click(save_options);
	$("#add").click(addUser);
	$("#import").click(importUser);
	$("#export").click(exportUser);
	
	var tab = location.hash;
	if(tab){
		tab = tab.replace(/^#/, "");
	} else {
		tab = "general";
	}
	initTab(tab);
});

