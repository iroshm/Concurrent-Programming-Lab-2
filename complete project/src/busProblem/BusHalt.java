package busProblem;

import java.util.concurrent.Semaphore;

public class BusHalt {
	
	private static int riderCount = 0;
    private static int maxBusCapacity = 50;

    // Semaphore used for riders to enter the waiting area, allowing 50 riders to remain at the bus-halt
    private static final Semaphore busHaltEntranceSem = new Semaphore(maxBusCapacity);

    // Semaphore used for riders to enter the boarding area
    private static final Semaphore riderBoardingAreaEntranceSem = new Semaphore(0);

    // Semaphore used for bus to depart after the riders are boarded
    private static final Semaphore busDepartureSem = new Semaphore(0);

    // Semaphore used to handle the access to the riders count variable
    private static final Semaphore mutex = new Semaphore(1);

    //Method to get the riders count
    public int getRidersCount() {
        return riderCount;
    }

    //Method to increment the riders count
    public void incrementRidersCount() {
        riderCount++;
    }

    //Method to decrement the riders count
    public void decrementRidersCount() {
        riderCount--;
    }

    //Method to access the semaphore that is used for riders to enter the waiting area
    public static Semaphore getBusHaltEntranceSem() {
        return busHaltEntranceSem;
    }

    //Method to access the semaphore that is used for riders to board the bus
    public static Semaphore getRiderBoardingAreaEntranceSem() {
        return riderBoardingAreaEntranceSem;
    }

    //Method to access the semaphore that is used for the bus to depart
    public static Semaphore getBusDepartureSem() {
        return busDepartureSem;
    }

    //Method to access the semaphore that is used for handle access to the riders count
    public static Semaphore getMutex() {
        return mutex;
    }

}
