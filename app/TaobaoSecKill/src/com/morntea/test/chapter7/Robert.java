package com.morntea.test.chapter7;
//Robert.java
/*
	����˵������ͼ����б�Ե��ȡ,robert����
	          ��ʽΪ:robertRed=Math.abs(red5-red9)+Math.abs(red8-red6);
	
	�����޸ģ�
	
	����޸�ʱ�䣺2003-12
		
	���ߣ�haibin
	
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class Robert extends Frame {
	Image im,tmp;
	int i,iw,ih;
	int[] pixels;
	boolean flag=false;
	
	//ImagePixel�Ĺ��췽��
	public Robert(){
		this.setTitle("Robert��Ե���");
		Panel pdown;
		Button load,run,quit;
		//���Ӵ��ڼ����¼�
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		pdown = new Panel();
		pdown.setBackground(Color.lightGray);
		
		load=new Button("װ��ͼ��");
		run = new Button("��Ե���");
		quit=new Button("�˳�");
		
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
		flag=true;
		repaint();
	}
	
	public  void jRun_ActionPerformed(ActionEvent e){
		if(flag) {
		try{
		PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
		pg.grabPixels();
		}catch (InterruptedException e3) {
			e3.printStackTrace();
		}
				
		//��ͼ����б�Ե��ȡ��Alphaֵ���ֲ���
		ColorModel cm=ColorModel.getRGBdefault();
		for(i=1;i<ih-1;i++)
		{
			for(int j=1;j<iw-1;j++)
			{
			//��ͼ����б�Ե��ȡ
			int alpha=cm.getAlpha(pixels[i*iw+j]);
			int red5=cm.getRed(pixels[i*iw+j]);
			int red6=cm.getRed(pixels[i*iw+j+1]);
			int red8=cm.getRed(pixels[(i+1)*iw+j]);
			int red9=cm.getRed(pixels[(i+1)*iw+j+1]);
			
			int robertRed=Math.max(Math.abs(red5-red9),Math.abs(red8-red6));
			
			int green5=cm.getGreen(pixels[i*iw+j]);
			int green6=cm.getGreen(pixels[i*iw+j+1]);
			int green8=cm.getGreen(pixels[(i+1)*iw+j]);
			int green9=cm.getGreen(pixels[(i+1)*iw+j+1]);
			
			int robertGreen=Math.max(Math.abs(green5-green9),Math.abs(green8-green6));
			
			int blue5=cm.getBlue(pixels[i*iw+j]);
			int blue6=cm.getBlue(pixels[i*iw+j+1]);
			int blue8=cm.getBlue(pixels[(i+1)*iw+j]);
			int blue9=cm.getBlue(pixels[(i+1)*iw+j+1]);
			
			int robertBlue=Math.max(Math.abs(blue5-blue9),Math.abs(blue8-blue6));
			
			pixels[i*iw+j]=alpha<<24|robertRed<<16|robertGreen<<8|robertBlue;
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
		if(flag){
			g.drawImage(tmp,10,20,this);
		}else { }
	}
	
	//����main���������ô��ڵĴ�С����ʾ����
	public static void main(String[] args) {
		Robert ro = new Robert();
		ro.setLocation(50,50);
		ro.setSize(500,400);
		ro.show();
	} 
}