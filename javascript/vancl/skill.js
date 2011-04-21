/**
 * http://skill.vancl.com/
 */
function updateLED() {
	var buyId = _GLOBAL.currentItem.Id;
	var nowTime = _GLOBAL.currentTime;
	var remaingCount = _GLOBAL.currentItem.RemaingCount;
	if (buyId == 0 || remaingCount == 0) {
		if (_GLOBAL.currentItem.CountDownTimer) {
			window.clearInterval(_GLOBAL.currentItem.CountDownTimer);
		}
	}
	if (buyId > 0) {
		var timeSpanObj = $("#currentItemTimeSpan_" + _GLOBAL.currentItem.Id);
		var btnDoSkillObj = $("#btnSkillWrapper");
		var startTime = _GLOBAL.currentItem.StarTime;
		var endTime = _GLOBAL.currentItem.EndTime;
		if (nowTime < startTime) {
			var _diff = parseInt((startTime - nowTime) / 1000);
			var _hour = get2Num(Math.floor(_diff % 86400 / 3600));
			var _minute = get2Num(Math.floor(_diff % 3600 / 60));
			var _second = get2Num(Math.floor(_diff % 60));
			timeSpanObj.html(_hour + (":" + _minute + ":" + _second));
		} 
			timeSpanObj.html("00:00:00");
			if (nowTime < endTime) {
					btnDoSkillObj.html("<img src=\"" + _GLOBAL.imageUrl + _GLOBAL.btnImgArr.play + "\" flag=\"play\" class=\"kill-btn\" alt=\"\u6B63\u5728\u8FDB\u884C\" />").click(function () {showBuyingWindow(buyId, $(this).find("img"));requestRemaingCount(buyId);});
			}
			if (_GLOBAL.currentItem.CountDownTimer) {
				clearInterval(_GLOBAL.currentItem.CountDownTimer);
			}
	}
}

var stop = false;
function showCode() {
	$("#imgValidateCode").load(function(){
		$("#txtValidateCode").focus();
	});
	$("#imgValidateCode").error(function(){
		if(!stop)changeImageValidate();
	});
	$("#txtValidateCode").blur(function(){
		if($("#txtValidateCode").val()!="") {
			$("#btn_qiangou").click();
		} else {
			$("#txtValidateCode").focus();
			changeImageValidate();
		}
	});
	changeImageValidate();
}

// wait util the time is up
var now = new Date();
var atTime = new Date(now.getFullYear(),now.getMonth(),now.getDate(), 15, 59, 56, 0);
document.title=atTime.toString();
setTimeout(function(){
		showCode();
	}, atTime.getTime()-now.getTime()
);
void(0);

//clear potential infinite loop
//javascript:stop = true;void(0);