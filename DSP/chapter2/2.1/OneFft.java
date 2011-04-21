//OneFft.java

import java.awt.*;

public class OneFft {
  	
  	// 付立叶变换点数
	int count;
	
  	//循环变量
  	int  i,j,k;
  	
  	//中间变量
  	int bfsize,p;
  	
  	int power;
  	
  	Complex[] w,x1,x2,x;
  	
  	Complex[] fd;
  	
  	
  	public void setData(Complex [] data,int power){
  	
  	 	this.power=power;
  	 	
  	 	//角度
  		double angle;
  		 		
  		//计算傅立叶变换的点数
  		count=1<<power;	
  		
  		//分配空间
  		w=new Complex[count/2];
  		x=new Complex[count];
      		x1=new Complex[count];
      		x2=new Complex[count];
      		fd=new Complex[count];
      		
      		//初始化
      		for(i=0;i<count/2;i++)
      		{
      			w[i]=new Complex();
      		}
      		for(i=0;i<count;i++)
      		{
      			x[i]=new Complex();
      			x1[i]=new Complex();
      			x2[i]=new Complex();
      			fd[i]=new Complex();
      		}
      		
      		//计算加权系数
      		for(i=0;i<count/2;i++)
      		{
		        angle=-i*Math.PI*2/count;
		        w[i].re=Math.cos(angle);
	        	w[i].im=Math.sin(angle);
      		}
      		
      		//将实域点写入x1
      		for(i=0;i<count;i++)
      		{
      			x1[i]=data[i];
      			
      		}
  	}
  	
  public Complex [] getData(){
  	
      	//蝶形运算
      	for(k=0;k<power;k++)
      	{
	        for(j=0;j<1<<k;j++)
	        {
          		bfsize=1<<(power-k);
          		for(i=0;i<bfsize/2;i++)
          		{
          		Complex temp1=new Complex(0,0);
          		Complex temp2=new Complex(0,0);
          		
            		p=j*bfsize;
            		x2[i+p]=temp1.Add(x1[i+p],x1[i+p+bfsize/2]);
            		
            		temp2=temp1.Sub(x1[i+p],x1[i+p+bfsize/2]);
            		
            		x2[i+p+bfsize/2]=temp1.Mul(temp2,w[i*(1<<k)]);
          		}
        	}
        	x=x1;
        	x1=x2;
        	x2=x;
      	}
      	
      	
      	//重新排序
      	for(j=0;j<count;j++)
      	{
	        p=0;
	        for(i=0;i<power;i++)
	        {
          	 	if((j&(1<<i))!=0)
            		p+=1<<(power-i-1);
        	}
        	fd[j]=x1[p];
      	}
      	return fd;
 }
 	//测试
 	/*
	public static void main(String[] args){
		Complex [] td;
		Complex [] fd;
		
		int i=0;
		td=new Complex[4];
		fd=new Complex[4];
		
		for(i=0;i<4;i++)
		{
			td[i]=new Complex();
			fd[i]=new Complex();
		}
		
		OneFft of=new OneFft();
		
		for(i=0;i<4;i++)
		{
			td[i].re=i+1;
			td[i].im=0;
		}
	
		of.setData(td,2);
		fd=of.getData();
		
		for(i=0;i<4;i++)
		{
			System.out.println("the re of answer is "+td[i].re+"\n");
				
		}
		
		for(i=0;i<4;i++)
		{
			System.out.println("the re of answer is "+fd[i].re+"\n");
			System.out.println("the im of answer is "+fd[i].im+"\n");
		}	
	}
	*/
		
}