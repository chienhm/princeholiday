//FFT.java
/*
	程序说明：对图像进行FFT变换。
	          
	
	程序修改：
	
	最后修改时间：2003-12
	
	作者：haibin
	
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
	
	//构造方法
	public FFT(){
		super("傅立叶变换");
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
		run = new Button("傅立叶变换");
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
		
		//获取图像的象素pixels
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
		
	public  void jRun_ActionPerformed(ActionEvent e){
		//必须先加载图像,然后才可以进行FFT变换
		if(flagLoad){
				
		//对图像进行傅立叶变换
		ColorModel cm=ColorModel.getRGBdefault();
		
		// 赋初值
		int w = 1;
		int h = 1;
		int wp=0;
		int hp=0;
		
		//计算进行付立叶变换的宽度和高度（2的整数次方）
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
		
		//分配内存
		Complex [] td=new Complex[h*w];
		Complex [] fd=new Complex[h*w];
		
		newPixels=new int[h*w];
		
		//初始化newPixels
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				newPixels[i*w+j]=pixels[i*iw+j]&0xff;
			}
		}
		
		//初始化fd,td
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				fd[i*w+j]=new Complex();
				td[i*w+j]=new Complex(newPixels[i*w+j],0);
			}
		}
		
		//初始化中间变量
		Complex [] tempW1=new Complex[w];
		Complex [] tempW2=new Complex[w];
		for(int j=0;j<w;j++)
		{
			tempW1[j]=new Complex(0,0);
			tempW2[j]=new Complex(0,0);
		}
		
		//在y方向上进行快速傅立叶变换
		for(int i=0;i<h;i++)
		{
			//每一行做傅立叶变换
			for(int j=0;j<w;j++)
			{
				tempW1[j]=td[i*w+j];
			}
			
			//进行一维FFT变换
			of=new OneFft();
			of.setData(tempW1,wp);
			tempW2=of.getData();
			
			for(int j=0;j<w;j++)
			{
				fd[i*w+j]=tempW2[j];
			}
		}
		
		//保存变换结果
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				td[j*h+i]=fd[i*w+j];
			}
		}
		
		//初始化中间变量
		tempW1=new Complex[h];
		tempW2=new Complex[h];
		for(int j=0;j<h;j++)
		{
			tempW1[j]=new Complex(0,0);					
			tempW2[j]=new Complex(0,0);
		}
			
		//对x方向进行傅立叶变换
		for(int i=0;i<w;i++)
		{
			//每一列做傅立叶变换
			for(int j=0;j<h;j++)
			{
				tempW1[j]=td[i*h+j];
			}
			
			//进行一维FFT变换
			of=new OneFft();
			of.setData(tempW1,hp);
			tempW2=of.getData();
			
			for(int j=0;j<h;j++)
			{
				fd[i*h+j]=tempW2[j];
			}
		}
		
		//计算频谱
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				double re=fd[j*h+i].re;
				double im=fd[j*h+i].im;
				
				int ii=0,jj=0;
				int temp=(int)(Math.sqrt(re*re+im*im)/100);
				if(temp>255) { temp=255; }
				
				//第i行，j列，变为：ii行，第jj列
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
		
		//将数组中的象素产生一个图像
		ImageProducer ip=new MemoryImageSource(w,h,newPixels,0,w);
		tmp=createImage(ip);
		
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
		if(flagLoad){
			g.drawImage(tmp,10,20,this);
		}else { }
	}
	
	//定义main方法，设置窗口的大小，显示窗口
	public static void main(String[] args) {
		FFT fft = new FFT();
		fft.setLocation(50,50);
		fft.setSize(500,400);
		fft.show();
	} 
}