package com.morntea.taobao;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.morntea.helper.ConsoleLog;

public class SkuFactory {
	public String defSelected;
	public Sku defaultSku;
	public ArrayList<Sku> skuList;
	
	public void load(String json) {
		Gson gson = new Gson();
	    JsonParser parser = new JsonParser();
	    try {
		    JsonElement je = parser.parse(json);
		    JsonObject s = je.getAsJsonObject();
		    JsonArray itemInfo = s.getAsJsonArray("defSelected");
		    if(itemInfo!=null)
		    for(JsonElement j : itemInfo) {
		    	if(!j.getAsString().equals("")) {
		    		defSelected = j.getAsString();
		    	}
		    }
		    /*String defSelected = gson.fromJson(itemInfo.get(1), String.class);
		    ConsoleLog.log(defSelected);*/
		    
		    JsonObject skuMap = s.getAsJsonObject("skuMap");
		    skuList = new ArrayList<Sku>();
		    if(skuMap!=null)
		    for(Entry<String, JsonElement> ent : skuMap.entrySet()) {
		    	ConsoleLog.log(ent.getKey() + " : " + ent.getValue());
			    Sku skuObj = gson.fromJson(ent.getValue(), Sku.class);
			    skuList.add(skuObj);
		    	if(defSelected!=null && ent.getKey().indexOf(defSelected)!=-1) {
		    		defaultSku = skuObj;
		    	}
		    }
		    if(defaultSku==null && skuList.size()>0) {
		    	defaultSku = skuList.get(0);
		    }		    
	    } catch (JsonParseException e) {
	    	e.printStackTrace();
	    }
	}
}
