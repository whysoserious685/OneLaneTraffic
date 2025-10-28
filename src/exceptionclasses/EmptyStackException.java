package exceptionclasses;
/**
 * <p>Title: The EmptyStackException Class</p>
 *
 * @author Lehan Zhang John Risolo
 */

public class EmptyStackException extends RuntimeException {

    public EmptyStackException() {
        super("Empty Stack Exception, ArrayStack collection is empty");
    }
    public EmptyStackException(String message) {
        super(message);
    }

}
