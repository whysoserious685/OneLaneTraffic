package onelanetraffic;

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
        clone.setReusePool(currentRoad.getReusePool());

        roadHistory.push(clone);
    }

    // need fix
    public Road undo() {
        return roadHistory.pop();
    }
}



