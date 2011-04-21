package com.morntea.test.chapter6;
//HuffmanCode.java
/*
	程序说明：对图像进行Huffman编码
	
	程序修改:
	
	最后修改时间：2003-12
	
	作者：haibin
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class HuffmanCode extends Frame {
	Image im,tmp;
	int iw,ih;
	int[] pixels;
	boolean flag=false;
	
	//构造方法
	public HuffmanCode(){
		this.setTitle("Huffman编码");
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
		run = new Button("编码(Huffman)");
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
			
		//保存图像的灰度
		int grey[]=new int [iw*ih];
		//获得图像的灰度值
		for(int i=0;i<iw*ih;i++)
		{
			grey[i]=pixels[i]&0xff;
		}	
		//对图像进行Huffman编码
		Huffman huffman=new Huffman(grey,iw,ih);
		System.out.println("iw:"+iw+"ih:"+ih);
		//h.test();
		huffman.huff();
		
		//图像熵
		float entropy;
		
		//平均码子长度
		float avgCode;
		
		//编码效率
		float efficiency;
		
		//出现频率
		float freq[]=new float[256];
		
		//Huffman编码
		String sCode[] =new String[256];
		
		entropy=huffman.getEntropy();
		avgCode=huffman.getAvgCode();
		efficiency=huffman.getEfficiency();
		
		//出现频率
		freq=huffman.getFreq();
		
		//Huffman编码
		sCode=huffman.getCode();
		
		
		//调用HuffmanShow.java进行显示
		HuffmanShow hs=new HuffmanShow(entropy,avgCode,efficiency);
		hs.setData(freq,sCode);
		hs.showTable();
		hs.show();

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
			g.drawImage(tmp,20,20,this);
		}else { }
	}
	
	//定义main方法，设置窗口的大小，显示窗口
	public static void main(String[] args) {
		HuffmanCode hc = new HuffmanCode();
		hc.setSize(500,400);
		hc.show();
	} 
}