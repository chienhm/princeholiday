package com.morntea.test;

public class Test {

	Object lock = new Object();

	public void sync2() {
		synchronized(lock){
			System.out.println("sync2");
			lock.notify();
		}
	}
	
	public void sync() {
		synchronized(lock){
			try {
				sync2();
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		new Test().sync();
	}

}
