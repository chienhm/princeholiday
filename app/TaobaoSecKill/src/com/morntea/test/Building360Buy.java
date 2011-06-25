package com.morntea.test;

import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.morntea.util.StringHelper;
import com.morntea.util.TimeHelper;
import com.morntea.util.WebHunter;

public class Building360Buy implements GrabFloor {
	private final static String FORUM_URL = "http://bbs.360buy.com/forum-forumdisplay-fid-52-page=1.html";
	private final static int CONCURRENT = 8;

	private String url;
	private int targetFloor;
	private int status;
	
	private Object readyLock = new Object();
	
	private static class LazyLoad {
		private final static Building360Buy instance = new Building360Buy(18911, 42850);
	}
	public static Building360Buy grabFloor() {
		return LazyLoad.instance;
	}
	
	private Building360Buy(int threadId, int targetFloor) {
		super();
		this.targetFloor = targetFloor;
		this.url = "http://bbs.360buy.com/thread-" + threadId + "-1-1.html";
	}
	
	private int getReplyNumber() {
		String html = WebHunter.getWebAsString(FORUM_URL+"?"+Math.random(), "utf-8");
		Pattern replyPattern = Pattern.compile("<td class=\"num\"><a href=\"" + url + "\" class=\"xi2\">\\d+</a><em>(\\d+)</em></td>");
		Matcher replyMatcher = replyPattern.matcher(html);
		int replyNumber = -1;
		if(replyMatcher.find()) {
			replyNumber = Integer.parseInt(replyMatcher.group(1));
		}
		return replyNumber;
	}
	
	public boolean isReady() {
		int threshold = targetFloor-CONCURRENT;
		TimeHelper th = new TimeHelper();
		while(true) {
			th.start();
			int totalFloor = getReplyNumber()+1;
			System.out.println(totalFloor + " [" + th.time() + "]");
			if(totalFloor==-1) {
				return false;
			} else if(totalFloor>=threshold && totalFloor<targetFloor) {
				System.out.println(threshold + " floor reached");
				return true;
			} else if (totalFloor>=targetFloor) {
				return false;
			}
		}
	}
	
	private void poll() {
		boolean ready = isReady();
		synchronized(readyLock) {
			status = ready ? 2 : 3;
			readyLock.notifyAll();
		}
	}
	
	public void dispatchAction(String action, PrintWriter out) {
		if(action==null || action.trim().equals("")) {
			out.print(StringHelper.readFromFile("javascript\\countdown.js"));
		} else if (action.equalsIgnoreCase("invoke")) {
			synchronized(readyLock) {
				switch(status) {
				case 0:
					status = 1;
					new Thread() {
						public void run() {
							System.out.println("start poll");
							poll();
						}
					}.start();
				case 1:
					try {
						System.out.println("start wait");
						readyLock.wait();
						out.print((status==2) ? "invoke();" : "");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
				case 2:
					System.out.println("invoke");
					out.print("invoke();");
					break;
				case 3:
					System.out.println("fail");
					out.print("");
					break;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		Building360Buy build = new Building360Buy(18911, 41749);
		build.isReady();
	}

}
