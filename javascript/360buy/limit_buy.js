// from http://qiang.360buy.com/LimitBuy.htm
// eval(function(p, a, c, k, e, r) { e = function(c) { return (c < a ? '' : e(parseInt(c / a))) + ((c = c % a) > 35 ? String.fromCharCode(c + 29) : c.toString(36)) }; if (!''.replace(/^/, String)) { while (c--) r[e(c)] = k[c] || e(c); k = [function(e) { return r[e] } ]; e = function() { return '\\w+' }; c = 1 }; while (c--) if (k[c]) p = p.replace(new RegExp('\\b' + e(c) + '\\b', 'g'), k[c]); return p } ('9($){$.R=j(a){g r={};G(g o 1c a){r[o]=a[o]}7 r};$.H=j(a,b){9(a){9(a.1d("l://h")==-1){7"l://h"+T.1e(T.1f()*4+10)+".V.m/n"+b+"/"+a}7 a}7""};$.w={z:"l://J.t.m/1g.X?Y=?",k:x,K:j(i){9(!q.k){7 x}9(q.k[i]){7 q.k[i]}7 x},A:j(){g c=j(a,i){g b=["s","1h","1i"];9(a){7"<8 6=\'"+b[i]+"\'><B>"+a.C+"</B><5 6=\'p-D\'>"+a.W+"</5></8>"}L{7"<8 6=\'"+b[i]+"\'><B></B><5 6=\'p-D\'></5></8>"}};g d=j(a){9(!a){7"<8 6=\'s Z\'><5 6=\'p-h\'></5><5 6=\'p-u\'></5></8>"}9(a.I&&(a.S==1||a.S==2)){7"<8 6=\'s 1j\'><5 6=\'p-h\'>"+(a.S==2?"<a 6=\'1k\' E=\'#\'><h F=\'l://J.t.m/1l/h/p-1m.1n\'/></a>":"")+"<a E="+(a.S==2?"\'#\'":("\'l://1o.t.m/1p/1q.1r?1s="+a.I+"&1t=1&1u=1\' M=\'N\'"))+"><h F=\'"+$.H(a.U,2)+"\' 11=\'1v\' 12=\'1w\' /></a></5><5 6=\'p-u\'>£¤"+a.P+"</5></8>"}L{7"<8 6=\'"+(a.S==0?"s Z":"s 1x")+"\'><5 6=\'p-h\'></5><5 6=\'p-u\'></5></8>"}};9(q.k){g e="",O="";g f=x;G(g i=0;i<3;i++){f=q.K(i);e+=c(f,0);O+=d(f)}$(".1y Q").y(e);$(".1z Q").y(O)}}};$.v=$.R($.w);$.v.z="l://J.t.m/1A.X?Y=?";$.v.A=j(){g b=j(a){9(a&&a.I){7"<8 6=\'s\'><5 6=\'p-h\'><a E=\'l://13.t.m/14/"+a.I+".y\' M=\'N\'><h F=\'"+$.H(a.U,4)+"\' 11=\'15\' 12=\'15\' /></a></5><5 6=\'p-D\'><a E=\'l://13.t.m/14/"+a.I+".y\' M=\'N\'>"+a.W+"</a></5><5 6=\'p-u\'><h F=\'l://u.V.m/1B"+a.I+",2.1C\'/></5></8>"}L{7"<8 6=\'s\'><5 6=\'p-h\'></5><5 6=\'p-D\'></5><5 6=\'p-u\'></5></8>"}};9(q.k){g c="";g d=x;G(g i=0;i<4;i++){d=q.K(i);c+=b(d)}$(".1D Q").y(c)}};$.16({17:$.w.z,k:{18:1E},19:"1a",1b:j(r){9(r){$.w.k=r;$.w.A()}}});$.16({17:$.v.z,k:{18:1F},19:"1a",1b:j(r){9(r){$.v.k=r;$.v.A()}}})}', 62, 104, '|||||div|class|return|li|if|||||||var|img||function|data|http|com||||this||fore|360buy|price|MoreSpecial|limitBuy|null|html|serviceUrl|replace|strong||name|href|src|for|getRandomImgUrl||qiang|getItem|else|target|_blank|contenthtml||ul|extendClass||Math||360buyimg||ashx|callback|wait||height|width|www|product|100|ajax|url|cid|dataType|json|success|in|indexOf|floor|random|LimitBuy|forea|foreb|rob|topimg|misc|img03|jpg|jd2008|purchase|InitCart|aspx|pid|pcount|ptype|182|193|tom|title|content|LimitBuyNormal|gp|png|contenta|3501|3502'.split('|'), 0, {}))

if ($) {
	$.extendClass = function(a) {
		var r = {};
		for ( var o in a) {
			r[o] = a[o]
		}
		return r
	};
	$.getRandomImgUrl = function(a, b) {
		if (a) {
			if (a.indexOf("http://img") == -1) {
				return "http://img" + Math.floor(Math.random() * 4 + 10)
						+ ".360buyimg.com/n" + b + "/" + a
			}
			return a
		}
		return ""
	};
	$.limitBuy = {
		serviceUrl : "http://qiang.360buy.com/LimitBuy.ashx?callback=?",
		data : null,
		getItem : function(i) {
			if (!this.data) {
				return null
			}
			if (this.data[i]) {
				return this.data[i]
			}
			return null
		},
		replace : function() {
			var c = function(a, i) {
				var b = [ "fore", "forea", "foreb" ];
				if (a) {
					return "<li class='" + b[i] + "'><strong>" + a.C
							+ "</strong><div class='p-name'>" + a.W
							+ "</div></li>"
				} else {
					return "<li class='"
							+ b[i]
							+ "'><strong></strong><div class='p-name'></div></li>"
				}
			};
			var d = function(a) {
				if (!a) {
					return "<li class='fore wait'><div class='p-img'></div><div class='p-price'></div></li>"
				}
				if (a.I && (a.S == 1 || a.S == 2)) {
					return "<li class='fore rob'><div class='p-img'>"
							+ (a.S == 2 ? "<a class='topimg' href='#'><img src='http://qiang.360buy.com/misc/img/p-img03.jpg'/></a>"
									: "")
							+ "<a href="
							+ (a.S == 2 ? "'#'"
									: ("'http://jd2008.360buy.com/purchase/InitCart.aspx?pid="
											+ a.I + "&pcount=1&ptype=1' target='_blank'"))
							+ "><img src='"
							+ $.getRandomImgUrl(a.U, 2)
							+ "' height='182' width='193' /></a></div><div class='p-price'>¡ê¡è"
							+ a.P + "</div></li>"
				} else {
					return "<li class='"
							+ (a.S == 0 ? "fore wait" : "fore tom")
							+ "'><div class='p-img'></div><div class='p-price'></div></li>"
				}
			};
			if (this.data) {
				var e = "", contenthtml = "";
				var f = null;
				for ( var i = 0; i < 3; i++) {
					f = this.getItem(i);
					e += c(f, 0);
					contenthtml += d(f)
				}
				$(".title ul").html(e);
				$(".content ul").html(contenthtml)
			}
		}
	};
	$.MoreSpecial = $.extendClass($.limitBuy);
	$.MoreSpecial.serviceUrl = "http://qiang.360buy.com/LimitBuyNormal.ashx?callback=?";
	$.MoreSpecial.replace = function() {
		var b = function(a) {
			if (a && a.I) {
				return "<li class='fore'><div class='p-img'><a href='http://www.360buy.com/product/"
						+ a.I
						+ ".html' target='_blank'><img src='"
						+ $.getRandomImgUrl(a.U, 4)
						+ "' height='100' width='100' /></a></div><div class='p-name'><a href='http://www.360buy.com/product/"
						+ a.I
						+ ".html' target='_blank'>"
						+ a.W
						+ "</a></div><div class='p-price'><img src='http://price.360buyimg.com/gp"
						+ a.I + ",2.png'/></div></li>"
			} else {
				return "<li class='fore'><div class='p-img'></div><div class='p-name'></div><div class='p-price'></div></li>"
			}
		};
		if (this.data) {
			var c = "";
			var d = null;
			for ( var i = 0; i < 4; i++) {
				d = this.getItem(i);
				c += b(d)
			}
			$(".contenta ul").html(c)
		}
	};
	$.ajax({
		url : $.limitBuy.serviceUrl,
		data : {
			cid : 3501
		},
		dataType : "json",
		success : function(r) {
			if (r) {
				$.limitBuy.data = r;
				$.limitBuy.replace()
			}
		}
	});
	$.ajax({
		url : $.MoreSpecial.serviceUrl,
		data : {
			cid : 3502
		},
		dataType : "json",
		success : function(r) {
			if (r) {
				$.MoreSpecial.data = r;
				$.MoreSpecial.replace()
			}
		}
	})
}