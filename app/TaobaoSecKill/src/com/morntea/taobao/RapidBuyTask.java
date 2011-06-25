package com.morntea.taobao;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RapidBuyTask implements Runnable{
	Socket socket = null;
	RapidBuyServer server;
	
	public RapidBuyTask(Socket s, RapidBuyServer ser){
		socket = s;
		server = ser;
	}
	
	@Override
	public void run() {
		if(socket == null){
			return;
		}
		try {
			RapidBuyController.logServerStatus("收到一条请求来自：   "+socket.getInetAddress().toString());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			String result = server.getResult();
			out.writeUTF("Result:"+result);
			out.flush();
			out.close();
			socket.close();
			socket = null;
			System.out.println("One Task Closed!!!!");
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
