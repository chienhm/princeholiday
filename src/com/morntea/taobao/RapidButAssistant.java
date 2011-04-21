package com.morntea.taobao;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class RapidButAssistant {
	public static void main(String[] args) {
		try {
			Socket socket = new Socket("localhost", 10001);
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.writeBytes("Hello, jlajdk \r\n\t\n");
			out.writeBytes("yes \r\n");
			out.flush();
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			File file = new File("I://test.txt");
			if(!file.exists()){
				file.createNewFile();
			}
			String s = reader.readLine();
			System.out.println(s);
			out.close();
			reader.close();
			socket.close();
			socket = null;
			System.out.println("Assistant Closed!!!");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
