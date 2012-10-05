
function rate() {
	$("input.good-rate").attr("checked", "checked");
	$(["description", "attitude", "delivery"]).each(function(i, item){
		$("input[type=radio][name="+item+"][value=5]").attr("checked", "checked");
	});
	$("button.submit[type=submit]").click();
}

rate();
