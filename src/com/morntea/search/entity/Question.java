package com.morntea.search.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.morntea.util.NumberHelper;

public class Question {
	public static final String SEPARATOR = ".\\s(&:：~!,?\"";
	public static final String LEFT_SEPARATOR = ",. (&:：?\"";
	public static final String RIGHT_SEPARATOR = ",.\\s)(&~!?\"";
	public String original = null; //the original question
	public String text = null; // question ignore useless phrase
	public String tip = null; // tip in bracket
	public int limit = 0; // answer length limit
	
	public int suggestLength = 0;
	public int maxLength = 0;
	public int minLength = 0;
	public String regex = null;
	public String format = null;
	public boolean onlyText = false;
	public Question(String question) {
		if(question==null) return;
		original = question.trim().replace(" ", "")
			.replace("（", "(").replace("）", ")")
			.replace("”", "\"").replace("“", "\"")
			.replace("？", "?");
		text = original;
		// find tip message in () and delete it from original string
		Pattern tipPattern = Pattern.compile("\\((.+?)\\)");
		Matcher tipMatcher = tipPattern.matcher(original);
		if(tipMatcher.find()) {
			tip = tipMatcher.group(1);
			text = original.replace(tipMatcher.group(), "").trim();
		}
		text = text.replace("?", "");
		
		if(tip!=null) {
			int t = tip.indexOf("个字");
			if(t!=-1) {
				String chNum = tip.substring(0, t);
				limit = NumberHelper.convertChineseNumber(chNum);
			} else if(tip.contains("几")) {
				onlyText = true;
				format = "(" + tip.replace("几", "\\d+") + ")";
				regex = format;
			} 
		}
		if(regex==null) {
			if(text.contains("一句")) {
				int pos = text.indexOf("的", 4);
				boolean after = true;
				int s,q,x,h;
				s = text.indexOf("上", 4);
				q = text.indexOf("前", 4);
				x = text.indexOf("下", 4);
				h = text.indexOf("后", 4);
				if(pos==-1) {pos = s!=-1?s:(q!=-1?q:(x!=-1?x:h));}
				if(s!=-1||q!=-1)after = false;
				if(pos!=-1){
					String poem = null;
					int quotLeft = text.indexOf("\"");
					if(quotLeft!=-1&&quotLeft<pos) {
						int quotRight = text.indexOf("\"", quotLeft+1);
						poem = text.substring(quotLeft+1, quotRight);
					} else {
						poem = text.substring(0, pos);
					}
					if(poem!=null) {
						String sepRegex = "[.,\\s]+";
						onlyText = true;
						suggestLength = poem.length();
						minLength = suggestLength/2;
						maxLength = suggestLength*2;
						regex = sepRegex + "([\u4e00-\u9fa5]+)" + sepRegex;
						if(after)regex = poem + regex;
						else regex = regex + poem;
					}
					//System.out.println(poem);
				}
			} 
		}
		if(regex==null) {
			if(text.contains("什么时候")||text.contains("哪一天")||text.contains("哪天")) {
				onlyText = true;
				format = "(" + "\\d+年\\d+月\\d+日" + ")";
				regex = format;
			}
		}
		if(regex==null) { // the last intelligent recognition
			int pos = text.indexOf("的");
			if(pos!=-1) {
				int isPos = text.indexOf("是", pos+1);
				if(isPos!=-1) {
					onlyText = true;
					String target = text.substring(pos+1, isPos);
					String leftRegex = "[是|为]?[ :\"(]+";
					String rightRegex = "["+RIGHT_SEPARATOR+"]+";
					regex = target + leftRegex + "([\\S]+?)" + rightRegex;
					//System.out.println(target);
				}
			}
		}
		
		System.out.println(text + ", tip:" + tip + ", limit:" + limit + ", format:" + regex);
	}
	
	public static void test() {
		new Question("水浒传的作者是谁？（三个字）");
	}
	
	public static void main(String args[]) {
		test();
	}
}
