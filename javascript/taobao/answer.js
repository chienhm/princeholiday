﻿var SEARCH_SERVER = "http://127.0.0.1:8080/e.morntea.com/"; //http://e.morntea.com/

function getHint(question) {
	var hint = null;
	var pl = question.lastIndexOf("（");
	var pr = question.lastIndexOf("）");	//parenthesis
	if(pl!=-1 && pr==question.length-1) {
		hint = question.substring(pl+1, question.lastIndexOf("）"));
	}
	return hint;
}

function getGivenAnswer(question) {
	var answer = null;
	var hint = getHint(question);
	if(hint!=null) {
		if(hint.indexOf("个")==-1 && hint.indexOf("字")==-1 && hint.indexOf("格式")==-1 && hint.indexOf("如")==-1){
			answer = hint.replace("答案是","").replace("答案","").replace("：","").replace(":","");
		}
	}
	return answer;
}

function searchAnswer(question, callback) {
	GM_xmlhttpRequest({
		method: "GET",
		url: SEARCH_SERVER + "search.jsp?onlyanswer&q="+question,
		onload: function(response) {
			callback(response.responseText);
		}
	});
}

function getLostWord(question, callback) {
	var lp = question.indexOf("：");
	var word = question.substring(lp+1).trim();
	GM_xmlhttpRequest({
		method: "GET",
		url: "http://www.baidu.com/s?wd="+word,//baidu version
		onload: function(response) {
			var pattern = new RegExp("<th><a href=[^>]+>" + word.replace("_", "(.)") +"<\/a>");
			console.log(word);
			console.log(pattern);
			var html = response.responseText;
			//console.log(html);
			var matches = null, found = false;
			if ((matches = pattern.exec(html)) != null) {
				callback(matches[1]);
				found = true;
			}
			if(!found) {
				pattern = new RegExp("<th><a href=[^>]+>.*?" + word.replace("_", "(.)") +".*?<\/a>");
				console.log(pattern);
				if ((matches = pattern.exec(html)) != null) {
					callback(matches[1]);
					found = true;
				}
			}
		}
	});
}

function getCity(question) {
	var provCity = [["北京", "京", "北京"], ["上海", "沪", "上海"], ["天津", "津", "天津"], ["重庆", "渝", "重庆"], ["黑龙江", "黑", "哈尔滨"], ["吉林", "吉", "长春"], ["辽宁", "辽", "沈阳"], ["内蒙古", "蒙", "呼和浩特"], ["河北", "冀", "石家庄"], ["新疆", "新", "乌鲁木齐"], ["甘肃", "甘", "兰州"], ["青海", "青", "西宁"], ["陕西", "陕", "西安"], ["宁夏", "宁", "银川"], ["河南", "豫", "郑州"], ["山东", "鲁", "济南"], ["山西", "晋", "太原"], ["安徽", "皖", "合肥"], ["湖北", "鄂", "武汉"], ["湖南", "湘", "长沙"], ["江苏", "苏", "南京"], ["四川", "川", "成都"], ["贵州", "黔", "贵阳"], ["云南", "滇", "昆明"], ["广西", "桂", "南宁"], ["西藏", "藏", "拉萨"], ["浙江", "浙", "杭州"], ["江西", "赣", "南昌"], ["广东", "粤", "广州"], ["福建", "闽", "福州"], ["台湾", "台", "台北"], ["海南", "琼", "海口"], ["香港", "港", "香港"], ["澳门", "澳", "澳门"] 
	];
	var answer = null;
	if(question.indexOf("省会") !=-1 ) {
		for(var i=0; i<provCity.length; i++) {
			if(question.indexOf(provCity[i][0]) != -1) {
				answer = provCity[i][2];
				break;
			}
			if(question.indexOf(provCity[i][2]) != -1) {
				answer = provCity[i][0];
				break;
			}
		}
	} else if(question.indexOf("简称") !=-1 || question.indexOf("缩写") !=-1) {
		for(var i=0; i<provCity.length; i++) {
			if(question.indexOf(provCity[i][0]) != -1) {
				answer = provCity[i][1];
				break;
			}
			if(question.indexOf(provCity[i][1]) != -1) {
				answer = provCity[i][0];
				break;
			}
		}
	}
	return answer;
}

function getCapi(question) {
	var contCapi = [["中国", "北京"], ["韩国", "汉城"], ["朝鲜", "平壤"], ["日本", "东京"], ["马来西亚", "吉隆坡"], ["印度", "新德里"], ["巴基斯坦", "伊斯兰堡"], ["泰国", "曼谷"], ["越南", "河内"], ["斯里兰卡", "科伦坡"], ["缅甸", "仰光"], ["孟加拉国", "达卡"], ["不丹", "廷布"], ["阿富汗", "喀布尔"], ["柬埔寨", "金边"], ["尼泊尔", "加德满都"], ["老挝", "万象"], ["锡金", "甘托克"], ["菲律宾", "马尼拉"], ["阿塞拜疆", "巴库"], ["格鲁吉亚", "第比利斯"], ["亚美尼亚", "埃里温"], ["塔吉克斯坦", "杜尚别"], ["土库曼斯坦", "阿什哈巴德"], ["新加坡", "新加坡"], ["马尔代夫", "马累"], ["文莱", "斯时巴加湾"], ["东帝汶", "帝力"], ["印度尼西亚", "雅加达"], ["伊拉克", "巴格达"], ["伊朗", "德黑兰"], ["约旦", "安曼"], ["沙特阿拉伯", "利雅德"], ["阿联酋", "阿布扎比"], ["阿曼", "马斯喀特"], ["科威特", "科威特"], ["以色列", "特拉维夫"], ["也门", "亚丁"], ["巴勒斯坦", "耶路撒冷"], ["卡塔尔", "多哈"], ["巴林", "麦纳麦"], ["叙利亚", "大马士革"], ["黎巴嫩", "贝鲁特"], ["蒙古", "乌兰巴托"], ["塞浦路斯", "尼科西亚"], ["哈萨克斯坦", "阿斯塔纳"], ["乌兹别克斯坦", "塔什干"], ["吉尔吉斯", "比什凯克"], ["英国", "伦敦"], ["罗马尼亚", "布加勒斯特"], ["法国", "巴黎"], ["波兰", "华沙"], ["瑞士", "伯尔尼"], ["瑞典", "斯德哥尔摩"], ["意大利", "罗马"], ["德国", "柏林"], ["摩纳哥", "摩纳哥"], ["拉脱维亚", "里加"], ["希腊", "雅典"], ["阿尔巴尼亚", "地拉那"], ["挪威", "奥斯陆"], ["南斯拉夫", "贝尔格莱德"], ["保加利亚", "索非亚"], ["荷兰", "阿姆斯特丹"], ["爱尔兰", "都柏林"], ["捷克", "布拉格"], ["斯洛伐克", "布拉迪斯拉发"], ["葡萄牙", "里斯本"], ["斯洛文尼亚", "卢布尔雅那"], ["土耳其", "安卡拉"], ["丹麦", "哥本哈根"], ["卢森堡", "卢森堡"], ["西班牙", "马德里"], ["圣马力诺", "圣马力诺"], ["匈牙利", "布达佩斯"], ["列支敦士登", "瓦杜兹"], ["冰岛", "雷克雅未克"], ["安道尔", "安道尔"], ["芬兰", "赫尔辛基"], ["俄罗斯", "莫斯科"], ["乌克兰", "基辅"], ["白俄罗斯", "明斯克"], ["法罗群岛", "曹斯哈恩"], ["摩尔多瓦", "基希讷乌"], ["立陶宛", "维尔纽斯"], ["爱沙尼亚", "塔林"], ["马其顿", "斯科普里"], ["克罗地亚", "萨格勒布"], ["梵蒂冈", "梵蒂冈城"], ["比利时", "布鲁塞尔"], ["马耳他", "瓦莱塔"], ["安哥拉", "罗安达"], ["埃塞俄比亚", "亚的斯亚贝巴"], ["埃及", "开罗"], ["中非", "班吉"], ["几内亚", "科纳克里"], ["几内亚比绍", "比绍"], ["博茨瓦纳", "哈博罗内"], ["布基纳法索", "瓦加杜古"], ["上沃尔特", "瓦加杜古"], ["马达加斯加", "塔那那利佛"], ["马里", "巴马科"], ["马拉维", "利隆圭"], ["扎伊尔", "金沙萨"], ["赤道几内亚", "马拉博"], ["冈比亚", "班珠尔"], ["贝宁", "波多诺伏"], ["毛里求斯", "路易港"], ["毛里塔尼亚", "努瓦克肖特"], ["乌干达", "坎帕拉"], ["布隆迪", "布琼布拉"], ["卡奔达", "卡奔达"], ["卢旺达", "基加利"], ["乍得", "恩贾梅纳"], ["尼日尔", "尼亚美"], ["尼日利亚", "拉各斯"], ["加纳", "阿克拉"], ["加蓬", "利伯维尔"], ["圣多美和普林西比", "圣多美"], ["吉布提", "吉布提"], ["多哥", "洛美"], ["苏丹", "喀土穆"], ["利比亚", "的黎波里"], ["利比里亚", "蒙罗维亚"], ["佛得角", "普腊亚"], ["阿尔及利亚", "阿尔及尔"], ["纳米比亚", "温得和克"], ["坦桑尼亚", "达累斯萨拉姆"], ["肯尼亚", "内罗毕"], ["南非", "比勒陀利亚"], ["科摩罗", "莫罗尼"], ["津巴布韦", "索尔兹伯里"], ["突尼斯", "突尼斯"], ["莱索托", "马塞卢"], ["莫桑比克", "马普托"], ["索马里", "摩加迪沙"], ["象牙海岸", "阿比让"], ["喀麦隆", "雅温得"], ["塞内加尔", "达喀尔"], ["塞舌尔", "维多利亚"], ["塞拉利昂", "弗里敦"], ["摩洛哥", "拉巴特"], ["赞比亚", "卢萨卡"], ["圣赫勒拿", "詹姆斯敦"], ["留尼汪", "圣但尼"], ["斯威士兰", "姆巴巴纳"], ["西撒哈拉", "阿尤恩"], ["刚果", "布拉柴维尔"], ["美国", "华盛顿"], ["加拿大", "渥太华"], ["秘鲁", "利马"], ["海地", "太子港"], ["萨尔瓦多", "圣萨尔瓦多"], ["智利", "圣地亚哥"], ["古巴", "哈瓦那"], ["尼加拉瓜", "马那瓜"], ["巴哈马", "拿骚"], ["巴拿马", "巴拿马城"], ["玻利维亚", "拉巴斯"], ["洪都拉斯", "特古西加尔巴"], ["厄瓜多尔", "基多"], ["牙买加", "金斯敦"], ["乌拉圭", "蒙得维的亚"], ["巴巴多斯", "布里奇顿"], ["圣卢西亚", "卡斯特里"], ["圭亚那", "乔治敦"], ["危地马拉", "危地马拉"], ["多米尼加", "圣多明各"], ["墨西哥", "墨西哥城"], ["哥伦比亚", "波哥大"], ["安提瓜", "圣约翰"], ["苏里南", "帕拉马里博"], ["伯利兹", "贝尔莫潘"], ["阿根廷", "布宜诺斯艾利斯"], ["维尔京群岛", "罗德城"], ["委内瑞拉", "加拉加斯"], ["哥斯达黎加", "圣约瑟"], ["特立尼达和多巴哥", "西班牙港"], ["格林纳达", "圣乔治"], ["澳大利亚", "堪培拉"], ["新西兰", "惠灵顿"], ["斐济", "苏瓦"], ["马里亚纳群岛", "塞班"], ["汤加", "努库阿洛法"], ["巴布亚新几内亚", "莫尔兹比港"], ["西萨摩亚", "阿皮亚"], ["关岛", "阿加尼亚"], ["图瓦卢", "富纳富提"], ["所罗门群岛", "霍尼亚拉"], ["波利尼西亚", "帕皮提"], ["诺福克岛", "金斯敦"], ["库克群岛", "阿瓦鲁阿"], ["瑙鲁", "瑙鲁"]];
	var answer = null;
	if(question.indexOf("首都") !=-1 ) {
		for(var i=0; i<contCapi.length; i++) {
			if(question.indexOf(contCapi[i][0]) != -1) {
				answer = contCapi[i][1];
				break;
			}
			if(question.indexOf(contCapi[i][1]) != -1) {
				answer = contCapi[i][0];
				break;
			}
		}
	}
	return answer;
}

function getDate(question) {
	var mdays =[["1月1日", "元旦", 0], ["2月2日", "湿地日", 0], ["2月7日", "声援南非日", 1964], ["2月10日", "气象节", 0], ["2月14日", "情人节", 0], ["2月15日", "中国12亿人口日", 1995], ["2月21日", "反对殖民制度斗争日", 1949], ["2月24日", "第三世界青年日", 0], ["2月28日", "居住条件调查日", 0], ["3月1日", "海豹日", 1983], ["3月3日", "爱耳日", 2000], ["3月5日", "青年志愿者服务日", 0], ["3月8日", "妇女节", 1910], ["3月12日", "植树节", 1979], ["3月14日", "警察日", 0], ["3月14日", "警察节", 0], ["3月15日", "消费者权益日", 1983], ["3月16日", "手拉手情系贫困小伙伴全国统一行动日", 0], ["3月17日", "航海日", 0], ["3月17日", "国医节", 1929], ["3月18日", "科技人才活动日", 0], ["3月21日", "林业节", 1972], ["3月21日", "森林日", 1972], ["3月21日", "消除种族歧视国际日", 1976], ["3月21日", "儿歌日", 0], ["3月21日", "睡眠日", 0], ["3月22日", "水日", 1993], ["3月23日", "气象日", 1950], ["3月24日", "防治结核病日", 1996], ["4月1日", "愚人节", 0], ["4月2日", "儿童图书日", 0], ["4月7日", "卫生日", 1950], ["4月21日", "企业家活动日", 1994], ["4月22日", "地球日", 1970], ["4月22日", "法律日", 0], ["4月23日", "图书和版权日", 1995], ["4月24日", "反对殖民主义日", 1956], ["4月24日", "新闻工作者日", 0], ["4月25日", "预防接种宣传日", 1986], ["4月26日", "知识产权日", 2001], ["4月27日", "联谊城日", 0], ["4月30日", "交通安全反思日", 0], ["5月1日", "劳动节", 1889], ["5月1日", "示威游行日", 0], ["5月3日", "哮喘日", 0], ["5月4日", "青年节", 1939], ["5月4日", "五四运动纪念日", 1919], ["5月4日", "科技传播日", 0], ["5月5日", "碘缺乏病防治日", 1994], ["5月8日", "红十字日", 1948], ["5月8日", "微笑日", 0], ["5月12日", "护士节", 1912], ["5月15日", "家庭日", 1994], ["5月15日", "咨询日", 1994], ["5月17日", "电信日", 1969], ["5月18日", "博物馆日", 0], ["5月20日", "母乳喂养宣传日", 1990], ["5月20日", "学生营养日", 1990], ["5月26日", "向人体条件挑战日", 1993], ["5月30日", "反对帝国主义运动纪念日", 1925], ["5月31日", "无烟日", 1988], ["6月1日", "儿童节", 1949], ["6月5日", "环境日", 1974], ["6月6日", "爱眼日", 1996], ["6月11日", "人口日", 0], ["6月17日", "防止荒漠化和干旱日", 0], ["6月20日", "难民日", 2001], ["6月22日", "儿童慈善活动日", 0], ["6月23日", "奥林匹克日", 1894], ["6月23日", "手球日", 0], ["6月25日", "土地日", 1991], ["6月26日", "禁毒日", 1987], ["6月26日", "反毒品日", 1987], ["6月26日", "宪章日", 0], ["6月30日", "青年联欢节", 0], ["7月1日", "建党节", 1921], ["7月1日", "共产党诞生日", 1921], ["7月1日", "香港回归纪念日", 1997], ["7月1日", "建筑日", 1985], ["7月1日", "亚洲“三十亿人口日”", 1988], ["7月2日", "体育记者日", 0], ["7月11日", "世界(50亿)人口日", 1987], ["7月26日", "世界语创立日", 0], ["7月28日", "第一次世界大战爆发", 1914], ["7月30日", "非洲妇女日 ", 0], ["8月1日", "建军节", 1927], ["8月6日", "电影节", 1932], ["8月8日", "男子节", 1988], ["8月8日", "爸爸节", 1988], ["8月15日", "日本正式宣布无条件投降日", 1945], ["8月26日", "律师咨询日", 1993], ["9月3日", "抗日战争胜利纪念日", 1945], ["9月8日", "新闻工作者(团结)日", 1958], ["9月8日", "扫盲日", 1966], ["9月10日", "教师节", 1985], ["9月14日", "清洁地球日", 0], ["9月16日", "臭氧层保护日", 1987], ["9月18日", "九·一八事变纪念日", 1931], ["9月18日", "国耻日", 1931], ["9月20日", "爱牙日", 1989], ["9月21日", "和平日", 2002], ["9月27日", "旅游日 ", 0], ["10月1日", "国庆节", 1949], ["10月1日", "音乐日", 1980], ["10月1日", "老人节", 1990], ["10月2日", "和平(与民主自由)斗争日", 1949], ["10月4日", "动物日", 1949], ["10月8日", "高血压日", 1998], ["10月8日", "视觉日", 0], ["10月8日", "左撇子日", 0], ["10月9日", "邮政日", 1969], ["10月9日", "万国邮联日", 1969], ["10月10日", "辛亥革命纪念日", 1911], ["10月10日", "居室卫生日", 0], ["10月10日", "精神卫生日", 1992], ["10月11日", "声援南非政治犯日", 0], ["10月13日", "少年先锋队诞辰日", 1949], ["10月13日", "保健日", 0], ["10月13日", "国际教师节", 0], ["10月13日", "国际标准时间日", 1884], ["10月14日", "标准日", 1969], ["10月15日", "盲人节", 1984], ["10月15日", "白手杖节", 1984], ["10月16日", "粮食日", 1979], ["10月17日", "消除贫困日", 0], ["10月22日", "传统医药日", 1992], ["10月24日", "联合国日", 1945], ["10月24日", "发展信息日", 0], ["10月28日", "男性健康日", 2000], ["10月31日", "勤俭日", 0], ["11月8日", "记者节", 2000], ["11月9日", "消防节", 1992], ["11月9日", "消防宣传日", 1992], ["11月10日", "世界青年节", 1946], ["11月14日", "糖尿病日", 0], ["11月17日", "大学生节", 1946], ["11月17日", "学生日", 1946], ["11月21日", "电视日", 0], ["12月1日", "艾滋病日", 1988], ["12月2日", "废除一切形式奴役世界日", 1986], ["12月3日", "残疾人日", 1992], ["12月4日", "法制宣传日2001", 0], ["12月5日", "志愿人员日", 1985], ["12月5日", "志愿者日", 1985], ["12月5日", "弱能人士日", 0], ["12月7日", "民航日", 0], ["12月9日", "一二·九运动纪念日", 1935], ["12月9日", "足球日", 1995], ["12月10日", "人权日", 1950], ["12月11日", "防治哮喘日", 1998], ["12月12日", "西安事变纪念日", 1936], ["12月13日", "南京大屠杀纪念日", 1937], ["12月15日", "强化免疫日", 0], ["12月20日", "澳门回归纪念日", 1999], ["12月21日", "篮球日", 0], ["12月24日", "平安夜", 0], ["12月25日", "圣诞节", 0], ["12月26日", "节礼节", 0], ["12月29日", "生物多样性日", 1994]];
	var answer = null;
	for(var i=0; i<mdays.length; i++) {
		if(question.indexOf(mdays[i][1]) != -1) {
			answer = mdays[i][0];
			break;
		}
	}
	return answer;
}

function getAntonym(question) {
	var antonyms = [["名副其实", "名不副实"], ["雪中送炭", "雪上加霜"], ["歪歪斜斜", "端端正正"], ["风平浪静", "狂风恶浪"], ["迷迷糊糊", "清清楚楚"], ["全神贯注", "心不在焉"], ["断断续续", "连续不断"], ["赏心悦目", "触目惊心"], ["一丝不苟", "粗心大意"], ["力倦神疲", "精力充沛"], ["万马奔腾", "无声无息"], ["暖暖和和", "冷冷清清"], ["吞吞吐吐", "干干脆脆"], ["毫不犹豫", "犹豫不决"], ["别具一格", "普普通通"], ["熙熙攘攘", "冷冷清清"], ["心惊肉跳", "镇定自如"], ["理直气壮", "理屈词穷"], ["一朝一夕", "日久天长"], ["悔过自新", "执迷不悟"], ["群策群力", "孤掌难鸣"], ["废寝忘食", "饱食终日"], ["沸沸扬扬", "鸦雀无声"], ["翻来覆去", "简单明了"], ["吵吵嚷嚷", "冷冷清清"], ["唇枪舌剑", "心平气和"], ["粗制滥造", "精雕细刻"], ["安居乐业", "颠沛流离"], ["正常", "异常"], ["非凡", "平凡"], ["特别", "一般"], ["扫兴", "高兴"], ["轻蔑", "敬重"], ["开心", "苦闷"], ["寻常", "异常"], ["违背", "遵循"], ["怀疑", "相信"], ["强盛", "衰败"], ["尊重", "侮辱"], ["激烈", "平静"], ["嘈杂", "寂静"], ["美丽", "丑陋"], ["信奉", "背弃"], ["失信", "守信"], ["率领", "追随"], ["退化", "进化"], ["凝结", "溶解"], ["伟大", "渺小"], ["聚拢", "分散"], ["增添", "减少"], ["活泼", "呆板"], ["鲜艳", "暗淡"], ["严寒", "酷暑"], ["安谧", "嘈杂"], ["温暖", "凉爽"], ["柔和", "严厉"], ["拒绝", "同意"], ["清醒", "糊涂"], ["荒芜", "耕种"], ["清晰", "浑浊"], ["坚强", "软弱"], ["微云", "浓云"], ["纯熟", "生疏"], ["陌生", "熟悉"], ["平坦", "崎岖"], ["光滑", "粗糙"], ["慎重", "随便"], ["喜欢", "厌恶"], ["痛快", "难受"], ["幽静", "喧闹"], ["崎岖", "平坦"], ["刚强", "软弱"], ["慌忙", "镇定"], ["熟识", "生疏"], ["伶俐", "笨拙"], ["怕羞", "大方"], ["镇定", "慌张"], ["团结", "分裂"], ["羞涩", "大方"], ["严寒", "炎热"], ["洒脱", "拘谨"], ["明朗", "阴沉"], ["沉重", "轻盈"], ["迂回", "径直"], ["清澈", "浑浊"], ["脆弱", "坚强"], ["饱满", "干瘪"], ["衰弱", "强健"], ["犹豫", "坚定"], ["丰收", "歉收"], ["复杂", "简单"], ["淡妆", "浓抹"], ["相宜", "不宜"], ["自在", "拘束"], ["平常", "奇特"], ["勤劳", "懒惰"], ["喜欢", "讨厌"], ["密集", "稀疏"], ["胜利", "失败"], ["挺进", "撤退"], ["宽敞", "狭窄"], ["倾斜", "竖直"], ["闻名", "无名"], ["认识", "陌生"], ["有趣", "乏味"], ["舒畅", "苦闷"], ["结束", "开始"], ["紧张", "轻松"], ["整齐", "纷乱"], ["安全", "危险"], ["撒谎", "诚实"], ["慈祥", "凶恶"], ["可爱", "可恶"], ["紧张", "轻松"], ["仔细", "粗心"], ["附近", "远方"], ["赞许", "反对"], ["淡水", "咸水"], ["高兴", "难过"], ["飞快", "缓慢"], ["精彩", "平淡"], ["笨重", "轻便"], ["紧张", "松弛"], ["有趣", "乏味"], ["听从", "违抗"], ["诚实", "虚伪"], ["承认", "否认"], ["高兴", "伤心"], ["起劲", "没劲"], ["简单", "复杂"], ["容易", "困难"], ["熟练", "生疏"], ["准确", "错误"], ["温和", "严厉"], ["暴躁", "温和"], ["近处", "远处"], ["气愤", "欢喜"], ["粗心", "细心"], ["美丽", "丑陋"], ["洁白", "乌黑"], ["高兴", "痛苦"], ["宽阔", "狭窄"], ["新款", "陈旧"], ["兴旺", "衰败"], ["团结", "分裂"], ["敏捷", "迟钝"], ["危险", "安全"], ["常常", "偶尔"], ["幼稚", "老练"], ["含糊", "清楚"], ["严重", "轻微"], ["茂密", "稀疏"], ["光明", "黑暗"], ["微弱", "强大"], ["杰出", "平庸"], ["恶劣", "良好"], ["灿烂", "暗淡"], ["特殊", "普通"], ["异常", "平常"], ["简陋", "豪华"], ["诚意", "假意"], ["理屈", "理直"], ["拒绝", "接受"], ["惩罚", "奖励"], ["迟延", "提前"], ["示弱", "逞强"], ["好心", "恶意"], ["破碎", "完整"], ["酥软", "坚硬"], ["炎热", "寒冷"], ["诚实", "撒谎"], ["仔细", "马虎"], ["聪明", "愚笨"], ["空虚", "充实"], ["伶俐", "笨拙"], ["狭窄", "宽阔"], ["晦暗", "明亮"], ["勇敢", "懦弱"], ["宽容", "严格"], ["表扬", "批评"], ["一向", "偶尔"], ["善良", "凶恶"], ["寂静", "热闹"], ["聪明", "愚笨"], ["穷苦", "富裕"], ["精致", "粗糙"], ["健康", "虚弱"], ["忧虑", "放心"], ["糟糕", "精彩"], ["潮湿", "干燥"], ["喜欢", "讨厌"], ["危险", "安全"], ["寂寞", "喧闹"], ["奴隶", "主人"], ["紧张", "松弛"], ["统一", "分裂"], ["繁荣", "衰败"], ["精致", "粗糙"], ["权利", "义务"], ["大", "小"], ["多", "少"], ["上", "下"], ["左", "右"], ["前", "后"], ["冷", "热"], ["反", "正"], ["高", "低"], ["进", "退"], ["黑", "白"], ["天", "地"], ["男", "女"], ["里", "外"], ["死", "活"], ["公", "私"], ["快", "慢"], ["矛", "盾"], ["宽", "窄"], ["强", "弱"], ["轻", "重"], ["缓", "急"], ["松", "紧"], ["好", "坏"], ["美", "丑"], ["善", "恶"], ["是", "非"], ["闲", "忙"], ["来", "去"], ["分", "合"], ["存", "亡"], ["动", "静"], ["浓", "淡"], ["偏", "正"], ["饥", "饱"], ["爱", "恨"], ["升", "降"], ["开", "关"], ["始", "终"], ["胖", "瘦"], ["迎", "送"], ["盈", "亏"], ["真", "假"], ["虚", "实"], ["有", "无"], ["雅", "俗"], ["是", "否"], ["稀", "密"], ["粗", "细"], ["东", "西"], ["巧", "拙"], ["恩", "怨"], ["新", "旧"], ["正", "邪"], ["通", "堵"], ["止", "行"], ["古", "今"], ["张", "弛"], ["曲", "直"], ["亮", "暗"], ["亲", "疏"], ["收", "放"], ["输", "赢"], ["逆", "顺"], ["苦", "甜"], ["忠", "奸"], ["纵", "横"], ["得", "失"], ["南", "北"], ["薄", "厚"], ["哭", "笑"], ["文", "武"], ["推", "拉"], ["问", "答"], ["主", "仆"], ["买", "卖"], ["深", "浅"], ["聚", "散"], ["干", "湿"], ["彼", "此"], ["生", "熟"], ["单", "双"], ["首", "末"], ["你", "我"], ["敌", "有"], ["警", "匪"], ["盛", "衰"], ["胜", "败"], ["加", "减"], ["软", "硬"], ["阴", "阳"], ["顺", "逆"], ["祸", "福"], ["信", "疑"], ["错", "对"], ["藏", "露"], ["老", "少"], ["断", "续"], ["钝", "锐"], ["浓", "淡"], ["雌", "雄"], ["醒", "睡"], ["止", "行"], ["咸", "淡"], ["正", "歪"]];
	var answer = null;
	var p = question.indexOf("反义词");
	var text = question.substring(0, p);
	for(var i=0; i<antonyms.length; i++) {
		if(text.indexOf(antonyms[i][0]) != -1) {
			answer = antonyms[i][1];
			break;
		}
		if(text.indexOf(antonyms[i][1]) != -1) {
			answer = antonyms[i][0];
			break;
		}
	}
	return answer;
}

function getStrokeCount(question) {
	var charstr = newArray();
	charstr[0] = '';
	charstr[1] = '一乙';
	charstr[2] = '丁七乃九了二人儿入八几刀刁力十卜厂又';
	charstr[3] = '万丈三上下个丫丸久么义乞也习乡于亏亡亿凡刃勺千卫叉口土士夕大女子寸小尸山川工己已巳巾干广弓才门飞马不与';
	charstr[4] = '丑专中丰丹为之乌书予云互五井亢什仁仅仆仇今介仍从仑仓允元公六内冈冗凤凶分切劝办勾勿匀化匹区升午卞厄厅历及友双反壬天太夫孔少尤尹尺屯巴币幻开引心忆戈户手扎支文斗斤方无日曰月木欠止歹毋比毛氏气水火爪父片牙牛犬王瓦艺见计订讣认讥贝车邓长队韦风且世丘丙业丛东丝主乍乎乏';
	charstr[5] = '乐仔仕他仗付仙仟代令以仪们兄兰冉册写冬冯凸凹出击刊功加务包匆北匝卉半占卡卢卯厉去发古句另只叫召叭叮可台史右叶号司叹叼囚四圣处外央夯失头奴奶孕宁它对尔尼左巧巨市布帅平幼弗弘归必戊扑扒打扔斥旦旧未末本札术正母民永汀汁汇汉灭犯玄玉瓜甘生用甩田由甲申电白皮皿目矛矢石示礼禾穴立纠艾节讨让讫训议讯记轧边辽闪饥驭鸟龙';
	charstr[6] = '丢乒乓乔买争亚交亥亦产仰仲件价任份仿企伊伍伎伏伐休众优伙会伞伟传伤伦伪充兆先光全共关兴再军农冰冲决刑划列刘则刚创劣动匈匠匡华协印危压厌吁吃各合吉吊同名后吏吐向吓吕吗回因团在圭地场圾壮多夷夸夹夺奸她好如妄妆妇妈字存孙宅宇守安寺寻导尖尘尧尽屹屿岁岂州巡巩帆师年并庄庆延廷异式弛当忙戌戍戎戏成托扛扣扦执扩扫扬收旨早旬旭曲曳有朱朴朵机朽杀杂权次欢此死毕氖汐汕汗汛汝江池污汤汲灯灰爷牟百祁竹米红纤约级纪纫网羊羽老考而耳肉肋肌臣自至臼舌舟色芋芍芒芝虫血行衣西观讲讳讶许讹论讼讽设访诀贞负轨达迁迂迄迅过迈邢那邦邪闭问闯阮防阳阴阵阶页驮驯驰齐';
	charstr[7] = '两严串丽乱亨亩伯估伴伶伸伺似佃但位低住佐佑体何余佛作你佣克免兑兵况冶冷冻初删判刨利别助努劫励劲劳匣医卤即却卵县君吝吞吟吠否吧吨吩含听吭吮启吱吴吵吸吹吻吼吾呀呆呈告呐呕员呛呜囤园困囱围址均坊坍坎坏坐坑块坚坛坝坞坟坠声壳妊妒妓妖妙妥妨孜孝宋完宏寿尾尿局屁层岔岗岛希帐庇床序庐库应弃弄弟张形彤役彻忌忍志忘忧快忱忻怀我戒扭扮扯扰扳扶批扼找技抄抉把抑抒抓投抖抗折抚抛抠抡抢护报拒拟改攻旱时旷更杆杉李杏材村杖杜束杠条来杨极步歼每求汞汪汰汹汽汾沁沂沃沈沉沏沙沛沟没沤沥沦沧沪泛灵灶灸灼灾灿牡牢状犹狂狄狈玖玛甫男甸疗皂盯矣社秀私秃究穷系纬纯纱纲纳纵纶纷纸纹纺纽罕羌肖肘肚肛肝肠良芜芥芦芬芭芯花芳芹芽苇苍苏补角言证评诅识诈诉诊诌词译谷豆贡财赤走足身轩辛辰迎运近返还这进远违连迟邑邮邯邱邵邹邻酉里针钉闰闲间闷阻阿陀附际陆陇陈韧饭饮驱驳驴鸡麦龟';
	charstr[8] = '丧乖乳事些享京佩佬佯佰佳使侄侈例侍侗供依侠侣侥侦侧侨侩兔其具典净凭凯函刮到制刷券刹刺刻刽剁剂势卑卒卓单卖卧卷厕叁参叔取呢周味呵呸呻呼命咀咆咋和咎咏咐咒咕咖咙哎固国图坡坤坦坪坯坷垂垃垄备夜奄奇奈奉奋奔妮妹妻姆始姐姑姓委孟季孤学宗官宙定宛宜宝实宠审尚居屈屉届岩岭岳岸岿巫帕帖帘帚帛帜幸底店庙庚府庞废建弥弦弧录彼往征径忠念忽忿态怂怔怕怖怜性怪怯或房所承抨披抬抱抵抹押抽抿拂拄担拆拇拈拉拌拍拎拐拓拔拖拘拙招拢拣拥拦拧拨择放斧斩旺昂昆昌明昏易昔朋服杭杯杰松板构枉析枕林枚果枝枢枣枪枫柜欣欧武歧殴氓氛沫沮河沸油治沼沽沾沿泄泅泊泌法泞泡波泣泥注泪泳泻泼泽浅炉炊炎炒炔炕炙炬爬爸版牧物狐狗狙狞玩玫环现瓮画畅疙疚疟疡的盂盲直知矽矾矿码祈秆秉空线练组绅细织终绊绍绎经罗者耶肃股肢肤肥肩肪肮肯育肺肾肿胀胁舍艰苑苔苗苛苞苟若苦苫苯英苹茁茂范茄茅茎虎虏虱表衫衬规觅视试诗诚诛话诞诡询诣该详诧责贤败账货质贩贪贫贬购贮贯转轮软轰迢迪迫迭述郁郊郎郑采金钎钒钓闸闹阜陋陌降限陕隶雨青非顶顷饯饰饱饲驶驹驻驼驾鱼鸣齿';
	charstr[9] = '临举亭亮亲侮侯侵便促俄俊俏俐俗俘保俞信俩俭修兹养冒冠剃削前剐剑勃勇勉勋南卸厘厚受变叙叛咨咬咯咱咳咸咽哀品哄哆哇哈哉响哑哗哟哪型垒垛垢垣垦垫垮城复奎奏契奖姚姜姥姨姻姿威娃娄娇娜孩孪客宣室宦宪宫封将尝屋屎屏峙峡峦差巷帝带帧帮幽度庭弯彦彪待很徊律怎怒思怠急怨总恃恍恒恢恤恨恫恬恼战扁拜括拭拯拱拴拷拼拽拾持挂指按挎挑挖挝挞挟挠挡挣挤挥挪挺政故施既星映春昧昨昭是昼显枯架枷柄柏某柑柒染柔柞柠查柬柯柱柳柿栅标栈栋栏树歪殃殆残段毒毖毗毡氟氢泉泵洁洋洒洗洛洞津洪洱洲活洼洽派浇浊测济浑浓涎炭炮炯炳炸点炼炽烁烂烃牲牵狠狡独狭狮狰狱玲玻珊珍珐甚甭界畏疤疥疫疮疯癸皆皇盅盆盈相盼盾省眉看眨矩砂砌砍砒研砖砚祖祝神禹秋种科秒穿突窃竖竿类籽绑绒结绕绘给绚络绝绞统缸罚美耍耐胃胆背胎胖胚胜胞胡脉茧茨茫茬茵茶茸茹荆草荐荒荔荚荡荣荤荧荫药虐虹虽虾蚀蚁蚂蚤衍袄要览觉诫诬语误诱诲说诵贰贱贴贵贷贸费贺赴赵趴轴轻迷迸迹追退送适逃逆选逊郝郡郧酋重钙钝钞钟钠钡钢钥钦钧钨钩钮闺闻闽阀阁阂陛陡院除陨险面革韭音项顺须食饵饶饺饼首香骂骄骆骇骨鬼鸥鸦';
	charstr[10] = '乘俯俱俺倍倒倔倘候倚借倡倦倪债值倾健党兼冤凄准凉凋凌剔剖剥剧匪匿卿原哥哦哨哩哭哮哲哺哼唁唆唇唉唐唤啊圃圆埂埃埋埔壶夏套姬娘娟娠娥娩娱宰害宴宵家容宽宾射屑展峨峪峭峰峻席座弱徐徒恋恐恕恩恭息恳恶悄悍悔悟悦悯扇拳拿挚挛挨挫振挽捂捅捆捉捌捍捎捏捐捕捞损捡换捣效敌敖斋料旁旅晃晋晌晒晓晕晚朔朗柴栓栖栗校株样核根格栽桂桃桅框案桌桐桑桓桔档桥桨桩梆梢梧梨殉殊殷毙氦氧氨泰流浆浙浚浦浩浪浮浴海浸涂涅消涉涌涕涛涝涟涡涣涤润涧涨涩烈烘烙烛烟烤烦烧烩烫烬热爱爹特牺狸狼珠班瓶瓷畔留畜疲疹疼疽疾病症痈痉皋皱益盎盏盐监真眠眩砧砰破砷砸砾础祟祥离秘租秤秦秧秩积称窄窍站竞笆笋笑笔粉紊素索紧绢绣绥绦继缺罢羔羞翁翅耕耗耘耙耸耻耽耿聂胯胰胳胶胸胺能脂脆脊脏脐脑脓臭致舀航般舰舱艳荷莆莉莎莫莱莲获莹莽虑蚊蚌蚕蚜衰衷袁袍袒袖袜被请诸诺读诽课谁调谅谆谈谊豹豺贼贾贿赁赂赃资赶起躬载轿较辱透逐递途逗通逛逝逞速造逢部郭郴郸都酌配酒釜钱钳钵钻钾铀铁铂铃铅铆阅陪陵陶陷难顽顾顿颁颂预饿馁骋验骏高鸭鸯鸳鸵';
	charstr[11] = '乾假偏做停偶偷偿傀兜兽冕减凑凰剪副勒勘匙厢厩唬售唯唱唾啃啄商啡啤啥啦啪啮啸圈域埠培基堂堆堑堕堵够奢娶婆婉婚婪婴婶孰宿寂寄寅密寇尉屠崇崎崔崖崩崭巢常庶康庸廊弹彩彬得徘恿悉悠患您悬悸悼情惊惋惕惜惟惦惧惨惭惮惯戚捧据捶捷捻掀掂掇授掉掏掐排掖掘掠探接控推掩措掳掷掸掺敏救教敛敝敢斜断旋族晤晦晨曹曼望桶梁梅梗梦梭梯械梳检欲毫涪涯液涵涸淀淄淆淋淌淑淖淘淡淤淫淬淮深淳混淹添清渊渍渐渔渗渠烯烷烹烽焉焊焕爽犁猎猖猛猜猪猫率球琅理琉琐甜略畦疵痊痒痔痕皑盒盔盖盗盘盛眯眶眷眺眼着睁矫硅硒硕票祭祷祸秸移秽窑窒竟章笛符笨第笺笼粒粕粗粘累绩绪续绰绳维绵绷绸综绽绿缀羚翌聊聋职脖脚脯脱脸舵舶舷船菇菊菌菏菜菠菩菱菲萄萌萍萎萝萤营萧萨著虚蛀蛆蛇蛊蛋衅衔袋袭袱谋谍谎谐谓谗谚谜象赊赦趾跃距躯辅辆逮逸逻鄂酗酚酝酞野铜铝铡铣铬铭铰铱铲银阉阎阐隅隆隋随隐雀雪颅领颇颈馅馆骑鸽鸿鹿麻黄龚';
	charstr[12] = '傅傈傍傣储傲凿剩割募博厦厨啼喀喂善喇喉喊喘喜喝喧喳喷喻堡堤堪堰塔壹奠奥婿媒媚嫂富寐寒寓尊就属屡嵌帽幂幅强彭御循悲惑惠惩惫惰惶惹惺愉愤愧慌慨掌掣揉揍描提插揖握揣揩揪揭援揽搀搁搂搅搓搔搜搭搽敞散敦敬斌斑斯普景晰晴晶智晾暂暑曾替最朝期棉棋棍棒棕棘棚棠森棱棵棺椅植椎椒椭椰榔欺款殖毯氮氯氰渝渡渣渤温渭港渴游渺湃湍湖湘湛湾湿溃溅溉滁滋滑滞焙焚焦焰然煮牌犀犊猩猴猾琢琳琴琵琶琼甥番畴疏痘痛痞痢痪登皖短硝硫硬确硷禄禽稀程稍税窖窗窘窜窝竣童等筋筏筐筑筒答策筛粟粤粥粪紫絮缄缅缆缉缎缓缔缕编缘羡翔翘联脾腆腊腋腑腔腕舒舜艇落葛葡董葫葬葱葵蒂蒋蛔蛙蛛蛤蛮蛰蜒街裁裂装裕裙裤谢谣谤谦赋赌赎赏赐赔趁超越趋跋跌跑践辈辉辊辜逼逾遁遂遇遍遏道遗酣酥釉释量铸铺链销锁锄锅锈锋锌锐锑阑阔隔隘隙雁雄雅集雇韩颊馈馋骗骚鲁鹃鹅黍黑鼎';
	charstr[13] = '催傻像剿勤叠嗅嗓嗜嗡嗣塌塑塘塞填墓媳嫁嫉嫌寝寞幌幕廉廓微想愁愈意愚感慈慎慑搏搐搞搪搬携摄摆摇摈摊摸数斟新暇暖暗椽椿楔楚楞楷楼概榆槐歇歌殿毁源溜溢溪溯溶溺滇滓滔滚满滤滥滦滨滩漓漠煌煎煞煤照献猿瑚瑞瑟瑰甄畸痰痴痹瘁盟睛睡督睦睫睬睹瞄矮硼碉碌碍碎碑碗碘碰禁福稗稚稠窟窥筷筹签简粮粱粳缚缝缠罩罪置署群聘肄肆腥腮腰腹腺腻腾腿舅蒙蒜蒲蒸蓄蓉蓑蓖蓝蓟蓬虞蛹蛾蜂蜕蜗衙裔裸褂解触詹誉誊谨谩谬豢貉赖跟跨跪路跳跺躲辐辑输辞辟遣遥鄙酪酬酮酱鉴锗错锚锡锣锤锥锦锨锭键锯锰障雍雏零雷雹雾靖靳靴靶韵颐频颓颖馏魁魂鲍鹊鹏鼓鼠龄';
	charstr[14] = '僚僧僳兢凳嗽嘉嘎嘘嘛境墅墒墙嫡嫩孵察寡寥寨廖弊彰愿慕慢慷截摔摘摧摹撂撇敲斡旗榜榨榴榷槛模歉滴漂漆漏演漫漱漳漾潍煽熄熊熏熔熙熬瑶璃疑瘟瘦瘩瞅碟碧碱碳碴磁磋稳竭端箍箔箕算管箩粹精缨缩翟翠聚肇腐膀膊膏膜舆舔舞蔑蔓蔗蔚蔡蔫蔷蔼蔽蜀蜘蜜蜡蝇蝉裳裴裹褐褪誓谭谰谱豪貌赘赚赛赫踊踌辕辖辗辣遭遮酵酶酷酸酿锹锻镀镁隧雌需静韶颗馒骡魄鲜鼻';
	charstr[15] = '僵僻凛劈嘱嘲嘶嘻嘿噎噶增墟墨墩履幢影德慧慰憋憎憨懂戮摩撅撑撒撕撞撤撩撬播撮撰撵擒敷暮暴槽樊樟横樱橡毅潘潜潦潭潮澄澈澎澜澳熟瘤瘪瘫瞎瞒碾磅磊磐磕稻稼稽稿箭箱篆篇篓糊缮聪膘膛膝艘蔬蕉蕊蕴蝎蝗蝴蝶褒褥谴豌豫趟趣踏踞踢踩踪躺遵醇醉醋镇镊镍镐镑霄震霉靠鞋鞍题颜额飘骸鲤鹤黎';
	charstr[16] = '儒冀凝嘴器噪噬壁憾懈懊懒撼擂擅操擎擞整橇橙橱潞澡激濒燃燎燕獭瓢瘴瘸瞥磨磺穆窿篙篡篮篱篷糕糖糙缴翰翱耪膨膳臻蕾薄薛薪薯融螟衡赞赠蹄辙辨辩避邀醒醚醛镜雕霍霓霖靛鞘颠餐鲸黔默';
	charstr[17] = '儡嚎嚏壕孺徽懦戴擦曙檀檄檬燥爵癌瞧瞩瞪瞬瞳磷礁穗簇簧糜糟糠繁翼臀臂臃臆藉藏藐螺襄豁赡赢蹈蹋辫镣霜霞鞠骤魏鳃龋';
	charstr[18] = '嚣彝戳瀑瞻翻藕藤藩襟覆蹦躇镭镰鞭鬃鹰';
	charstr[19] = '孽攀攒曝爆瓣疆癣簿羹藻蘑蟹警蹬蹭蹲蹿靡颤鳖麓';
	charstr[20] = '嚷嚼壤巍攘灌籍糯纂耀蠕譬躁魔鳞';
	charstr[21] = '蠢赣露霸霹髓';
	charstr[22] = '囊瓤蘸镶';
	charstr[23] = '攫罐颧';
	charstr[24] = '矗';

	var answer = 0;
	var keywords = ["笔画", "笔划", "多少划", "多少画", "几划", "几画"];
	var p = 0;
	for (var i=0; i<keywords.length; i++) {
		p = question.indexOf(keywords[i]);
		if (p != -1) {
			break;
		}
	}
	var str = question.substring(0,p);
	for (i=0; i<str.length; i++) {
		for (j=1; j<charstr.length; j++) {
			if (charstr[j].indexOf(str.charAt(i)) > 0) {
				answer += j;
			}
		}
	}
	return answer;
}
