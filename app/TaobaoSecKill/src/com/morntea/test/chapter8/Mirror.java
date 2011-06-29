package com.morntea.test.chapter8;
//Mirror.java
/*
	����˵������ͼ�����ˮƽ����ʹ�ֱ����!
	
	����޸�ʱ�䣺2003-12
	
	���ߣ�haibin
	
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class Mirror extends Frame {
	Image im,tmp;
	int iw,ih;
	int[] pixels;
	boolean flagLoad=false;
	
	public Mirror(){
		this.setTitle("ͼ���ˮƽ�ʹ�ֱ����");
		Panel pdown;
		Button load,horizon,vertical,quit;
		
		//���Ӵ��ڼ����¼�
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		pdown = new Panel();
		pdown.setBackground(Color.lightGray);
		
		load=new Button("����ͼ��");
		horizon=new Button("ˮƽ����");
		vertical= new Button("��ֱ����");
		quit=new Button("�˳�");
		
		this.add(pdown,BorderLayout.SOUTH);
		
		pdown.add(load);
		pdown.add(horizon);
		pdown.add(vertical);
		pdown.add(quit);
		
		load.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jLoad_ActionPerformed(e);
			}
		});
		
		horizon.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jHorizon_ActionPerformed(e);
			}
		});
		
		vertical.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
				jVertical_ActionPerformed(e);
			}
		});
		
		quit.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
				jQuit_ActionPerformed(e);
			}
		});
	}
	
	public void jLoad_ActionPerformed(ActionEvent e){
		//����MediaTracker����ͼ��ļ���
		MediaTracker tracker = new MediaTracker(this);
		im=Toolkit.getDefaultToolkit().getImage("Miss.jpg");
		tracker.addImage(im,0);
	
		//�ȴ�ͼ�����ȫ����
		try{
		tracker.waitForID(0);
 		}catch(InterruptedException e2){ e2.printStackTrace();}
 	
 		//��ȡͼ��Ŀ���iw�͸߶�ih
		iw=im.getWidth(this);
		ih=im.getHeight(this);
		pixels=new int[iw*ih];
		
		try{
		PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
		pg.grabPixels();
		}catch (InterruptedException e3) {
			e3.printStackTrace();
		}
	
		//�������е����ز���һ��ͼ��
		ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
		tmp=createImage(ip);
		flagLoad=true;
		repaint();
	}
	
	public  void jHorizon_ActionPerformed(ActionEvent e){
		if(flagLoad){
		//���Խ��������ľ���!
		/*
		try{
		PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
		pg.grabPixels();
		}catch (InterruptedException e3) {
			e3.printStackTrace();
		}
		*/		
		
		//��ͼ�����ˮƽ����Alphaֵ���ֲ���
		int [] tempPixels=new int[iw*ih];
		for(int i=0;i<iw*ih;i++)
		{
			tempPixels[i]=pixels[i];
		}
		
		for(int i=0;i<ih;i++)
		{
			for(int j=0;j<iw;j++)
			{
				//�����㷨:��һ�б�Ϊ���һ��
				pixels[i*iw+j]=tempPixels[i*iw+(iw-j-1)];
			}
		}
		
		//�������е����ز���һ��ͼ��
		ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
		tmp=createImage(ip);
		repaint();
		}else{
		 JOptionPane.showMessageDialog(null,"���ȴ�һ��ͼƬ!",
                         "Alert",JOptionPane.WARNING_MESSAGE);
		}
	}
		
	public void jVertical_ActionPerformed(ActionEvent e){
		if(flagLoad){
		//���Խ��������ľ���
		/*
		try{
		PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
		pg.grabPixels();
		}catch (InterruptedException e3) {
			e3.printStackTrace();
		}
		*/
		//��ͼ����д�ֱ����Alphaֵ���ֲ���
		int [] tempPixels=new int[iw*ih];
		for(int i=0;i<iw*ih;i++)
		{
			tempPixels[i]=pixels[i];
		}
			
		for(int i=0;i<ih;i++)
		{
			for(int j=0;j<iw;j++)
			{
				//�����㷨:��һ�б�Ϊ���һ��
				pixels[i*iw+j]=tempPixels[(ih-i-1)*iw+j];
			}
		}
		
		//�������е����ز���һ��ͼ��
		ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
		tmp=createImage(ip);
		repaint();
		}else{
		 JOptionPane.showMessageDialog(null,"���ȴ�һ��ͼƬ!",
                         "Alert",JOptionPane.WARNING_MESSAGE);
	  	}
	}
	
	//�����˳�
	public void jQuit_ActionPerformed(ActionEvent e){
		//System.exit(0);
		JOptionPane op =new JOptionPane();
		int exit=op.showConfirmDialog(this,"��Ҫ�˳���? ? ?","�˳�",JOptionPane.YES_NO_OPTION);
		
		if(exit==JOptionPane.YES_OPTION)
		{
			System.exit(0);
			
		}else{ }	
	}
	
	//����paint()��������ʾͼ����Ϣ��
	public void paint(Graphics g){
		if(flagLoad){
			g.drawImage(tmp,10,20,this);
		}
		else{ }
	}
	
	//����main���������ô��ڵĴ�С����ʾ����
	public static void main(String[] args) {
		Mirror mirror = new Mirror();
		mirror.setLocation(50,50);
		mirror.setSize(500,400);
		mirror.show();
	} 
}