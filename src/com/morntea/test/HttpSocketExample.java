package com.morntea.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.StringTokenizer;

public class HttpSocketExample implements Runnable {
	/**
     * 
     */
	ServerSocket serverSocket;// 服务器Socket

	/**
	 * 服务器监听端口, 默认为 80.
	 */
	public static int PORT = 8888;// 标准HTTP端口

	/**
	 * 开始服务器 Socket 线程.
	 */
	public HttpSocketExample() {
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (Exception e) {
			System.out.println("无法启动HTTP服务器:" + e.getLocalizedMessage());
		}
		if (serverSocket == null)
			System.exit(1);// 无法开始服务器
		new Thread(this).start();
		System.out.println("HTTP服务器正在运行,端口:" + PORT);
	}

	/**
	 * 运行服务器主线程, 监听客户端请求并返回响应.
	 */
	public void run() {
		while (true) {
			try {
				Socket client = null;// 客户Socket
				client = serverSocket.accept();// 客户机(这里是 IE 等浏览器)已经连接到当前服务器
				if (client != null) {
					System.out.println("连接到服务器的用户:" + client);
					try {
						// 第一阶段: 打开输入流
						BufferedReader in = new BufferedReader(
								new InputStreamReader(client.getInputStream()));

						System.out.println("客户端发送的请求信息:\n***************");
						// 读取第一行, 请求地址
						String line = in.readLine();
						System.out.println(line);
						String resource = line.substring(line.indexOf('/'),
								line.lastIndexOf('/') - 5);
						// 获得请求的资源的地址
						resource = URLDecoder.decode(resource, "UTF-8");// 反编码
																		// URL
																		// 地址
						String method = new StringTokenizer(line).nextElement()
								.toString();// 获取请求方法, GET 或者 POST

						// 读取所有浏览器发送过来的请求参数头部信息
						while ((line = in.readLine()) != null) {
							System.out.println(line);

							if (line.equals(""))
								break;
						}

						// 显示 POST 表单提交的内容, 这个内容位于请求的主体部分
						if ("POST".equalsIgnoreCase(method)) {
							System.out.println(in.readLine());
						}

						System.out.println("请求信息结束\n***************");
						System.out.println("用户请求的资源是:" + resource);
						System.out.println("请求的类型是: " + method);
						// GIF 图片就读取一个真实的图片数据并返回给客户端
						if (resource.endsWith(".gif")) {
							fileService("images/test.gif", client);
							closeSocket(client);
							continue;
						}

						// 请求 JPG 格式就报错 404
						if (resource.endsWith(".jpg")) {
							PrintWriter out = new PrintWriter(client
									.getOutputStream(), true);
							out.println("HTTP/1.0 404 Not found");// 返回应答消息,并结束应答
							out.println();// 根据 HTTP 协议, 空行将结束头信息
							out.close();
							closeSocket(client);
							continue;
						} else {
							// 用 writer 对客户端 socket 输出一段 HTML 代码
							PrintWriter out = new PrintWriter(client
									.getOutputStream(), true);
							out.println("HTTP/1.0 200 OK");// 返回应答消息,并结束应答
							out.println("Content-Type:text/html;charset=GBK");
							out.println();// 根据 HTTP 协议, 空行将结束头信息

							out.println("<h1> Hello Http Server</h1>");
							out.println("你好, 这是一个 Java HTTP 服务器 demo 应用.<br>");
							out.println("您请求的路径是: " + resource + "<br>");
							out
									.println("这是一个支持虚拟路径的图片:<img src='abc.gif'><br>"
											+ "<a href='abc.gif'>点击打开abc.gif, 是个服务器虚拟路径的图片文件.</a>");
							out
									.println("<br>这是个会反馈 404 错误的的图片:<img src='test.jpg'><br><a href='test.jpg'>点击打开test.jpg</a><br>");
							out
									.println("<form method=post action='/'& gt;POST 表单 <input name=username value='用户'> < input name=submit type=submit value=submit></form>");
							out.close();

							closeSocket(client);
						}
					} catch (Exception e) {
						System.out.println("HTTP服务器错误:"
								+ e.getLocalizedMessage());
					}
				}
				// System.out.println(client+"连接到HTTP服务器");//如果加入这一句,服务器响应速度会很慢
			} catch (Exception e) {
				System.out.println("HTTP服务器错误:" + e.getLocalizedMessage());
			}
		}
	}

	/**
	 * 关闭客户端 socket 并打印一条调试信息.
	 * 
	 * @param socket
	 *            客户端 socket.
	 */
	void closeSocket(Socket socket) {
		try {
			socket.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		System.out.println(socket + "离开了HTTP服务器");
	}

	/**
	 * 读取一个文件的内容并返回给浏览器端.
	 * 
	 * @param fileName
	 *            文件名
	 * @param socket
	 *            客户端 socket.
	 */
	void fileService(String fileName, Socket socket) {

		try {
			PrintStream out = new PrintStream(socket.getOutputStream(), true);
			File fileToSend = new File(fileName);
			if (fileToSend.exists() && !fileToSend.isDirectory()) {
				out.println("HTTP/1.0 200 OK");// 返回应答消息,并结束应答
				out.println("Content-Type:application/binary");
				out.println("Content-Length:" + fileToSend.length());// 返回内容字节数
				out.println();// 根据 HTTP 协议, 空行将结束头信息

				FileInputStream fis = new FileInputStream(fileToSend);
				byte data[] = new byte[fis.available()];
				fis.read(data);
				out.write(data);
				out.close();
				fis.close();
			}
		} catch (Exception e) {
			System.out.println("传送文件时出错:" + e.getLocalizedMessage());
		}
	}

	/**
	 * 打印用途说明.
	 */
	private static void usage() {
		System.out
				.println("Usage: java HTTPServer <port>\nDefault port is 80.");
	}

	/**
	 * 启动简易 HTTP 服务器
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			if (args.length != 1) {
				usage();
			} else if (args.length == 1) {
				PORT = Integer.parseInt(args[0]);
			}
		} catch (Exception ex) {
			System.err
					.println("Invalid port arguments. It must be a integer that greater than 0");
		}

		new HttpSocketExample();
	}

}