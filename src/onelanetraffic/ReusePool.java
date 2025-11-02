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

    public ReusePool copy() {
        ReusePool newPool = new ReusePool();
        LinkedQueue<Vehicle> newQueue = new LinkedQueue<>();
        LinkedQueue<Vehicle> temp = new LinkedQueue<>();

        try {
            while (!reusePool.isEmpty()) {
                Vehicle v = reusePool.dequeue();
                newQueue.enqueue(v);  // same vehicle (immutable)
                temp.enqueue(v);      // save to restore original
            }

            // restore the original pool
            while (!temp.isEmpty()) {
                reusePool.enqueue(temp.dequeue());
            }
        } catch (EmptyQueueException ex) {
            System.out.println(ex.getMessage());
        }

        newPool.reusePool = newQueue;
        return newPool;
    }

    @Override
    public String toString() {
        return "\nVehicles in the repair shop:\n" + reusePool.toString();
    }

}

