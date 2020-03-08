/*
	Thread that manages the random Cargo creation.
	Manages the number of stations and creates at random times cargo for the cities.
*/

package com.train.platform.model;
import java.util.Random;
	
class CargoManager extends LoggerThread {
	public static Random random = new Random();

	public void run() {
		setName("CargoManager");
		log("started");
		try {
			while (true) {
				sleep(random.nextInt( Country.RandomCargoPeriodMs ));
				Cargo newCargo = Cargo.getRandomCargo();
				Country.getInstance().deliverCargoToSourceStation(newCargo);
				log("New cargo available: "+newCargo);
			}
		} catch (InterruptedException e) {
			log("Something wrong happened");
		}
	}
	
	
}