package com.morntea.test.chapter6;
//HuffmanCode.java
/*
	����˵������ͼ�����Huffman����
	
	�����޸�:
	
	����޸�ʱ�䣺2003-12
	
	���ߣ�haibin
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class HuffmanCode extends Frame {
	Image im,tmp;
	int iw,ih;
	int[] pixels;
	boolean flag=false;
	
	//���췽��
	public HuffmanCode(){
		this.setTitle("Huffman����");
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
		run = new Button("����(Huffman)");
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
			
		//����ͼ��ĻҶ�
		int grey[]=new int [iw*ih];
		//���ͼ��ĻҶ�ֵ
		for(int i=0;i<iw*ih;i++)
		{
			grey[i]=pixels[i]&0xff;
		}	
		//��ͼ�����Huffman����
		Huffman huffman=new Huffman(grey,iw,ih);
		System.out.println("iw:"+iw+"ih:"+ih);
		//h.test();
		huffman.huff();
		
		//ͼ����
		float entropy;
		
		//ƽ�����ӳ���
		float avgCode;
		
		//����Ч��
		float efficiency;
		
		//����Ƶ��
		float freq[]=new float[256];
		
		//Huffman����
		String sCode[] =new String[256];
		
		entropy=huffman.getEntropy();
		avgCode=huffman.getAvgCode();
		efficiency=huffman.getEfficiency();
		
		//����Ƶ��
		freq=huffman.getFreq();
		
		//Huffman����
		sCode=huffman.getCode();
		
		
		//����HuffmanShow.java������ʾ
		HuffmanShow hs=new HuffmanShow(entropy,avgCode,efficiency);
		hs.setData(freq,sCode);
		hs.showTable();
		hs.show();

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
			g.drawImage(tmp,20,20,this);
		}else { }
	}
	
	//����main���������ô��ڵĴ�С����ʾ����
	public static void main(String[] args) {
		HuffmanCode hc = new HuffmanCode();
		hc.setSize(500,400);
		hc.show();
	} 
}