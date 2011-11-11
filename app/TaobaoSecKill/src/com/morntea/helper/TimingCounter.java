package com.morntea.helper;

import java.util.Date;

public class TimingCounter {
	private Date start;
	private Date end;
	
	public TimingCounter() {
		this.start = new Date();
	}
	
	public void set() {
		this.start = new Date();
	}
	
	public long log() {
		this.end = new Date();
		long mills = end.getTime() - start.getTime();
		this.set();
		return mills;
	}
}
