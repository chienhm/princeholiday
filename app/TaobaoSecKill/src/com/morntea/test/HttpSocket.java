package com.morntea.test;

import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;

public class HttpSocket implements Runnable {
	ServerSocket serverSocket;
	static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public static int PORT = 8888;

	public HttpSocket() {
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (Exception e) {
			System.err.println("Can't start http server:" + e.getLocalizedMessage());
		}
		if (serverSocket == null) {
			System.exit(1);
		}
		
		new Thread(this).start();
		System.out.println("HTTP server is running, port:" + PORT);
	}

	public void run() {
		while (true) {
			try {
				Socket client = null;
				client = serverSocket.accept();
				new HttpSocketThread(client).start();
				// System.out.println(client+" connects to server");//this line will slow down the server
			} catch (Exception e) {
				System.err.println("Server error:" + e.getLocalizedMessage());
			}
		}
	}


	private static void usage() {
		System.out
				.println("Usage: java HTTPServer <port>\nDefault port is 80.");
	}

	public static void main(String[] args) {
		try {
			if (args.length != 1) {
				usage();
			} else if (args.length == 1) {
				PORT = Integer.parseInt(args[0]);
			}
		} catch (Exception ex) {
			System.err
					.println("Invalid port arguments. It must be a integer that greater than 0");
		}

		new HttpSocket();
	}

}