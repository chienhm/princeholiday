//http://qiang.360buy.com/LimitBuy.htm

var tick;
function loulin(){
	$.ajax({url: $.limitBuy.serviceUrl, data: ({cid:3501}), dataType: "json", success: 
		function (r) {
			if (r) {
				$.limitBuy.data = r;$.limitBuy.replace();
				if(r[0].S==1) {
					window.document.title="!!!�Ͻ��µ�!!!";
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
	{"C":"��Դ","I":"378512","P":179.0000,"R":500,"S":2,"U":"http:\/\/img10.360buyimg.com\/n4\/4083\/1ba76b47-ed50-48e8-b199-76c5fcd6cd9e.jpg","W":"���ѿˣ�Antec��VP 350P ��Դ������W 120mm�������� ����PFC �ڻ�������ƣ�"},
	{"C":"�����c","I":"","P":0,"R":500,"S":0,"U":"","W":""},
	{"C":"��������c","I":"","P":0,"R":500,"S":0,"U":"","W":""}
])
jsonp1307001100555([
	{"C":"������","I":"358501","P":1399.0000,"R":500,"S":2,"U":"http:\/\/img10.360buyimg.com\/n4\/896\/202c249f-37c2-4155-b4ce-6a01aa11e310.jpg","W":"�곞��acer��AO721-14Css 11.6Ӣ��ʼǱ����� ��K145 2G 250G ���� ���� ����ͷ HDMI)̫����"},
	{"C":"���������","I":"","P":0,"R":500,"S":0,"U":"","W":""},
	{"C":"��ʾ��","I":"","P":0,"R":500,"S":0,"U":"","W":""}
])

http://jd2008.360buy.com/purchase/InitCart.aspx?pid=358501&pcount=1&ptype=1
*/
