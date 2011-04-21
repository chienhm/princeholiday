package com.morntea.taobao;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WebHunter {
	public static String getWebAsString(String url) {
		return getWebAsString(url, "gb2312");
	}

	public static String getWebAsString(String url, String encoding) {
		URL urlObj = null;
		String temp = null;
		try {
			urlObj = new URL(url);
			
			HttpURLConnection httpConn = (HttpURLConnection) urlObj.openConnection();
	        httpConn.setRequestMethod("GET");   
	        httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14) Gecko/20080404 Firefox/2.0.0.14");  
	        
	        InputStream is = httpConn.getInputStream();
	        //InputStream is = urlObj.openStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(is, encoding));
			
			while((temp = in.readLine()) != null){
				temp = temp.trim();
				if(temp.startsWith("<div class=\"disqualified\">")){
					in.close();
					return temp;
				}else if(temp.startsWith("<div class=\"tb-skin\">")){
					in.close();
					return "";
				}
			}
		} catch (MalformedURLException me) {
			me.printStackTrace();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} finally{
		}
		return "";
	}

	public static boolean savePageToFile(String pageUrl, String filePath) {
		final int BUFFER_SIZE = 1024;
		boolean rt = false;
		
		HttpURLConnection connect = null;
		BufferedInputStream in = null;
		FileOutputStream file = null;
		byte[] buf = new byte[BUFFER_SIZE];
		int size = 0;

		try {
			URL url = new URL(pageUrl);
			connect = (HttpURLConnection) url.openConnection(); 
			connect.setRequestMethod("GET");
			connect.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14) Gecko/20080404 Firefox/2.0.0.14");
			connect.setRequestProperty("Referer", pageUrl);
			connect.connect();
			in = new BufferedInputStream(connect.getInputStream());
			file = new FileOutputStream(filePath);
			while ((size = in.read(buf)) != -1) {
				file.write(buf, 0, size);
			}
			rt = true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if(file!=null)file.close();
				if(in!=null)in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			connect.disconnect();
		}
		return rt;
	}
}
