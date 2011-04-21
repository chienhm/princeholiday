//LoadFromApplet.java
/*
	程序说明：该程序从本地加载图像Miss.jpg，然后显示这幅图像！
	
	作者：haibin
	
	最后修改时间：2003-12
	
*/

import java.awt.*;
import java.applet.*;

public class LoadFromApplet extends Applet {
	Image imObj;
	
	//init()方法，加载图像Miss.jpg
	public void init(){
		imObj=getImage(getCodeBase(),"Miss.jpg");
	}
	
	//Applet的paint()方法，显示图像信息
	public void paint(Graphics g){
		g.drawImage(imObj,0,0,this);
	}
}