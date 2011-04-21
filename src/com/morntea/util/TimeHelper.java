package com.morntea.util;

public class TimeHelper {
	private long s = 0, e = 0;
	public void start() {
		s = System.currentTimeMillis();
	}
	public long time() {
		if(s!=0)e = System.currentTimeMillis();
		long elapse = e-s;
		s = 0;e=0;
		return elapse;
	}
}
