/**
 * This logger class logs the state of the system, every N seconds
 * 
 * It also logs a status file.
 * 
 * */
package com.train.platform.model;

import java.io.*;

public class Visualizer extends LoggerThread {
	public final static     int SLEEP_TIME_SECS = 1;
	public final static boolean LOG_ONLY_ON_CHANGES = false ;
	public final static String STATUS_FILENAME = "status_log.yml" ;
	int timer = 0;
	
	public void run() {
		setName("Visualizer");
		log("Thread started");
		try {
			while(true) {
				logChangedStatus();
				writeStatusToLogFile();
			}
		} catch (InterruptedException e) {
			log("Interrupted.");
		}
	}

	private void logChangedStatus() {
		String currentStatus = Country.getInstance().currentState(false);
		String previousStatus = currentStatus;
		if (LOG_ONLY_ON_CHANGES) {
			if (currentStatus != previousStatus) {
				log("Status changed: " + currentStatus);
			}
		} else {
			log("Status: " + currentStatus);
		}
	}

	private void writeStatusToLogFile() throws InterruptedException {
		try {
			String currentStatusLong = Country.getInstance().currentState(true);
			File f = new File(STATUS_FILENAME);
			FileWriter fstream = new FileWriter(f);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(currentStatusLong);
			out.close();
		} catch (Exception e) {
			log("Some error writing..");
		}
		sleep(SLEEP_TIME_SECS * 1000);
		timer++;
	}
}