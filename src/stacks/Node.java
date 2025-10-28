package stacks;

/**
 * <p>Title: Node Class</p>
 *
 * <p>Description: Defines a node class capable of storing a 
 * reference to an object and a reference to the next node in
 * a linked list. Accessors and mutators are defined for both.</p>
 */
public class
Node<E>
{
  private E data; //a reference to an object whose type is not yet known
  private Node<E> next; //the address of the next node in the list

  /**
   * default constructor - initializes data and next to null 
   */
  public Node()
  {
	  data = null;
	  next = null;
  }
  
  /**
   * parameterized constructor - initializes data to the user 
   * specified value; next is set to null
   * @param newItem the value to be stored in the node
   */
  public Node(E newItem)
  {
    data = newItem;
    next = null;
  }

  /**
   * parameterized constructor - initializes data and next to user 
   * specified values
   * @param newItem the object reference to be stored in the node
   * @param nextItem the reference to the next node in the list
   */
  public Node(E newItem, Node<E> nextItem)
  {
    data = newItem;
    next = nextItem;
  }

  /**
   * setItem - stores a new value in data
   * @param newItem the object reference to be stored in the node
   */
  public void setItem(E newItem)
  {
    data = newItem;
  }

  /**
   * setNext - stores a new value in next
   * @param nextItem the reference to be stored in next
   */
  public void setNext(Node<E> nextItem)
  {
    next = nextItem;
  }

  /**
   * getItem - returns the reference stored in data
   * @return a reference to the data stored in the node
   */
  public E getItem()
  {
    return data;
  }

  /**
   * getNext - returns the reference stored in next
   * @return the reference stored in next
   */
  public Node<E> getNext()
  {
    return next;
  }
  
}