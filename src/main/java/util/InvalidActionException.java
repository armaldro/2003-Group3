package util;

/**
 * Thrown when the manager picks an action the rules do not allow
 * (e.g. serving an order that is not READY).
 *
 * <p>The brief (section 3.4) requires that an invalid choice explains why it
 * failed and lets the manager choose again WITHOUT using up the staff task.
 * So: the entity throws this with a clear message, the controller does not
 * consume the task, and the boundary prints {@link #getMessage()}.
 *
 * <p>NOTE: shared by every member. Tell the team it exists.
 */
public class InvalidActionException extends Exception {

    /**
     * @param message human-readable reason shown to the manager
     */
    public InvalidActionException(String message) {
        super(message);
    }
}
