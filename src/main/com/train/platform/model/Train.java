/**
 * 
 * A train is a Thread which goes to stations.
 * 
 * 
 * Every train has a:
 *	- speed (modeled through slowness)
 *	- position (might be in a station[0..7], might be a track[0..7]).

 * The status of the train is (provided the system has NumberOFStations):
 * - arriving at a station
 * - loading
 * - unloading, etc
 * - departing
 * 
 * an interesting refactor could be to use a Position object and so forget about integers (it's 
 * quite easy to confuse positions (16) and railway/station indexes (8). Ive tried to be as
 * strict as possible with names in order not to introduce strange bugs..
 * 
*/
package com.train.platform.model;

import com.train.platform.enums.TrainStatus;
import com.train.platform.processor.Processor;

import java.util.ArrayList;
import java.lang.Math;
import java.util.concurrent.ConcurrentHashMap;


public class Train extends LoggerThread {
    int slowness ;
    int position ;
	ArrayList<Cargo> cargo;
    int trainNumber;
    final int cargoCapacity;
    TrainStatus trainStatus;
    private ConcurrentHashMap<TrainStatus, Processor> processors;

    public Train(int trainNumber, int railwayPeriod, int initialPosition, int cargoCapacity) {
        this.slowness      = railwayPeriod;
        this.position      = 2*initialPosition;
        this.cargo = new ArrayList<>();
        this.trainNumber = trainNumber;
        this.cargoCapacity = cargoCapacity;
        this.trainStatus = TrainStatus.STATION_START;
		initiateProcessors();
	}

	private void initiateProcessors() {
		processors = new ConcurrentHashMap<>();
		processors.put(TrainStatus.STATION_START,(() -> registerToStation(position/2)));
		processors.put(TrainStatus.STATION_UNLOAD_CARGO, (() -> unloadCargo()));
		processors.put(TrainStatus.STATION_LOAD_CARGO, (() -> loadCargo()));
		processors.put(TrainStatus.STATION_END,
			() -> {
				incrementPosition();
				registerToRailway(position/2);
				incrementPosition();
				notifyAll();
			}
		);
	}

	public void run() {
		setName("Train."+ trainNumber);
    	log("started: "+ toString() );
    	
    	myStation().addTrain(this);
    	while (true) {
    		setStatus(TrainStatus.STATION_START);
    		setStatus(TrainStatus.STATION_UNLOAD_CARGO);
    		setStatus(TrainStatus.STATION_LOAD_CARGO);
    		setStatus(TrainStatus.STATION_END);
    	}
    }
    
    public synchronized void  registerToStation(int stationNumber) {
    	Station myStation = Country.getInstance().getStation(stationNumber);
    	log("Train entering station: "+ myStation.toStringMini() );
    	myStation.addTrain(this);
    }
    
    public synchronized void registerToRailway(int railwayNumber) {
    	TrainTrack myRailwayTrack = Country.getInstance().getRailway(railwayNumber);
    	Station myStation = Country.getInstance().getStation(railwayNumber);
    	myStation.removeTrain(this);
		waitUntilTrackIsBusy(myRailwayTrack);
		myRailwayTrack.addTrain(this);
    	logTrainStatus(myRailwayTrack);
    	myRailwayTrack.removeTrain(this);
    }

	private void waitUntilTrackIsBusy(TrainTrack myRailwayTrack) {
		while(myRailwayTrack.isBusy()) {
			try {
				wait();
			} catch (InterruptedException e) {
				log("DEBUG seems like I've been waken up!");
			}
		}
	}

	private void logTrainStatus(TrainTrack myRailwayTrack) {
		try {
			log("Train entering railway track: " + myRailwayTrack.toStringMini() );
			Thread.sleep(slowness * 1000);
		} catch (InterruptedException e) {
			log("TrainThread interrupted when on Railway: "+ myRailwayTrack);
		}
	}

	public synchronized void setStatus(TrainStatus trainStatus) {
    	this.trainStatus = trainStatus;
		processors.get(trainStatus).process();
    }

    private void incrementPosition() {
		position = Position.nextPos(position);
	}

	void unloadCargo() {
		synchronized(cargo) {
	    	debugLog("2. UnLoadCargo for "+this+" at position: " + position);
	    	for(int k = 0; k < cargo.size(); k++) {
				unloadCargoOnDestinationStation(k);
				waitUntilCargoIsLoadingOrUnloading(Country.UnloadTimeMilliSecs, "Interrupted while unloading some cargo..");
			}
		}
	}

	private void waitUntilCargoIsLoadingOrUnloading(int unloadTimeMilliSecs, String s) {
		try {
			Thread.sleep(unloadTimeMilliSecs);
		} catch (Exception e) {
			log(s);
		}
	}

	private void unloadCargoOnDestinationStation(int k) {
		Cargo c = cargo.get(k);
		if (c.destinationStation == myStation().getIndex() ) {
			Cargo tmpCargo = cargo.remove(k);
			verboseLog("Cargo correctly unloaded in station "+myStation().toStringMini()+": " + tmpCargo );
		}
	}

	private Station myStation() {
		return (Station) PlaceOfTrain.getCountryPlace(position);
	}

	public synchronized void loadCargo() {
		Station myStation = myStation();
		synchronized(myStation.depotLock) {
			int numberOfCargoToGet = Math.min(cargoCapacity - cargo.size(), myStation.getCargo().size());
			for (int i=0 ; i < numberOfCargoToGet ; i++) {
				cargo.add(myStation.removeAndGetCargo());
			}
			waitUntilCargoIsLoadingOrUnloading(Country.LoadTimeMilliSecs, "Interrupted while loading some cargo..");
		}
	}

    public String toStringVerbose() {
        return "Train."+ trainNumber
        		+ "("
        		+ "pos => " + position
        		+ ",cs => "  + cargo.size()
        		+ ")";
    }

	@Override
	public String toString() {
		return toStringMini();
	}
	public String toStringMini() {
		return "Tr"+ trainNumber +"(P"+position+",C"+ cargo.size()+")";
	}
}