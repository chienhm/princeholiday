package com.morntea.test.chapter4;
//Histogram.java
/*
	����˵������ͼ�����ֱ��ͼ���Ȼ�����,��ʾͼ���ֱ��ͼ.
	
	�����޸ģ�
	
	����޸�ʱ�䣺2003-12
	
	���ߣ�haibin
	
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class Histogram extends Frame {
	Image im,tmp;
	int i,iw,ih;
	int[] pixels;
	boolean flagLoad=false;
	boolean flagGrey=false;
	
	//���췽��
	public Histogram(){
		this.setTitle("ͼ���ֱ��ͼ���Ȼ�");
		
		Panel pdown;
		Button load,grey,hist,run,save,quit;
		//��Ӵ��ڼ����¼�
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		pdown = new Panel();
		pdown.setBackground(Color.lightGray);
		
		load=new Button("Load");
		grey=new Button("Grey");
		hist=new Button("Zhi Fang Tu");
		run=new Button("Average");
		save=new Button("Save");
		quit=new Button("Exit");
		
		this.add(pdown,BorderLayout.SOUTH);
		
		pdown.add(load);
		pdown.add(grey);
		pdown.add(hist);
		pdown.add(run);
		pdown.add(save);
		pdown.add(quit);
		
		load.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jLoad_ActionPerformed(e);
			}
		});
		
		hist.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jHist_ActionPerformed(e);
			}
		});
		
		grey.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jGrey_ActionPerformed(e);
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
		//����MediaTracker����ͼ��ļ���
		MediaTracker tracker = new MediaTracker(this);
		im=Toolkit.getDefaultToolkit().getImage("Miss.jpg");
		tracker.addImage(im,0);
	
		//�ȴ�ͼ�����ȫ����
		try{
		tracker.waitForID(0);
 		}catch(InterruptedException e2){ e2.printStackTrace();}
 	
 		//��ȡͼ��Ŀ��iw�͸߶�ih
		iw=im.getWidth(this);
		ih=im.getHeight(this);
		pixels=new int[iw*ih];
		
		try{
		PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
		pg.grabPixels();
		}catch (InterruptedException e3) {
			e3.printStackTrace();
		}
	
		//���ͼ���RGBֵ��Alphaֵ
		ColorModel cm=ColorModel.getRGBdefault();
		for(i=0;i<iw*ih;i++)
		{
			int alpha=cm.getAlpha(pixels[i]);
			int red=cm.getRed(pixels[i]);
			int green=cm.getGreen(pixels[i]);
			int blue=cm.getBlue(pixels[i]);
			pixels[i]=alpha<<24|red<<16|green<<8|blue;
		}
	
		//�������е����ز���һ��ͼ��
		ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
		tmp=createImage(ip);
		flagLoad=true;
		repaint();
	}
	
	public void jGrey_ActionPerformed(ActionEvent e){
		//���뱣֤ͼ������˼���,Ȼ��ſ��Խ��лҶȻ�
		if(flagLoad){
		try{
		PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
		pg.grabPixels();
		}catch (InterruptedException e3) {
			e3.printStackTrace();
		}
		
		ColorModel cm=ColorModel.getRGBdefault();
		for(i=0;i<ih;i++)
		{
			for(int j=0;j<iw;j++)
			{
				int alpha=cm.getAlpha(pixels[i*iw+j]);
				int red=cm.getRed(pixels[i*iw+j]);
				int green=cm.getGreen(pixels[i*iw+j]);
				int blue=cm.getBlue(pixels[i*iw+j]);
				
				int grey=(int)(0.3*red+0.59*green+0.11*blue);
				
				pixels[i*iw+j]=alpha<<24|grey<<16|grey<<8|grey;
			}
		}
		//�������е����ز���һ��ͼ��
		ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
		tmp=createImage(ip);
		flagGrey=true;
		repaint();
			
	}else{
		 JOptionPane.showMessageDialog(null,"���ȴ�һ��ͼƬ!",
                         "Alert",JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public void jHist_ActionPerformed(ActionEvent e){
		//ͼ���Ѿ�����,���ұ���˻Ҷ�ͼ��
		if(flagLoad && flagGrey){
	
		//��ʾͼ���ֱ��ͼ
		Hist h=new Hist();
		
		//��������
		h.getData(pixels,iw,ih);
		h.setLocation(50,50);
		h.setSize(480,300);
		h.show();
		
		}else{
		 JOptionPane.showMessageDialog(null,"���ȴ�һ��ͼƬ,���߰Ѹ�ͼƬ��Ϊ�Ҷ�ͼ��!",
                         "Alert",JOptionPane.WARNING_MESSAGE);
		}
	}
	
	//��ͼ�����ֱ��ͼ���Ȼ�����
	public  void jRun_ActionPerformed(ActionEvent e){
		//ͼ���Ѿ�����,���ұ���˻Ҷ�ͼ��
		if(flagLoad && flagGrey){
				
		//��ȡͼ���ֱ��ͼ
		int [] histogram=new int [256];
		ColorModel cm=ColorModel.getRGBdefault();
		for(i=0;i<ih;i++)
		{
			for(int j=0;j<iw;j++)
			{
				int grey=pixels[i*iw+j]&0xff;
				histogram[grey]++;
			}
		}
		//ֱ��ͼ���Ȼ�����
		double a=(double)255/(iw*ih);
		double [] c=new double [256];
		c[0]=(a*histogram[0]);
		for(i=1;i<256;i++)
		{
			c[i]=c[i-1]+(int)(a*histogram[i]);
		}
		for(i=0;i<ih;i++)
		{
			for(int j=0;j<iw;j++)
			{
				int alpha=cm.getAlpha(pixels[i*iw+j]);
				int grey=pixels[i*iw+j]&0x0000ff;
				int hist=(int)c[grey];
				
				pixels[i*iw+j]=alpha<<24|hist<<16|hist<<8|hist;
 				
			}
		}
		//�������е����ز���һ��ͼ��
		ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
		tmp=createImage(ip);

		repaint();
	}else{
		 JOptionPane.showMessageDialog(null,"���ȴ�һ��ͼƬ,���߰Ѹ�ͼƬ��Ϊ�Ҷ�ͼ��!",
                         "Alert",JOptionPane.WARNING_MESSAGE);
		}
	}
	public void jSave_ActionPerformed(ActionEvent e){
		//Save Image
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
		if(flagLoad){
			g.drawImage(tmp,10,20,this);
		}else { }
	}
	
	//����main���������ô��ڵĴ�С����ʾ����
	public static void main(String[] args) {
		Histogram hist = new Histogram();
		hist.setLocation(50,50);
		hist.setSize(500,400);
		hist.show();
	} 
}