package com.morntea.test;

import java.net.MalformedURLException;
import java.net.URL;

public class T {

	/**
	 * @param args
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException {
		// TODO Auto-generated method stub
		URL url = new URL("http://www.baidu.com:999");
		System.out.println(url.getPort());
	}

}
