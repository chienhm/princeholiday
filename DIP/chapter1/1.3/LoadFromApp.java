//LoadFromApp.java
/*
	����˵�����ó���ӱ��ؼ���ͼ��Miss.jpg��Ȼ����ʾ���ͼ��
	
	���ߣ�haibin
	
	����޸�ʱ�䣺2003-12
	
*/

import java.awt.*;
import java.awt.event.*;

public class LoadFromApp extends Frame {
	Image im;
	
	//LoadFromApp�Ĺ��췽��������ͼ��Miss.jpg
	public LoadFromApp(){
		super("����ͼ��");
		im=Toolkit.getDefaultToolkit().getImage("Miss.jpg");
		
		//��Ӵ��ڼ����¼�
		addWindowListener(new WindowAdapter(){
		public void windowClosing(WindowEvent e){
			System.exit(0);
		}
	});
	
	}
	
	//LoadFromApp��paint()��������ʾͼ����Ϣ
	public void paint(Graphics g){
		g.drawImage(im,30,30,this);
	}
	
	//����main���������ô��ڵĴ�С����ʾ����
	public static void main(String[] args) {
		LoadFromApp f = new LoadFromApp();
		f.setSize(500,400);
		f.show();
	} 
}