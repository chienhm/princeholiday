//LoadFromApplet.java
/*
	����˵�����ó���ӱ��ؼ���ͼ��Miss.jpg��Ȼ����ʾ���ͼ��
	
	���ߣ�haibin
	
	����޸�ʱ�䣺2003-12
	
*/

import java.awt.*;
import java.applet.*;

public class LoadFromApplet extends Applet {
	Image imObj;
	
	//init()����������ͼ��Miss.jpg
	public void init(){
		imObj=getImage(getCodeBase(),"Miss.jpg");
	}
	
	//Applet��paint()��������ʾͼ����Ϣ
	public void paint(Graphics g){
		g.drawImage(imObj,0,0,this);
	}
}