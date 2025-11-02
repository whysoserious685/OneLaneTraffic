package onelanetraffic;

import exceptionclasses.EmptyQueueException;
import exceptionclasses.EmptyStackException;
import stacks.LinkedStack;

public class HistoryTracking {
    private LinkedStack<Road> roadHistory; // a linkedStack to store previous road states

    public HistoryTracking() {
        roadHistory = new LinkedStack<>();
    }

    /**
     * create a deep copy of the current road with all its fields and push
     * the copy into the stack of road history
     *
     * @param currentRoad the road needed to be stored in the stack
     */
    public void addHistory(Road currentRoad) {
        Road clone = new Road(currentRoad.getSize());
        for (int i = 0; i < clone.getSize(); i++) {
            clone.getVehicles()[i] = currentRoad.getVehicles()[i];
        }

        clone.setNumVehicles(currentRoad.getNumVehicles());
        // deep copy of the reusePool
        clone.setReusePool(currentRoad.getReusePool().copy());

        roadHistory.push(clone);
    }

    // need fix
    public Road undo() {
        if (roadHistory.isEmpty()) {
            System.out.println("No more steps to undo. History is empty.");
            return null;
        }
        return roadHistory.pop();
    }

    public boolean isEmpty() {
        return roadHistory.isEmpty();
    }
}



