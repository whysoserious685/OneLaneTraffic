package onelanetraffic;

import stacks.LinkedStack;

public class HistoryTracking {
    private LinkedStack<Road> history;
    private int count;

    public HistoryTracking() {
        history = new LinkedStack<>();
        count = 0;
    }

    public void addHistory(Road roadState) {
        history.push(roadState);
    }

    public LinkedStack<Road> getHistory() {
        return history;
    }

    public void undo(int times) {
        for (int i = 0; i < times; i++) {
            history.pop();
            count--;
        }
    }

}
