package com.morntea.helper;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper {
	/**
	 * Get matched string by regular expression, only the first matched 
	 * subsequence is returned.<br/>
	 * Example: StringHelper.regFetch("abcd-123.html", "d-(\\d+).html") => 123
	 * @param  src
     *        The source string to find from
     * @param  regex
     *         The regular expression rule
     * @return  The first matched subsequence, if not found, return empty string.
	 * */
	public static String regFetch(String src, String regex) {
		String value = "";
		Pattern pattern = Pattern.compile(regex,
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(src);
		if (matcher.find()) {
			value = matcher.group(1);
		}
		return value;
	}
	
	public static String[] regArrFetch(String src, String regex) {
		String[] value = {};
		Pattern pattern = Pattern.compile(regex,
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(src);
		if (matcher.find()) {
			int size = matcher.groupCount();
			value = new String[size];
			for(int i=0; i<size; i++){
				value[i] = matcher.group(i+1);
			}
		}
		return value;
	}
	
	public static String getInput() {
		System.out.print("Wating for your input:");
		Scanner scanner = new Scanner(System.in);
		return scanner.nextLine();
	}
}
