var index = -1;
var interval = 0;
var categories = [
	{id:"11302000000", name:"连衣裙"},
	{id:"11301000000", name:"时尚女裤"},
	{id:"11303000000", name:"半身裙"},
	{id:"11304000000", name:"短裤热裤"},
	{id:"10102000000", name:"风格T恤"},
	{id:"10101000000", name:"夏日雪纺"},
	{id:"10104000000", name:"时尚衬衫"},
	{id:"10103000000", name:"底衫吊带"},
	{id:"10301000000", name:"魅力女鞋"},
	{id:"10302000000", name:"潮流女包"},
	{id:"10303000000", name:"时尚配件"},
	{id:"10201000000", name:"衬衫T恤"},
	{id:"10204000000", name:"夏款裤装"},
	{id:"10203000000", name:"男包配饰"},
	{id:"10202000000", name:"休闲男鞋"},
	{id:"11102000000", name:"时尚文胸"},
	{id:"11101000000", name:"家居睡衣"},
	{id:"11104000000", name:"打底丝袜"},
	{id:"11103000000", name:"舒适内裤"},
	{id:"10601000000", name:"童装孕装"},
	{id:"10602000000", name:"玩具早教"},
	{id:"10603000000", name:"母婴用品"},
	{id:"10604000000", name:"童鞋配饰"},
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
	{id:"10902000000", name:"3折封顶"},
	{id:"10904000000", name:"热销精品"}
];
var template = "";

function findLuck() {
	index++;
	if(index==categories.length) {
		index = 0;
	}
	$("#task").html("正在查找 <b>" + categories[index].name + "</b> 类目全额兑换的宝贝...");
	$("#result").html("");
	getPage(categories[index]);
}

function loopAll() {
	interval = setInterval(findLuck, 2000, null);
}

function getPage(category) {
	var xhr = new XMLHttpRequest();
	xhr.open("GET", "http://taojinbi.taobao.com/home/category_search_home.htm?page=1&order=1&isAsc=1&category_id="+category.id+"&tracelog=qzexcoin&discountPriceMin=&discountPriceMax=&isExchangeCoin=yes&exchangeCoinMin=&exchangeCoinMax=", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4) {
			var items = parseHtml(xhr.responseText);
			updateArray(items);
		}
	}
	xhr.send();
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

var itemRegExp = /<a.*?href="(.+?)" target="_blank">[^\w]+<img .+? src="(.+?)" \/>[^\w]+<p class="title" title=".+?">(.+?)<\/p>[^\w]+<p class="price"><del>(.+?)<\/del><span class="discount">\/.+?折<\/span> <span class="salescount">已成交(.+)件<\/span> <\/p>[^\w]+<p class="price qz-price"> <em>(.+?)<\/em>+(.+?)<span class="coin">淘金币<\/span> <span class="favorable">(.+?)<\/span>  <\/p>[^\w]+<p class="modes">[^\w]+<span data-tip="可用金币兑换" class="mode4 J_Mode">\((.+)淘金币\)/ig;

function parseHtml(html) {
	var items = new Array();
	if(html.indexOf("没有找到")!=-1) { //html.indexOf("listmodbox")==-1
		$("#result").html("没有找到宝贝！");
	} else {
		var count = html.split("=\"可用金币兑换\"").length-1;
		console.log(count);
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
			console.log("Item found: " + item.name);
		}
		if(i!=count) { //sanity check
			console.log(count + " items found, but only " + i + " items parsed.");
		}
		//console.log(items);
	}
	return items;
}

function updateArray(items) {	
	if(categories[index].items == undefined) {
		categories[index].items = items;
		for(var j=0; j<items.length; j++) {
			addItem("list", items[j]);
		}
	} else {
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
	}
}

function addItem(area, item) {
	var html = template.replace(/\$\{(.+?)\}/ig, function(match){
		if(item[match[1]]!=undefined) {
			return item[match[1]];
		}
	});
	$(html).prependTo("#"+area);
	showNote(item);
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

var running = false;
function start() {
	if(running) {
		clearInterval(interval);
		$("#start").val("开始查找");
		$("#task").html("查找完毕！");
		$("#result").html("");
		running = false;
	} else {
		loopAll();
		$("#start").val("停止查找");
		running = true;
	}
}

function init() {
	template = $("#\\$\\{id\\}").parent().html();
	$("#\\$\\{id\\}").hide();
}

document.addEventListener('DOMContentLoaded', function () {
	init();	
	$("#start").bind('click', start);
});