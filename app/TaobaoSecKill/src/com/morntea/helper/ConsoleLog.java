package com.morntea.helper;

import java.util.HashMap;

public class ConsoleLog {
	public static void log(Object log) {
		if(log instanceof HashMap) {
			System.out.println("[" + DateHelper.currentTime() + "] " + log.getClass().getName());
			@SuppressWarnings("rawtypes")
			HashMap m = (HashMap)log;
			for(Object key : m.keySet()) {
				System.out.println(key + "=" + m.get(key));
			}
		} else {
			System.out.println("[" + DateHelper.currentTime() + "] " + log);
		}
	}
}
