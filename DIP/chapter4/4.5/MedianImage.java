//MedianImage.java
/*
	����˵������ͼ�������ֵ�˲�.
	          ��ʽΪ:
	
	�����޸ģ�
	
	����޸�ʱ�䣺2003-12
	
	���ߣ�haibin
	
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
	
	//���췽��
	public MedianImage(){
		super("��ֵ�˲�");
		Panel pdown;
		Button load,run,save,quit;
		//��Ӵ��ڼ����¼�
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		pdown = new Panel();
		pdown.setBackground(Color.lightGray);
		
		load=new Button("װ��ͼ��");
		run = new Button("��ֵ�˲�");
		save=new Button("����");
		quit=new Button("�˳�");
		
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
		//����MediaTracker����ͼ��ļ���
		MediaTracker tracker = new MediaTracker(this);
		im=Toolkit.getDefaultToolkit().getImage("Miss.jpg");
		tracker.addImage(im,0);
	
		//�ȴ�ͼ�����ȫ����
		try{
		tracker.waitForID(0);
		}catch(InterruptedException e2){ e2.printStackTrace();}
		
		//��ȡͼ��Ŀ��iw�͸߶�ih
		iw=im.getWidth(this);
		ih=im.getHeight(this);
		pixels=new int[iw*ih];
		
		try{
		PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
		pg.grabPixels();
		}catch (InterruptedException e3) {
			e3.printStackTrace();
		}
	
		//�������е����ز���һ��ͼ��
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
				
		//��ͼ�������ֵ�˲���Alphaֵ���ֲ���
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
			
			//ˮƽ���������ֵ�˲�
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
			
			//ˮƽ���������ֵ�˲�
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
			
			//ˮƽ���������ֵ�˲�
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
		
		//�������е����ز���һ��ͼ��
		ImageProducer ip=new MemoryImageSource(iw,ih,pixels,0,iw);
		tmp=createImage(ip);
		flag=true;
		repaint();
		}else{
		 JOptionPane.showMessageDialog(null,"���ȴ�һ��ͼƬ!",
                         "Alert",JOptionPane.WARNING_MESSAGE);
		}
	}
	public void jSave_ActionPerformed(ActionEvent e){
		//Save Image
		if(flag){ }
		else{
		 JOptionPane.showMessageDialog(null,"���ȴ�һ��ͼƬ!",
                         "Alert",JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public void jQuit_ActionPerformed(ActionEvent e){
		//System.exit(0);
		JOptionPane op =new JOptionPane();
		int exit=op.showConfirmDialog(this,"��Ҫ�˳���? ? ?","�˳�",JOptionPane.YES_NO_OPTION);
		
		if(exit==JOptionPane.YES_OPTION)
		{
			System.exit(0);
			
		}else{ }
	}

	//����paint()��������ʾͼ����Ϣ��
	public void paint(Graphics g){
		if(flag){
			g.drawImage(tmp,10,20,this);
		}else { }
	}
	
	//����main���������ô��ڵĴ�С����ʾ����
	public static void main(String[] args) {
		MedianImage mi = new MedianImage();
		mi.setLocation(50,50);
		mi.setSize(500,400);
		mi.show();
	} 
}