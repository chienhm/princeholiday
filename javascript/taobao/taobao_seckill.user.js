// ==UserScript==
// @name      Taobao SecKill Assistant
// @version	  5.1
// @namespace      http://www.morntea.com/
// @description    Assistant for seckill
// @author	  Lou Lin(loulin@morntea.com)
// @include	  http://item.taobao.com/*
// @include   http://item.lp.taobao.com/*
// @include	  http://buy.taobao.com/*
// @include	  http://item.tmall.com/*
// @include	  http://e.morntea.com/*
// @include	  http://127.0.0.1:8080/*
// @include	  http://cnrdloull1c:8080/*
// @include	  https://cashier.alipay.com/standard/payment/cashier.htm*
// @require   util.js
// @require   encode.js
// @require   pinyin.js
// @require   answer.js
// ==/UserScript==

var MS_VALID_TIME = -2 * 1000;	//time delay after seckill starts
var MS_AHEAD_TIME = 1 * 100;	//the last time interval before seckill starts
var REFRESH_INTERVAL = 5 * 60 * 1000;	//refresh time interval
var RAPID_REFRESH = 20; //rapid refresh interval at the end
var MS_START_TIME = "05 28, 2011 15:00:00";
var ITEM_ID = "9995686670"; //item id

/* Automation Config */
var SKUID = ""; //style selection
var AUTO_REFRESH = true; //if allow auto refresh
var ANSWER = "";
var ANSWER_SERVER = "http://192.168.0.129:10001/";
var AUTO_PAY = false;
var DES_PASSWORD = "DES PASS"; //xxx

/* Good Info for Answering Questions, 
 * display under answer input, click to set input value, click again to test auto submit */
var SHOP_NAME = ""; //店铺名称
var SHOP_URL = ""; //店铺网址
var SHOP_DISCOUNT = ""; //店铺折扣
var SHOP_YEAR = ""; //哪一年

var GOOD_SELLER = ""; //商家名称
var GOOD_PRICE = ""; //原价
var GOOD_PPRICE = ""; //活动价
var GOOD_ATTR = [
	["国家", ""],	// "这款产品是哪个国家生产的？"
	["城市", ""],	// "天堂伞出自哪个城市？"
	["品名", ""],
	["型号|编号|货号", ""],
	["牌子|品牌", ""],	//(item_30.htm)
	["产地|产自|哪里生产", ""],
	["材质|材料|成分", ""],
	["数量", ""],	//"请输入本商品套件的小件数量"(item_48.htm)
	["尺寸|规格", ""],
	["直径", ""],
	["重量|净重", ""],
	["功能", ""],
	["风格", ""],
	["工艺", ""],
	["颜色", ""],
	["容量", ""]
];

var NOTE = ""; //附言
/* Address and User Info */
var PROVINCE = "310000";
var CITY = "310100";
var AREA = "310112";
var AREA_CODE = "310112";
var POST_CODE = "200240";
var ADDRESS = "某某区某某路某某号";
var NAME = "某某";
var PHONE = "02134200000";

var LOG_CONSOLE = 0; //0. window title, 1. GM_log, 2. console.log, other. alert

/* check seckill time and if user login */
function init() {
	var reload = false;
	if(!isMsTimeValid()) {
		log("init:秒杀时间过期或未设置");
		checkLogin();
		reload = true;
	}
	setMsTime();
	if(reload && isMsTimeValid()) {window.location.reload();}
}

/* test if seckill time is valid */
function isMsTimeValid() {
	var mstime = GM_getValue("mstime");
	if(typeof mstime == "undefined") {
		return false;
	} else {
		var mstime = new Date(mstime);
		if((mstime.getTime() - new Date().getTime())<MS_VALID_TIME) {
			return false;
		}
	}
	return true;
}

/* test if user has logged in, if not, jump to login */
function checkLogin() {
	GM_xmlhttpRequest({
		method: "GET",
		url: "http://i.taobao.com/my_taobao.htm",
		onload: function(response) {
			if(response.responseText.indexOf("支付宝会员登录")!=-1) {
				if(confirm("尚未登录，是否登录？")) {
					window.location.href = "https://login.taobao.com/member/login.jhtml?f=top&redirectURL=" + window.location.href;
				}
			}
		}
	});
}
/* async get answer from answer server */
function getAnswer(answerObj) {
	GM_xmlhttpRequest({
		method: "GET",
		url: ANSWER_SERVER + "?" + Math.random(),
		headers: {'Content-Type': 'application/x-www-form-urlencoded'},
		onload: function(response) {
			var answer = response.responseText;
			if(answer!="" && answerObj!=null && answerObj.value=="") {
				answer = answer.substring(answer.indexOf(":")+1).replace(/[\r\n\s]+/g, ""); //alert("["+answer+"]");
				answerObj.value = answer;
			}
		}
	});
}

/* set seckill time */
function setMsTime() {
	var strMsTime = MS_START_TIME;
	GM_setValue("mstime", strMsTime);
	log("秒杀时间设为：" + strMsTime);
}

function destroyGlobal(confVar) {
	if(typeof GM_getValue(confVar) != "undefined") {
		//log("销毁" + confVar + "=" + GM_getValue(confVar));
		GM_deleteValue(confVar);
	}
}

function destroy() {
	destroyGlobal("mstime");
	//destroyGlobal("answerindex");
	destroyGlobal("timeline");
}

/* save seckill question in global config in FF */
function saveQuestion() {
	var msQuestionObj = document.getElementById("seckill");
	if(msQuestionObj!=null) {	
		var question = msQuestionObj.children[2].innerHTML.split("<br>")[1]; //.lastChild.innerHTML.split("<br>")[1];		
		GM_setValue("question", question); //question = "888+999-1880=？（填答案即可） ";
		log(GM_getValue("question"));
	}
}

/* test if the question is an expression */
function parseExpr(str) { // 例子：“2*6*8/3=？（填答案即可） ”，返回“2*6*8/3”
	var expr = str.replace(/（.+）/g, "").replace(/[ =？\s]+/g, "");
	if(/^[\d\+\-\*\/\(\)]+$/.test(expr)) {
		return expr;
	} else {
		return null;
	}
}

/* test if current commodity could be baught */
function canBuy() {
	var msQuestionObj = $("seckill");
	var buyButton = $("J_LinkBuy");
	var priceObj = $("J_StrPrice"); // 标价
	var vipPriceObj = $("J_SpanVip"); // VIP价
	var limitPriceObj = $("J_SpanLimitProm"); //限时促销价
	if (msQuestionObj!=null // if it has question
		|| (
			buyButton!=null // if buy button appears
			&& (
				(priceObj && priceObj.innerHTML=="1.00")
				|| (vipPriceObj && vipPriceObj.innerHTML=="1.00")
				|| (limitPriceObj && limitPriceObj.innerHTML=="1.00")
				|| (ITEM_ID!="" && location.href.indexOf(ITEM_ID)!=-1)
				)
			)
		) {
		return true;
	}
	return false;
}

/* start to buy, simulate click “立即购买” */
function autoBuy() {
	if(SKUID!=""){
		$("skuId").value = SKUID;
	} else {		
		if($("J_itemViewed")) {
			var script = $("J_itemViewed").nextSibling.innerHTML;
			var re = /(?:skuId)" : "(\d+)"/g;
			var arr;
			while ((arr = re.exec(script)) != null) {
				log("skuId:"+arr[1]);
				$("skuId").value = arr[1]; //默认取第一个skuId
				break;
			}
		}
	}
	document.getElementById("J_FrmBid").submit(); //document.forms["bidForm"].submit();
	//location.assign( "javascript:var evt = document.createEvent('MouseEvents');evt.initMouseEvent('click', true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);document.getElementById('J_LinkBuy').dispatchEvent(evt);void(0)" );
}

/* speed up buy */
function fastInput() {
	if($("J_OrderForm")==null) return;
	$("n_prov").value = PROVINCE;
	$("n_city").value = CITY;
	$("n_area").value = AREA;
	$("divisionCode").value = AREA_CODE;
	$("J_postCode").value = POST_CODE;
	$("deliverAddress").value = ADDRESS;
	$("deliverName").value = NAME;
	$("deliverPhoneBak").value = PHONE;
	
	// set other infomation
	$("J_msgtosaler").value = NOTE;
	if($("shippingHidden")!=null){
		if($("shippingHidden").value=="") {
			for(var ship=1; ship<8; ship++) {
				if($("shipping"+ship)!=null) {
					$("shipping"+ship).checked="checked";
					$("shippingHidden").value = ship;break;
				}
			}
		}
	}
	var bonus = getFormElementByName("bonus_id");
	if(bonus!=null) bonus.checked="checked";
	
	// get and save shop information
	var shop_name=null, good_name=null;
	
	good_name = $("NameText").textContent;
	var shopinfo = $("good-info").textContent;
	var arr;
	if ((arr = /卖家：\s*(\S+)/g.exec(shopinfo)) != null) {
		shop_name = arr[1];
	}
			
	/* set verify code */
	var verifyCode = $("J_checkCodeInput");
	if(verifyCode!=null) {
		window.scrollTo(0, verifyCode.offsetTop-300); // Used to simply scroll down the window to fully display verify code
		verifyCode.tabIndex = 20;
		verifyCode.style.imeMode = "disabled";
		verifyCode.focus();
		verifyCode.setAttribute("onblur", 
		"var f=document.forms['nameform'];" + 
		"if(f['J_checkCodeInput'].value.length==4 && !f['_fm.g._0.s']){document.forms['nameform'].submit();}");
		var nextCode = $("J_nextcheckCode");
		nextCode.setAttribute("onclick", "var vc=document.getElementById('checkCodeImg');vc.src=vc.src+'&'+Math.random();return false;");
	}
	
	/* The following code helps to answer question */
	var ANSWER_INPUT_NAME = "_fm.g._0.s";
	var answerObj = getFormElementByName(ANSWER_INPUT_NAME); // sometimes the answer input name is _fma.b._0.se
	if(answerObj==null){
		ANSWER_INPUT_NAME = "_fma.b._0.se";
		answerObj = getFormElementByName(ANSWER_INPUT_NAME);
	}
	if(answerObj.value=="10000000000")answerObj = null;
	if(answerObj!=null) {
		answerObj.value = "";
		answerObj.tabIndex = 21;
		// can be used to switch between answer and checkcode
		answerObj.setAttribute("onblur", 
		"var f=document.forms['nameform'];var ci=f['J_checkCodeInput'];" + 
		"if((!ci||ci.value.length==4) && f['"+ANSWER_INPUT_NAME+"'].value!=''){document.forms['nameform'].submit();}" + 
		"ci.focus();"); 
		if(ANSWER!="") answerObj.value = ANSWER;
		
		function setAnswer(answer){answerObj.value = answer;}
		
		getAnswer(answerObj); // get answer from server
		
		var question = GM_getValue("question").trim();
		//question = "“朱”字有多少划"; // used for test
		/*------------------------------------------------ Answer Engine ------------------------------------------------*/
		// 请在5-20之间选一个数字填入/请任意输入1-20内一个数字(item_54.htm)
		if(answerObj.value=="" && question.indexOf("一个数")!=-1) {
			var range = getNumRange(question);
			if(range) {
				if(GM_getValue("answerindex")) {
					var num = parseInt(GM_getValue("answerindex"))+1;
					if(num>range.to || num<range.from) {
						num = range.from;
					}
					GM_setValue("answerindex", num);
					answerObj.value = num;
				} else {
					GM_setValue("answerindex", range.from);
					answerObj.value = range.from;
				}
			}
		}
		// 请输入“红辣椒陶瓷”五个字中的一个
		if(answerObj.value=="" 
			&& (question.indexOf("中的一个")!=-1 
				|| question.indexOf("选一字")!=-1 
				|| question.indexOf("一个字")!=-1)) { 
			var word = getQuotedString(question);
			if(GM_getValue("answerindex")) {
				var na = parseInt(GM_getValue("answerindex"));
				if(na>=word.length)na=0;
				answerObj.value = word.charAt(na);
				GM_setValue("answerindex", na+1);
			} else {
				var na = word.length-1;
				answerObj.value = word.charAt(na);
				GM_setValue("answerindex", "0");
			}
		}
		// 请填写成语中的空缺字：足_多谋"; (item_35.htm)
		if(answerObj.value=="" && question.indexOf("空缺字")!=-1) {
			getLostWord(question, setAnswer);
		}
		
		// “秋色连波”的下一句是什么？ (item_37.htm/item_55.htm)
		if(answerObj.value=="" 
			&& (question.indexOf("一句")!=-1 
				|| question.indexOf("上句")!=-1 
				|| question.indexOf("下句")!=-1
				|| question.indexOf("诗")!=-1)) {
			searchAnswer(urlEncode(question), setAnswer);
		}
		
		// 打出“萧綦”这两个字的拼音 (item_29.htm)
		if(answerObj.value=="" && question.indexOf("拼音")!=-1) {
			var word = getQuotedString(question);
			var py = getPinyin(word.charAt(0)).pinyin;
			for(var i=1; i<word.length; i++) {
				py += " " + getPinyin(word.charAt(i)).pinyin;
			}
			answerObj.value = py;
		}
		// “梓”读第几声？（例：第一声）(item_31.htm)
		if(answerObj.value=="" && question.indexOf("几声")!=-1) {
			var word = getQuotedString(question);
			var objPinyin = getPinyin(word);
			var tone = objPinyin.shengdiao;
			if(question.indexOf("（")!=-1) {
				var hint = getBracketString(question);
				if(hint.indexOf("例")!=-1) {
					hint = hint.replace("例", "").replace("：", "");
					tone = hint.replace(/[一二三四几]/, shuzi2hanzi(objPinyin.shengdiao));
				}
			}
			answerObj.value = tone;
		}
		
		// 西安是哪个省的省会？（答案两个字）(item_31.htm)
		if(answerObj.value=="" && question.indexOf("省")!=-1) { 
			if(question.indexOf("省会")!=-1 || question.indexOf("缩写")!=-1 || question.indexOf("简称")!=-1) {
				answerObj.value = getCity(question);
			} else if(question.indexOf("哪个省")!=-1) {
				getCity(question, setAnswer);
			}
		}
		// 韩国的首都是什么城市？如：釜山(item_64.htm)
		if(answerObj.value=="" && question.indexOf("首都")!=-1) {
			answerObj.value = getCapi(question);
		}
		// 
		if(answerObj.value=="" && (question.indexOf("哪一天")!=-1) || (question.indexOf("哪天")!=-1)) {
			answerObj.value = getDate(question);
		}
		// 
		if(answerObj.value=="" && question.indexOf("反义词")!=-1) {
			answerObj.value = getAntonym(question);
		}
		// 2011年的中国情人节“七夕节”是在星期几？（如：星期五）(item_77.htm)
		if(answerObj.value=="" && question.indexOf("星期")!=-1) {
			var word = getQuotedString(question);
			getWeek(word, setAnswer);
		}
		// “淘”字有几划？ 如：9划(item_65.htm)
		if(answerObj.value=="" && (question.indexOf("笔画")!=-1 || question.indexOf("笔划")!=-1 || question.indexOf("多少划")!=-1 || question.indexOf("多少画")!=-1 || question.indexOf("几划")!=-1 || question.indexOf("几画")!=-1)) {
			var word = getQuotedString(question);
			var number = getStrokeCount(word);
			if(number!=0) answerObj.value = number;
        }
		// If the question is an expression, or go to baidu to search this answer !!!!!!!!!!!!!!
		if(answerObj.value=="") {
			var expr = parseExpr(question);
			if(expr!=null) {
				try {
					answerObj.value = eval(expr);
				} catch(e) {
					answerObj.value = "";
				}
			}
		}
		// If the answer is given, such as "（答案：XX）"
		if(answerObj.value=="" && question.lastIndexOf("（")!=-1) { 
				var answer = getGivenAnswer(question);
				if(answer!=null) answerObj.value = answer;
		}
		// "我们店铺的全称是？ "(item_32.htm)
		if(answerObj.value=="" && question.indexOf("店")!=-1 
				&& (question.indexOf("全称")!=-1 || question.indexOf("名")!=-1)) {
			if(SHOP_NAME!="") answerObj.value = SHOP_NAME;
			else answerObj.value = shop_name;
		}
		//
		if(answerObj.value=="" &&  (question.indexOf("店")!=-1 && question.indexOf("网址")!=-1)) {
			if(SHOP_URL!="") answerObj.value = SHOP_URL;
		}
		//“请问店铺活动是全场几折？”(item_45.htm)
		if(answerObj.value=="" &&  (question.indexOf("店")!=-1 && question.indexOf("折")!=-1)) {
			if(SHOP_DISCOUNT!="") answerObj.value = SHOP_DISCOUNT;
		}
		//“请输入此款产品的市场价”(item_44.htm)
		if(answerObj.value=="" &&  question.indexOf("价")!=-1) {
			var _price_ = null;
			if(GOOD_PRICE!="") _price_ = GOOD_PRICE;
			if(question.indexOf("活动价")!=-1 || question.indexOf("折扣价")!=-1 || question.indexOf("淘宝价")!=-1) {
				if(GOOD_PPRICE!="") _price_ = GOOD_PPRICE;
			}
			if(_price_!=null) {
				/*------------Tmp------------(item_46.htm)*/
				var hint = getHint(question);
				if(hint.indexOf(".00")!=-1) {
					if(_price_.indexOf(".")==-1) {
						_price_ += ".00";
					} else if (_price_.lastIndexOf(".")==_price_.length-2) {
						_price_ += "0";
					}
				}
				/*------------Tmp------------(item_46.htm)*/
				answerObj.value = _price_;
			}
		}
		//
		if(answerObj.value=="" &&  question.indexOf("年")!=-1) {
			if(SHOP_YEAR!="") answerObj.value = SHOP_YEAR;
		}
		
		// compare all of the good's attributes to match the question
		if(answerObj.value=="") {
		out:for(var i=0; i<GOOD_ATTR.length; i++) {
				var keywords = GOOD_ATTR[i][0].split("|");
				var value = GOOD_ATTR[i][1];
				for(var j=0; j<keywords.length; j++) {
					if(keywords[j] && value && question.indexOf(keywords[j])!=-1) {
						answerObj.value = value;
						break out;
					}
				}
			}
		}
		/*------------------------------------------------  Answer Engine End  ------------------------------------------------*/
		function getPos(q){
			var p = q.lastIndexOf("？");
			if(p==-1) p = q.lastIndexOf("（");
			if(p==-1) p = q.length;
			var w = q.lastIndexOf("什么");
			if(p-w<=5) p = w;
			return p;
		}
		var pos = getPos(question); var searchStr = question; var tail = "";
		if(pos!=-1) {searchStr = question.substring(0, pos); tail = question.substring(pos);}
		
		// search answer at last if no answer can be got directly
		if(answerObj.value=="") { //&& (question.indexOf("是什么")!=-1) || question.indexOf("是哪里")!=-1 || question.indexOf("是谁")!=-1) {
			searchAnswer(urlEncode(searchStr), function s(a){if(answerObj.value=="")setAnswer(a);});
		}
		
		if(verifyCode==null || (verifyCode.value!="")) { // && answerObj.value==""
			answerObj.focus();
		}
		/*------------------------------------------------ Answer Helper Begin ------------------------------------------------*/
		// 以下是问题回答辅助
		// 直接将问题显示在答案输入框下面
		
		function createBlock(text) {
			var newDiv = document.createElement("div");
			newDiv.appendChild(document.createTextNode(text));
			return newDiv;
		}
		
		var newDiv = document.createElement("div");
		var newLink = document.createElement("a");
		newLink.href = "http://www.baidu.com/baidu?wd=" + searchStr;
		newLink.setAttribute("style", "color: red; font-size: 14px; font-weight: bold;");
		newLink.setAttribute("target", "_blank");
		newLink.innerHTML = searchStr;
		newDiv.appendChild(newLink);
		newDiv.appendChild(document.createTextNode(tail));
		answerObj.parentNode.appendChild(newDiv);
		
		if(question.indexOf("反")!=-1 || question.indexOf("倒")!=-1){var button = document.createElement("input");button.setAttribute("type","button");button.setAttribute("value","Reverse");button.setAttribute("onclick","function reverse(str){var rt='';for(var i=str.length-1;i>=0;i--) {rt+=str[i];}return rt;} var a=document.forms['nameform']['"+ANSWER_INPUT_NAME+"'];a.value=reverse(a.value);");answerObj.parentNode.appendChild(button);}//Reverse The Answer
		
		answerObj.parentNode.appendChild(createBlock("文本问题："+question));
		if(good_name!=null) {answerObj.parentNode.appendChild(createBlock("宝贝名称："+good_name));}
		if(shop_name!= null) {answerObj.parentNode.appendChild(createBlock("卖家名称："+shop_name));}
		
		if(SHOP_NAME!="")addChoice(answerObj.parentNode, ANSWER_INPUT_NAME, SHOP_NAME);
		if(GOOD_SELLER!="")addChoice(answerObj.parentNode, ANSWER_INPUT_NAME, GOOD_SELLER);
		/*------------------------------------------------  Answer Helper End  ------------------------------------------------*/
	} //answer question end
	
	// submit directly
	if (answerObj==null && verifyCode==null 
		//|| (answer.value!="" && verifyCode.value!="")
		) {
		log("问题：" + GM_getValue("question"));
		var action = getFormElementByName("action");
		if( getFormElementByName("item_id_num").value==ITEM_ID || (action!=null && action.value.indexOf("secKill")!=-1) ) { //buynow/secKillBuyNowAction
			$("performSubmit").click(); //document.forms["nameform"].submit();
			$("J_OrderForm").submit();
		}
	}
}

/* Auto Send Payment Info by Set DES Password */
function autoPay() {
	var passObj = $("payPassword");
	if(passObj) {
		passObj.value = DES_PASSWORD;
		$("balancePayForm").submit();
	}
}

function monitor() {
	var link = location.href;
	//-------------------------------------------------- //支付
	if(link.indexOf("cashier.alipay.com")!=-1) {
		if(AUTO_PAY)autoPay();
		else log("自动支付已禁用");
	}
	//-------------------------------------------------- //填写订单
	else if ($("performSubmit")) {
	//(link.indexOf("buy.taobao.com")!=-1 || link.indexOf("taobao/buy")!=-1) { //http://buy.taobao.com/auction/buy_now.jhtml,http://e.morntea.com/util/taobao/buy_26.htm
		log(fT(new Date()) + "填写订单，问题：" + GM_getValue("question"));
		fastInput();
		destroy();
	} 
	//-------------------------------------------------- //购买已提交
	else if(link.indexOf("buy_now.htm")!=-1) { //http://buy.taobao.com/auction/buy_now.htm
		log(fT(new Date()) + "提交购买");
	} 
	//-------------------------------------------------- //商品页面
	else {
		if($("detail")) {
			var detail = $("detail").textContent;
			if(detail.indexOf("秒杀完毕")!=-1 || detail.indexOf("已下架")!=-1) {
				log("宝贝已秒杀完毕或已下架！");return;
			}
		}
		if($("content")) {
			var content = $("content").textContent;
			if(content.indexOf("此宝贝已不能购买")!=-1) {
				log("不能购买或者帐号被封！");return;
			}
		}
		init();
		
		if($("J_itemViewed")) { //有skuId需赋值后提交
			var script = $("J_itemViewed").nextSibling.innerHTML;
			var re = /skuId/g;
			if(re.test(script)) {
				log("注意，检测到skuId！");
			}
		}
		
		if(canBuy()) {
			log(fT(new Date()) + "可以购买");
			saveQuestion();
			autoBuy();
		} else {
			var mstime = GM_getValue("mstime");
			var now = new Date();
			if(typeof mstime != "undefined") {
				mstime = new Date(mstime);
				var left = mstime.getTime() - now.getTime();
				if(left>0)log("剩余" + ((left)>1000?(parseInt(left/1000)+"秒"+(left%1000)):left) + "毫秒");
				log("当前[" + fT(now) + "]");
				
				if (left < MS_VALID_TIME) {
					log("秒杀时间过期或未设置");
				} else if (left <= MS_AHEAD_TIME) { // The last several seconds
					if(AUTO_REFRESH) {
						setTimeout("window.location.reload();", RAPID_REFRESH);
					} else {
						log("自动刷新已禁用，需手动刷新");
					}
				} else { // long long before second kill
					var waitMicro;
					if(left > MS_AHEAD_TIME + REFRESH_INTERVAL) {
						waitMicro = REFRESH_INTERVAL-parseInt(Math.random()*(REFRESH_INTERVAL/10));
					} else {
						waitMicro = left - MS_AHEAD_TIME;
					}
					log(waitMicro/1000 + "s后刷新;[" + fT(mstime) + "]秒杀");	
					
					// calculate network delay
					function reload() {
						GM_setValue("timeline", (new Date().getTime())+"");
						window.location.reload();
					}
					var timeline = GM_getValue("timeline");
					if(typeof timeline != "undefined") {
						log("网络延迟：" + (new Date().getTime()-timeline) + "毫秒");
					}
					
					setTimeout(function(){reload();}, waitMicro);
				}
			}
		}
	}
};
//GM_deleteValue("answerindex");
//GM_setValue("answerindex", "0");
//http://e.morntea.com/util/taobao/item_25.htm
//GM_setValue("question", "天堂伞出自哪个城市？");
monitor();