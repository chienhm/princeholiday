//Mirror.java
/*
	程序说明：对图像进行水平镜像和垂直镜像!
	
	最后修改时间：2003-12
	
	作者：haibin
	
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class Mirror extends Frame {
	Image im,tmp;
	int iw,ih;
	int[] pixels;
	boolean flagLoad=false;
	
	public Mirror(){
		this.setTitle("图像的水平和垂直镜像");
		Panel pdown;
		Button load,horizon,vertical,quit;
		
		//添加窗口监听事件
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		pdown = new Panel();
		pdown.setBackground(Color.lightGray);
		
		load=new Button("加载图像");
		horizon=new Button("水平镜像");
		vertical= new Button("垂直镜像");
		quit=new Button("退出");
		
		this.add(pdown,BorderLayout.SOUTH);
		
		pdown.add(load);
		pdown.add(horizon);
		pdown.add(vertical);
		pdown.add(quit);
		
		load.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jLoad_ActionPerformed(e);
			}
		});
		
		horizon.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jHorizon_ActionPerformed(e);
			}
		});
		
		vertical.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
				jVertical_ActionPerformed(e);
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
	
	public  void jHorizon_ActionPerformed(ActionEvent e){
		if(flagLoad){
		//可以进行连续的镜像!
		/*
		try{
		PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
		pg.grabPixels();
		}catch (InterruptedException e3) {
			e3.printStackTrace();
		}
		*/		
		
		//对图像进行水平镜像，Alpha值保持不变
		int [] tempPixels=new int[iw*ih];
		for(int i=0;i<iw*ih;i++)
		{
			tempPixels[i]=pixels[i];
		}
		
		for(int i=0;i<ih;i++)
		{
			for(int j=0;j<iw;j++)
			{
				//核心算法:第一列变为最后一列
				pixels[i*iw+j]=tempPixels[i*iw+(iw-j-1)];
			}
		}
		
		//将数组中的象素产生一个图像
		ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
		tmp=createImage(ip);
		repaint();
		}else{
		 JOptionPane.showMessageDialog(null,"请先打开一幅图片!",
                         "Alert",JOptionPane.WARNING_MESSAGE);
		}
	}
		
	public void jVertical_ActionPerformed(ActionEvent e){
		if(flagLoad){
		//可以进行连续的镜像
		/*
		try{
		PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
		pg.grabPixels();
		}catch (InterruptedException e3) {
			e3.printStackTrace();
		}
		*/
		//对图像进行垂直镜像，Alpha值保持不变
		int [] tempPixels=new int[iw*ih];
		for(int i=0;i<iw*ih;i++)
		{
			tempPixels[i]=pixels[i];
		}
			
		for(int i=0;i<ih;i++)
		{
			for(int j=0;j<iw;j++)
			{
				//核心算法:第一行变为最后一行
				pixels[i*iw+j]=tempPixels[(ih-i-1)*iw+j];
			}
		}
		
		//将数组中的象素产生一个图像
		ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
		tmp=createImage(ip);
		repaint();
		}else{
		 JOptionPane.showMessageDialog(null,"请先打开一幅图片!",
                         "Alert",JOptionPane.WARNING_MESSAGE);
	  	}
	}
	
	//程序退出
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
		}
		else{ }
	}
	
	//定义main方法，设置窗口的大小，显示窗口
	public static void main(String[] args) {
		Mirror mirror = new Mirror();
		mirror.setLocation(50,50);
		mirror.setSize(500,400);
		mirror.show();
	} 
}