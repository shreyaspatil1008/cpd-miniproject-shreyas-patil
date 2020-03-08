/**
 * This is a particular APlace where ONLY ONE TRAIN can exist at a given time.
 * 
 * In the problem this is called 'Track'.
 *
 */
package com.train.platform.model;

public class TrainTrack extends PlaceOfTrain {
	Train trainHere;
	int position;

	public TrainTrack(int pos) {
		position = pos;
		trainHere = null ;
	}
	
	public synchronized void addTrain(Train newTrain) {
		validateNewTrain(newTrain, trainHere == newTrain, "Fatal BUG! I'm trying to register for a Railway I already belong:\n", " ME:     ", " MYSELF: ", 67);
		validateNewTrain(newTrain, trainHere != null, "Kaboom! These two train collided:\n", " NEW:  ", " HERE: ", 66);
		trainHere = newTrain;
	}

	private void validateNewTrain(Train newTrain, boolean b, String s, String s2, String s3, int i) {
		if (b) {
			System.err.println(s
					+ s2 + newTrain + '\n'
					+ s3 + trainHere + '\n'
			);
			System.exit(i);
		}
	}

	public synchronized void removeTrain(Train oldTrain) {
		validateNewTrain(oldTrain, trainHere != oldTrain, "Trying to remove a train which is not there:\n", " - THIS: ", " - HERE: ", 99);
		trainHere = null;
	}
	
	public boolean isEmpty() {
		return trainHere == null;
	}

	public String toString() {
		return "R"+position+"("+ (trainHere == null ? '-' : trainHere)+")";
	}
	public String toStringMini() {
		return "R"+position + (trainHere == null
					? "" 
					: "{" + trainHere.toStringMini() + "}"
				);
	}
	public int getPosition() {
		return 2 * position + 1;
	}

}
