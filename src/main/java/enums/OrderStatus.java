package enums;

/**
 * The stages an Order passes through from being received to being paid or cancelled.
 * 
 * <p>An order moves forward one stage at a time, except it can be cancelled before it is served.
 * Only {@code Order} changes its status.
 */
public enum OrderStatus {

    /** Order has just been received by waiter; Set when the waiter takes the order */
    RECEIVED("Received"),
    
    /** Order not finished cooking by chef; CHEF moves order to NOT READY state, orders with 1 prepUnit skips NOT_READY and moves from RECEIVED to READY */
    NOT_READY("Not ready"),

    /** Order is ready, chef has finished cooking the order; CHEF moves order to READY state */
    READY("Ready"),

    /** Order is served; WAITER moves order to SERVED state*/
    SERVED("Served"),

    /** Customer has paid for the order; CASHIER moves order to PAID state */
    PAID("Paid"),

    /** Order is cancelled; when a customer satisfaction reaches 0; cancelled by the game when customer satisfaction reaches 0 */
    CANCELLED("Cancelled");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    /** @return the text to show the manager */
    public String getLabel() {
        return label;
    }

}