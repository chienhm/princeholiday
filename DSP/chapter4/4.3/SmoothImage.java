//SmoothImage.java
/*
	����˵������ͼ�����ƽ��������
	          ��ʽΪ:averageRed=(red1+red2+red3+red4+red6+red7+red8+red9)/8;
	                 averageGreen=(green1+green2+green3+green4+green6+green7+green8+green9)/8;
	                 averageBlue=(blue1+blue2+blue3+blue4+blue6+blue7+blue8+blue9)/8;
	
	�����޸ģ�
	
	����޸�ʱ�䣺2003-12
	
	���ߣ�haibin
	
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class SmoothImage extends Frame {
	Image im,tmp;
	int iw,ih;
	int[] pixels;
	boolean flag=false;
	
	//���췽��
	public SmoothImage(){
		super("ͼ��ƽ��");
		
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
		run = new Button("ͼ��ƽ��");
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
				
		//��ͼ�����ƽ��������Alphaֵ���ֲ���
		ColorModel cm=ColorModel.getRGBdefault();
		for(int i=1;i<ih-1;i++)
		{
			for(int j=1;j<iw-1;j++)
			{       
			int alpha=cm.getAlpha(pixels[i*iw+j]);
			int red=cm.getRed(pixels[i*iw+j]);
			int green=cm.getGreen(pixels[i*iw+j]);
			int blue=cm.getBlue(pixels[i*iw+j]);
			
			//��ͼ�����ƽ��
			int red1=cm.getRed(pixels[(i-1)*iw+j-1]);
			int red2=cm.getRed(pixels[(i-1)*iw+j]);
			int red3=cm.getRed(pixels[(i-1)*iw+j+1]);
			int red4=cm.getRed(pixels[i*iw+j-1]);
			int red6=cm.getRed(pixels[i*iw+j+1]);
			int red7=cm.getRed(pixels[(i+1)*iw+j-1]);
			int red8=cm.getRed(pixels[(i+1)*iw+j]);
			int red9=cm.getRed(pixels[(i+1)*iw+j+1]);
			int averageRed=(red1+red2+red3+red4+red6+red7+red8+red9)/8;
			
			int green1=cm.getGreen(pixels[(i-1)*iw+j-1]);
			int green2=cm.getGreen(pixels[(i-1)*iw+j]);
			int green3=cm.getGreen(pixels[(i-1)*iw+j+1]);
			int green4=cm.getGreen(pixels[i*iw+j-1]);
			int green6=cm.getGreen(pixels[i*iw+j+1]);
			int green7=cm.getGreen(pixels[(i+1)*iw+j-1]);
			int green8=cm.getGreen(pixels[(i+1)*iw+j]);
			int green9=cm.getGreen(pixels[(i+1)*iw+j+1]);
			int averageGreen=(green1+green2+green3+green4+green6+green7+green8+green9)/8;
			
			int blue1=cm.getBlue(pixels[(i-1)*iw+j-1]);
			int blue2=cm.getBlue(pixels[(i-1)*iw+j]);
			int blue3=cm.getBlue(pixels[(i-1)*iw+j+1]);
			int blue4=cm.getBlue(pixels[i*iw+j-1]);
			int blue6=cm.getBlue(pixels[i*iw+j+1]);
			int blue7=cm.getBlue(pixels[(i+1)*iw+j-1]);
			int blue8=cm.getBlue(pixels[(i+1)*iw+j]);
			int blue9=cm.getBlue(pixels[(i+1)*iw+j+1]);
			int averageBlue=(blue1+blue2+blue3+blue4+blue6+blue7+blue8+blue9)/8;
			
			pixels[i*iw+j]=alpha<<24|averageRed<<16|averageGreen<<8|averageBlue;
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
		SmoothImage si = new SmoothImage();
		si.setLocation(50,50);
		si.setSize(500,400);
		si.show();
	} 
}