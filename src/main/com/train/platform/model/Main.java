/**
	 * This is the Main class
	 * 
	 * Deploys the 8 threads for trains, a Visualizer thread to print output
	 * and a CargoManager to 'produce' cargo objects.
	 * 
	 */
package com.train.platform.model;

public class Main {
	
	public static void main(String[] args){
		Country country = Country.getInstance();
		System.out.println("Start simulation of "+
				country.totalTrains +" trains and "+ country.totalStations +" stations");
		CargoManager cargoThread = new CargoManager();
		cargoThread.start();
		Thread visualizer = new Visualizer();
		visualizer.start();
		System.out.println("Main.class: exiting main thread"); 
	}
}