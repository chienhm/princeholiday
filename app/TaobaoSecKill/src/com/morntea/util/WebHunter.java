package com.morntea.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

public class WebHunter {
	public static String getWebAsString(String url) {
		return getWebAsString(url, "gb2312");
	}

	public static String getWebAsString(String url, String encoding) {
		URL urlObj = null;
		String temp = null;
		StringBuffer sb = new StringBuffer();
		try {
			urlObj = new URL(url);
			
			HttpURLConnection httpConn = (HttpURLConnection) urlObj.openConnection();   
	        httpConn.setRequestMethod("GET");   
	        httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14) Gecko/20080404 Firefox/2.0.0.14");  
	        
	        InputStream is = httpConn.getInputStream();
	        //InputStream is = urlObj.openStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(is, encoding));
			while ((temp = in.readLine()) != null) {
				sb.append(temp + "\n");
				//break;
			}
			in.close();
		} catch (MalformedURLException me) {
			me.printStackTrace();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			//e.printStackTrace();
		}
		return sb.toString();
	}
	
	public static String getHtml(String url) {
		URL urlObj = null;
		String temp = null;
		StringBuffer sb = new StringBuffer();
		try {
			urlObj = new URL(url);
			BufferedReader in =new BufferedReader(new InputStreamReader(urlObj.openStream()));
			while ((temp = in.readLine()) != null) {
				sb.append(temp + "\n");
			}
			in.close();
		} catch (MalformedURLException me) {
			me.printStackTrace();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			//e.printStackTrace();
		}
		return sb.toString();
	}
	
public static void socketHttp(String keyword) {
	  try { 
          Socket socket = new Socket("www.baidu.com", 80); 
          boolean autoflush = true;
          PrintWriter out = new PrintWriter(socket.getOutputStream(), autoflush); 
          BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           //send an HTTP request to the web server
          out.println("GET /s?wd="+keyword+" HTTP/1.1"); 
          out.println("Host: www.baidu.com"); 
          out.println("Connection: Close"); 
          out.println(); 
          //read the response        
          boolean loop = true;
          StringBuffer sb = new StringBuffer(8096); 
          while (loop) { 
             if (in.ready()) {
               int i = 0;
               while (i != -1) {
                   i = in.read();
                   sb.append((char) i);
               }
               loop = false; 
           } 
          //Thread.currentThread().sleep(50);  
         }  
         //display the response to the out console  
         //System.out.println(sb.toString());
         socket.close();
      } catch (UnknownHostException e) {
           System.err.println("Don't know about host: Victest."); 
           System.exit(1); 
      } catch (IOException e) { 
          System.err.println("Couldn't get I/O for " + "the connection to: Victest.");  
          System.exit(1); 
      } 

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
