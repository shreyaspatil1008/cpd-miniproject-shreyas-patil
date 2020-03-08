/*
	Model class for cargo entities
	
	A cargo is delivered (randomly) to a station and has to be delivered to another station.	
	A station holds a Cargo (thus a cargo doesn't need to know where it is, just where its going)
*/
package com.train.platform.model;
import java.util.Random;

public class Cargo {
	private static Random random = new Random();
	
	int sourceStation;
	int destinationStation;

	public Cargo(int sourceStation, int destinationStation) {
		this.sourceStation = sourceStation;
		this.destinationStation = destinationStation;
	}
	
	public String toString() {
		return "Cargo("+ sourceStation +"->"+ destinationStation +")";
	}
	
	public static Cargo getRandomCargo() {
		int srcStation = random.nextInt( Country.getInstance().totalStations );
		int dstStation = random.nextInt( Country.getInstance().totalStations -1);
		if (dstStation >= srcStation)
			dstStation++;
		return new Cargo(srcStation,dstStation);
	}

	public int getSourceStation() {
		return sourceStation;
	}

	public int getDestinationStation() {
		return destinationStation;
	}

}