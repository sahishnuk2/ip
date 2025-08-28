package sharva.exceptions;

/**
 * Exception class for invalid commands.
 */
public class InvalidCommandException extends SharvaException {
    public InvalidCommandException() {
        super("Sorry bro, I don't know what that means!");
    }
}
