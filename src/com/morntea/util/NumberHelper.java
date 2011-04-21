package com.morntea.util;

public class NumberHelper {
	
	public static int convertChineseNumber(String cn) {
		int number = 0;
		char c = cn.charAt(0);
		switch(c) {
			case '1':number=1;break;
			case '一':number=1;break;
			case '2':number=2;break;
			case '二':number=2;break;
			case '两':number=2;break;
			case '3':number=3;break;
			case '三':number=3;break;
			case '4':number=4;break;
			case '四':number=4;break;
			case '5':number=5;break;
			case '五':number=5;break;
			case '6':number=6;break;
			case '六':number=6;break;
			case '7':number=7;break;
			case '七':number=7;break;
			case '8':number=8;break;
			case '八':number=8;break;
			case '9':number=9;break;
			case '九':number=9;break;
			case '十':number=10;break;
			case '零':number=0;break;
		}
		return number;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
