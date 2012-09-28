var page = location.pathname;
var host = location.host;

//-----------------------------------------------------------------------------
if(host=="www.homevv.com" && page=="/vvmallAddOrder.jhtml") {
	var WEEK = "日一二三四五六";
	var today = new Date();
	var tomorrow = new Date();
	tomorrow.setTime(today.getTime()+24*60*60*1000);
	var month = tomorrow.getMonth()+1;
	month = (month<9?"0":"") + month;
	var day = tomorrow.getDate();
	day = (day<9?"0":"") + day;
	var week = "星期" + WEEK[tomorrow.getDay()];
	var tstr = tomorrow.getFullYear() + "-" + month + "-" + day + " " + week;
	$("#deliveryDate").empty();
	$("#deliveryDate").append("<option value=\""+tstr+"\">"+tstr+"</option>");
	$("#deliveryDate")[0].selectedIndex = 0;
	$("#deliveryTime").empty();
	$("#deliveryTime").append("<option value=\"上午09:00-14:00\">上午09:00-14:00</option>");
	$("#deliveryTime")[0].selectedIndex = 0;
	$("input[type=button][value=保存支付方式及配送方式]").click();
	
	//发票
	$("input[type=radio][name=order.invoiceType][value=1]").attr("checked", true).click();
	$("#invoiceTitle[value=个人]").attr("checked", true);
	$("#order\\.invoiceTitle").val("个人");
	$("#order\\.invoiceContent").val("建材家具");
	$("input[name=companyTitle]").val("个人");
	console.log($("#order\\.invoiceTitle").val());
	console.log($("#order\\.invoiceContent").val());
	$("input[type=button][value=保存发票信息]").click();
	//积分
	$("#couponStateShow").parent().click();
	$("#integralInput").val($("#availibleIntegral").text());
	$("input[type=button][name=integralBtn]").click();
	//优惠券
	//$("#voucherStateShow").parent().click();
	//$("#voucherNo").val("2C0H269FFNUZEB");
	//$("#inputVoucherBtn").click();
	//验证码
	$("#checkVildate").focus();
	$("html, body").animate({ scrollTop: $(document).height()-1000 }, "fast");
}

if(page=="/vvshopCartPreview.jhtml" || page=="/vvshopCartView.jhtml") {
	console.log($("#totalGroupAmountLable"));
	var cost = parseFloat($("#totalGroupAmountLable").text().substring(1));
	console.log(cost);

	chrome.extension.sendRequest({cmd: "GET_AUTO"}, function(response) {
		console.log("Rush buy: " + (response.skmod?"on":"off"));
		if(response.skmod) {
			if(cost>5.5 && cost<10) {
				$("#submitBtnArea a img").click();
			} else {
				location.reload();
			}
		}
	});
}