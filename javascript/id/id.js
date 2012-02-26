//http://www.stats.gov.cn/tjbz/xzqhdm/
function checksum(id) {
	//511002198809200315
	var wi = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1];
	var table = ["1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"];
	var sum = 0;
	for(var i=0; i<id.length; i++) {
		sum += parseInt(id.charAt(i)) * wi[i];
	}
	return table[sum % 11];
}

function getBirthday(year, month, day) {
	var date = new Date(year, month-1, day);
	//isDate(date)
	return date.getFullYear() + (date.getMonth()<9?"0":"") + (date.getMonth()+1) + (date.getDate()<10?"0":"") + date.getDate();
}

function genAllId(area6, birthday8, gender1) {
	var id;
	for(var i=gender1; i<1000; i+=2) {
		console.log(genId(area6, birthday8, (i<10?"00":(i<100?"0":""))+i));
	}
}

function genId(area6, birthday8, random3) {
	var id = area6 + birthday8 + random3;
	return id + checksum(id);
}