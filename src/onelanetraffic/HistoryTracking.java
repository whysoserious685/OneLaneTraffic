package onelanetraffic;

import stacks.LinkedStack;

public class HistoryTracking {
    private LinkedStack<Road> roadHistory; // a linkedStack to store previous road states

    public HistoryTracking() {
        roadHistory = new LinkedStack<>(); // a linkedStack object used as history tracking for previous road states
    }

    /**
     * create a deep copy of the current road with all its fields (except current)
     * and push the copy into the roadHistory stack.
     *
     * @param currentRoad the road needed to be stored into roadHistory
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

    /**
     * Reverts to the previous state of the road by removing and returning the most
     * recently saved Road object from the history stack.
     * If the history stack is empty, return null and prints a message.
     *
     * @return the previous state of the Road, or null if the
     *         roadHistory stack is empty.
     */
    public Road undo() {
        if (roadHistory.isEmpty()) {
            System.out.println("No more steps to undo. History is empty.");
            return null;
        }
        return roadHistory.pop();
    }

    /**
     * Checks whether the roadHistory stack is empty.
     *
     * @return true if the roadHistory stack is empty; false otherwise
     */
    public boolean isEmpty() {
        return roadHistory.isEmpty();
    }
}



