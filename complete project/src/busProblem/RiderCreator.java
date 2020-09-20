package busProblem;

import java.util.Random;


public class RiderCreator implements Runnable{

	    private BusHalt halt;
	    private static Random rand;
	    private float meanArrivalTime;

	    public RiderCreator(float meanArrivalTime, BusHalt halt) {
	        this.meanArrivalTime = meanArrivalTime;
	        this.halt = halt;
	        rand = new Random();
	    }

	    @Override
	    public void run() {

	        int indexOfRider = 1;
	        
	        // Creating rider threads 
	        while (!Thread.currentThread().isInterrupted()) {

	            try {
	                // Initializing the rider threads
	                Rider rider = new Rider(halt.getBusHaltEntranceSem(), halt.getRiderBoardingAreaEntranceSem(), halt.getBusDepartureSem(), halt.getMutex(), indexOfRider, halt);
	                // Starting the rider threads
	                (new Thread(rider)).start();

	                indexOfRider++;
	                // Sleeping the thread with arrival time between riders
	                Thread.sleep(getExpoDistRiderInterArrivalTime());
	                // Catch exception
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    // Exponentially distributed time
	    public long getExpoDistRiderInterArrivalTime() {
	        float lambda = 1 / meanArrivalTime;
	        return Math.round(-Math.log(1 - rand.nextFloat()) / lambda);
	    }


}
