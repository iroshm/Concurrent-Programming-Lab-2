package busProblem;

import java.util.Random;



public class BusCreator implements Runnable {
	
    private BusHalt halt;
    private static Random rand;
    private float meanArrivalTime;

    public BusCreator(float meanArrivalTime, BusHalt halt) {
        this.meanArrivalTime = meanArrivalTime;
        this.halt = halt;
        rand = new Random();
    }

    @Override
    public void run() {

        int indexOfBus = 1;

        // Creating threads for busses
        while (!Thread.currentThread().isInterrupted()) {

            try {
                // Initializing the bus threads
                Bus bus = new Bus(halt.getRiderBoardingAreaEntranceSem(), halt.getBusDepartureSem(), halt.getMutex(), indexOfBus, halt);
                // Starting the bus threads
                (new Thread(bus)).start();

                indexOfBus++;
                // Sleeping the thread with arrival time between the busses
                Thread.sleep(getExpoDistBusInterArrivalTime());
                // Catch exceptions
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Finished arriving busses.");
    }

    // Exponentially distributed time
    public long getExpoDistBusInterArrivalTime() {
        float lambda = 1 / meanArrivalTime;
        return Math.round(-Math.log(1 - rand.nextFloat()) / lambda);
    }

}
