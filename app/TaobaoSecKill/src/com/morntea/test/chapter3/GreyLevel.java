package com.morntea.test.chapter3;
//GreyLevel.java
/*
	����˵������ͼ�����������ͼ��Miss.jpgΪ256��256������ֵK����Ϊ256��128��64��32��16��8��4��2
	
	�����޸ģ�
	
	����޸�ʱ�䣺2003-12
	
	���ߣ�haibin
	
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class GreyLevel extends Frame {
	Image im,tmp;
	int i,iw,ih;
	int[] pixels;
	boolean flag=false;
	
	public GreyLevel(){
		super("ͼ������");
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
		load=new Button("Load Image");
		run = new Button("Liang Hua");
		quit=new Button("Exit");
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
		im=Toolkit.getDefaultToolkit().getImage("1.jpg");
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
		}
		*/
		
		//�趨Ĭ��ֵΪ256���Ҷ�
		int level=256;
		String s=JOptionPane.showInputDialog(null,"������������Kֵ(256/128/64/32/16/8/4/2)��");
		
		if(s!=null){
			level=Integer.parseInt(s);
		}
		
		//��������Ƿ���ȷ
		if((level>256)|(level<0))
		{
			level=256;
			JOptionPane.showMessageDialog(null,"���벻��ȷ������������!");
		}
		
		int greyLevel=256/level;
		
		//��ͼ�������������
		ColorModel cm=ColorModel.getRGBdefault();
		for(i=0;i<iw*ih;i++)
		{
			int alpha=cm.getAlpha(pixels[i]);
			int grey=cm.getRed(pixels[i]);
			int temp=grey/greyLevel;
			
			grey=temp*greyLevel;
			pixels[i]=alpha<<24|grey<<16|grey<<8|grey;
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
		GreyLevel gl = new GreyLevel();
		gl.setLocation(60,60);
		gl.setSize(500,400);
		gl.show();
	} 
}