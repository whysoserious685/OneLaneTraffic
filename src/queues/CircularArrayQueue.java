package queues;
import exceptionclasses.*;

/**
 * <p>Title: CircularArrayQueue Class</p>
 *
 * <p>Description: Provides basic Queue functionality, including the ability
 * to enqueue and dequeue items to/from the queue, get the front-most item, 
 * determine whether or not the queue is empty, determine the queue's size, 
 * and to get a String representation of the items in the queue.</p>
 *
 * @author Lehan Zhang,  Lucas Astrup
 */

public class CircularArrayQueue<E> implements QueueADT<E> {

	private int front;
	private int rear;
	private E[] contents;
	private int count;

	/**
	 * default constructor -- creates an empty queue.
	 */
	@SuppressWarnings("unchecked")
	public CircularArrayQueue() {
		front = 0;
		rear = 0;
		contents = (E[]) (new Object[100]);
		count = 0;
	}

	/**
	 * parameterized constructor --
	 * creates an empty queue that is initially capable of storing 
	 * 'size' items.
	 * @param size the initial size of the queue as specified by the user
	 */
	@SuppressWarnings("unchecked")
	public CircularArrayQueue(int size) {
		front = 0;
		rear = 0;
		if (size > 0)
			contents = (E[]) (new Object[size]);
		else
			contents = (E[]) (new Object[100]);
		count = 0;
	}

	/**
	 * enqueue --
	 * stores a new item at the rear of the queue; if the queue becomes
	 * full, it's size is automatically increased to accommodate additional items.
	 * @param newItem a reference to the item to be stored at the rear of the queue
	 */
	public void enqueue(E newItem) {
		if (count == contents.length) {
			expandCapacity();
		}

		contents[rear] = newItem;
		rear = (rear + 1) % contents.length;
		count++;
	}

	/**
	 * deQueue -- removes the front-most item from the queue.
	 * @return a reference to the object which was stored at the front of the
	 * queue
	 * @throws EmptyQueueException if the queue is empty
	 */
	public E dequeue() throws EmptyQueueException {
		if (count == 0) {
			throw new EmptyQueueException ("CircularArrayQueue collection is empty");
		}

		E item = contents[front];
		front = (front +1) % contents.length;
		count--;

		return item;
	}

	/**
	 * front --
	 * returns the item stored at the front of the queue; the queue 
	 * is not modified.
	 * @return a reference to the object which is stored at the front of the queue
	 * @throws EmptyQueueException if the queue is empty
	 */
	public E front() throws EmptyQueueException {
		if (isEmpty())
			throw new EmptyQueueException("CircularArrayQueue is empty!");
		else
			return contents[front];
	}

	/**
	 * isEmpty -- determines whether or not the queue is empty.
	 * @return true if the queue is empty; false otherwise
	 */
	public boolean isEmpty() {
		return count == 0;
	}

	/**
	 * count -- returns the count of the number of items in the queue.
	 * @return count
	 */
	public int size() {
		return count;
	}

	/**
	 * search -- looking for the given item location in the array 
	 * @param item the object to search for
	 * @return a 1-based index representing the order of the items in the queue; returns -1 if not found
	 */
	public int search(E item) {
		int location = 1;
		int current = front;

		for (int i = 0; i < count ; i++ ) {

			if(contents[current].equals(item)) {
				return location;
			}
			current = (current +1) % contents.length;
			location++;
		}

		return -1;
	}

	/**
	 * expandCapacity --
	 * a private method called upon by the enqueue method when the queue 
	 * becomes full; the queue size is doubled to accommodate the storage of
	 * additional items.
	 */
	@SuppressWarnings("unchecked")
	private void expandCapacity() {
		E temp[] = (E[]) new Object[contents.length*2];
		int current = front;
		for (int i = 0; i < count ; i++ ) {

			if (count == 0) {
				break;
			}
			else {
				temp[i]= contents[current];
				current = (current +1) % contents.length;
			}
		}
		front = 0;
		rear=contents.length;
		contents=temp;

	}

	/**
	 * toString method - returns a String representing the current state of the queue.
	 * @return a String containing all items in the queue
	 */
	public String toString() {
		String str = "";
		int current = front;

		for (int i = 0; i < count ; i++ ) {

			if (count == 0) {
				return str;
			}
			else {
				str += contents[current].toString() + "\n";
				current = (current +1) % contents.length;
			}
		}

		return str;
	}
}
