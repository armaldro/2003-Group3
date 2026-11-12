package enums;

/**
 * The states of a customer, from arriving to leaving the restaurant.
 */
public enum CustomerState {
    /** In the waiting queue for a table. */
    WAITING,
    /** Seated at a table, has not ordered yet. */
    SEATED,
    /** Order taken, waiting for the dish to be served. */
    ORDERED,
    /** Dish served; eating takes one full turn. */
    EATING,
    /** Finished eating, waiting for the Cashier. */
    READY_TO_PAY,
    /** Has paid the bill. */
    PAID,
    /** Satisfaction reached 0, so the customer left without paying. */
    LEFT
}