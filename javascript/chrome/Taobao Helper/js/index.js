var b = chrome.extension.getBackgroundPage();

function save_options() {
	var config = b.getConfig();
	
	config.autoLogin = $("#autolog").attr("checked")=="checked";
	config.batDelete = $("#batdelete").attr("checked")=="checked";
	
	b.saveConfig(config);
	
	var status = document.getElementById("status");
	status.innerHTML = "Options Saved.";
	setTimeout(function() {
		status.innerHTML = "";
	}, 1000);
}

function restore_options() {
	var config = b.getConfig();
	if(config!=null) {
		$("#autolog").attr("checked", config.autoLogin);
		$("#batdelete").attr("checked", config.batDelete);
	}
	var users = b.getUser();
	if(users) {
		$.each(users, function(n, p) {
			addUserItem(n);
		})
	}
}

/*---------------------------------------------------------------------------
 * account
 *---------------------------------------------------------------------------*/
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

/*---------------------------------------------------------------------------
 * Main
 *---------------------------------------------------------------------------*/
function initTab(tab) {
	var CONTAINER = ".navlinks";
	var FOCUS_CLASS = "onpage";
	var items = [
		{name : "通用选项", tab : "general"},
		{name : "帐户设置", tab : "account"},
		{name : "领取金币", tab : "coin"},
		{name : "更新日志", tab : "history"},
		{name : "反馈", tab : "feedback"},
		{name : "广告", tab : "adv"}
	];
	$.each(items, function(i, e){
		var item = $("<a href='#"+e.tab+"'><span>"+e.name+"</span></a>");
		var target = $("#_"+e.tab);
		item.click(function(){
			$.each(items, function(i, e2){
				$("#_"+e2.tab).hide();
			});
			target.show();
			$(CONTAINER+" ."+FOCUS_CLASS).removeClass(FOCUS_CLASS);
			$(this).addClass(FOCUS_CLASS);
		});
		
		$(CONTAINER).append(item);
		target.hide();
		if(e.tab==tab) {
			item.addClass(FOCUS_CLASS);
			target.show();
		}
	});
}

$(function () {
	restore_options();
	$("#oSave").click(save_options);
	$("#add").click(addUser);
	
	var tab = location.hash;
	if(tab){
		tab = tab.replace(/^#/, "");
	} else {
		tab = "general";
	}
	initTab(tab);
});

