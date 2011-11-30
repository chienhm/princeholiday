package com.morntea.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

	public static Date getDate(int year, int month, int day) {
		return getDate(year, month, day, 0, 0, 0);
	}

	public static Date getDate(int year, int month, int day, int hour,
			int minute, int second) {
		return getDate(year, month, day, hour, minute, second, 0);
	}
	
	public static Date getDate(int year, int month, int day, int hour,
			int minute, int second, int millis) {
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, day, hour, minute, second);
		c.set(Calendar.MILLISECOND, millis);
		return c.getTime();
	}
	
	public static boolean inTheFuture(Date date) {
		return new Date().compareTo(date)<0;
	}
	
	public static void waitUntil(Date date) {
		Date now = new Date();
		long millis = date.getTime() - now.getTime();
		if(millis>0) {
			System.out.println("[" + DateHelper.format(now) + "]...Waiting...=>[" + DateHelper.format(date) + "]");
			try {
				Thread.sleep(millis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String format(Date date) {
		DateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return sf.format(date);
	}
	
	public static String currentTime() {
		return format(new Date());
	}
}
