//http://qiang.360buy.com/LimitBuy.htm

var tick;
var lastCommodity = "";
function loulin() {
	$.ajax({
		url : $.limitBuy.serviceUrl,
		data : ({
			cid : 3501
		}),
		dataType : "json",
		success : function(r) {
			if (r) {
				$.limitBuy.data = r;
				$.limitBuy.replace();
				if (r[1].P != 0
						|| (lastCommodity != "" && r[0].C != lastCommodity)) {
					clearTimeout(tick);
					alert("目标出现：" + r[0].C + "\n" + r[0].P + "元");
					window.open("http://jd2008.360buy.com/purchase/InitCart.aspx?pid=" + r[0].I + "&pcount=1&ptype=1");
				} else {
					console.log(r[0].C + "("+r[0].P+"), " + r[1].C + "("+r[1].P+"), " + r[2].C + "("+r[2].P+")");
					lastCommodity = r[0].C;
					tick = setTimeout(loulin, 1000);
				}
			}
		}
	});
}
// loulin();
// clearTimeout(tick);
var now = new Date();
var atTime = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 9, 59,
		55, 300);
document.title = atTime.toString();
setTimeout(function() {
	loulin();
}, atTime.getTime() - now.getTime());

/*
 * javascript:clearTimeout(tick);void(0);
 * 
 * jsonp1308212187000([{"C":"插座","I":"119568","P":49.0000,"R":500,"S":2,"U":"http:\/\/img10.360buyimg.com\/n4\/361\/1072ee0b-ba9b-47c1-889b-913622857b85.jpg","W":"APC
 * 防涌插座P8E-CH
 * 过载保护\/防雷击\/防浪涌\/噪音滤除（8联\/3米\/600焦耳）"},{"C":"鼠标","I":"","P":0,"R":500,"S":0,"U":"","W":""},{"C":"耳机\/耳麦","I":"","P":0,"R":500,"S":0,"U":"","W":""}])
 * 
 * http://jd2008.360buy.com/purchase/InitCart.aspx?pid=358501&pcount=1&ptype=1
 */
