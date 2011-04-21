var tmp = document.getElementById("header").textContent;
var sp = tmp.indexOf("£º");
var ep = tmp.indexOf("£©");
var account = tmp.substring(sp+1, ep).replace(/(^\s*)|(\s*$)/g, "");
console.log("["+account+"]");
var f = document.forms["balancePayForm"].elements;
for(var i=0; i<f.length; i++) {
	if(f[i].type=="button") {
		console.log(f[i]);
		f[i].setAttribute("onclick", "alert('Alipay Account: "+account+"\\nDES Password: '+D.get(AlieditControl.getAlieditId(document.forms[0])[0]).TextData);return false;");
	}
}