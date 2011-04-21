package com.morntea.search;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.morntea.util.WebHunter;

public class AnswerEngineSearchV1 {

	final String SOGOU = "http://www.sogou.com/web?query=";
	final String BAIDU = "http://www.baidu.com/s?wd=";
	final String GOOGLE = "http://www.google.com/search?q=";
	private String question;
	private String answer;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AnswerEngineSearchV1 aes = new AnswerEngineSearchV1("春花秋月何时了的下一句是什么");
		System.out.println(aes.evaluateAnswer());

	}
	public AnswerEngineSearchV1(String question) {
		super();
		this.question = question;
	}
	
	public String evaluateAnswer() {
		String answer = null;
		String sogou = null;
		String url;
		long beginTime, endTime;

		//url = "http://192.168.0.124/util/taobao/sogou.htm";
		url = SOGOU + question;
		beginTime = System.currentTimeMillis();  
		sogou = WebHunter.getWebAsString(url);
		endTime = System.currentTimeMillis(); 
		System.out.println(endTime-beginTime);
		
		//String text = sogou.replaceAll("<[^>]+>", "");
		//System.out.println(text);
		
		
		Map<String, Integer> answerMap = new HashMap<String, Integer>();
		int maxHit = 0, minLenth = 1000;
		String answerMany = null;
		
		Pattern answerPattern = Pattern.compile("<td class=\"vrg\" nowrap>答：</td><td>(.+?)</td>", Pattern.DOTALL);
		Pattern headPattern = Pattern.compile("^[^.\\s(A-Za-z:]+");
		Matcher answerMatcher = answerPattern.matcher(sogou);
		while (answerMatcher.find()) {
			String answerGot = answerMatcher.group(1).replaceAll("<[^>]+>", "");
			
			// If the answer does not contain any Chinese character, discard it
			Matcher headMatcher = headPattern.matcher(answerGot);
			if(headMatcher.find()) {
				answerGot = headMatcher.group();
			} else {
				continue;
			}
			//System.out.println("["+answerGot+"]");
			
			if(answerGot.length()<minLenth) {
				minLenth = answerGot.length();
				answer = answerGot;
			}
			if(answerMap.containsKey(answerGot)) {
				answerMap.put(answerGot, answerMap.get(answerGot)+1);
				if(answerMap.get(answerGot)>maxHit) {
					maxHit = answerMap.get(answerGot);
					answerMany = answerGot;
				}
			} else {
				answerMap.put(answerGot, 0);
			}
		}
		//System.out.println(answer + ", " + answerMany);
		// If the most answer is not the shortest answer and the most answer occurs morn than once
		if((answer!=answerMany) && (answerMany!=null && answerMap.get(answerMany)>1)) {
			answer = answerMany;
		}

		String text = sogou.replaceAll("<[^>]+>", "");
		maxHit = 0;
		for(String anwserEntry : answerMap.keySet()){
			String reg = "("+anwserEntry+")";
			Pattern p = Pattern.compile(reg); 
			Matcher m = p.matcher(text);
			int count = 0;
			while (m.find()&&m.groupCount()>=1) {
				count++;
			}
			
			answerMap.put(anwserEntry, count);
			if(count>maxHit) {
				maxHit = count;
				answer = anwserEntry;
			}
		}		
		
		return answer;
	}
	
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getQuestion() {
		return question;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getAnswer() {
		return answer;
	}

}
