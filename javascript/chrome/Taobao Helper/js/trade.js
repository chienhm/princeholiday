
/*---------------------------------------------------------------------------*/
chrome.extension.sendRequest({cmd: "GET_OPTIONS"}, function(response) {
	var config = response;
	
	/*************************************************************************/
	if(document.title=="已买到的宝贝") {
		if(config.batDelete) {
			$("input.selector").attr("disabled", false);
			$("input.selector").click(function(){
				$(this).parents("tr.order-hd").css("background", this.checked ? "#EBF39A" : "#E8F2FF");
			});
			$("#J_AllSelector").click(function(){
				$("tbody tr.order-hd").each(function(i, e){
					$(e).css("background", $("#J_AllSelector").attr("checked") ? "#EBF39A" : "#E8F2FF");
				});
			});
			
			$("#J_BatchReceive").after("<a href='#' class='J_MakePoint toolbtn' id='J_BatchDelete' title='此功能由淘小蜜扩展，请至淘小蜜选项中配置。'>批量删除</a>");
			$("#J_BatchDelete").click(function(){
				var tbdList = [];
				$("#J_BoughtTable tbody").each(
					function(i, e){
						var orderid = $(e).attr("data-orderid");
						if($("#cb"+orderid).attr("checked")) {
							tbdList.push({"id":orderid, "e":e});
						}
					} /* each function */
				); /* each */
				console.log(tbdList);
				if(confirm("您已选择"+tbdList.length+"份订单，确定删除？（若误操作，请到回收站撤回）")) {
					for(var i=0; i<tbdList.length; i++) {
						setTimeout(enclosure(tbdList[i]), 1000 * i);
					};
				} /* end if */
			});
		}
	}

});

function enclosure(tbd){
	return function(){
		deleteOrder(tbd);
	};
}

function deleteOrder(order) {
	var orderid = order.id;
	var token = $("#J_Token").val();
	if(token==null) {
		console.log("token lost.");
		return;
	}
	console.log("delete order id " + orderid);
	
	var archive = $(order.e).attr("data-isarchive")=="true";
	$.post(
		"http://trade.taobao.com/trade/itemlist/recyled_order.htm", 
		{
			t			: new Date().getTime(),
			act			: "delOrder",
			_tb_token_	: token,
			order_ids	: orderid,
			isArchive	: archive
		}, 
		function(result){
			console.log(orderid + " delete finished.");
			$(order.e).hide();
		}
	); /* $.post() */
}