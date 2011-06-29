//FFT.java
/*
	����˵������ͼ�����FFT�任��
	          
	
	�����޸ģ�
	
	����޸�ʱ�䣺2003-12
	
	���ߣ�haibin
	
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class FFT extends Frame {
	Image im,tmp;
	int iw,ih;
	int[] pixels;
	int [] newPixels;
	boolean flagLoad=false;
	
	OneFft of;
	
	//���췽��
	public FFT(){
		super("����Ҷ�任");
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
		run = new Button("����Ҷ�任");
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
		
		//��ȡͼ�������pixels
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
		
	public  void jRun_ActionPerformed(ActionEvent e){
		//�����ȼ���ͼ��,Ȼ��ſ��Խ���FFT�任
		if(flagLoad){
				
		//��ͼ����и���Ҷ�任
		ColorModel cm=ColorModel.getRGBdefault();
		
		// ����ֵ
		int w = 1;
		int h = 1;
		int wp=0;
		int hp=0;
		
		//������и���Ҷ�任�Ŀ��Ⱥ͸߶ȣ�2�������η���
		while(w*2<=iw)
		{
			w*=2;
			wp++;
		}
		while(h*2<=ih)
		{
			h*=2;
			hp++;
		}
		
		//�����ڴ�
		Complex [] td=new Complex[h*w];
		Complex [] fd=new Complex[h*w];
		
		newPixels=new int[h*w];
		
		//��ʼ��newPixels
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				newPixels[i*w+j]=pixels[i*iw+j]&0xff;
			}
		}
		
		//��ʼ��fd,td
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				fd[i*w+j]=new Complex();
				td[i*w+j]=new Complex(newPixels[i*w+j],0);
			}
		}
		
		//��ʼ���м����
		Complex [] tempW1=new Complex[w];
		Complex [] tempW2=new Complex[w];
		for(int j=0;j<w;j++)
		{
			tempW1[j]=new Complex(0,0);
			tempW2[j]=new Complex(0,0);
		}
		
		//��y�����Ͻ��п��ٸ���Ҷ�任
		for(int i=0;i<h;i++)
		{
			//ÿһ��������Ҷ�任
			for(int j=0;j<w;j++)
			{
				tempW1[j]=td[i*w+j];
			}
			
			//����һάFFT�任
			of=new OneFft();
			of.setData(tempW1,wp);
			tempW2=of.getData();
			
			for(int j=0;j<w;j++)
			{
				fd[i*w+j]=tempW2[j];
			}
		}
		
		//����任���
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				td[j*h+i]=fd[i*w+j];
			}
		}
		
		//��ʼ���м����
		tempW1=new Complex[h];
		tempW2=new Complex[h];
		for(int j=0;j<h;j++)
		{
			tempW1[j]=new Complex(0,0);					
			tempW2[j]=new Complex(0,0);
		}
			
		//��x������и���Ҷ�任
		for(int i=0;i<w;i++)
		{
			//ÿһ��������Ҷ�任
			for(int j=0;j<h;j++)
			{
				tempW1[j]=td[i*h+j];
			}
			
			//����һάFFT�任
			of=new OneFft();
			of.setData(tempW1,hp);
			tempW2=of.getData();
			
			for(int j=0;j<h;j++)
			{
				fd[i*h+j]=tempW2[j];
			}
		}
		
		//����Ƶ��
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				double re=fd[j*h+i].re;
				double im=fd[j*h+i].im;
				
				int ii=0,jj=0;
				int temp=(int)(Math.sqrt(re*re+im*im)/100);
				if(temp>255) { temp=255; }
				
				//��i�У�j�У���Ϊ��ii�У���jj��
				if(i<h/2) { ii=i+h/2; } else { ii=i-h/2; }
				if(j<w/2) { jj=j+w/2; } else { jj=j-w/2; }
				
				newPixels[ii*w+jj]=temp;
			}
		}
		
		for(int i=0;i<w*h;i++)
		{
			int alpha=cm.getAlpha(pixels[i]);
			int x=newPixels[i];
			newPixels[i]=alpha<<24|x<<16|x<<8|x;
		}
		
		//�������е����ز���һ��ͼ��
		ImageProducer ip=new MemoryImageSource(w,h,newPixels,0,w);
		tmp=createImage(ip);
		
		repaint();
	}else{
		 JOptionPane.showMessageDialog(null,"���ȴ�һ��ͼƬ!",
                         "Alert",JOptionPane.WARNING_MESSAGE);
		}
	}
	public void jQuit_ActionPerformed(ActionEvent e)
	{
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
		FFT fft = new FFT();
		fft.setLocation(50,50);
		fft.setSize(500,400);
		fft.show();
	} 
}