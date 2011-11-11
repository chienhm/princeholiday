package com.morntea.helper;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Http {
	private static int BUFFER_SIZE = 1024;
	private static String CONTENT_TYPE = "application/x-www-form-urlencoded";

	public String doGet(String url) {
		byte[] data = {};
		try {
			URL u = new URL(url);
			HttpURLConnection uc = (HttpURLConnection) u.openConnection();
			byte[] b = {};
			byte[] t = new byte[BUFFER_SIZE];
			int r;
			BufferedInputStream bin = new BufferedInputStream(
					uc.getInputStream());
			while ((r = bin.read(t)) > -1) {
				b = putData(b, t, r);
			}
			bin.close();
			uc.disconnect();
			data = b;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(data);
	}

	public String doPost(String url, String postData) {
		byte[] dat = {};
		HttpURLConnection connection = null;
		DataOutputStream dout = null;
		BufferedInputStream bin = null;
		try {
			URL u = new URL(url);
			connection = (HttpURLConnection) u.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", CONTENT_TYPE);
			connection
					.setRequestProperty(
							"User-Agent",
							"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14) Gecko/20080404 Firefox/2.0.0.14");
			connection.setRequestProperty("Content-Length",
					String.valueOf(postData.length()));
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			dout = new DataOutputStream(connection.getOutputStream());
			dout.write(postData.getBytes());
			dout.flush();
			dout.close();
			bin = new BufferedInputStream(connection.getInputStream());
			byte[] buff = new byte[BUFFER_SIZE], bs = {};
			int r;
			while ((r = bin.read(buff)) > -1) {
				bs = putData(bs, buff, r);
			}
			dat = bs;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bin != null)
					bin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			connection.disconnect();
		}
		return new String(dat);
	}

	private final byte[] putData(byte[] b, byte[] t, int r) {
		byte[] tb = new byte[b.length + r];
		System.arraycopy(b, 0, tb, 0, b.length);
		System.arraycopy(t, 0, tb, b.length, r);
		return tb;
	}

	private HttpClient httpclient;

	public Http(HttpClient httpclient) {
		this.httpclient = httpclient;
		HttpClientParams.setCookiePolicy(httpclient.getParams(), 
				CookiePolicy.BROWSER_COMPATIBILITY
		);
	}
	
	public void terminate() {
		if(httpclient!=null) {
			httpclient.getConnectionManager().shutdown();
		}
	}

	public String get(String url) {
		String html = null;
		HttpGet httpget = new HttpGet(url);

		try {
			ConsoleLog.log("GET " + url);
			HttpResponse response = httpclient.execute(httpget);
			ConsoleLog.log("1");
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				html = EntityUtils.toString(entity);
				ConsoleLog.log("2");
			}
			EntityUtils.consume(entity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return html;
	}
	
	public String post(String url, Map<String, String> data) {
		String html = null;
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		if(data!=null)
		for(String key:data.keySet()){
			nvp.add(new BasicNameValuePair(key, data.get(key)));
		}
		HttpPost httpost = new HttpPost(url);
		httpost.setHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14) Gecko/20080404 Firefox/2.0.0.14");
		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvp, "gb2312"));
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			ConsoleLog.log("POST " + url);
			html = httpclient.execute(httpost, responseHandler);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return html;
	}
	
	public void download(String url, String filePath) {
		HttpGet httpGet = new HttpGet(url);  
        HttpResponse response;
		try {
			ConsoleLog.log("Download " + url);
			response = httpclient.execute(httpGet);
	        if(HttpStatus.SC_OK==response.getStatusLine().getStatusCode()){  
	            HttpEntity entity = response.getEntity();  
	            if (entity != null) {  
	                System.out.println(entity.getContentType());  
	               // System.out.println(entity.isStreaming());  
	                File storeFile = new File(filePath);    
	                FileOutputStream output = new FileOutputStream(storeFile); 
	                InputStream input = entity.getContent();  
	                byte b[] = new byte[1024];  
	                int j = 0;  
	                while( (j = input.read(b))!=-1){  
	                    output.write(b,0,j);  
	                }  
	                output.flush();  
	                output.close();   
	            }  
	            if (entity != null) {  
	            	EntityUtils.consume(entity);
	            }  
	        }  
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
    }
	
	public void showCookie() {
		List<Cookie> cookies = ((DefaultHttpClient) httpclient).getCookieStore().getCookies();
		if (cookies.isEmpty()) {
			System.out.println("None");
		} else {
			System.out.println("----------------------------------------------------");
			for (int i = 0; i < cookies.size(); i++) {
				Cookie cookie = cookies.get(i);
				System.out.println("- " + cookie.toString());
				//System.out.println(cookie.getName() + ": " + cookie.getValue());
			}
			System.out.println("----------------------------------------------------");
		}
	}
	
	public void addCookie(String name, String value, String domain, String path, Date expiryDate) {
		CookieStore cs = ((DefaultHttpClient) httpclient).getCookieStore();
		BasicClientCookie cookie = new BasicClientCookie(name, value);
		cookie.setDomain(domain);
		cookie.setPath(path);
		cookie.setExpiryDate(expiryDate);
		cs.addCookie(cookie);
	}
	
	public String getCookie(String name) {
		String value = "";
		List<Cookie> cookies = ((DefaultHttpClient) httpclient).getCookieStore().getCookies();
		if (cookies.isEmpty()) {
			System.out.println("None");
		} else {
			for (int i = 0; i < cookies.size(); i++) {
				// System.out.println("- " + cookies.get(i).toString());
				Cookie cookie = cookies.get(i);
				if (cookie.getName().equals(name)) {
					value = cookie.getValue();
					break;
				}
			}
		}
		return value;
	}
	
	public void cleanCookie() {
		CookieStore cs = ((DefaultHttpClient) httpclient).getCookieStore();
		cs.clear();
	}
}
