package com.morntea.util;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Http {
	private static int BUFFER_SIZE = 4096;
	private static String CONTENT_TYPE = "application/x-www-form-urlencoded";

	public String doGet(String url) {
		byte[] data = {};
		try {
			URL u = new URL(url);
			HttpURLConnection uc = (HttpURLConnection) u.openConnection();
			byte[] b = {};
			byte[] t = new byte[BUFFER_SIZE];
			int r;
			BufferedInputStream bin = new BufferedInputStream(uc
					.getInputStream());
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
	
	public String getHtml(String url) {
		byte[] data = {};
		try {
			URL u = new URL(url);
			HttpURLConnection uc = (HttpURLConnection) u.openConnection();
			byte[] b = {};
			byte[] t = new byte[BUFFER_SIZE];
			int r;
			BufferedInputStream bin = new BufferedInputStream(u.openStream());
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
			connection = (HttpURLConnection)u.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", CONTENT_TYPE);
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14) Gecko/20080404 Firefox/2.0.0.14");
			connection.setRequestProperty("Content-Length", String
					.valueOf(postData.length()));
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
				if(bin!=null)bin.close();
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
}
