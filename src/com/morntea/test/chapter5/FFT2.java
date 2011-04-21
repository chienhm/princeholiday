package com.morntea.test.chapter5;
//FFT2.java


public class FFT2 {
	
	int iw,ih;
	double []pixels;
	Complex []td;
	Complex []fd;
	
	FFT1 fft1;
	
	// 进行付立叶变换的宽度和高度（2的整数次方）
	// 赋初值
	int w = 1;
	int h = 1;
	int wp=0;
	int hp=0;
	
	//构造函数
	public FFT2()
	{
		System.out.println("FFT2()");
	}
	
	//传递数据
	public void setData(int iw,int ih,double []pixels){
			
			this.iw=iw;
			this.ih=ih;
			
			this.pixels=new double[iw*ih];
			this.pixels=pixels;
			
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
		
		td=new Complex[h*w];
		fd=new Complex[h*w];
		
		//初始化fd,td
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				fd[i*w+j]=new Complex();
				td[i*w+j]=new Complex(pixels[i*w+j],0);
			}
		}
		
		//在y方向上进行快速傅立叶变换
		for(int i=0;i<h;i++)
		{
			//每一行做傅立叶变换
			Complex [] tempW1=new Complex[w];
			Complex [] tempW2=new Complex[w];
			for(int j=0;j<w;j++)
			{
				tempW1[j]=new Complex(0,0);
				tempW2[j]=new Complex(0,0);
			}
			
			for(int j=0;j<w;j++)
			{
				tempW1[j]=td[i*w+j];
			}
			
			fft1=new FFT1();
			fft1.setData(tempW1,wp);
			
			tempW2=fft1.getData();
			
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
		
		
		//对x方向进行傅立叶变换
		for(int i=0;i<w;i++)
		{
			//每一列做傅立叶变换
			Complex [] tempW1=new Complex[h];
			Complex [] tempW2=new Complex[h];
			
			for(int j=0;j<h;j++)
			{
				tempW1[j]=new Complex(0,0);					
				tempW2[j]=new Complex(0,0);
			}
			
			for(int j=0;j<h;j++)
			{
				tempW1[j]=td[i*h+j];
			}
		
			fft1.setData(tempW1,hp);
			
			tempW2=fft1.getData();
			
			for(int j=0;j<h;j++)
			{
				fd[i*h+j]=tempW2[j];
			}
		}
		
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				td[i*w+j]=fd[j*h+i];
			}
		}
	}	
		
	//返回FFT变换后的值	
	public Complex [] getComplex(){
		
		return td;
	
	}
	
	public double []getPixels(){
		
		//获得频谱
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				double re=td[i*w+j].re;
				double im=td[i*w+j].im;
				
				double temp=Math.sqrt(re*re+im*im)/100.0;
				
				if(temp>255) { temp=255;}
				
				//第i行，j列，变为：ii行，第jj列
				/*
				int ii=0,jj=0;
				
				if(i<h/2) { ii=i+h/2;} else {ii=i-h/2;}
				if(j<w/2) {jj=j+w/2;} else { jj=j-w/2;}
				
				pixels[ii*w+jj]=temp;
				*/
				pixels[i*w+j]=temp;
			}
		}
		return pixels;
	}
		
	public static void main(String []args)
	{
		new FFT2();
	}
	
}