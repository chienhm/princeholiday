package com.morntea.test.chapter6;
//HuffmanShow.java

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;

public class HuffmanShow extends JFrame{
	
	
	JPanel jpanel=new JPanel();
	
	JLabel entropyText=new JLabel();
	JLabel avgCodeText=new JLabel();
	JLabel efficiencyText=new JLabel();
	
	int col=4;
	int row=256;
	
	final String[] names={"灰度值","出现频率","Huffman编码","码子长度"};
	
	final Object [][] data;	
	
	float entropy;
	float avgCode;
	float efficiency;
	
	float freq[];
	String sCode[];
	
	JButton exit=new JButton("关闭");
	
	public HuffmanShow(float entropy,float avgCode,float efficiency)
	{
		super("Huffman");
		setLocation(100,100);
		setSize(400,330);
		//添加窗口监听事件
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				//System.exit(0);
			}
		});
		
		this.entropy=entropy;
		this.avgCode=avgCode;
		this.efficiency=efficiency;
		
		entropyText.setText("         图像熵："+Float.toString(entropy));
		avgCodeText.setText("         平均码长："+Float.toString(avgCode));
		efficiencyText.setText("          编码效率："+Float.toString(efficiency));
		
		jpanel.setLayout(new BorderLayout());
		
		jpanel.add("North",entropyText);
		jpanel.add("Center",avgCodeText);
		jpanel.add("South",efficiencyText);
		
		data=new Object[row][col];
		
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jExit_ActionPerformed(e);
			}
		});
			
	}
	
	
	public void setData(float freq[],String sCode[])
	{
		this.freq=new float[row];
		this.sCode=new String[row];
		
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<col;j++)
			{
				data[i][j]=new Object();
			}
		}
		
		for(int i=0;i<row;i++)
		{
			data[i][0]=Integer.toString(i);
		}
		
		this.freq=freq;
		this.sCode=sCode;
		
		for(int i=0;i<256;i++)
		{
			data[i][1]=Float.toString(freq[i]);
			data[i][2]=sCode[i];
			data[i][3]=Integer.toString(sCode[i].length());	
		}
		
	}
	
	
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
        	scrollpane.setPreferredSize(new Dimension(380, 200));
        	
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