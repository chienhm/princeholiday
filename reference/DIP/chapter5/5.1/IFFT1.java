//IFFT1.java


public class IFFT1 {
  	
  	// 付立叶变换点数
	int count;
  	int power;
  	
  	Complex[] x;
  	Complex[] fd;
  	
  	FFT1 fft1;
  	
  	public IFFT1(){
  		
  		//System.out.println("IFFT1.java");
  	}
  	
  	
  	public void setData(Complex [] data,int power){
  	
  	 	this.power=power;
  		 		
  		//计算傅立叶变换的点数
  		count=1<<power;	
  		
  		//分配空间
  		x=new Complex[count];
      		fd=new Complex[count];
      		
      		for(int i=0;i<count;i++)
      		{
      			x[i]=new Complex();
      			fd[i]=new Complex();
      		}
      		
      		//将频域点写入x
      		for(int i=0;i<count;i++)
      		{
      			x[i]=data[i];
      			
      		}
  	}
  	
  public Complex [] getData(){
  	
  	// 求共轭
  	for(int i=0;i<count;i++)
  	{
  		double im=-x[i].im;
  		x[i].im=im;
  	}
  	
  	fft1=new FFT1();
  	fft1.setData(x,power);
  	fd=fft1.getData();
  	
  	for(int i=0;i<count;i++)
  	{
  		double re=fd[i].re;
  		double im=-fd[i].im;
  		fd[i].im=im/count;
  		fd[i].re=re/count;
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
		
		IFFT1 ifft1=new IFFT1();
		
		td[0].re=10;
		td[0].im=0;
		
		td[1].re=-2;
		td[1].im=2;
		
		td[2].re=-2;
		td[2].im=0;
		
		td[3].re=-2;
		td[3].im=-2;
	
		ifft1.setData(td,2);
		fd=ifft1.getData();
		
		for(i=0;i<4;i++)
		{
			System.out.println("the re of answer is "+td[i].re+"\n");
			System.out.println("the im of answer is "+td[i].im+"\n");
				
		}
		
		for(i=0;i<4;i++)
		{
			System.out.println("the re of answer is "+fd[i].re+"\n");
			System.out.println("the im of answer is "+fd[i].im+"\n");
		}	
	}
	*/
		
}