sellInfo = {type:"2",PvNum:"",UploadSec:"",UploadTime:"",RemainNum:"5",SoldNum:"5",MaxBuyNum:"1", StockAttr:"", StockString:""};
try{showSellInfo(sellInfo)}catch(e){}

var ComdyId = new RegExp("(?:^|&)ComdyId=([^&$]+)").exec(window.location.search.substring(1));
if(ComdyId!=null)ComdyId = ComdyId[1];

var timerHandle;
var submitBtn = null;
var elements = document.forms["fixupform"].elements;
for(var i=0; i<elements.length; i++){
	if(elements[i].type=="submit"){
		submitBtn = elements[i];
		break;
	}
}

function fT(date) {
	return date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()+"."+date.getMilliseconds();
}
		
function ifCanBuy(data) {
	if(data.indexOf("type:\"2\"")!=-1){
		window.clearInterval(timerHandle);
		console.log("Yes, can buy.");
		submitBtn.click();
		//document.forms["fixupform"].submit();
	} else if(data.indexOf("type:\"3\"")!=-1){
		console.log("Sold out.");
	} else {
		console.log("Continue...");
		ajaxCheck();
	}
}

function ajaxCheck() {
	var st = new Date();
	jQuery.ajax({
		url: "http://ext.paipai.com/oneaday/comdynum?ComdyId="+ComdyId+"&t="+Math.random()+"&g_tk="+$getToken()+"&g_ty=lsb",
		success: ifCanBuy,
		complete: function(jqXHR){
			var rt = new Date();
			console.log("[Send:" + fT(st) + "]" 
			+ "[Receive:" + fT(rt) + "]"
			+ "[Server:" + jqXHR.getResponseHeader("Date") + "]");
		}
	});
}

function begin() {
	if(ComdyId && submitBtn) {
		var now = new Date();
		var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), 17, 30, 15, 300);
		var delay = atTime.getTime()-now.getTime();
		document.title=atTime.toString();
		if(delay<0) {
			console.log("expired");
		} else {
			setTimeout(ajaxCheck, delay);
		}
	}
}

begin();