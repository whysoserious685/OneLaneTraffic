package queues;
import exceptionclasses.EmptyQueueException;

/**
 * <p>Title: The LinkedQueue Class</p>
 *
 * <p>Description: Defines the properties and behaviors of a linked queue.</p>
 *
 * @author Lehan Zhang Lucas Astrup
 */

public class LinkedQueue<E> implements QueueADT<E> {
    private Node<E> front, rear; //references to the first and last nodes

    /**
     * default constructor - creates an empty queue
     */
    public LinkedQueue() {
        front = rear = null;
    }

    /**
     * enqueue method - adds the specified item to the rear of the queue
     * @param newItem a reference to the item to be added to the queue
     */
    public void enqueue (E newItem) {
        Node<E> temp = new Node<E>(newItem);

        if(isEmpty()) {
            front = rear = temp;
        } else {
            rear.setNext(temp);
            rear = temp;
        }
    }

    /**
     * dequeue method - removes the item at the front of the queue
     * @return a reference to the item removed from the front of the queue
     * @throws EmptyQueueException
     * @throws EmptyQueueException if the queue is empty
     */
    public E dequeue() throws EmptyQueueException {
        if (isEmpty()) {
            throw new EmptyQueueException("LinkedQueue collection is empty");
        }

        E item = front.getItem();
        front = front.getNext();

        if (front == null) {
            rear = null;
        }

        return item;
    }

    /**
     * front method - returns a reference to the item at the front of the queue
     * without removing it from the queue
     * @return a reference to the item at the front of the queue
     * @throws EmptyQueueException if the queue is empty
     */
    public E front() throws EmptyQueueException {
        if (isEmpty()) {
            throw new EmptyQueueException("Queue is empty");
        }
        return front.getItem();
    }

    /**
     * isEmpty method - determines whether or not the queue is empty
     * @return true if the queue is empty; false if the queue is not empty
     */
    public boolean isEmpty() {
        return size()==0;
    }

    /**
     * size method - returns a count of the number of items in the queue
     * @return the number of items in the queue
     */
    public int size() {
        int count=0;
        Node<E> temp = front;

        while(temp != null) {
            count++;
            temp = temp.getNext();
        }
        return count;
    }

    /**
     * Searches method - search for a given item in the linked list
     * from front to rear and returns its position. The search uses 1-based
     * indexing,meaning the first element is at position 1.
     *
     * @param item the element to search for in the list
     * @return the 1-based position of the item if found; -1 if not found
     */
    public int search(E item) {
        int position = 1;
        Node<E> current = front;

        while(current != null) {
            if(current.getItem().equals(item)) {
                return position;
            }
            current = current.getNext();
            position++;
        }

        return -1;
//        for (int i = 1; i <= size(); i++) {
//            if (current.getItem().equals(item)) {
//                return i;
//            }
//            current = current.getNext();
//        }
//        return -1;
    }

    /**
     * Removes and returns the last element from the linked list.
     *
     * This method traverses the list to find the last node, removes it from the list,
     * and returns its value. If the list is empty, it throws an EmptyQueueException.
     *
     * @return the element removed from the end of the list
     * @throws EmptyQueueException if the list is empty
     */
    public E removeLast()throws EmptyQueueException {
        if(isEmpty()) {
            throw new EmptyQueueException("The queue is empty.");
        }
        E lastItem = rear.getItem();
        Node<E> current = front;

        if(size() == 1) {
            front = null;
            rear = null;
        } else {
            while(current.getNext()!=rear) {
                current = current.getNext();
            }
            rear = current;
            rear.setNext(null);
        }
        return lastItem;
    }

    /**
     * toString method - returns a String representing the state of the queue
     * @return a string containing all items in the queue
     */
    public String toString() {
        String copy = "";
        Node<E> current = front;

        while(current!=null) {
            copy+=current.getItem()+"\n";
            current = current.getNext();
        }
        return copy;
    }
}
