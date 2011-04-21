//LineGrey.java
/*
	程序说明：对图像的灰度进行线性拉伸。
	          公式为grey2 = k * grey1 + a; 
	
	程序修改：
	
	最后修改时间：2003-12
	
	作者：haibin
	
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class LineGrey extends Frame {
	Image im,tmp;
	int i,iw,ih;
	int[] pixels;
	boolean flag=false;
	
	//构造方法
	public LineGrey(){
		super("线性灰度变换");
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
		run = new Button("线性拉伸");
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
		
		try{
		PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
		pg.grabPixels();
		}catch (InterruptedException e3) {
			e3.printStackTrace();
		}
	
		//获得图像的RGB值和Alpha值
		ColorModel cm=ColorModel.getRGBdefault();
		for(i=0;i<iw*ih;i++)
		{
			int alpha=cm.getAlpha(pixels[i]);
			int red=cm.getRed(pixels[i]);
			int green=cm.getGreen(pixels[i]);
			int blue=cm.getBlue(pixels[i]);
			pixels[i]=alpha<<24|red<<16|green<<8|blue;
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
				
		//对图像进行进行线性拉伸，Alpha值保持不变
		ColorModel cm=ColorModel.getRGBdefault();
		for(i=0;i<iw*ih;i++)
		{
			int alpha=cm.getAlpha(pixels[i]);
			int red=cm.getRed(pixels[i]);
			int green=cm.getGreen(pixels[i]);
			int blue=cm.getBlue(pixels[i]);
			
			//增加了图像的亮度
			red=(int)(1.1 * red +30);
			green=(int)(1.1 * green +30);
			blue=(int)(1.1 * blue +30);
			if(red>=255)
			{
				red=255;
			}
			if(green>=255)
			{
				green=255;
			}
			if(blue>=255)
			{
				blue=255;
			}
			pixels[i]=alpha<<24|red<<16|green<<8|blue;
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
		LineGrey lg = new LineGrey();
		lg.setLocation(50,50);
		lg.setSize(540,400);
		lg.show();
	} 
}