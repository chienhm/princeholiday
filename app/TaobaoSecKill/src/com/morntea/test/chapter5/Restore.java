package com.morntea.test.chapter5;
//Restore.java
/*
	����˵������ͼ����и�ԭ��
	
	�����޸ģ�
	
	����޸�ʱ�䣺2003-12
	
	���ߣ�haibin
	
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class Restore extends Frame {
	Image im,tmp;
	int iw,ih;
	int[] pixels;
	
	double [] newPixels;
	double [] newKernel;
	
	Complex [] complex;
	Complex [] comKernel;
	
	boolean flagLoad=false;
	boolean flagBlur=false;
	
	FFT2 fft2;
	IFFT2 ifft2;
	
	//���췽��
	public Restore(){
		this.setTitle("ͼ��ģ���͸�ԭ");
		Panel pdown;
		Button load,blur,run,quit;
		//���Ӵ��ڼ����¼�
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		pdown = new Panel();
		pdown.setBackground(Color.lightGray);
		
		load=new Button("װ��ͼ��");
		blur=new Button("ͼ��ģ��");
		run = new Button("ͼ��ԭ");
		quit=new Button("�˳�");
		
		this.add(pdown,BorderLayout.SOUTH);
		
		pdown.add(load);
		pdown.add(blur);
		pdown.add(run);
		pdown.add(quit);
		
		load.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jLoad_ActionPerformed(e);
			}
		});
		
		blur.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jBlur_ActionPerformed(e);
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
		flagLoad=true;
		repaint();
	}
		
	public void jBlur_ActionPerformed(ActionEvent e)
	{
		if(flagLoad){
		//��ͼ�����ģ������
		newPixels=new double [iw*ih];
		newKernel=new double [iw*ih];
		
		for(int i=0;i<ih;i++)
		{
			for(int j=0;j<iw;j++)
			{
				newPixels[i*iw+j]=pixels[i*iw+j]&0xff;
				if((i<5)&& (j<5))
				{
					newKernel[i*iw+j]=1.0/25;
					
				}else{ newKernel[i*iw+j]=0; }
			}
		}
		
		//��ʼ��
		complex=new Complex[iw*ih];
		comKernel=new Complex[iw*ih];
		
		for(int i=0;i<iw*ih;i++)
		{
			complex[i]=new Complex(0,0);
			comKernel[i]=new Complex(0,0);
		}
		
		//��ԭͼ�����FFT
		fft2=new FFT2();
		fft2.setData(iw,ih,newPixels);
		complex=fft2.getComplex();
		
		//�Ծ����˽���FFT
		fft2=new FFT2();
		fft2.setData(iw,ih,newKernel);		
		comKernel=fft2.getComplex();
		
		//Ƶ�����
		for(int i=0;i<iw*ih;i++)
		{
			double re=complex[i].re*comKernel[i].re - complex[i].im*comKernel[i].im;
			double im=complex[i].re*comKernel[i].im +complex[i].im*comKernel[i].re;
			complex[i].re=re;
			complex[i].im=im;	
		}
		
		
		
		//����FFT���任
		ifft2=new IFFT2();
		ifft2.setData(iw,ih,complex);
		newPixels=ifft2.getPixels();
		
		//��һ��
		double max=newPixels[0];
		for(int i=1;i<iw*ih;i++)
		{
			if(max<newPixels[i])
			{
				max=newPixels[i];
			}
		}
		
		//System.out.println("max: "+max);
		
		ColorModel cm=ColorModel.getRGBdefault();
		
		for(int i=0;i<ih;i++)
		{
			for(int j=0;j<iw;j++)
			{
				int alpha=cm.getAlpha(pixels[i*iw+j]);
				int x=(int)(newPixels[i*iw+j]*255/max);
				
				pixels[i*iw+j]=alpha<<24|x<<16|x<<8|x;
			}
		}
		
		//�������е����ز���һ��ͼ��
		ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
		tmp=createImage(ip);
		
		repaint();
		flagBlur=true;
		
		}else{
		 JOptionPane.showMessageDialog(null,"���ȴ�һ��ͼƬ!",
                         "Alert",JOptionPane.WARNING_MESSAGE);
		}
		
	}
	
	public  void jRun_ActionPerformed(ActionEvent e){
		
		if(flagLoad & flagBlur){
			
		//��ͼ����и�ԭ
		newPixels=new double [iw*ih];
		newKernel=new double [iw*ih];
		
		//��ʼ��
		for(int i=0;i<ih;i++)
		{
			for(int j=0;j<iw;j++)
			{
				newPixels[i*iw+j]=pixels[i*iw+j]&0xff;
				if((i<5)&& (j<5))
				{
					newKernel[i*iw+j]=1.0/25;
					
				}else{ newKernel[i*iw+j]=0; }
			}
		}
		
		//��ʼ��
		complex=new Complex[iw*ih];
		comKernel=new Complex[iw*ih];
		for(int i=0;i<iw*ih;i++)
		{
			complex[i]=new Complex(0,0);
			comKernel[i]=new Complex(0,0);
		}
		
		//��ԭͼ�����FFT
		fft2=new FFT2();
		fft2.setData(iw,ih,newPixels);
		complex=fft2.getComplex();
		
		//�Ծ����˽���FFT
		fft2=new FFT2();
		fft2.setData(iw,ih,newKernel);
		comKernel=fft2.getComplex();
		
		//���˲���ԭ
		for(int i=0;i<ih;i++)
		{
			for(int j=0;j<iw;j++)
			{
				double re=complex[i*iw+j].re;
				double im=complex[i*iw+j].im;
				double reKernel=comKernel[i*iw+j].re;
				double imKernel=comKernel[i*iw+j].im;
				double x=reKernel*reKernel+imKernel*imKernel;
				
				if(x>1e-3)
				{
					double r=(re*reKernel+im*imKernel)/x;
					double m=(im*reKernel-re*imKernel)/x;	
					complex[i*iw+j].re=r;
					complex[i*iw+j].im=m;
				}
				
				
			}
		}
		
		//����FFT���任
		ifft2=new IFFT2();
		ifft2.setData(iw,ih,complex);
		newPixels=ifft2.getPixels();
		
		//��һ��
		double max=newPixels[0];
		for(int i=1;i<iw*ih;i++)
		{
			if(max<newPixels[i])
			{
				max=newPixels[i];
			}
		}
		
		System.out.println("max: "+max);
		
		ColorModel cm=ColorModel.getRGBdefault();
		
		for(int i=0;i<ih;i++)
		{
			for(int j=0;j<iw;j++)
			{
				int alpha=cm.getAlpha(pixels[i*iw+j]);
				int x=(int)(newPixels[i*iw+j]*255/max);
				//int x=(int)newPixels[i*iw+j];
				
				pixels[i*iw+j]=alpha<<24|x<<16|x<<8|x;
			}
		}
		
		//�������е����ز���һ��ͼ��
		ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
		tmp=createImage(ip);
		
		repaint();
	}else{
		 JOptionPane.showMessageDialog(null,"���ȴ�һ��ͼƬ��������ģ������!",
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
		if(flagLoad){
			g.drawImage(tmp,10,20,this);
		}else { }
	}
	
	//����main���������ô��ڵĴ�С����ʾ����
	public static void main(String[] args) {
		Restore restore = new Restore();
		restore.setLocation(50,50);
		restore.setSize(500,400);
		restore.show();
	} 
}