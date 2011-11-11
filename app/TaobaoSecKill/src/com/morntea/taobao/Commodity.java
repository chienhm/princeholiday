package com.morntea.taobao;

import java.util.Map;

import org.apache.http.impl.client.DefaultHttpClient;

import com.morntea.helper.ConsoleLog;
import com.morntea.helper.HtmlParserHelper;
import com.morntea.helper.Http;
import com.morntea.helper.StringHelper;

public class Commodity {
	Http http = null;
	public void load() {
		//System.out.println(html);
	}
	
	public Map<String, String> getTmallData(String id) {
		return getData("http://detail.tmall.com/item.htm?id=", id);
	}

	public Map<String, String> getData(String id) {
		return getData("http://item.taobao.com/item.htm?id=", id);
	}
	
	public Map<String, String> getData(String baseUrl, String id) {
		if(http==null)http = new Http(new DefaultHttpClient());
		String html = null;
		html = http.get(baseUrl + id);
		//http.showCookie();
		Map<String, String> data = HtmlParserHelper.parseForm(html, "id", "J_FrmBid");
		//ConsoleLog.log(data);
		String skuJson = StringHelper.regFetch(html, "\"valItemInfo\":(.+?),\\s*\"isAreaSell\"");
		if(skuJson.isEmpty()) {
			skuJson = StringHelper.regFetch(html, "\"valItemInfo\":(.+?)\\s*}\\);");
		}
		if(!skuJson.isEmpty()){
		    SkuFactory sf = new SkuFactory();
		    sf.load(skuJson);
		    Sku sku = sf.defaultSku;
		    data.put("buy_param", id+"_1_"+sku.skuId);
		    data.put("skuId", sku.skuId);
		    data.put("use_cod", "false");
		}
		data.put("buyer_from", "ecity");
		data.put("_input_charset", "UTF-8");
		data.put("deliveryCityCode", "310100");
		
		ConsoleLog.log(data);
		return data;
	}
}
