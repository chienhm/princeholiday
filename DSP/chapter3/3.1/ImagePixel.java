//ImagePixel.java
/*
	程序说明：获取图像Miss.jpg的象素，保存在一个数组中！
	          同时可以获得图像的RGB值和Alpha值。
	
	作者：haibin
	
	最后修改时间：2003-12
	
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class ImagePixel extends Frame {
	Image im;
	int i,iw,ih;
	int[] pixels;
	
	boolean loadFlag=false;

	public ImagePixel(){
		super("提取图像的象素");
		setLocation(100,100);
		Panel pdown;
		Button load,run,quit;
		
		//添加窗口监听事件
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		pdown = new Panel();
		pdown.setBackground(Color.lightGray);
		load=new Button("装载图像");
		run = new Button("提取象素");
		quit=new Button("退出");
		
		this.add(pdown,BorderLayout.SOUTH);
		
		pdown.add(load);
		pdown.add(run);
		pdown.add(quit);
		
		load.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jLoad_ActionPerformed(e);
			}
		});
		
		run.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jRun_ActionPerformed(e);
			}
		});
		
		quit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jQuit_ActionPerformed(e);
			}
		});
	}
	
	public void jLoad_ActionPerformed(ActionEvent e){
		//利用MediaTracker跟踪图像的加载
		MediaTracker tracker = new MediaTracker(this);
		im=Toolkit.getDefaultToolkit().getImage("Miss.jpg");
		tracker.addImage(im,0);
		
		//等待图像的完全加载
		try{
		tracker.waitForID(0);
	 	}catch(InterruptedException ee){ ee.printStackTrace();}
	 	
		loadFlag=true;
		repaint();
		
	}
	
	public  void jRun_ActionPerformed(ActionEvent e){
		if(loadFlag){
		
		//获取图像的宽度iw和高度ih
		iw=im.getWidth(this);
		ih=im.getHeight(this);
		
		//提取图像的象素pixels
		pixels=new int[iw*ih];
		try{
		PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
		pg.grabPixels();
		}catch (InterruptedException eee) {
			eee.printStackTrace();
		}
		
		//调用PixelsShow.java进行显示
		PixelsShow ps=new PixelsShow(iw,ih);
		ps.setData(pixels);
		ps.showTable();
		ps.show();
		repaint();
		}else{
	 	JOptionPane.showMessageDialog(null,"请先打开一幅图片!",
               	 "Alert",JOptionPane.WARNING_MESSAGE);
               	}
	}
	
	public void jQuit_ActionPerformed(ActionEvent e)
	{
		//System.exit(0);
		JOptionPane op =new JOptionPane();
		int exit=op.showConfirmDialog(this,"你要退出吗? ? ?","退出",JOptionPane.YES_NO_OPTION);
		
		if(exit==JOptionPane.YES_OPTION)
		{
			System.exit(0);
			
		}else{ }
		
	}
	
	//调用paint()方法，显示图像信息。
	public void paint(Graphics g){
		if(loadFlag)
		{
			g.drawImage(im,10,10,this);
		}
	}
	
	//定义main方法，设置窗口的大小，显示窗口
	public static void main(String[] args) {
		ImagePixel ip = new ImagePixel();
		ip.setLocation(50,50);
		ip.setSize(500,400);
		ip.show();
	} 
}