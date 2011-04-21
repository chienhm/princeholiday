package com.morntea.test.chapter5;
//FFT2.java


public class FFT2 {
	
	int iw,ih;
	double []pixels;
	Complex []td;
	Complex []fd;
	
	FFT1 fft1;
	
	// ���и���Ҷ�任�Ŀ�Ⱥ͸߶ȣ�2�������η���
	// ����ֵ
	int w = 1;
	int h = 1;
	int wp=0;
	int hp=0;
	
	//���캯��
	public FFT2()
	{
		System.out.println("FFT2()");
	}
	
	//��������
	public void setData(int iw,int ih,double []pixels){
			
			this.iw=iw;
			this.ih=ih;
			
			this.pixels=new double[iw*ih];
			this.pixels=pixels;
			
		//������и���Ҷ�任�Ŀ�Ⱥ͸߶ȣ�2�������η���
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
		
		//��ʼ��fd,td
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				fd[i*w+j]=new Complex();
				td[i*w+j]=new Complex(pixels[i*w+j],0);
			}
		}
		
		//��y�����Ͻ��п��ٸ���Ҷ�任
		for(int i=0;i<h;i++)
		{
			//ÿһ��������Ҷ�任
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
		
		//����任���
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				td[j*h+i]=fd[i*w+j];
			}
		}
		
		
		//��x������и���Ҷ�任
		for(int i=0;i<w;i++)
		{
			//ÿһ��������Ҷ�任
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
		
	//����FFT�任���ֵ	
	public Complex [] getComplex(){
		
		return td;
	
	}
	
	public double []getPixels(){
		
		//���Ƶ��
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				double re=td[i*w+j].re;
				double im=td[i*w+j].im;
				
				double temp=Math.sqrt(re*re+im*im)/100.0;
				
				if(temp>255) { temp=255;}
				
				//��i�У�j�У���Ϊ��ii�У���jj��
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