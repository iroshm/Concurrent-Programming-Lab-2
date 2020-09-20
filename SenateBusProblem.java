package senate_bus_problem;

import java.util.logging.Level;
import java.util.logging.Logger;


import java.util.Random;
import java.util.concurrent.Semaphore;

public class SenateBusProblem {
	
	//This semaphore,indiacte a single user boarded in to the bus(which is currently on the bus stop).
    static Semaphore rider_board_to_bus = new Semaphore(0);
	 
	//Using this semaphore, riders can identify arrival of a new bus
    static Semaphore bus_indicator = new Semaphore(0);
    
    //Mutex will use to protect waiting_rider_count shared vriable  and initially s=1            
    static Semaphore mutex = new Semaphore(1); 
    
    // waiting_rider_count contains number of riders in the waiting place
 	static int waiting_rider_count = 0;   
    
    // Bus class implements by runnable interface (instances are intended to be executed by a thread)
    class Bus implements Runnable {
    	
    	// bus id
        private int bid;
        
        //this variable contains number of riders who are get in to a newly arrival bus.
        //private int rider_count = 0;

        //To contain total number of riders who are waiting for a new bus thread
        private int riders_selected_to_board; 
        
        // each bus will have different bus index
        Bus(int index) {
            this.bid = index;
        }
        
        @Override
        public void run() {
            try {
            	
            	//When bus arrives to the halt shared variable waiting_rider_count will lock by the bus thread
                mutex.acquire(); 
                
                System.out.println("Bus " + bid + " locked the bus stop !!");//The number of riders available for the bus is 50 out of all the passengers in the boarding area. If the waiting passengers is less than 50, then all can get in.
                
                if(waiting_rider_count < 50) {
                	riders_selected_to_board = waiting_rider_count;
                } else {
                	riders_selected_to_board = 50;
                }
                //riders_selected_to_board = Math.min(waiting_rider_count, 50);   
                
                System.out.println("waiting  : " + waiting_rider_count + " To board : "+riders_selected_to_board);   
                
                //Get all riders who are waiting in the bus halt in to the bus
                for (int rider = 0; rider < riders_selected_to_board; rider++) { 
                	
                    System.out.println("Bus " + bid + " released for "+rider+"th rider");
                    
                    // Bus thread turn semaphore count to 1 and give chance one thread to come in to the bus 
                    bus_indicator.release(); 
                    
                    //Bus thread wait here until rider get in to the bus. After get in bus thread lock the rider_board_to_bus semaphore by turning s to 0
                    rider_board_to_bus.acquire();       
                    
                    System.out.println("Bus " + bid + " acquired boarded !!!!! !!");
                    
                }
                
                //after taking riders into the bus update the waiting_rider_count shared variable
                
                if((waiting_rider_count - 50) > 0) {
                	waiting_rider_count = (waiting_rider_count - 50);
                }else {
                	waiting_rider_count = 0;
                }
                //waiting_rider_count = Math.max((waiting_rider_count - 50), 0);
                
              //bus thread unlock the mutex of shared variable waiting_rider_count
                mutex.release();                           
                
            } catch (InterruptedException ex) {              
            	System.out.println("Error inside Bus!!!");
            }
            
            System.out.println("Bus " + bid + " departed with " + riders_selected_to_board + " riders on board!");                                    
        }
    }
    
    
    
    // Rider class implements by runnable interface (instances are intended to be executed by a thread)
    class Rider implements Runnable {

        private int rid;

        // each rider will have different bus index
        Rider(int index) {
            this.rid = index;
        }

        @Override
        public void run() {
            try {
                
                //waiting_rider_count is a shared variable , So before updating it each rider should lock it
            	//After bus arriving riders will wait here. They can't access waiting_rider_count
                mutex.acquire();                
                System.out.println("Rider " + rid + " is waiting !!");
                waiting_rider_count += 1;                   
                mutex.release();                

                // After arriving to the bus halt rider wait for a bus
                // Because each thread trying to aquire the lock which semaphore count is 0
                //then each rider will block here until bus thread release the lock
                bus_indicator.acquire();   
                
                //after bus thread release bus_indicator one thread get chance to the bus.
                System.out.println("Rider " + rid + "got onboard.");      
                
                //indicate to bus thread that rider successfully got onboard
                rider_board_to_bus.release();              

            } catch (InterruptedException ex) { //Exception if the above procedure got interupted in the middle
                //error

            }
        }
    }
    
    
    public static void main(String[] args) {
    	System.out.println("Main thread Started!!!");
    	
    	SenateBusProblem lab = new SenateBusProblem();  
    	
    	int bid=0;
    	int rid=0;
    	
        long time_difference_of_bus=0;
        long time_difference_of_rider=0;
        long current_time=0;
        long previous_bus=System.currentTimeMillis();
        long previous_rider=System.currentTimeMillis();
        
        //mean of the exponential distributions of inter-arrival times of riders and buses
        double riderMean=30000;
        double busMean=1200000;          
        double rbus  = 0.0;
        double rrider=0;
        double rider_wait=0;
        double bus_wait=0;
        
        rbus = new Random().nextDouble(); 
        
        //To calculate rider waiting time
        rider_wait = Math.round(Math.log10(rbus)*-1*riderMean);
        
        rrider = new Random().nextDouble();   
        
      //To calculate bus waiting time
        bus_wait = Math.round(Math.log10(rrider)*-1*busMean); 
        
        
        while(Boolean.TRUE) {     
           
        	current_time=System.currentTimeMillis();
        	////time gap between current and previous rider
        	time_difference_of_rider=current_time-previous_rider;                      
           //time gap between current and previous bus
           time_difference_of_bus=current_time-previous_bus;                         
           
         //if both times are eqaul let new rider to in
           if(time_difference_of_rider==rider_wait){                           
              
               Rider new_rider = lab.new Rider(rid++);
               new Thread(new_rider).start();
               previous_rider=current_time;
                  
               rrider  = new Random().nextDouble();   
               rider_wait = Math.round(Math.log10(rrider)*-1*riderMean); 
           }
           
         //if both times are eqaul let new bus to in
           if(time_difference_of_bus==bus_wait){                           
               Bus new_bus = lab.new Bus(bid++);
               new Thread(new_bus).start();
               previous_bus=current_time;
              
               rbus  = new Random().nextDouble();
               bus_wait = Math.round(Math.log10(rbus)*-1*busMean);
           }
       }
       
    }


}
