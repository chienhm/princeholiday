//LoadFromApp.java
/*
	程序说明：该程序从本地加载图像Miss.jpg，然后显示这幅图像！
	
	作者：haibin
	
	最后修改时间：2003-12
	
*/

import java.awt.*;
import java.awt.event.*;

public class LoadFromApp extends Frame {
	Image im;
	
	//LoadFromApp的构造方法，加载图像Miss.jpg
	public LoadFromApp(){
		super("加载图像！");
		im=Toolkit.getDefaultToolkit().getImage("Miss.jpg");
		
		//添加窗口监听事件
		addWindowListener(new WindowAdapter(){
		public void windowClosing(WindowEvent e){
			System.exit(0);
		}
	});
	
	}
	
	//LoadFromApp的paint()方法，显示图像信息
	public void paint(Graphics g){
		g.drawImage(im,30,30,this);
	}
	
	//定义main方法，设置窗口的大小，显示窗口
	public static void main(String[] args) {
		LoadFromApp f = new LoadFromApp();
		f.setSize(500,400);
		f.show();
	} 
}