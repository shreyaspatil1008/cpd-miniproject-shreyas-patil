/*
	8 stations.
	Each station can have any number of trains inside and any number of cargos to be delivered to
	some other station.
	
	Index is N      - 0..7
	Position is 2N  - 0..14
*/
package com.train.platform.model;
import java.util.Arrays;
import java.util.ArrayList;

public class Station extends PlaceOfTrain {
	private ArrayList<Train> trainsOnThisStation;
	private ArrayList<Cargo> cargo;
	private              int index;
	protected         Object depotLock  = new Object() ;
	
	public Station(int label) {
		index = label;
		trainsOnThisStation = new ArrayList<>();
		cargo = new ArrayList<>();
	}
	
	public synchronized void addTrain(Train newTrain) {
		synchronized(trainsOnThisStation) {
			if (trainsOnThisStation.indexOf(newTrain) != -1) {
				System.err.println("This train is already present in this station: " + newTrain);
			} else {
				trainsOnThisStation.add(newTrain);
			}
		}
	}
	
	public synchronized void removeTrain(Train train) {
		synchronized(trainsOnThisStation) {
			if (trainsOnThisStation == null) {
				System.err.println("this train wasn't in this station before!");
			}
			trainsOnThisStation.remove(train);
		}
	}
	
	public boolean isEmpty() {
		return trainsOnThisStation.isEmpty();
	}
	public String toString(boolean verbose) {
		if (verbose) 
			return "S"+index+"(C:"+ cargo.size() +", Ts:"+ trainsOnThisStation +")";
		else {
			int occurrences[]; 
			occurrences = new int[ trainsOnThisStation.size() ];
			for (int i = 0; i< trainsOnThisStation.size(); i++) {
				occurrences[i] = trainsOnThisStation.get(i).trainNumber;
			}
			return "S"+index+"(C:"+ cargo.size() +", Ts:"+Arrays.toString(occurrences)+")";
		}
	}
	public String toString() {
		return toString(true); 
	}
	public String toStringMini() {
		return toString(false);
	}
	public int getPosition() {
		return 2 * index;
	}
	
	public synchronized void addCargo(Cargo c) {
		cargo.add(c) ;
	}

	public synchronized ArrayList<Cargo> getCargo() {
		return cargo;
	}

	public synchronized void removeCargo(int cargoIndex) {
		this.cargo.remove(cargoIndex);
	}

	public int getIndex() {
		return index;
	}

	/**
	 * This methods basically does a 'pop' from the array. For the
	 * exercise, it's not important which cargo is taken, so for simplicity
	 * we'll get the first one.
	 * 
	 * The important thing is that this code is synchronized so any
	 * access to the array should be locked to one thread at a time.
	 * 
	 * Actually 'remove' does exactly what we want
	 * 
	 * @return returns the 'popped' cargo.
	 * 
	 */
	public synchronized Cargo removeAndGetCargo() {
		return cargo.remove(0);
	}

}