package com.morntea.util;

public class Validator {
	public static boolean isHanZi(String str) {
		char[] chars = str.toCharArray();
		boolean isGB2312 = false;
		for (int i = 0; i < chars.length; i++) {
			byte[] bytes = ("" + chars[i]).getBytes();
			if (bytes.length == 2) {
				int[] ints = new int[2];
				ints[0] = bytes[0] & 0xff;
				ints[1] = bytes[1] & 0xff;
				if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40
						&& ints[1] <= 0xFE) {
					isGB2312 = true;
					break;
				}
			}
		}
		return isGB2312;
	}

	public static boolean isAlphabit(String fstrData) {
		char c = fstrData.charAt(0);
		if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNumeric(String str) {

		for (int i = str.length(); --i >= 0;) {

			if (!Character.isDigit(str.charAt(i))) {

				return false;

			}

		}

		return true;

	}
}
