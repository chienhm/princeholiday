package com.morntea.test;

import com.morntea.util.Http;
import com.morntea.util.WebHunter;

public class GetWebTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String sogou = null;
		String url;
		long beginTime, endTime;

		System.out.println("Socket");
		beginTime = System.currentTimeMillis();  
		WebHunter.socketHttp("1234");
		endTime = System.currentTimeMillis(); 
		System.out.println(endTime-beginTime);

		//url = "http://e.morntea.com/util/taobao/item_04.htm";
		url = "http://www.sogou.com/sogou?query=EMS速递";
		System.out.println("getWebAsString: openConnection-getInputStream");
		beginTime = System.currentTimeMillis();  
		sogou = WebHunter.getWebAsString(url, "gbk");
		endTime = System.currentTimeMillis(); 
		System.out.println(endTime-beginTime);

		//url = "http://e.morntea.com/util/taobao/item_01.htm";
		url = "http://www.sogou.com/sogou?query=申通快递";
		System.out.println("Url.openStream");
		beginTime = System.currentTimeMillis();  
		sogou = WebHunter.getHtml(url);
		endTime = System.currentTimeMillis(); 
		System.out.println(endTime-beginTime);
		
		//url = "http://192.168.0.124/util/taobao/item_02.htm";
		url = "http://www.sogou.com/sogou?query=顺丰快递";
		System.out.println("doGet openConnection-BufferedInputStream");
		beginTime = System.currentTimeMillis(); 
		sogou = new Http().doGet(url);
		endTime = System.currentTimeMillis(); 
		System.out.println(endTime-beginTime);

		//url = "http://192.168.0.124/util/taobao/sogou.htm";
		url = "http://www.sogou.com/sogou?query=圆通快递";
		System.out.println("Url.openStream.Bytes getHtml");
		beginTime = System.currentTimeMillis(); 
		sogou = new Http().getHtml(url);
		endTime = System.currentTimeMillis(); 
		System.out.println(endTime-beginTime);
		
		String text = sogou.replaceAll("<[^>]+>", "");
		System.out.println(text);
	}

}
