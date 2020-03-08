/**
 *
 * models a position, a circular number from 0 to 2N-1, where N =  Country.NumberOfStations
 * Every position corresponds to a place:
 */

package com.train.platform.model;

public class Position {
	final int absolutePosition ;

	Position(int pos) {
		absolutePosition = pos;
	}
	
	public static int nextPos(int pos) {
		int next = pos+1;
		if (next == Country.getInstance().totalStations * 2)
			next = 0;
		return next;
	}

	public static int prevPos(int pos) {
		int next = pos-1;
		if (next == -1)
			next = Country.getInstance().totalStations * 2 - 1;
		return next;
	}
}
