package com.morntea.search;

public class AnswerEngineSearchTest {
	public static void main(String[] args) {
		AnswerEngine aes = new AnswerEngineSearch("\"山外青山楼外楼”在原诗《题临安邸》中的下句是");
		System.out.println("答案是：" + aes.evaluateAnswer().text);

	}

}
