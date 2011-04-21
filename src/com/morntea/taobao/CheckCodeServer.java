package com.morntea.taobao;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CheckCodeServer {

	ServerSocket server = null;
	boolean serve;
	String result = "";
	
	public CheckCodeServer(int port) throws IOException{
		server = new ServerSocket(port);
	}
	
	public void serve(){
		serve = true;
		while(serve){
			Socket s = null;
			try {
				s = server.accept();
			} catch (IOException e) {
				break;
			}
			if(s != null){
			}
		}
	}
}
