//ChangeGrey.java
/*
	程序说明：
	
	最后修改时间：2003-12
	
	作者：haibin
	
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class ChangeGrey extends Frame {
	Image im,tmp;
	int iw,ih;
	int[] pixels;
	boolean flag=false;
	
	//构造方法
	public ChangeGrey(){
		this.setTitle("二值化处理");
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
		run = new Button("二值化处理");
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
	
		//将数组中的象素产生一个图像
		ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
		tmp=createImage(ip);
		flag=true;
		repaint();
	}
	
	public  void jRun_ActionPerformed(ActionEvent e){
		if(flag) {
		try{
		PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
		pg.grabPixels();
		}catch (InterruptedException e3) {
			e3.printStackTrace();
		}
		
		//设定二值化的域值，默认值为100
		int grey=100;
		Object tmpGrey="100";
    		String s=JOptionPane.showInputDialog(null,"输入二值化的域值（0-255）：",tmpGrey);
    		//还会有异常抛出！
    		if(s!=null){
    			grey=Integer.parseInt(s);
    		}
		if(grey>255)
		{
			grey=255;
		}else if(grey<0)
		{
			grey=0;
		}
		//对图像进行二值化处理，Alpha值保持不变
		ColorModel cm=ColorModel.getRGBdefault();
		for(int i=0;i<iw*ih;i++)
		{
			int red,green,blue;
			int alpha=cm.getAlpha(pixels[i]);
			if(cm.getRed(pixels[i])>grey)
			{
				red = 255;
			}else{ red=0;}
			
			if(cm.getGreen(pixels[i])>grey)
			{
				green=255;
			}else{green=0;}
			
			if(cm.getBlue(pixels[i])>grey)
			{
				blue=255;
			}else{blue=0;}
			
			pixels[i]=alpha<<24|red<<16|green<<8|blue;
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
		if(flag){
			g.drawImage(tmp,20,20,this);
		}else { }
	}
	
	//定义main方法，设置窗口的大小，显示窗口
	public static void main(String[] args) {
		ChangeGrey cg = new ChangeGrey();
		cg.setLocation(50,50);
		cg.setSize(500,400);
		cg.show();
	} 
}