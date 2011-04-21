package com.morntea.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

import com.morntea.util.StringHelper;

public class HttpSocketThread extends Thread {
	private Socket socket = null;
	
	public HttpSocketThread(Socket socket) {
		super();
		this.socket = socket;
	}
	
	public void run() {
		if (socket != null) {
			
			System.out.println("[" + HttpSocket.SDF.format(new Date()) + "]" + socket + " connect to server");
			try {
				BufferedReader in = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));

				String line = in.readLine();
				String action = null;
				System.out.println(line);
				if (line.startsWith("GET")){
					String tmp[] = line.split(" ");
					if(tmp.length>1) {
						action = tmp[1].substring(1);
					}
				}
				System.out.println(action);
				
				//Thread.sleep(1000 * 5);
				
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				//out.println("OK" + HttpSocket.SDF.format(new Date()));

				GrabFloor gf = Building360Buy.grabFloor();
				gf.dispatchAction(action, out);
				
				out.close();
				closeSocket(socket);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void closeSocket(Socket socket) {
		try {
			socket.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		System.out.println("[" + HttpSocket.SDF.format(new Date()) + "]" + socket + " quit");
	}
	
	public static void main(String[] args) {
		String js = StringHelper.readFromFile("C:\\WorkSpace\\SecKill\\javascript\\paipai.seckill.js");
		System.out.println(js);
	}
}
