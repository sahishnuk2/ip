package sharva.exceptions;

/**
 * Exception class for invalid indices.
 */
public class InvalidIndexException extends SharvaException {
    public InvalidIndexException() {
        super("Invalid task number!");
    }
}
