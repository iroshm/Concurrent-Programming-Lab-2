package busProblem;

import java.util.Scanner;


public class Main {
	
	 public static void main(String[] args) throws InterruptedException {
		 
		 	// Setting mean arrival time for riders and buses
	        float riderArrivalMeanTime = 30f * 1000;
	        float busArrivalMeanTime = 20 * 60f * 1000 ;
	        
	        // Create waiting area object
	        BusHalt waitingArea = new BusHalt();

	        
	        // Create rider creator object
	        RiderCreator riderGenerator = new RiderCreator(riderArrivalMeanTime, waitingArea);
	        (new Thread(riderGenerator)).start();
	        // Create bus creator object
	        BusCreator busGenerator = new BusCreator(busArrivalMeanTime,waitingArea);
	        (new Thread(busGenerator)).start();

	       
	    }


}
