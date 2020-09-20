package busProblem;

import java.util.concurrent.Semaphore;



public class Rider implements Runnable{

    private final Semaphore semaphore_for_bus_depature;
    private final Semaphore mutex;
    private final int id;
    private BusHalt halt;
    private final Semaphore semaphore_for_handle_waiting_area_riders;
    private final Semaphore semaphore_for_rider_boarding;

    public Rider(Semaphore riderWaitingAreaEntranceSem, Semaphore rider_bording, Semaphore bus_depature, Semaphore mutex, int index, BusHalt bushalt) {
        this.semaphore_for_handle_waiting_area_riders = riderWaitingAreaEntranceSem;
        this.semaphore_for_rider_boarding = rider_bording;
        this.semaphore_for_bus_depature = bus_depature;
        this.id = index;
        this.mutex = mutex;
        this.halt = bushalt;
    }

    @Override
    public void run() {

        try {
        	// only 50 people can get into the bus halt. using this semaphore after each thread decrement the semaphore count by 1(initially set to 50)
        	semaphore_for_handle_waiting_area_riders.acquire();

            	//people count in bus halt is a shared variable , So before updating it each rider should lock it
        		//After bus arriving riders will wait here. They can't access waiting_rider_count
                mutex.acquire();
                	System.out.println("---Rider " + id + " entered to the boarding area---");
                	halt.incrementRidersCount();
                mutex.release();

                // After arriving to the bus halt rider wait for a bus
                // Because each thread trying to aquire the lock which semaphore count is 0
                //then each rider will block here until bus thread release the lock
                semaphore_for_rider_boarding.acquire();
                System.out.println("Rider " + id + " boarded in to the bus");

            // Releasing the semaphore for enter to bus halt
                semaphore_for_handle_waiting_area_riders.release();

            
                halt.decrementRidersCount();

          //indicate to bus thread that riders successfully got onboard
            if (halt.getRidersCount() == 0)
            	semaphore_for_bus_depature.release();
            
            else
            	semaphore_for_rider_boarding.release(); // if there are more riders existing allow them to get in

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
