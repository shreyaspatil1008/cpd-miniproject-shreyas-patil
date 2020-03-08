/**
 * This "Hypothetical country" is the main class (and only singleton).
 * 
 *  This contains the main of the exercise and all the relevant variables.
 *  This models a hypothetical country where there are 8 stations and 4 trains.
 *  
 *  This should contain the state of the entire app.
 *  
 *
 */
package com.train.platform.model;
import java.util.Date;

public class Country {
	
	public static Country country;
	
	public final int totalStations;
	public final int totalTrains;
	public static final int RandomCargoPeriodMs = 5000 ;
	public static final int UnloadTimeMilliSecs   = 200 ;
	public static final int LoadTimeMilliSecs     = 100 ;
	public static final int CargoCapacity         = 10 ;
	
	final Station[] stations;
	final TrainTrack[] railwayTracks;
	final   Train[] trains;
	
	private Country(int totalStations, int totalTrains) {
		this.totalTrains = totalTrains;
		trains   = new Train[totalTrains];
		stations = new Station[totalStations];
		railwayTracks = new TrainTrack[totalStations];
		this.totalStations = totalStations;
		initializeTotalStations(totalStations);
		startAllTrains(totalTrains);
	}

	private void startAllTrains(int totalTrains) {
		for(int cordinalNumber = 0; cordinalNumber < this.totalTrains ; cordinalNumber++) {
 			trains[cordinalNumber] = new Train(
					cordinalNumber,
					2 + cordinalNumber,
					2 * cordinalNumber,
					Country.CargoCapacity
			);
 			trains[cordinalNumber].start();
		}
	}

	private void initializeTotalStations(int totalStations) {
		for(int i = 0; i < this.totalStations; i++) {
			stations[i] = new Station(i);
			railwayTracks[i] = new TrainTrack(i);
		}
	}

	public static Country getInstance() {
		if (country == null) {
			//The value of totalStations and totalTrains can be read from some properties file.
			//For this problem it is hardcoded while instantiating it
			country = new Country(8, 4);
		}
		return country;
	}
	

	/**
	  * @param verbose:
	 * 	true: multiline description
	 *  false:  single line description, like
	 * @return
	 */
	
	public String currentState(boolean verbose) {
		if (verbose) {
			return verboseLogs();
		} else {
			return nonVerboseLogs();
		}

	}

	private String nonVerboseLogs() {
		StringBuilder ret = new StringBuilder();
		for(int i=0;i<totalStations;i++) {
			ret.append('[');
			ret.append(stations[i].toStringMini());
			ret.append(']');
			ret.append("<");
			ret.append(railwayTracks[i].toStringMini());
			ret.append(">");
			ret.append("  ");
		}
		return ret.toString();
	}

	private String verboseLogs() {
		StringBuilder ret = new StringBuilder();
		ret.append("# Current Country Status (in YAML format)\n");
		ret.append("Date: "+ (new Date()) +"\n");
		ret.append("Country:\n");
		for(int i=0;i<totalStations;i++) {
			ret.append(" Station "+i+": ");
			ret.append(stations[i].toString());
			ret.append('\n');
			ret.append(" Railway "+i+": ");
			ret.append(railwayTracks[i].toString());
			ret.append('\n');
		}
		return ret.toString();
	}

	public void deliverCargoToSourceStation(Cargo cargo) {
		int sourceStation = cargo.getSourceStation();
		stations[sourceStation].addCargo(cargo);
	}
	
	public String toString() {
		return currentState(false);
	}
	
	public Station getStation(int which) {
		return stations[which];
	}
	public Train getTrain(int which) {
		return trains[which];
	}
	public TrainTrack getRailway(int railwayNumber) {
		return railwayTracks[railwayNumber];
	}
}
