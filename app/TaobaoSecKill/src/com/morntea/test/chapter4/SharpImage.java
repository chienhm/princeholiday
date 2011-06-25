package com.morntea.test.chapter4;
//SharpImage.java
/*
	程序说明：对图像进行平尖锐化处理
	          公式为:sharpRed=Math.abs(red6-red5)+Math.abs(red8-red5);
	                 sharpGreen=Math.abs(green6-green5)+Math.abs(green8-green5);
	                 sharpBlue=Math.abs(blue6-blue5)+Math.abs(blue8-blue5);
	
	程序修改：
	
	最后修改时间：2003-12
	
	作者：haibin
	
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class SharpImage extends Frame {
	Image im,tmp;
	int iw,ih;
	int[] pixels;
	boolean flag=false;
	
	//构造方法
	public SharpImage(){
		super("图象尖锐化处理");
		Panel pdown;
		Button load,run,quit,save;
		//添加窗口监听事件
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		pdown = new Panel();
		pdown.setBackground(Color.lightGray);
		
		load=new Button("Load");
		run = new Button("Sharp");
		save=new Button("Save");
		quit=new Button("Exit");
		
		this.add(pdown,BorderLayout.SOUTH);
		
		pdown.add(load);
		pdown.add(run);
		pdown.add(save);
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
		save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jSave_ActionPerformed(e);
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
	try{
	PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
	pg.grabPixels();
	}catch (InterruptedException e3) {
		e3.printStackTrace();
	}
	
	//象素的中间变量
	int tempPixels[]=new int[iw*ih];
	
	for(int i=0;i<iw*ih;i++)
	{
		tempPixels[i]=pixels[i];
	}
			
	//对图像进行尖锐化处理，Alpha值保持不变
	ColorModel cm=ColorModel.getRGBdefault();
	for(int i=1;i<ih-1;i++)
	{
		for(int j=1;j<iw-1;j++)
		{
		int alpha=cm.getAlpha(pixels[i*iw+j]);

		//对图像进行尖锐化
		int red6=cm.getRed(pixels[i*iw+j+1]);
		int red5=cm.getRed(pixels[i*iw+j]);
		int red8=cm.getRed(pixels[(i+1)*iw+j]);
		int sharpRed=Math.abs(red6-red5)+Math.abs(red8-red5);
		
		int green5=cm.getGreen(pixels[i*iw+j]);
		int green6=cm.getGreen(pixels[i*iw+j+1]);
		int green8=cm.getGreen(pixels[(i+1)*iw+j]);
		int sharpGreen=Math.abs(green6-green5)+Math.abs(green8-green5);
		
		int blue5=cm.getBlue(pixels[i*iw+j]);
		int blue6=cm.getBlue(pixels[i*iw+j+1]);
		int blue8=cm.getBlue(pixels[(i+1)*iw+j]);
		int sharpBlue=Math.abs(blue6-blue5)+Math.abs(blue8-blue5);
		
		if(sharpRed>255) {sharpRed=255;}
		if(sharpGreen>255) {sharpGreen=255;}
		if(sharpBlue>255) {sharpBlue=255;}
		
		tempPixels[i*iw+j]=alpha<<24|sharpRed<<16|sharpGreen<<8|sharpBlue;
		}
	}
	
	//将数组中的象素产生一个图像
	ImageProducer ip=new MemoryImageSource(iw,ih,tempPixels,0,iw);
	tmp=createImage(ip);
	flag=true;
	repaint();
	}else{
	 JOptionPane.showMessageDialog(null,"请先打开一幅图片!",
                 "Alert",JOptionPane.WARNING_MESSAGE);
  	}
}

	public void jQuit_ActionPerformed(ActionEvent e){
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
			g.drawImage(tmp,10,200,this);
		}else { }
	}
	
	public void jSave_ActionPerformed(ActionEvent e){
		//Save Image
		try {
			BufferedImage bufImg = new BufferedImage(tmp.getWidth(null), tmp.getHeight(null),BufferedImage.TYPE_INT_RGB);  
	        Graphics g = bufImg .createGraphics();  
	        g.drawImage(tmp, 0, 0, null);  
	        g.dispose(); 
			ImageIO.write(bufImg, "JPEG", new File("1-S.jpg"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	//定义main方法，设置窗口的大小，显示窗口
	public static void main(String[] args) {
		SharpImage si = new SharpImage();
		si.setLocation(150,150);
		si.setSize(500,400);
		si.show();
	} 
}