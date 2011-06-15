//http://qiang.360buy.com/LimitBuy.htm

var tick;
function loulin(){
	$.ajax({url: $.limitBuy.serviceUrl, data: ({cid:3501}), dataType: "json", success: 
		function (r) {
			if (r) {
				$.limitBuy.data = r;$.limitBuy.replace();
				if(r[0].S==1) {
					window.document.title="!!!赶紧下单!!!";
					window.open("http://jd2008.360buy.com/purchase/InitCart.aspx?pid="+r[0].I+"&pcount=1&ptype=1");
				} else {
					tick = setTimeout(loulin, 1000);
				}
			}
		}
	});
}
//loulin(); 

var now = new Date();var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), 9, 59, 55, 300);document.title=atTime.toString();setTimeout(function(){loulin();}, atTime.getTime()-now.getTime());

/*
javascript:clearTimeout(tick);void(0);

jsonp1307001100549([
	{"C":"电源","I":"378512","P":179.0000,"R":500,"S":2,"U":"http:\/\/img10.360buyimg.com\/n4\/4083\/1ba76b47-ed50-48e8-b199-76c5fcd6cd9e.jpg","W":"安钛克（Antec）VP 350P 电源（额宿W 120mm静音风扇 主动PFC 黑化外型设计＿"},
	{"C":"上网朿","I":"","P":0,"R":500,"S":0,"U":"","W":""},
	{"C":"数码摄像朿","I":"","P":0,"R":500,"S":0,"U":"","W":""}
])
jsonp1307001100555([
	{"C":"上网本","I":"358501","P":1399.0000,"R":500,"S":2,"U":"http:\/\/img10.360buyimg.com\/n4\/896\/202c249f-37c2-4155-b4ce-6a01aa11e310.jpg","W":"宏碁（acer）AO721-14Css 11.6英寸笔记本电脑 （K145 2G 250G 蓝牙 无线 摄像头 HDMI)太空银"},
	{"C":"数码摄像机","I":"","P":0,"R":500,"S":0,"U":"","W":""},
	{"C":"显示器","I":"","P":0,"R":500,"S":0,"U":"","W":""}
])

http://jd2008.360buy.com/purchase/InitCart.aspx?pid=358501&pcount=1&ptype=1
*/
