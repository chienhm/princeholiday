package com.morntea.test;

import java.io.PrintWriter;

public interface GrabFloor {
	public boolean isReady();
	public void dispatchAction(String action, PrintWriter out);
}
