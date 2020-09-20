package busProblem;

import java.util.Scanner;


public class MainClass {
	
	 public static void main(String[] args) throws InterruptedException {

	        float riderArrivalMeanTime = 30f * 1000;
	        float busArrivalMeanTime = 20 * 60f * 1000 ;
	        
	        Scanner scanner = new Scanner(System.in);
	        String userInput;
	        BusHalt waitingArea = new BusHalt();

	        System.out.println("\n*******  Press any key to exit.  *******\n" );

	        RiderCreator riderGenerator = new RiderCreator(riderArrivalMeanTime, waitingArea);
	        (new Thread(riderGenerator)).start();

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
