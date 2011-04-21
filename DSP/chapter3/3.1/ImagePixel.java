//ImagePixel.java
/*
	����˵������ȡͼ��Miss.jpg�����أ�������һ�������У�
	          ͬʱ���Ի��ͼ���RGBֵ��Alphaֵ��
	
	���ߣ�haibin
	
	����޸�ʱ�䣺2003-12
	
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class ImagePixel extends Frame {
	Image im;
	int i,iw,ih;
	int[] pixels;
	
	boolean loadFlag=false;

	public ImagePixel(){
		super("��ȡͼ�������");
		setLocation(100,100);
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
		run = new Button("��ȡ����");
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
	 	}catch(InterruptedException ee){ ee.printStackTrace();}
	 	
		loadFlag=true;
		repaint();
		
	}
	
	public  void jRun_ActionPerformed(ActionEvent e){
		if(loadFlag){
		
		//��ȡͼ��Ŀ��iw�͸߶�ih
		iw=im.getWidth(this);
		ih=im.getHeight(this);
		
		//��ȡͼ�������pixels
		pixels=new int[iw*ih];
		try{
		PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
		pg.grabPixels();
		}catch (InterruptedException eee) {
			eee.printStackTrace();
		}
		
		//����PixelsShow.java������ʾ
		PixelsShow ps=new PixelsShow(iw,ih);
		ps.setData(pixels);
		ps.showTable();
		ps.show();
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
		if(loadFlag)
		{
			g.drawImage(im,10,10,this);
		}
	}
	
	//����main���������ô��ڵĴ�С����ʾ����
	public static void main(String[] args) {
		ImagePixel ip = new ImagePixel();
		ip.setLocation(50,50);
		ip.setSize(500,400);
		ip.show();
	} 
}