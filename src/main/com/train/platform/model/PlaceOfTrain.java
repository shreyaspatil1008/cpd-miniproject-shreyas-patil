/**
 * A APlace is an "interface" common for both RailwayTracks and Stations.
 */

package com.train.platform.model;

public abstract class PlaceOfTrain {
	
	public abstract void addTrain(Train t);
	public abstract void removeTrain(Train t);
	public abstract boolean isEmpty();
	public abstract int getPosition();
	
	public boolean isBusy () {
		return ! isEmpty() ;
	}
	
	public static PlaceOfTrain getCountryPlace(int position) {
		if (position % 2 == 0) {
			return Country.getInstance().getStation(position/2);
		} else {
			return Country.getInstance().getRailway(position/2);
		}
	}
}
