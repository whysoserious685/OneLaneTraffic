package onelanetraffic;

import exceptionclasses.EmptyQueueException;
import queues.LinkedQueue;

public class ReusePool {
    LinkedQueue <Vehicle> reusePool; // a linked queue to store crashed vehicles that can be reused

    /**
     * Default constructor for the ReusePool class.
     * Initializes a new linked queue to manage a pool of reusable vehicles.
     */
    public ReusePool() {
        reusePool = new LinkedQueue<>();
    }

    /**
     * Recycles a vehicle by adding a crashed vehicle to the reuse pool.
     * This allows the vehicle to be reused later if needed.
     *
     * @param crashedVehicle the vehicle that has been crashed and is to be recycled
     */
    public void recycleVehicle(Vehicle crashedVehicle) {
        reusePool.enqueue(crashedVehicle);
    }

    /**
     * Retrieves and removes a vehicle from the reuse pool for reuse.
     * If the pool is empty, an EmptyQueueException is thrown.
     *
     * @return the first vehicle object stored in the reuse pool
     * @throws EmptyQueueException if the reuse pool is empty
     */
    public Vehicle reuseVehicle() throws EmptyQueueException {
        if (reusePool.isEmpty()) {
            throw new EmptyQueueException("The pool is empty, there is no vehicle to reuse.");
        }
        return reusePool.dequeue();
    }

    /**
     * Creates a deep copy of the current ReusePool object, including all vehicles in the reuse pool.
     *
     * @return a new ReusePool object containing a copy of the current reuse pool's vehicles
     */
    public ReusePool copy() {
        ReusePool newPool = new ReusePool();
        LinkedQueue<Vehicle> newQueue = new LinkedQueue<>();
        LinkedQueue<Vehicle> temp = new LinkedQueue<>();

        try {
            while (!reusePool.isEmpty()) {
                Vehicle v = reusePool.dequeue();
                newQueue.enqueue(v);  // same vehicle (immutable)
                temp.enqueue(v); // save to restore original
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

    /**
     * Returns a string representation of the ReusePool object
     *
     * @return a string describing the vehicles in the reusePool
     */
    @Override
    public String toString() {
        return "\nVehicles in the repair shop:\n" + reusePool.toString();
    }

}

