package enums;

/**
 * The tasks the manager can give the Waiter in one turn.
 *
 * <p>The number shown to the manager is the task's position in this list plus 1,
 * so the console can build its menu straight from {@link #values()}.
 */
public enum WaiterTask {

    /** Seat one queued customer at a free table. */
    SEAT("Seat a customer"),

    /** Take the order of one seated customer. */
    TAKE_ORDER("Take an order"),

    /** Serve one order that was already READY when the Waiter acts. */
    SERVE("Serve a ready dish"),

    /** Do nothing this turn. */
    WAIT("Wait");

    private final String label;

    WaiterTask(String label) {
        this.label = label;
    }

    /** @return the text to show the manager */
    public String getLabel() {
        return label;
    }
}
