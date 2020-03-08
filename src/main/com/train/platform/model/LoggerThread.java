package com.train.platform.model;

public abstract class LoggerThread extends Thread {
	public static boolean Debug   = false;
	public static boolean Verbose = true;
	
	public void debugLog(String message) {
		if (Debug) {
			print("DEBUG " + message);
		}
	}
	public void verboseLog(String message) {
		if (Verbose) {
			print(message);
		}
	}
	public void  log(String message) {		//logs a generic message
		print(message);
	}
	
	private void print(String message) {
		System.out.println("Th{"+getName()+"} " + message);
	}
}
