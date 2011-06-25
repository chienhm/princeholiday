package com.morntea.test.chapter3;
//Sample.java
/*
	程序说明：对图像进行采样！图像Miss.jpg为256×256，采样得N值可以是256，128，64，32，16等。
	
	程序修改：
	
	最后修改时间：2003-12
	
	作者：haibin
	
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class Sample extends Frame {
	Image im,tmp;
	int iw,ih;
	int[] pixels;
	boolean flag=false;
	
	//构造方法
	public Sample(){
		super("对图像采样");
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
		run = new Button("进行采样");
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
 		}catch(InterruptedException e2){ e2.printStackTrace();}
 	
 		//获取图像的宽度iw和高度ih
		iw=im.getWidth(this);
		ih=im.getHeight(this);
		pixels=new int[iw*ih];
		
		try{
		PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
		pg.grabPixels();
		}catch (InterruptedException e3) {
			e3.printStackTrace();
		}
	
		//将数组中的象素产生一个图像
		ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
		tmp=createImage(ip);
		flag=true;
		repaint();
	}
	
	public  void jRun_ActionPerformed(ActionEvent e){
		if(flag){
		/*
		try{
		PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
		pg.grabPixels();
		}catch (InterruptedException e3) {
			e3.printStackTrace();
		}*/
		
		//设定
		int grey=iw;
		String s=JOptionPane.showInputDialog(null,"请输入N值（256/128/64/32/16）：");
		
		if(s!=null){
			grey=Integer.parseInt(s);
		}
		
		//检查输入是否正确
		switch(grey)
		{
			case 256:break;
			case 128:break;
			case 64:break;
			case 32:break;
			case 16:break;
			default:grey=256;
				JOptionPane.showMessageDialog(null,"输入不正确，请重新输入!");
				break;
		}
		/*
		if((grey>256)|(grey<8)){
			grey=256;
			JOptionPane.showMessageDialog(null,"输入不正确，请重新输入!");
		}
		*/
		//对图像进行采样
		ColorModel cm=ColorModel.getRGBdefault();
		for(int i=0;i<iw*ih-1;i=i+(int)(256/grey))
		{
			int red,green,blue;
			int alpha=cm.getAlpha(pixels[i]);
			
			red=cm.getRed(pixels[i]);
			green=cm.getGreen(pixels[i]);
			blue=cm.getBlue(pixels[i]);
			
			for(int j=0;j<(int)(256/grey);j++)
			{
				pixels[i+j]=alpha<<24|red<<16|green<<8|blue;
			}
		}
		
		//将数组中的象素产生一个图像
		ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
		tmp=createImage(ip);
		flag=true;
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
		if(flag){
			g.drawImage(tmp,10,20,this);
		}else { }
	}
	
	//定义main方法，设置窗口的大小，显示窗口
	public static void main(String[] args) {
		Sample sample = new Sample();
		sample.setLocation(50,50);
		sample.setSize(500,400);
		sample.show();
	} 
}