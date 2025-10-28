package queues;
import exceptionclasses.*;
/**
 * <p>Title: The QueueADT Interface</p>
 *
 * <p>Description: Defines the behaviors of a basic queue.</p>
 */
public interface QueueADT<E>
{
	/**  Adds one item to the rear of the queue. */
	public void enqueue (E newItem);

	/**  Removes and returns the item at the front of the queue. */
	public E dequeue() throws EmptyQueueException;
	
	/**  Returns without removing the item at the front of the queue. */
	public E front() throws EmptyQueueException;

	/**  Determines whether or not the queue is empty. */
	public boolean isEmpty();

	/**  Returns the number of items in the queue. */
	public int size();

	/**  Returns a string representing the state of the queue. */
	public String toString();
}
