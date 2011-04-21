package com.morntea.test.chapter3;
//GreyLevel.java
/*
	程序说明：对图像进行量化！图像Miss.jpg为256×256，量化值K可以为256，128，64，32，16，8，4，2
	
	程序修改：
	
	最后修改时间：2003-12
	
	作者：haibin
	
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class GreyLevel extends Frame {
	Image im,tmp;
	int i,iw,ih;
	int[] pixels;
	boolean flag=false;
	
	public GreyLevel(){
		super("图像量化");
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
		load=new Button("Load Image");
		run = new Button("Liang Hua");
		quit=new Button("Exit");
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
		im=Toolkit.getDefaultToolkit().getImage("1.jpg");
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
		}
		*/
		
		//设定默认值为256级灰度
		int level=256;
		String s=JOptionPane.showInputDialog(null,"请输入量化级K值(256/128/64/32/16/8/4/2)：");
		
		if(s!=null){
			level=Integer.parseInt(s);
		}
		
		//检查输入是否正确
		if((level>256)|(level<0))
		{
			level=256;
			JOptionPane.showMessageDialog(null,"输入不正确，请重新输入!");
		}
		
		int greyLevel=256/level;
		
		//对图像进行量化处理
		ColorModel cm=ColorModel.getRGBdefault();
		for(i=0;i<iw*ih;i++)
		{
			int alpha=cm.getAlpha(pixels[i]);
			int grey=cm.getRed(pixels[i]);
			int temp=grey/greyLevel;
			
			grey=temp*greyLevel;
			pixels[i]=alpha<<24|grey<<16|grey<<8|grey;
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
		GreyLevel gl = new GreyLevel();
		gl.setLocation(60,60);
		gl.setSize(500,400);
		gl.show();
	} 
}