package com.morntea.test.chapter5;
//IFFT2.java

public class IFFT2 {
	
	int iw,ih;
	double []pixels;
	Complex [] td;
	Complex [] fd;
	
	IFFT1 ifft1;
	
	// 赋初值
	int w = 1;
	int h = 1;
	int wp=0;
	int hp=0;
	
	//构造函数
	public IFFT2()
	{
		System.out.println("IFFT2()");
	}
	
	//传递数据
	public void setData(int iw,int ih,Complex []complex){
			
		this.iw=iw;
		this.ih=ih;
		
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
		td=new Complex[h*w];
		fd=new Complex[h*w];
		
		//初始化fd,td
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				fd[i*w+j]=new Complex(complex[i*w+j].re,complex[i*w+j].im);
				td[i*w+j]=new Complex();
			}
		}
		
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				td[i*w+j]=fd[j*h+i];
			}
		}
		
		//对x方向进行FFT反变换
		for(int i=0;i<w;i++)
		{
			//每一列做FFT反变换
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
			 
			ifft1=new IFFT1();
			ifft1.setData(tempW1,hp);
			
			tempW2=ifft1.getData();
			
			for(int j=0;j<h;j++)
			{
				fd[i*h+j]=tempW2[j];
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
		
		//在y方向上进行FFT反变换
		for(int i=0;i<h;i++)
		{
			//每一行做FFT反变换
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
			
			ifft1=new IFFT1();
			ifft1.setData(tempW1,wp);
			
			tempW2=ifft1.getData();
			
			for(int j=0;j<w;j++)
			{
				fd[i*w+j]=tempW2[j];
			}
		}
		
	}
		
	//
	public Complex[] getComplex(){
		return td;
	}
	
	//返回变换后的值	
	public double [] getPixels(){
		
		//计算原象素值
		pixels=new double[iw*ih];
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				double re=fd[i*w+j].re;
				double im=fd[i*w+j].im;
				
				double temp=Math.sqrt(re*re+im*im);
				
				if(temp>255) { temp=255; }
				
				pixels[i*w+j]=temp;
			}
		}
	return pixels;
	
	}
		
	public static void main(String []args)
	{
		new IFFT2();
	}
	
}