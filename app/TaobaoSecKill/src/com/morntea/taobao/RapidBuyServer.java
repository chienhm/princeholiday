package com.morntea.taobao;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RapidBuyServer{
	ServerSocket server = null;
	boolean serve;
	String result = "";
	
	public RapidBuyServer(int port) throws IOException{
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
				RapidBuyTask task = new RapidBuyTask(s, this);
				new Thread(task).start();
			}
		}
	}
	
	public synchronized void setResult(String s){
		if(!s.equals("")){
			result = s;
			System.out.println(s);
			notifyAll();
		}
	}
	
	public String getResult(){
		synchronized (this) {
			while(result.equals("")){
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	public void stop(){
		serve = false;
		result = "";
	}
	
	public void close(){
		if(server != null){
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			server = null;
		}
	}
}
