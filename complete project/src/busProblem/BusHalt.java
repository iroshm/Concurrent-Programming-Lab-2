package busProblem;

import java.util.concurrent.Semaphore;

public class BusHalt {
	
    private static int maxBusCapacity = 50;
    private static int riderCount = 0;

    // Semaphore for handle the access to the riders count variable
    private static final Semaphore mutex = new Semaphore(1);
    
    // Semaphore used for riders to enter the waiting area, allowing 50 riders to remain at the bus-halt
    private static final Semaphore busHaltEntranceSem = new Semaphore(maxBusCapacity);

    // Semaphore used for riders to enter the boarding area
    private static final Semaphore riderBoardingAreaEntranceSem = new Semaphore(0);

    // Semaphore used for bus to depart after the riders are boarded
    private static final Semaphore busDepartureSem = new Semaphore(0);

    // Get the semaphore for handle access to the riders count
    public static Semaphore getMutex() {
        return mutex;
    }
    
    // Get the semaphore for riders to enter the waiting area
    public static Semaphore getBusHaltEntranceSem() {
        return busHaltEntranceSem;
    }
    
    // Get the semaphore for riders to board the bus
    public static Semaphore getRiderBoardingAreaEntranceSem() {
        return riderBoardingAreaEntranceSem;
    }
    
    // Get the semaphore for the bus to depart
    public static Semaphore getBusDepartureSem() {
        return busDepartureSem;
    }
    
    // Get the riders count
    public int getNumberOfRiders() {
        return riderCount;
    }

    // Increment the riders count
    public void incrementRiders() {
        riderCount++;
    }

    // Decrement the riders count
    public void decrementRidersCount() {
        riderCount--;
    }

}
