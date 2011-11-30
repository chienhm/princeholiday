package com.morntea.helper;

import java.io.IOException;

public class Windows {
	public static void cmd(String cmd) {
		Runtime run = Runtime.getRuntime();
		try {
			run.exec("cmd.exe /c " + cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void iexplore(String url) {
		cmd("start iexplore " + url);
	}
}
