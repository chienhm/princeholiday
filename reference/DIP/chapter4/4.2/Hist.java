//Hist.java

import java.awt.*;
import java.awt.event.*;
import java.awt.Window;

public class Hist extends Frame {
	
	int data[];
	int histogram[]=new int[256];
	
	public Hist(){
		this.setTitle("ͼ��ĻҶ�ֱ��ͼ");
		
		Panel pdown;
		Button quit;
		
		pdown=new Panel();
		quit=new Button("�رմ���");
		
		this.add(pdown,BorderLayout.SOUTH);
		
		pdown.add(quit);
		
		quit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jQuit_ActionPerformed(e);
			}
		});
		
		//��Ӵ��ڼ����¼�
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
	
	}
	
	public void jQuit_ActionPerformed(ActionEvent e){
		this.hide();
	}
	
	public void getData(int [] data,int iw,int ih){
		this.data=data;
		for(int i=0;i<iw*ih;i++)
		{
			int grey=data[i]&0xff;
			histogram[grey]++;
		}
		
		//�ҳ�������,���б�׼��.
		int temp=histogram[0];
		for(int i=1;i<256;i++)
		{
			if(temp<=histogram[i])
			{
				temp=histogram[i];	
			}
		}
		
		for(int i=0;i<256;i++)
		{
			histogram[i]=histogram[i]*200/temp;
		}
			
			
	}
	public void paint(Graphics g){
		
		//����ˮƽ�ʹ�ֱ����
		g.drawLine(100,250,356,250);
		g.drawLine(100,50,100,250);
		
		//������������
		g.drawString("0",98,263);
		g.drawString("50",145,263);
		g.drawString("100",193,263);
		g.drawString("150",243,263);
		g.drawString("200",293,263);
		g.drawString("250",343,263);   
		
		//������������
		g.drawString("0.5",83,145);
		g.drawString("1",90,60);
		
		//����ͼ���ֱ��ͼ
		for(int i=0;i<256;i++)
		{
			g.drawLine(100+i,250,100+i,250-histogram[i]);
		}
		
		g.drawString("��ͼ��ĻҶ�ֱ��ͼ������ʾ.",160,280);
			
	}

}