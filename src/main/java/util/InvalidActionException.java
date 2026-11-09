package util;

/**
 * Thrown when the manager picks an action that breaks a game rule, for example
 * seating a customer at a table that is already occupied.
 *
 * <p>The message is the reason shown to the manager. The controller catches this
 * exception, shows the reason and asks again; the staff task is not used up.
 *
 * <p>This is a checked exception (it extends {@code Exception}), so every caller
 * must either catch it or declare it with {@code throws} (Chapter 4, Topics 4.3 and 4.7).
 */
public class InvalidActionException extends Exception {

    /**
     * Creates the exception with the reason shown to the manager.
     *
     * @param reason why the action is not allowed
     */
    public InvalidActionException(String reason) {
        super(reason);
    }
}
