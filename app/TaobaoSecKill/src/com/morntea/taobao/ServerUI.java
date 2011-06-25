package com.morntea.taobao;
import java.awt.Color;
import java.awt.Font;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.jdesktop.jdic.browser.WebBrowser;


public class ServerUI extends JFrame {
	private static final long serialVersionUID = 1L;

	public JTextArea result_lb;
	public JTextField time_tf;
	public JTextArea serverStatus_ta;
	public JTextField url_tf;
	public JLabel question_lb;
	public JButton startButton;
	public JButton stopButton;
	public JButton clearButton;
	public WebBrowser broswer;

	/**
	 * Create the frame
	 */
	public ServerUI() {
		super();
		getContentPane().setLayout(null);
		setBounds(100, 100, 1123, 878);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JLabel urlLabel = new JLabel();
		urlLabel.setText("URL:");
		urlLabel.setBounds(374, 581, 45, 30);
		getContentPane().add(urlLabel);

		url_tf = new JTextField();
		url_tf.setBounds(411, 584, 601, 25);
		getContentPane().add(url_tf);

		startButton = new JButton();
		startButton.setText("Start");
		startButton.setBounds(1018, 584, 79, 25);
		getContentPane().add(startButton);

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 584, 353, 246);
		getContentPane().add(scrollPane);
		
				serverStatus_ta = new JTextArea();
				scrollPane.setViewportView(serverStatus_ta);

		final JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 10, 1087, 568);
		getContentPane().add(scrollPane_1);
		
		try {
			broswer = new WebBrowser();
			broswer.setURL(new URL("http://www.baidu.com/"));
		} catch (Exception e1) {
		}
		if(broswer != null){
			scrollPane_1.setViewportView(broswer);
		}

		final JLabel label = new JLabel();
		label.setText("问题：");
		label.setBounds(374, 655, 45, 30);
		getContentPane().add(label);

		question_lb = new JLabel();
		question_lb.setFont(new Font("幼圆", Font.BOLD, 14));
		question_lb.setBorder(new LineBorder(Color.black, 1, false));
		question_lb.setText("");
		question_lb.setBounds(411, 655, 601, 30);
		getContentPane().add(question_lb);

		final JLabel label_1 = new JLabel();
		label_1.setText("答案：");
		label_1.setBounds(374, 695, 45, 30);
		getContentPane().add(label_1);

		final JLabel timeLabel = new JLabel();
		timeLabel.setText("Time：");
		timeLabel.setBounds(374, 622, 45, 23);
		getContentPane().add(timeLabel);

		time_tf = new JTextField();
		time_tf.setBounds(411, 620, 601, 25);
		getContentPane().add(time_tf);

		result_lb = new JTextArea();
		result_lb.setFont(new Font("", Font.BOLD, 20));
		result_lb.setBorder(new LineBorder(Color.black, 1, false));
		result_lb.setBounds(411, 693, 686, 137);
		getContentPane().add(result_lb);

		stopButton = new JButton();
		stopButton.setText("Stop");
		stopButton.setBounds(1018, 621, 79, 25);
		getContentPane().add(stopButton);

		clearButton = new JButton();
		clearButton.setText("Clear");
		clearButton.setBounds(1018, 657, 79, 26);
		getContentPane().add(clearButton);
		//
	}
}
