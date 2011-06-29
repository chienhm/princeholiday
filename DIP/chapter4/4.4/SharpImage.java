//SharpImage.java
/*
	����˵������ͼ�����ƽ���񻯴���
	          ��ʽΪ:sharpRed=Math.abs(red6-red5)+Math.abs(red8-red5);
	                 sharpGreen=Math.abs(green6-green5)+Math.abs(green8-green5);
	                 sharpBlue=Math.abs(blue6-blue5)+Math.abs(blue8-blue5);
	
	�����޸ģ�
	
	����޸�ʱ�䣺2003-12
	
	���ߣ�haibin
	
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class SharpImage extends Frame {
	Image im,tmp;
	int iw,ih;
	int[] pixels;
	boolean flag=false;
	
	//���췽��
	public SharpImage(){
		super("ͼ����񻯴���");
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
		run = new Button("ͼ�����");
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
	try{
	PixelGrabber pg=new PixelGrabber(im,0,0,iw,ih,pixels,0,iw);
	pg.grabPixels();
	}catch (InterruptedException e3) {
		e3.printStackTrace();
	}
	
	//���ص��м����
	int tempPixels[]=new int[iw*ih];
	
	for(int i=0;i<iw*ih;i++)
	{
		tempPixels[i]=pixels[i];
	}
			
	//��ͼ����м��񻯴���Alphaֵ���ֲ���
	ColorModel cm=ColorModel.getRGBdefault();
	for(int i=1;i<ih-1;i++)
	{
		for(int j=1;j<iw-1;j++)
		{
		int alpha=cm.getAlpha(pixels[i*iw+j]);

		//��ͼ����м���
		int red6=cm.getRed(pixels[i*iw+j+1]);
		int red5=cm.getRed(pixels[i*iw+j]);
		int red8=cm.getRed(pixels[(i+1)*iw+j]);
		int sharpRed=Math.abs(red6-red5)+Math.abs(red8-red5);
		
		int green5=cm.getGreen(pixels[i*iw+j]);
		int green6=cm.getGreen(pixels[i*iw+j+1]);
		int green8=cm.getGreen(pixels[(i+1)*iw+j]);
		int sharpGreen=Math.abs(green6-green5)+Math.abs(green8-green5);
		
		int blue5=cm.getBlue(pixels[i*iw+j]);
		int blue6=cm.getBlue(pixels[i*iw+j+1]);
		int blue8=cm.getBlue(pixels[(i+1)*iw+j]);
		int sharpBlue=Math.abs(blue6-blue5)+Math.abs(blue8-blue5);
		
		if(sharpRed>255) {sharpRed=255;}
		if(sharpGreen>255) {sharpGreen=255;}
		if(sharpBlue>255) {sharpBlue=255;}
		
		tempPixels[i*iw+j]=alpha<<24|sharpRed<<16|sharpGreen<<8|sharpBlue;
		}
	}
	
	//�������е����ز���һ��ͼ��
	ImageProducer ip=new MemoryImageSource(iw,ih,tempPixels,0,iw);
	tmp=createImage(ip);
	flag=true;
	repaint();
	}else{
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
		SharpImage si = new SharpImage();
		si.setLocation(50,50);
		si.setSize(500,400);
		si.show();
	} 
}