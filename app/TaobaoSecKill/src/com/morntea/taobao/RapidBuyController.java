package com.morntea.taobao;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RapidBuyController implements ActionListener, KeyListener{
	static ServerUI ui = new ServerUI();
	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	Thread count;
	RapidBuyServer server;
	boolean running;
	
	public RapidBuyController(int port) {
		try {
			server = new RapidBuyServer(port);
			ui.startButton.addActionListener(this);
			ui.stopButton.addActionListener(this);
			ui.clearButton.addActionListener(this);
			ui.result_lb.addKeyListener(this);
			ui.setTitle("秒杀服务器1.0版");
			running = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start(){
		if(server != null && ui != null){
			new Thread(new Runnable() {
				public void run() {
					server.serve();
				}
			}).start();
			ui.setVisible(true);
			ui.result_lb.requestFocus();
		}
	}
	
	public static void logServerStatus(String log){
		ui.serverStatus_ta.append(log);
		ui.serverStatus_ta.append("\n");
	}
	
	public static void clearServerStatus(){
		ui.serverStatus_ta.setText("");
	}
	
	public static void setGoogleResult(String question){
		String s = "http://www.baidu.com/s?wd="+question;
		System.out.println(s);
		try {
			ui.broswer.setURL(new URL(s));
		} catch (MalformedURLException e) {
		} 
	}
	
	public static void startSickill(String question){
		ui.question_lb.setText(question);
		ui.result_lb.setBackground(Color.green);
		ui.result_lb.setText("");
		ui.result_lb.requestFocus();
	}
	
	public void countDown(final Long t){
		if(!running){
			return;
		}
		count = new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Long timeLeft = t / 1000;
				while(timeLeft > 0 && running){
					try {
						ui.setTitle("倒计时开始，秒杀剩余时间还有：  "+timeLeft+"  秒！");
						Thread.sleep(1000);
						--timeLeft;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if(running){
					ui.setTitle("秒杀开始啦！！请速度填入答案！");
				}else{
					ui.setTitle("秒杀服务器  1.0  版");
				}
			}
		});
		count.start();
	}
	
	public void clearUI(){
		ui.setTitle("秒杀服务器1.0版");
		ui.question_lb.setText("");
		ui.result_lb.setText("");
		ui.result_lb.setBackground(Color.white);
		ui.serverStatus_ta.setText("");
		try {
			ui.broswer.setURL(new URL("http://www.baidu.com/"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public static String handleQuestion(String s){
		if(!s.equals("")){
			int index = s.indexOf("<br");
			if(index < 0){
				index = s.indexOf('>');
			}else {
				index = s.indexOf('>', index);
			}
			++index;
			int last = s.indexOf("</div>");
			s = s.substring(index, last).trim();
		}
		return s;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == ui.startButton){
			final String url = ui.url_tf.getText().trim();
			final String time = ui.time_tf.getText().trim();
			if(url.equals("")){
				return;
			}
			new Thread(new Runnable() {
				public void run() {
					if(time.equals("")){
						String question = WebHunter.getWebAsString(url);
						System.out.println(question);
						question = handleQuestion(question);
						setGoogleResult(question);
						startSickill(question);
					}else{
						try {
							Date d = format.parse(time);
							long t = d.getTime();
							long cur = System.currentTimeMillis();
							if(t < cur){
								return;
							}
							long time = t - cur;
							running = true;
							countDown(time);
							ui.url_tf.setEditable(false);
							ui.time_tf.setEditable(false);
							Thread.sleep(time-400);
							String question = WebHunter.getWebAsString(url);
							question = handleQuestion(question);
							setGoogleResult(question);
							System.out.println(question);
							startSickill(question);
						} catch (ParseException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
		}else if(e.getSource() == ui.stopButton){
			running = false;
			ui.setTitle("秒杀服务器1.0版");
			server.stop();
			clearUI();
			ui.url_tf.setEditable(true);
			ui.time_tf.setEditable(true);
		}else if(e.getSource() == ui.clearButton){
			ui.question_lb.setText("");
			ui.result_lb.setText("");
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			server.setResult(ui.result_lb.getText().trim());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		RapidBuyController controller = new RapidBuyController(10001);
		controller.start();
	}
}
