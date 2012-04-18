//http://www.stats.gov.cn/tjbz/xzqhdm/
//http://www.gov.cn/test/2005-06/15/content_18253.htm

function formatDate(year, month, day) {
	if(!isDate(year, month, day)) {
		return "";
	}
	return year + (month<9?"0":"") + month + (day<10?"0":"") + day;
}

function parseDate(dateString) {
	var year = Number(dateString.substring(0, 4));
	var month = Number(dateString.substring(4, 6));
	var day = Number(dateString.substring(6, 8));
	if(isDate(year, month, day)) {
		return new Date(year, month-1, day);
	} else {
		return null;
	}
}

function isDateValid(dateString) {
	return isDate(Number(dateString.substring(0, 4)), Number(dateString.substring(4, 6)), Number(dateString.substring(6, 8)));
}

function isDate(year, month, day) {
	month = month-1;
	var date = new Date(year, month, day);
	return (date.getFullYear()==year) && (date.getMonth()==month) && (date.getDate()==day);
}

function isValid(id) {
	if(id.length==15 || id.length==18) {
		var digitLen = 15;		
		var areaCode = id.substring(0, 6);
		var birthday;
		
		if(id.length==15) {
			digitLen = 15;
			birthday = "19" + id.substring(6, 12);
		} else {
			digitLen = 17;
			birthday = id.substring(6, 14);
			var checksum = id.charAt(17).toUpperCase();
			if((checksum<"0" || checksum>"9") && checksum!="X") {
				console.log("The last character " + checksum + " is invalid (should be 1-9 or X).");
				return -1;
			}
		}
		
		//check digits
		for(var i=0; i<digitLen; i++) {
			if(id.charAt(i)<"0" || id.charAt(i)>"9") {
				console.log("Invalid character at the index " + i + ".");
				return -1;
			}
		}
		
		//asert(birthday.isDigit)
		if(!isDateValid(birthday)) {
			console.log("Birthday part (" + birthday + ") is invalid.");
			return -1;
		}
		
		//checksum
		if(id.length==18) {
			var cs = calcChecksum(id.substring(0,17));
			if(checksum!=cs) {
				console.log("Wrong checksum " + checksum + ", should be " + cs + ".");
				return 1;
			}
		}
		
		return 0;
	} else {
		console.log("Invalid length, should have 15 or 18 characters.");
		return -1;
	}
}

function parseId(id) {
	if(isValid(id)==0) {
		var idInfo = {area:null, birthday:null, gender:false};
		idInfo.area = getArea(id.substring(0, 6));
		var genderCode;
		if(id.length==15) {
			idInfo.birthday = parseDate(id.substring(6, 12));
			genderCode = parseInt(id.substring(15, 16));
		} else {
			idInfo.birthday = parseDate(id.substring(6, 14));
			genderCode = parseInt(id.substring(16, 17));
		}
		idInfo.gender = (genderCode % 2==1);
		return idInfo;
	}
	return null;
}

function calcChecksum(id) {
	//511002198809200315
	var wi = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1];
	var table = ["1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"];
	var sum = 0;
	for(var i=0; i<id.length; i++) {
		sum += parseInt(id.charAt(i)) * wi[i];
	}
	return table[sum % 11];
}

function findCodeIndex(codes, code) {
	var low = 0, high = codes.length-1, mid = parseInt(high/2);
	while(low<=high) {
		//console.log(low + "," + mid + "," + high);
		if(code==codes[mid]) {
			return mid;
		}
		if(code<codes[mid])high = mid-1;
		else low = mid+1;
		mid = parseInt((low+high)/2);
	}
	return -1;
}

function findIndex(index, codes, code, max) {
	while(index<codes.length) {
		if(codes[index]>max)break;
		if(codes[index]==code) {
			return index;
		}
		index++;
	}
	return -1;
}

function getArea(area6) {
	var provinceCode = parseInt(area6.substring(0, 2)+"0000");
	var cityCode = parseInt(area6.substring(0, 4)+"00");
	var districtCode = parseInt(area6);
	var area = {province:"", city:"", district:""};
	
	var index = findCodeIndex(codes, provinceCode);
	if(index==-1) {
		index = findCodeIndex(oldCodes, provinceCode);
		if(index!=-1) {
			area.province = oldNames[index];
		}
		return area;
	}
	area.province = names[index];
	
	var max = parseInt(area6.substring(0, 2)+"9999");
	index = findIndex(index+1, codes, cityCode, max);
	
	if(index!=-1) {
		area.city = names[index];
		max = parseInt(area6.substring(0, 4)+"99");
		index = findIndex(index+1, codes, districtCode, max);
		if(index!=-1) {
			area.district = names[index];
		} else {
			index = findCodeIndex(oldCodes, districtCode);
			if(index!=-1) {
				area.district = oldNames[index];
			}
		}
	} else {
		index = findCodeIndex(oldCodes, cityCode);
		if(index!=-1) {
			area.city = oldNames[index];
			max = parseInt(area6.substring(0, 4)+"99");
			index = findIndex(index+1, oldCodes, districtCode, max);
			if(index!=-1) {
				area.district = oldNames[index];
			}
		}
	}
	return area;
}

function genProvince() {
	for(var i=0; i<codes.length; i++) {
		
	}
}

function genAllId(area6, birthday8, gender1) {
	for(var i=gender1; i<1000; i+=2) {
		console.log(genId(area6, birthday8, (i<10?"00":(i<100?"0":""))+i));
	}
}

function genId(area6, birthday8, random3) {
	var id = area6 + birthday8 + random3;
	return id + calcChecksum(id);
}
