function initTab() {
	var items = [
		{name : "Character Inspect", target : "character"},
		{name : "Unicode Decoder", target : "unicode"},
		{name : "Encrypt", target : "encrypt"},
		{name : "Feedback", target : "feedback"}
	];
	$.each(items, function(i, e){
		var item = $("<a href='#'><span>"+e.name+"</span></a>");
		item.click(function(){
			$.each(items, function(i, e2){
				$("#"+e2.target).hide();
			});
			$("#"+e.target).show();
			$(".navItems .onpage").removeAttr("class");
			$(this).attr("class", "onpage");
		});
		
		$(".navItems").append(item);
		$("#"+e.target).hide();
		if(i==1) {		
			item.attr("class", "onpage");
			$("#"+e.target).show();
		}
	});
}

function initDecoder() {
	var title = ["Unicode Encode", "Unicode Decode", "Unicode HTML Encode", "Unicode HTML Decode", "escape", "unescape", "encodeURI", "decodeURI", "encodeURIComponent", "decodeURIComponent"];
	$.each(title, function(i, e) {
		$("#option").append("<input type=\"checkbox\" name=\"option\" value=\""+i+"\" checked=\"checked\"/> " + title[i] + " ");
		$("#result").append("<h3 id=\"t"+i+"\">"+title[i]+"</h3>");
		$("#result").append("<textarea id=\"r"+i+"\" class=\"code result\"></textarea>");
	});
	$("#unicode_source").focus();
	$("#unicode_source").bind("keyup change", unicode);
	$("#option :checkbox").click(show);
	$("#all").click(function(){
		$("#option :checkbox").attr("checked", this.checked);
		show();
	});
}

function show() {
	$.each($("#option :checkbox"), function(i, e){
		if($(e).attr("checked") && $("#unicode_source").val()) {
			$("#t"+$(e).val()).show();
			$("#r"+$(e).val()).show();
		} else {
			$("#t"+$(e).val()).hide();
			$("#r"+$(e).val()).hide();
		}
	})
}

function unicode() {
	var src = $("#unicode_source").val();
	if(src) {
		var n = 0;
		var dst = "";
		var dst1 = "";
		for(var i=0; i<src.length; i++) {
			dst += "\\u" + src.charCodeAt(i).toString(16).toUpperCase();
			dst1 += "&#" + src.charCodeAt(i) + ";";
		}
		$("#r"+(n++)).text(dst);
		
		dst = src.replace(/\\u([\da-f]+)/ig, function($0, $1) {return String.fromCharCode(parseInt($1, 16));});
		$("#r"+(n++)).text(dst);
		
		$("#r"+(n++)).text(dst1);
		
		dst = src.replace(/&#(\d+);/ig, function($0, $1) {return String.fromCharCode(parseInt($1));});
		$("#r"+(n++)).text(dst);
		
		$("#r"+(n++)).text(escape(src));
		
		$("#r"+(n++)).text(unescape(src));
		
		$("#r"+(n++)).text(encodeURI(src));
		
		try {
			$("#r"+n).text(decodeURI(src));
		} catch (e) {
			$("#r"+n).text("Error: " + e.message);
		}
		n++;
		
		$("#r"+(n++)).text(encodeURIComponent(src));
		
		try {
			$("#r"+n).text(decodeURIComponent(src));
		} catch (e) {
			$("#r"+n).text("Error: " + e.message);
		}
		n++;
	}
	show();		
}

$(function()	{
	initTab();
	initDecoder();
});