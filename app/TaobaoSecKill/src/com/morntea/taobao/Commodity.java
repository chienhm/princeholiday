package com.morntea.taobao;

import java.util.Map;

import org.apache.http.impl.client.DefaultHttpClient;

import com.morntea.helper.HtmlParserHelper;
import com.morntea.helper.Http;
import com.morntea.helper.StringHelper;

public class Commodity {
	public static String TEST_ITEM = "http://127.0.0.1:8080/e.morntea.com/util/taobao/e.htm?id=";
	public static String ITEM = "http://item.taobao.com/item.htm?id=";
	public static boolean test = false;
	Http http = null;
	public void load() {
		//System.out.println(html);
	}
	
	public Map<String, String> getTmallData(String id) {
		return getData("http://detail.tmall.com/item.htm?id=", id);
	}

	public Map<String, String> getData(String id) {
		return getData(test?TEST_ITEM:ITEM, id);
	}
	
	public Map<String, String> getData(String baseUrl, String id) {
		if(http==null)http = new Http(new DefaultHttpClient());
		String html = null;
		html = http.get(baseUrl + id);
		//System.out.println(html);
		Map<String, String> data = HtmlParserHelper.parseForm(html, "id", "J_FrmBid");
		//ConsoleLog.log(data);
		String skuJson = StringHelper.regFetch(html, "\"valItemInfo\":(.+?),\\s*\"isAreaSell\"");
		if(skuJson.isEmpty()) {
			skuJson = StringHelper.regFetch(html, "\"valItemInfo\":(.+?)\\s*}\\);");
		}
		//ConsoleLog.log(skuJson);
		if(!skuJson.isEmpty()){
		    SkuFactory sf = new SkuFactory();
		    sf.load(skuJson);
		    Sku sku = sf.defaultSku;
		    if(sku!=null) {
			    data.put("buy_param", id+"_1_"+sku.skuId);
			    data.put("skuId", sku.skuId);
		    }
		    data.put("use_cod", "false");
		}
		//data.put("buyer_from", "ecity");
		//data.put("_input_charset", "UTF-8");
		data.put("deliveryCityCode", "310100");
		return data;
	}
}
