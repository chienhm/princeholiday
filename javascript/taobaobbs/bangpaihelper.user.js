// ==UserScript==
// @name           BangPaiHelper
// @namespace      http://www.cdntaobao.com
// @include        http://bangpai.taobao.com/*
// ==/UserScript==

/* 1. 必须先输入验证码，内容补充上几个a
 * 2. 打开图片上传窗口，填上图片名称
 * 3. 截图后连击几次，待图片传上后自动提交
 * 4. 成功后立即返回订单付款（记得支付宝充钱）
 */
var timer = null;
var editorFrm = null;
var title = document.title;

function helper() {
	var frmLen = document.getElementsByTagName("iframe").length;
	console.log(frmLen);
	if(frmLen==0) return;
	for(var i=0; i<frmLen; i++) {
		if(document.getElementsByTagName("iframe")[i].title=="kissy-editor") {
			//console.log("editor");
			editorFrm = document.getElementsByTagName("iframe")[i].contentWindow;
			break;
		}
	}
	if(editorFrm!=null) {
		timer = setInterval(function(){checkImg();}, 100);
	}
}

function checkImg() {
	var frameBody = editorFrm.document.body;
	var content = frameBody.innerHTML;
	var verifyCode = document.getElementById("checkCodeInput").value;
	if(content=="" || content=="<p><br></p>" || verifyCode=="" || verifyCode.indexOf("验证码")!=-1) {
		document.title = "!!!!!!!!!!!!!回复内容必须大于8字符，验证码先输入!!!!!!!!!!!!!!";
		//frameBody.innerHTML = content + "aaaaaaa";
		//console.log("["+verifyCode+"][点击输入验证码]");
		return;
	}
	document.title = title;
	console.log("waiting image ... ");
	if(content.indexOf("img")!=-1) {
		clearInterval(timer);
		console.log(content);
		document.getElementById("submitBtn").click();
	}
}

setTimeout(function(){helper();}, 100);