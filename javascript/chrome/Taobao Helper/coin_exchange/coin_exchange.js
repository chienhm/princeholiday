var b = chrome.extension.getBackgroundPage();
var index = -1;
var interval = 0;
var template = "";
var running = false;
var lastId = null;
var categories = [
	{id:"11302000000", name:"连衣裙"},
	{id:"11301000000", name:"休闲女裤"},
	{id:"11303000000", name:"半身裙"},
	{id:"11304000000", name:"牛仔裤"},
	{id:"10102000000", name:"长袖T恤"},
	{id:"10101000000", name:"毛衣针织"},
	{id:"10104000000", name:"风衣皮衣"},
	{id:"10103000000", name:"卫衣外套"},
	{id:"10301000000", name:"魅力女鞋"},
	{id:"10302000000", name:"时尚箱包"},
	{id:"10304000000", name:"休闲男鞋"},
	{id:"10303000000", name:"男女配饰"},
	{id:"10201000000", name:"夏秋上装"},
	{id:"10204000000", name:"夏秋裤装"},
	{id:"10203000000", name:"毛衣针织"},
	{id:"10202000000", name:"夹克卫衣"},
	{id:"11102000000", name:"时尚文胸"},
	{id:"11101000000", name:"家居睡衣"},
	{id:"11103000000", name:"袜子内裤"},
	{id:"10601000000", name:"童装孕装"},
	{id:"10602000000", name:"玩具早教"},
	{id:"10603000000", name:"母婴用品"},
	{id:"10604000000", name:"童鞋配饰"},
	{id:"10402000000", name:"床上用品"},
	{id:"10401000000", name:"家装厨卫"},
	{id:"10403000000", name:"居家百货"},
	{id:"10404000000", name:"游泳户外"},
	{id:"10701000000", name:"美容护理"},
	{id:"10703000000", name:"零食特产"},
	{id:"10702000000", name:"滋补保健"},
	{id:"10501000000", name:"家用电器"},
	{id:"10502000000", name:"数码配件"},
	{id:"10903000000", name:"销量千件"},
	{id:"10901000000", name:"10元特价"},
	{id:"10902000000", name:"3折封顶"}
];

function findLuck() {
	while(true) {
		index++;
		if(index==categories.length) {
			index = 0;
		}
		if(categories[index].skip) continue;
		break;
	}
	if(lastId) $("#"+lastId).css("background-color", "#F1F1F1");
	lastId = categories[index].id;
	$("#"+categories[index].id).css("background-color", "lightgreen");
	$("#result").html("");
	getPage(categories[index]);
}

function loopAll() {
	interval = setInterval(findLuck, 2000, null);
}

function getPage(category) {
	$.get("http://taojinbi.taobao.com/home/category_search_home.htm?page=1&order=1&isAsc=1&category_id="+category.id+"&tracelog=qzexcoin&discountPriceMin=&discountPriceMax=&isExchangeCoin=yes&exchangeCoinMin=&exchangeCoinMax=", function(html) {
		var items = parseHtml(html);
		updateArray(items);
	});
}

function getId(url) {
	var s = url.indexOf("id=");
	if(s!=-1) {
		s += 3;
		var e = url.indexOf("&", s);
		if(e==-1) {
			return url.substring(s);
		} else {
			return url.substring(s, e);
		}
	}
	return null;
}
/* http://regexpal.com/ http://gskinner.com/RegExr/ */
var itemRegExp = /<a.*?href="(.+?)" target="_blank">[^\w]+<img .+? src="(.+?)" \/>[^\w]+<p class="title" title=".+?">(.+?)<\/p>[^\w]+<p class="price"><del>(.+?)<\/del><span class="discount">\/.+?折<\/span> <span class="salescount">已成交(.+)件<\/span> <\/p>[^\w]+<p class="price qz-price"> <em>(.+?)<\/em>+(.+?)<span class="coin">淘金币<\/span>\s*<span class="favorable">(.+?)<\/span>\s*<\/p>[^\w]+<p class="modes">[^\w]+<span data-tip="可用金币兑换" class="mode4 J_Mode">\((.+)淘金币\)/ig;

function parseHtml(html) {
	var items = new Array();
	if(html.indexOf("没有找到")!=-1) { //html.indexOf("listmodbox")==-1
		$("#result").html("没有找到宝贝！");
	} else {
		var count = html.split("=\"可用金币兑换\"").length-1;
		//console.log(count);
		var i = 0;
		var result = null;
		while ((result = itemRegExp.exec(html)) != null)  {
			i++;
			var item = {url:result[1], image:result[2], name:result[3], price:result[4], sales:result[5], nowPrice:result[6], nowCoin:result[7], ship:result[8], coin:result[9]};
			item.id = getId(item.url);
			items.push(item);
			if(item.id=="") {
				console.error(item.name + " gets no item id.");
			}
			console.log("[" + categories[index].name + "][" + item.coin + "金币]" + item.name);
		}
		if(i!=count) { //sanity check
			console.error(count + " items found, but only " + i + " items parsed.");
		}
		//console.log(items);
	}
	return items;
}

function updateArray(items) {
	if(categories[index].items) {
		var found = false;
		var len = categories[index].items.length;
		for(var i=0; i<len; i++) {
			found = false;
			for(var j=0; j<items.length; j++) {
				if(categories[index].items[i].id == items[j].id){
					found = true;
					break;
				}
			}
			if(found) {
				continue;
			} else { //move item from list to history
				moveItem("history", categories[index].items[i]);
				categories[index].items.splice(i, 1);
				i--;
				len--;
			}
		}
		for(var j=0; j<items.length; j++) {
			found = false;
			for(var i=0; i<len; i++) {
				if(categories[index].items[i].id == items[j].id){
					found = true;
					break;
				}
			}
			if(!found) {
				categories[index].items.push(items[j]);
				addItem("list", items[j]);
			}
		}
	} else {
		categories[index].items = items;
		for(var j=0; j<items.length; j++) {
			addItem("list", items[j]);
		}
	}
}

//************************************************************************************************
// UI related
function addItem(area, item) {
	var html = template.replace(/\$\{(.+?)\}/ig, function(match, result){return item[result];});
	var config = b.getConfig("exchange");
	if(!config.min) config.min = -1;
	if(!config.max) config.max = -1;
	if(item.coin<config.min || (config.max!=-1&&item.coin>config.max)) {
		$(html).hide().prependTo("#"+area);
	} else {
		$(html).prependTo("#"+area);
		if(config.message) {
			showNote(item);
		}
	}
}

function moveItem(area, item) {	
	console.log("Remove " + item.name);
	$("#"+area).prepend($("#"+item.id).detach());
}

function removeItem(item) {
	console.log("Remove " + item.name);
	$("#"+item.id).remove();
}

function showNote(item) {
	var notification = webkitNotifications.createNotification(
		item.image,
		item.coin + "淘金币(" + item.ship + ")",
		item.name
	);
	notification.show();
	setTimeout(function(){notification.cancel();}, 10000);
}

function start() {
	if(running) {
		clearInterval(interval);
		$("#start").val("开始查找");
		$("#result").html("");
		running = false;
	} else {
		loopAll();
		$("#start").val("停止查找");
		running = true;
	}
}

/*************************************************************************************************
 * category init, update to latest version. If retrieving failed, use default data.
 */
function initCategory() {
	var savedCategory = b.getConfig("category");
	if(savedCategory.length) {
		categories = savedCategory;
	} else {
		console.log("first time to save category.");
		b.saveConfig(categories, "category");
	}
	showCategory();
	/*
	$.get("http://taojinbi.taobao.com/home/award_exchange_home.htm", function(html){
		var reg=/<span>\s*?<a href=".+?category_id=(\d+)"\s*?>(.+?)<\/a>\s*?<\/span>/ig;
		var result = null;
		var _categories = new Array();
		while((result = reg.exec(html)) != null) {
			_categories.push({id:result[1], name:result[2]});
			console.log("	{id:\""+result[1]+"\", name:\""+result[2]+"\"},");
		}
		if(_categories.length>0) {
			categories = _categories;
		} else {
			console.error("Failed to get categories.");
		}
	});*/
	$.each(categories, function(i, e){
		e.items = null;
	});
}

function skip(id, bool) {
	for(var i=0; i<categories.length; i++){
		if(categories[i].id==id) {
			categories[i].skip = bool;
			$("#"+id).css("background-color", bool?"white":"#F1F1F1");
			b.saveConfig(categories, "category");
			break;
		}
	}
}

function showCategory() {
	$.each(categories, function(i, e){
		var checkbox = $("<input type='checkbox' name='cat' value='"+e.id+"' "+(e.skip?"":"checked='checked'")+">" + e.name + "</input>");
		checkbox.click(function(){skip(e.id, checkbox.attr("checked")!="checked");});
		var box = $("<div class='category' id='"+e.id+"'></div>");
		if(e.skip) box.css("background-color", "white");
		box.append(checkbox);
		$("#cat").append(box);
	});
}

function range() {
	var min = -1;
	var max = -1;
	var _min = $("#min").val();
	var _max = $("#max").val();
	if(_min!="") {
		min = parseInt(_min);
	}
	if(_max!="") {
		max = parseInt(_max);
	}
	var config = b.getConfig("exchange");
	config.min = (max==-1)?min:(min<max?min:max);
	config.max = (min==-1)?max:((max==-1)?-1:(min<max?max:min));
	b.saveConfig(config, "exchange");	
	
	$.each(categories, function(i, e){
		if(e.items) {
			$.each(e.items, function(index, item) {
				if(item.coin<config.min || (config.max!=-1&&item.coin>config.max)) {
					$("#"+item.id).hide();
				} else {
					$("#"+item.id).show();
				}
			});
		}
	});
}

function message() {
	var config = b.getConfig("exchange");
	config.message = $("#message").attr("checked")=="checked";
	b.saveConfig(config, "exchange");
}

function initOption() {
	var config = b.getConfig("exchange");
	if(config.min!=undefined && config.min!=-1) $("#min").val(config.min);
	if(config.max!=undefined && config.max!=-1) $("#max").val(config.max);
	$("#message").attr("checked", config.message);
}

$(function () {
	initOption();
	initCategory();
	template = $("#template").text();
	$("#start").click(start);
	$("#range").click(range);
	$("#message").click(message);
});