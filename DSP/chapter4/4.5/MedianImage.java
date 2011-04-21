//MedianImage.java
/*
	程序说明：对图像进行中值滤波.
	          公式为:
	
	程序修改：
	
	最后修改时间：2003-12
	
	作者：haibin
	
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class MedianImage extends Frame {
	Image im,tmp;
	int iw,ih;
	int[] pixels;
	boolean flag=false;
	
	//构造方法
	public MedianImage(){
		super("中值滤波");
		Panel pdown;
		Button load,run,save,quit;
		//添加窗口监听事件
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		pdown = new Panel();
		pdown.setBackground(Color.lightGray);
		
		load=new Button("装载图像");
		run = new Button("中值滤波");
		save=new Button("保存");
		quit=new Button("退出");
		
		this.add(pdown,BorderLayout.SOUTH);
		
		pdown.add(load);
		pdown.add(run);
		pdown.add(save);
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
		save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jSave_ActionPerformed(e);
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
		flag=true;
		repaint();
	}
	
	public  void jRun_ActionPerformed(ActionEvent e){
		if(flag){
		try{
		PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
		pg.grabPixels();
		}catch (InterruptedException e3) {
			e3.printStackTrace();
		}
				
		//对图像进行中值滤波，Alpha值保持不变
		ColorModel cm=ColorModel.getRGBdefault();
		for(int i=1;i<ih-1;i++)
		{
			for(int j=1;j<iw-1;j++)
			{
			int red,green,blue;
			int alpha=cm.getAlpha(pixels[i*iw+j]);
			
			int red2=cm.getRed(pixels[(i-1)*iw+j]);
			int red4=cm.getRed(pixels[i*iw+j-1]);
			int red5=cm.getRed(pixels[i*iw+j]);
			int red6=cm.getRed(pixels[i*iw+j+1]);
			int red8=cm.getRed(pixels[(i+1)*iw+j]);
			
			//水平方向进行中值滤波
			if(red4>=red5){
				if(red5>=red6) {red=red5;}
				else{
				if(red4>=red6) {red=red6;}
				else{red=red4;}
			}}
			else{
			if(red4>red6) {red=red4;}
				else{
				if(red5>red6) {red=red6;}
				else{red=red5;}
			}}
				
			int green2=cm.getGreen(pixels[(i-1)*iw+j]);
			int green4=cm.getGreen(pixels[i*iw+j-1]);
			int green5=cm.getGreen(pixels[i*iw+j]);
			int green6=cm.getGreen(pixels[i*iw+j+1]);
			int green8=cm.getGreen(pixels[(i+1)*iw+j]);
			
			//水平方向进行中值滤波
			if(green4>=green5){
				if(green5>=green6) {green=green5;}
				else{
				if(green4>=green6) {green=green6;}
				else{green=green4;}
			}}
			else{
			if(green4>green6) {green=green4;}
				else{
				if(green5>green6) {green=green6;}
				else{green=green5;}
			}}
				
			
			int blue2=cm.getBlue(pixels[(i-1)*iw+j]);
			int blue4=cm.getBlue(pixels[i*iw+j-1]);
			int blue5=cm.getBlue(pixels[i*iw+j]);
			int blue6=cm.getBlue(pixels[i*iw+j+1]);
			int blue8=cm.getBlue(pixels[(i+1)*iw+j]);
			
			//水平方向进行中值滤波
			if(blue4>=blue5){
				if(blue5>=blue6) {blue=blue5;}
				else{
				if(blue4>=blue6) {blue=blue6;}
				else{blue=blue4;}
			}}
			else{
			if(blue4>blue6) {blue=blue4;}
				else{
				if(blue5>blue6) {blue=blue6;}
				else{blue=blue5;}
			}}
			pixels[i*iw+j]=alpha<<24|red<<16|green<<8|blue;
			}
		}
		
		//将数组中的象素产生一个图像
		ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
		tmp=createImage(ip);
		flag=true;
		repaint();
		}else{
		 JOptionPane.showMessageDialog(null,"请先打开一幅图片!",
                         "Alert",JOptionPane.WARNING_MESSAGE);
		}
	}
	public void jSave_ActionPerformed(ActionEvent e){
		//Save Image
		if(flag){ }
		else{
		 JOptionPane.showMessageDialog(null,"请先打开一幅图片!",
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
		if(flag){
			g.drawImage(tmp,10,20,this);
		}else { }
	}
	
	//定义main方法，设置窗口的大小，显示窗口
	public static void main(String[] args) {
		MedianImage mi = new MedianImage();
		mi.setLocation(50,50);
		mi.setSize(500,400);
		mi.show();
	} 
}