//Restore.java
/*
	程序说明：对图像进行复原。
	
	程序修改：
	
	最后修改时间：2003-12
	
	作者：haibin
	
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
	
	//构造方法
	public Restore(){
		this.setTitle("图形模糊和复原");
		Panel pdown;
		Button load,blur,run,quit;
		//添加窗口监听事件
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		pdown = new Panel();
		pdown.setBackground(Color.lightGray);
		
		load=new Button("装载图像");
		blur=new Button("图像模糊");
		run = new Button("图像复原");
		quit=new Button("退出");
		
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
		flagLoad=true;
		repaint();
	}
		
	public void jBlur_ActionPerformed(ActionEvent e)
	{
		if(flagLoad){
		//对图像进行模糊处理
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
		
		//初始化
		complex=new Complex[iw*ih];
		comKernel=new Complex[iw*ih];
		
		for(int i=0;i<iw*ih;i++)
		{
			complex[i]=new Complex(0,0);
			comKernel[i]=new Complex(0,0);
		}
		
		//对原图像进行FFT
		fft2=new FFT2();
		fft2.setData(iw,ih,newPixels);
		complex=fft2.getComplex();
		
		//对卷积核进行FFT
		fft2=new FFT2();
		fft2.setData(iw,ih,newKernel);		
		comKernel=fft2.getComplex();
		
		//频域相乘
		for(int i=0;i<iw*ih;i++)
		{
			double re=complex[i].re*comKernel[i].re - complex[i].im*comKernel[i].im;
			double im=complex[i].re*comKernel[i].im +complex[i].im*comKernel[i].re;
			complex[i].re=re;
			complex[i].im=im;	
		}
		
		
		
		//进行FFT反变换
		ifft2=new IFFT2();
		ifft2.setData(iw,ih,complex);
		newPixels=ifft2.getPixels();
		
		//归一化
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
		
		//将数组中的象素产生一个图像
		ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
		tmp=createImage(ip);
		
		repaint();
		flagBlur=true;
		
		}else{
		 JOptionPane.showMessageDialog(null,"请先打开一幅图片!",
                         "Alert",JOptionPane.WARNING_MESSAGE);
		}
		
	}
	
	public  void jRun_ActionPerformed(ActionEvent e){
		
		if(flagLoad & flagBlur){
			
		//对图像进行复原
		newPixels=new double [iw*ih];
		newKernel=new double [iw*ih];
		
		//初始化
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
		
		//初始化
		complex=new Complex[iw*ih];
		comKernel=new Complex[iw*ih];
		for(int i=0;i<iw*ih;i++)
		{
			complex[i]=new Complex(0,0);
			comKernel[i]=new Complex(0,0);
		}
		
		//对原图像进行FFT
		fft2=new FFT2();
		fft2.setData(iw,ih,newPixels);
		complex=fft2.getComplex();
		
		//对卷积核进行FFT
		fft2=new FFT2();
		fft2.setData(iw,ih,newKernel);
		comKernel=fft2.getComplex();
		
		//逆滤波复原
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
		
		//进行FFT反变换
		ifft2=new IFFT2();
		ifft2.setData(iw,ih,complex);
		newPixels=ifft2.getPixels();
		
		//归一化
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
		
		//将数组中的象素产生一个图像
		ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
		tmp=createImage(ip);
		
		repaint();
	}else{
		 JOptionPane.showMessageDialog(null,"请先打开一幅图片，并进行模糊处理!",
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
		if(flagLoad){
			g.drawImage(tmp,10,20,this);
		}else { }
	}
	
	//定义main方法，设置窗口的大小，显示窗口
	public static void main(String[] args) {
		Restore restore = new Restore();
		restore.setLocation(50,50);
		restore.setSize(500,400);
		restore.show();
	} 
}