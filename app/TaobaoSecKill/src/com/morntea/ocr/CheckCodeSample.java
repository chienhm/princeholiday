package com.morntea.ocr;

import com.morntea.util.WebHunter;

public class CheckCodeSample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String url = "http://checkcode.taobao.com/auction/checkcode?sessionID=19233fcea509a7da786bd586dd91578f";
		int i = 0;
		while(i++<500) {
			int imageIndex = i;
			WebHunter.savePageToFile(url + "&" + Math.random(), ".\\checkcode3\\"+imageIndex+".jpg");
		}
	}

}
