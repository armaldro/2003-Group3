package control;

import java.util.function.IntFunction;

import entity.Customer;

/**
 * One scheduled arrival: the turn a customer arrives on, and which type of
 * customer it is.
 *
 * <p>The type is given as a constructor reference, for example
 * {@code new Arrival(4, VIPCustomer::new)}. Each customer class supplies its own
 * behaviour by overriding methods of {@code Customer}, so a new type of customer
 * joins the game through a new entry in the schedule in {@link GameConfig}, and
 * the turn sequence in {@link GameController} does not change. A new type may
 * still bring features of its own elsewhere, such as how it is shown on screen.
 *
 * @author CT2003 Group 3
 */
public final class Arrival {

    private final int turn;
    private final IntFunction<Customer> customerType;

    /**
     * Creates a scheduled arrival.
     *
     * @param turn         the turn the customer arrives on, from 1
     * @param customerType creates the customer from its id, for example {@code VIPCustomer::new}
     * @throws IllegalArgumentException if turn is less than 1 or customerType is null
     */
    public Arrival(int turn, IntFunction<Customer> customerType) {
        if (turn < 1) {
            throw new IllegalArgumentException("arrival turn must be at least 1");
        }
        if (customerType == null) {
            throw new IllegalArgumentException("customer type cannot be null");
        }
        this.turn = turn;
        this.customerType = customerType;
    }

    /**
     * Returns the turn the customer arrives on.
     *
     * @return the arrival turn
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Creates the arriving customer.
     *
     * @param id the id to give the new customer
     * @return a new customer of this arrival's type, waiting for a table
     * @throws IllegalStateException if the customer type creates no customer
     */
    public Customer createCustomer(int id) {
        Customer customer = customerType.apply(id);
        if (customer == null) {
            throw new IllegalStateException("the arrival on turn " + turn + " created no customer");
        }
        return customer;
    }
}
