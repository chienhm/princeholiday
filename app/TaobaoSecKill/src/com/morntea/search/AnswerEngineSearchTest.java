package com.morntea.search;

public class AnswerEngineSearchTest {
	public static void main(String[] args) {
		AnswerEngine aes = new AnswerEngineSearch("\"ɽ����ɽ¥��¥����ԭʫ�����ٰ�ۡ���е��¾���");
		System.out.println("���ǣ�" + aes.evaluateAnswer().text);

	}

}
