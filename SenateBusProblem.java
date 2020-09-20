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
    	
    	int bus_id=0, rider_id=0;                           //Ids are assigned for buses and riders for the easy demonstration of the program
        long diff_bus=0,diff_rider=0,time_curr=0,time_prev_bus=System.currentTimeMillis(),time_prev_rider=System.currentTimeMillis();
        double mean_rider=30000,mean_bus=1200000;           //Declaring the mean of the exponential distributions of inter-arrival times of riders and buses
        double rand_bus  = 0.0,rand_rider=0,wait_time_rider=0,wait_time_bus=0;
        
        rand_bus = new Random().nextDouble();   
        wait_time_rider = Math.round(Math.log10(rand_bus)*-1*mean_rider);  //Calculating the time before the next bus arrives
        
        rand_rider = new Random().nextDouble();   
        wait_time_bus = Math.round(Math.log10(rand_rider)*-1*mean_bus);     //Calculating the time before the next bus arrives
        //System.out.println("rand - "+Math.round(rand*100.0)/100.0+ " wait_time_rider -  "+wait_time_rider+" wait_time_bus - "+wait_time_bus);
        
        while(Boolean.TRUE) {     
           
           time_curr=System.currentTimeMillis();
           diff_rider=time_curr-time_prev_rider;                      //Calculating the time passed after the previous rider
           diff_bus=time_curr-time_prev_bus;                          //Calculating the time passed after the previous bus
           
           
           if(diff_rider==wait_time_rider){                           //Checking if it is the time for the next rider to come
               //System.out.println("rand - "+Math.round(rand*100.0)/100.0+ " wait_time_rider -  "+wait_time_rider);
               Rider new_rider = lab.new Rider(rider_id++);
               new Thread(new_rider).start();
               time_prev_rider=time_curr;
                  
               rand_rider  = new Random().nextDouble();   
               wait_time_rider = Math.round(Math.log10(rand_rider)*-1*mean_rider); //Calculating the inter arrival time before the next rider arrives
           }
           if(diff_bus==wait_time_bus){                           //Checking if it is the time for the next bus to come
               //System.out.println("rand - "+Math.round(rand*100.0)/100.0+ " wait_time_bus - "+wait_time_bus);
               Bus new_bus = lab.new Bus(bus_id++);
               new Thread(new_bus).start();
               time_prev_bus=time_curr;
              
               rand_bus  = new Random().nextDouble();
               wait_time_bus = Math.round(Math.log10(rand_bus)*-1*mean_bus);//Calculating the inter arrival time before the next bus arrives
           }
       }
       
    }


}
