package com.morntea.search;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.morntea.search.entity.Answer;
import com.morntea.search.entity.Question;
import com.morntea.util.WebHunter;

public class AnswerEngineSearch extends AnswerEngine {

	final String SOGOU = "http://www.sogou.com/web?query=";
	final String BAIDU = "http://www.baidu.com/s?wd=";
	final String GOOGLE = "http://www.google.com/search?q=";
	
	Pattern headPattern = Pattern.compile("^[^"+Question.SEPARATOR+"]+");
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AnswerEngine aes = new AnswerEngineSearch("山外青山楼外楼”在原诗《题临安邸》中的下句是");//"建党节是哪一天（几月几日）");
		System.out.println("答案是："+aes.evaluateAnswer().text);

	}
	
	public AnswerEngineSearch(String question) {
		setQuestion(new Question(question));
	}
	
	private String filter(String rawAnswer) {
		String answer = null;
		//replace all regex symbols
		rawAnswer = rawAnswer.replaceAll("<[^>]+>|[\\(\\)\\|\\[_]", "").trim();
		//System.out.println("["+rawAnswer+"]");
		
		// If the answer does not contain any Chinese character, discard it
		// the first phrase without separator
		Matcher headMatcher = headPattern.matcher(rawAnswer);
		if(headMatcher.find()) {
			answer = headMatcher.group();
		} else {
			return null;
		}
		
		if(question.limit!=0&&answer.length()!=question.limit) {
			return null;
		}
		
		// If answer has only one character and is not a normal ASCII
		if(answer.length()==1 && answer.charAt(0)<255 
				&& !Character.isLetterOrDigit(answer.charAt(0))) {
			return null;
		}
		
		if(question.format!=null) {
			if(!Pattern.matches(question.format, answer))return null;
			//Matcher m = Pattern.compile(question.format).matcher(answer);
			//if(!m.find())return null;
		}
		
		// rawAnswer should not be contained in the question
		if(question.text.indexOf(answer)!=-1 && answer.length()>3) {
			return null;
		}
		return answer;
	}
	
	public class AnswerParser extends Thread {
		private Pattern answerPattern;
		private String targetString;
		Map<String, Integer> answerMap = new HashMap<String, Integer>();
		
		public AnswerParser(String regex, String text) {
			answerPattern = Pattern.compile(regex, Pattern.DOTALL);
			targetString = text;
			start();
		}
		
		public void run() {
			Matcher answerMatcher = null;
			answerMatcher = answerPattern.matcher(targetString);
			while (answerMatcher.find()) {
				String answerGot = filter(answerMatcher.group(1));			
				if(answerGot!=null)answerMap.put(answerGot, 0);
			}
		}
	}
		
	public Answer evaluateAnswer() {
		String answer = null;
		String html = null;
		String text = null;
		String url;
		long beginTime, endTime;

		//url = "http://192.168.0.124/util/taobao/sogou.htm";
		url = SOGOU + question.text;
		beginTime = System.currentTimeMillis();  
		html = WebHunter.getWebAsString(url);
		endTime = System.currentTimeMillis(); 
		System.out.println("Search Time: " + (endTime-beginTime));

		beginTime = System.currentTimeMillis(); 
		
		html = html.replace("&ldquo;", "\"").replace("&rdquo;", "\"");
		text = html.replaceAll("<[^>]+>", "");
		//System.out.println(text);
		
		Map<String, Integer> answerMap = new HashMap<String, Integer>();
		
		String answerFormat = "<td class=\"vrg\" nowrap>答：</td><td>(.+?)</td>";
		
		// two threads parse the answer
		AnswerParser t1 = null;
		if(question.regex!=null) {
			String targetString = html;
			if(question.onlyText){targetString = text;}
			t1 = new AnswerParser(question.regex, targetString);
		}
		AnswerParser t2 = new AnswerParser(answerFormat, html);
		try {
			if(t1!=null) {
				t1.join();
				System.out.println("自定义规则：\t" + t1.answerMap);
				answerMap.putAll(t1.answerMap);
			}
			t2.join();
			System.out.println("HTML问答规则：\t" + t2.answerMap);
			answerMap.putAll(t2.answerMap);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		

		int maxHit = 0;
		for(String answerEntry : answerMap.keySet()){
			String leftRegex = "["+Question.LEFT_SEPARATOR+"]+";
			String rightRegex = "["+Question.RIGHT_SEPARATOR+"]+";
			String reg = null;
			if(question.format!=null) {
				reg = "(" + answerEntry+")";
			} else {
				reg = leftRegex + "(" + answerEntry+")" + rightRegex;
			}
			//System.out.println(reg);
			Pattern p = Pattern.compile(reg); 
			Matcher m = p.matcher(text);
			int count = 0;
			while (m.find()&&m.groupCount()>=1) {
				count++;
			}
			
			answerMap.put(answerEntry, count);
			if(question.maxLength!=0&&
					(answerEntry.length()>question.maxLength 
							|| answerEntry.length()<question.minLength))continue;
			if(count>maxHit) {
				maxHit = count;
				answer = answerEntry;
			}
		}

		endTime = System.currentTimeMillis();
		System.out.println(answerMap); 
		System.out.println("Parse Time: " + (endTime-beginTime));
		return new Answer(answer, 5);
	}
}
