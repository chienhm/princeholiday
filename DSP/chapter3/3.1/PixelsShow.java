//PixelsShow.java

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;

public class PixelsShow extends JFrame{

	JPanel jpanel=new JPanel();
	JLabel jlabel=new JLabel();
	int width=0;
	int height=0;
	
	final String[] names;
	final Object [][] data;	
	int  pixels[];
	
	JButton exit=new JButton("关闭");
	
	//构造函数
	public PixelsShow(int iw,int ih)
	{
		super("图像的象素数组");
		setLocation(100,100);
		setSize(400,400);
		//添加窗口监听事件
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				//System.exit(0);
			}
		});
		
		width=iw;
		height=ih;
		
		names=new String[width];
		for(int i=0;i<width;i++)
		{
			names[i]=Integer.toString(i);
		}
		
		jlabel.setText("图像的高度为："+ih+"\n"+"   图像的宽度为："+iw);
		jpanel.setLayout(new FlowLayout());
		jpanel.add(jlabel);
		
		data=new Object[height][width];
		
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jExit_ActionPerformed(e);
			}
		});	
	}
	
	//传递数据
	public void setData(int[] pix)
	{
		this.pixels=new int[pix.length];
		
		//初始化data数组
		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				data[i][j]=new Object();
			}
		}
		
		this.pixels=pix;
		
		//给data数组赋值为图像的灰度值
		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				data[i][j]=Integer.toString(pixels[i*width+j]&0xff);
			}
		}
	}
	
	//显示表格数据
	public void showTable()
	{
		TableModel dataModel = new AbstractTableModel() 
		{
            		public int getColumnCount() { return names.length; }
            		public int getRowCount() { return data.length;}
            		public Object getValueAt(int row, int col) {return data[row][col];}
	
		        public String getColumnName(int column) {return names[column];}
	            	public Class getColumnClass(int c) {return getValueAt(0, c).getClass();}
            		public boolean isCellEditable(int row, int col) {return true;}
            		public void setValueAt(Object aValue, int row, int column) 
            		{
		                System.out.println("Setting value to: " + aValue);
        	        	data[row][column] = aValue;
                	}
         	};
         	JTable tableView = new JTable(dataModel);
         	tableView.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
         	
         	
         	JScrollPane scrollpane = new JScrollPane(tableView);
		scrollpane.setBorder(new BevelBorder(BevelBorder.LOWERED));
        	scrollpane.setPreferredSize(new Dimension(380, 300));
        	
        	//setLayout(new BorderLayout());
        	Container pane=getContentPane();
        	pane.setLayout(new BorderLayout());
        	pane.add("North",scrollpane);
        	pane.add("Center",jpanel);
        	pane.add("South",exit);
        }	
        
        public void jExit_ActionPerformed(ActionEvent e)
        {
        	this.hide();
        }
        
	
}