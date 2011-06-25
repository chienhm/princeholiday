package com.morntea.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.zip.GZIPInputStream;

public class House {
	public static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	public static String httpGet(String url, String headerFile, String encoding) {
		URL urlObj = null;
		String temp = null;
		StringBuffer sb = new StringBuffer();
		boolean isZipData = false;
		try {
			urlObj = new URL(url);

			HttpURLConnection httpConn = (HttpURLConnection) urlObj.openConnection();
			httpConn.setRequestMethod("GET");
			
			/* Add request header from header configure file */
			if (headerFile != null) {
				FileInputStream fileInput = new FileInputStream(headerFile);
				BufferedReader br = new BufferedReader(new InputStreamReader(fileInput));

				String line;
				while ((line = br.readLine()) != null) {
					int f = line.indexOf("=");
					if (f != -1) {
						String key = line.substring(0, f);
						String value = line.substring(f + 1);
						if(key.equalsIgnoreCase("Accept-Encoding") && value.contains("gzip")) {
							isZipData = true;
						}
						//System.out.println(key + "=" + value);
						httpConn.setRequestProperty(key, value);
					}
				}
			}
			
			/* Get response data */
			InputStream is = httpConn.getInputStream();
			InputStreamReader isr;
			if(isZipData) {
				GZIPInputStream gzip = new GZIPInputStream(is);
				isr = new InputStreamReader(gzip, encoding);
			} else {
				isr = new InputStreamReader(is, encoding);
			}
			BufferedReader in = new BufferedReader(isr);
			while ((temp = in.readLine()) != null) {
				sb.append(temp + "\n");
				// break;
			}
			in.close();
		} catch (MalformedURLException me) {
			me.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(sb);
		return sb.toString();
	}
	
	public static void checkTime(String url) {
		long start = System.currentTimeMillis();
		System.out.println("[sta]" + df.format(Calendar.getInstance().getTime()));
		URL urlObj = null;
		try {
			urlObj = new URL(url);
			HttpURLConnection httpConn = (HttpURLConnection) urlObj.openConnection();
			httpConn.setRequestMethod("GET");

			System.out.println("[ser]" + df.format(httpConn.getHeaderFieldDate("Date", 0)));

			System.out.println("[end]" + df.format(Calendar.getInstance().getTime()));
			System.out.println("[elapse]" + (System.currentTimeMillis() - start));
			
		} catch (MalformedURLException me) {
			me.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}

	public static void main(String args[]) {
		/*httpGet("http://spikes.bbs.soufun.com/spike_js.aspx?action=spike&spikeid=2322", 
				"header.ini", 
				"GB2312");*/
		
		/* Test date difference between server and client */
		/*int ms = 700;
		int delay = 0;
		Calendar now = Calendar.getInstance();
		int diff = now.get(Calendar.MILLISECOND)-ms;
		if(diff<0) {
			delay = -diff;
		} else {
			delay = 1000 - diff;
		}
		System.out.println("[now]" + df.format(now.getTime()));
		
		Timer timer = new Timer();
        timer.schedule( new TimerTask() {
				public void run() {
		        	checkTime("http://spikes.bbs.soufun.com/spike.aspx?spikeid=2322");
				}
		}, delay);
        */
        
		/*Calendar st = Calendar.getInstance();
		st.set(2011, 1, 22, 15, 29, 58);
		st.set(Calendar.MILLISECOND, 500);
		System.out.println(df.format(st.getTime()));
		
		final String secUrl = "http://spikes.bbs.soufun.com/spike_js.aspx?action=spike&spikeid=2322";
		long delay = st.getTime().getTime() - new Date().getTime();
		//System.out.println(st.getTime().getTime() + "," + st.getTimeInMillis());
		//System.out.println(new Date().getTime() + "," + System.currentTimeMillis());
		Timer timer = new Timer();
		for(int i = 0; i<6; i++) {
			System.out.println(delay);
	        timer.schedule( new TimerTask() {
					public void run() {
						httpGet(secUrl, "header.ini", "GB2312");
					}
			}, delay);
			delay += 50;
		}*/
	}
}
