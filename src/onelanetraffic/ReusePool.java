package onelanetraffic;

import exceptionclasses.EmptyQueueException;
import queues.LinkedQueue;

public class ReusePool {
    LinkedQueue <Vehicle> reusePool; // a linked queue to store crashed vehicles that can be reused

    public ReusePool() {
        reusePool = new LinkedQueue<>();
    }

    public void recycleVehicle(Vehicle crashedVehicle) {
        reusePool.enqueue(crashedVehicle);
    }

    public Vehicle reuseVehicle() throws EmptyQueueException {
        if (reusePool.isEmpty()) {
            throw new EmptyQueueException("The pool is empty, there is no vehicle to reuse.");
        }
        return reusePool.dequeue();
    }

    public String toString() {
        return "\nVehicles in the repair shop:\n" + reusePool.toString();
    }

}

