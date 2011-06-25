package com.morntea.test.chapter3;
//Sample.java
/*
	����˵������ͼ����в�����ͼ��Miss.jpgΪ256��256��������Nֵ������256��128��64��32��16�ȡ�
	
	�����޸ģ�
	
	����޸�ʱ�䣺2003-12
	
	���ߣ�haibin
	
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class Sample extends Frame {
	Image im,tmp;
	int iw,ih;
	int[] pixels;
	boolean flag=false;
	
	//���췽��
	public Sample(){
		super("��ͼ�����");
		Panel pdown;
		Button load,run,quit;
		//��Ӵ��ڼ����¼�
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		pdown = new Panel();
		pdown.setBackground(Color.lightGray);
		load=new Button("װ��ͼ��");
		run = new Button("���в���");
		quit=new Button("�˳�");
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
		/*
		try{
		PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
		pg.grabPixels();
		}catch (InterruptedException e3) {
			e3.printStackTrace();
		}*/
		
		//�趨
		int grey=iw;
		String s=JOptionPane.showInputDialog(null,"������Nֵ��256/128/64/32/16����");
		
		if(s!=null){
			grey=Integer.parseInt(s);
		}
		
		//��������Ƿ���ȷ
		switch(grey)
		{
			case 256:break;
			case 128:break;
			case 64:break;
			case 32:break;
			case 16:break;
			default:grey=256;
				JOptionPane.showMessageDialog(null,"���벻��ȷ������������!");
				break;
		}
		/*
		if((grey>256)|(grey<8)){
			grey=256;
			JOptionPane.showMessageDialog(null,"���벻��ȷ������������!");
		}
		*/
		//��ͼ����в���
		ColorModel cm=ColorModel.getRGBdefault();
		for(int i=0;i<iw*ih-1;i=i+(int)(256/grey))
		{
			int red,green,blue;
			int alpha=cm.getAlpha(pixels[i]);
			
			red=cm.getRed(pixels[i]);
			green=cm.getGreen(pixels[i]);
			blue=cm.getBlue(pixels[i]);
			
			for(int j=0;j<(int)(256/grey);j++)
			{
				pixels[i+j]=alpha<<24|red<<16|green<<8|blue;
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
	
	public void jQuit_ActionPerformed(ActionEvent e)
	{
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
		Sample sample = new Sample();
		sample.setLocation(50,50);
		sample.setSize(500,400);
		sample.show();
	} 
}