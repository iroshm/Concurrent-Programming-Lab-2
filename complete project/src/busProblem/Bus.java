package busProblem;

import java.util.concurrent.Semaphore;



public class Bus implements Runnable{
	
	private final Semaphore mutex;
	
    private final Semaphore semaphore_for_bus_depature;
    
    private final Semaphore semaphore_for_rider_boarding;
    
    private final int id;
    
    private BusHalt halt;


    public Bus(Semaphore rider_bording, Semaphore bus_depature, Semaphore mutex, int index, BusHalt bushalt) {
    	this.id = index;
        this.mutex = mutex;
        this.halt = bushalt;
        this.semaphore_for_rider_boarding = rider_bording;
        this.semaphore_for_bus_depature = bus_depature;
    }

    @Override
    public void run() {

        try {
        	//When bus arrives to the halt shared variable will lock by the bus thread
            mutex.acquire();
                
	            System.out.println("Bus " + id + " arrived to the bus-halt.");
	            System.out.println("There are " + halt.getNumberOfRiders() + " riders waiting currently.");

                // check the waiting rider count larger than 0 or not
                if (halt.getNumberOfRiders() > 0) {

                	// Bus thread turn semaphore count to 1 and give chance one thread to come in to the bus 
                	semaphore_for_rider_boarding.release();
                    //Bus thread wait here until riders get in to the bus. After get in bus thread lock the busDepartureSem semaphore by turning it to 0
                    semaphore_for_bus_depature.acquire();
                }
                
            //bus thread unlock the mutex of shared variable
            mutex.release();

           
            System.out.println("Bus " + id + " departed from the bus-halt.");
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
