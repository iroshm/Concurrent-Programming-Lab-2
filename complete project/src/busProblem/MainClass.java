package busProblem;

import java.util.Scanner;


public class MainClass {
	
	 public static void main(String[] args) throws InterruptedException {
		 
		 	// Setting mean arrival time for riders and buses
	        float riderArrivalMeanTime = 30f * 1000;
	        float busArrivalMeanTime = 20 * 60f * 1000 ;
	        
	        // Get user input
	        Scanner scanner = new Scanner(System.in);
	        String userInput;
	        // Create waiting area object
	        BusHalt waitingArea = new BusHalt();

	        System.out.println("### PRESS ANY KEY TO STOP ###\n" );
	        // Create rider creator object
	        RiderCreator riderGenerator = new RiderCreator(riderArrivalMeanTime, waitingArea);
	        (new Thread(riderGenerator)).start();
	        // Create bus creator object
	        BusCreator busGenerator = new BusCreator(busArrivalMeanTime,waitingArea);
	        (new Thread(busGenerator)).start();

	        // Program Termination with a user input
	        while(true){
	            userInput = scanner.nextLine();
	            if(userInput != null)
	                System.exit(0);
	        }
	    }


}
