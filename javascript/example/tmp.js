//---------------------------------------------------------------------------¹ºÂòµç×ÓÈ¯
var rate = "9";
var now = new Date();
jQuery.get("/eta/search_ticket.xhtml?viewCode=ajax&CURRENTPAGE=1&PAG_CNT=900&MERC_ID=888009953110155&BON_RAT="+rate+"&DT_RANGE=&BEG_AMT=0.01&END_AMT=999999999999&TOT_REC_NUM=10&t="+now.getTime(), function(html){
	var found = false;
	var regExp = /<td>(.+?)<\/td>[^<]+<td>.+?ÕÛ<\/td>[^<]+<td>(.+?)<\/td>[^<]+<td><a id="(\d+)_/ig;
	var r = null;
	while((r = regExp.exec(html))!=null) {
		var value = parseFloat(r[1].replace(",", ""));
		var id = r[3];
		if(value>49) {
			console.log("Yes.");
			if(value<1001 && !found) {
				window.open("https://cmpay.10086.cn/eta/buy_ticket_info.xhtml?BON_ID="+id+"&BON_RAT="+rate);
			}
		}
		found = true;
		console.log(value + ", " + r[2] + ", " + id);
	}
	if(!found) {
	    console.log(html);
	}
});
//---------------------------------------------- 
//Dear guest code viewer, It's your turn to write the code. If you have any idea, please kindly let me know. Thanks!