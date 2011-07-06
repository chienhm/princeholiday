package com.morntea.app.proxyhunter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

public class ProxyTest {
	static String VERIFY_URL = "http://www.ip.cn/getip.php?action=getip&ip_url=";
	public boolean test(String ip) {
		URL url = null;
		try {
			url = new URL(VERIFY_URL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, 80));  
		try {
			if(url!=null){
				StringBuffer sb = new StringBuffer();
				HttpURLConnection httpConn = (HttpURLConnection) url.openConnection(proxy);
				httpConn.setReadTimeout(10000);
		        httpConn.setRequestMethod("GET");   
		        InputStream is = httpConn.getInputStream();
		        //InputStream is = urlObj.openStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(is, "gb2312"));
				String temp;
				while ((temp = in.readLine()) != null) {
					sb.append(temp + "\n");
				}
				in.close();
				System.out.print(sb.toString());
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ProxyTest pt = new ProxyTest();
		System.out.print(pt.test("giotto.dut.ac.za"));
	}

}
