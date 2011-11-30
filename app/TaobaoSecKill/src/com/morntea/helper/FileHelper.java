package com.morntea.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileHelper {
	public static String readFile(String filePath) {
		StringBuffer fileData = new StringBuffer(1024);
		int bufferSize = 1024;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			char[] buf = new char[bufferSize];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[bufferSize];
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileData.toString();
	}

	public static String readFileV2(String filePath) {
		StringBuilder content = new StringBuilder();
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(filePath));
			String str;
			while ((str = in.readLine()) != null) {
				content.append(str);
				content.append("\n");
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content.toString();
	}

	public static void writeFile(String filePath, String content) {
		FileWriter fwo;
		try {
			fwo = new FileWriter(filePath, false);
			BufferedWriter bwo = new BufferedWriter(fwo);
			bwo.write(content);
			bwo.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
