package entity;

import enums.CustomerState;
import enums.OrderStatus;
import util.InvalidActionException;

/**
 * The Waiter: each turn, seats one customer, takes one order, serves one READY
 * dish, or waits.
 *
 * <p>Every action follows the same three steps:
 * <ol>
 *   <li>Check every rule. If one fails, throw {@link InvalidActionException}
 *       and change nothing, so the turn is not used up.</li>
 *   <li>Do the action.</li>
 *   <li>Call {@code markActed()}.</li>
 * </ol>
 *
 * <p>The Waiter does not need to check whether a dish became READY this turn:
 * the GameController runs the Waiter before the Chef, so any order that is
 * READY when the Waiter acts was finished on an earlier turn.
 */
public class Waiter extends Staff {

    /** Creates the Waiter. */
    public Waiter() {
        super("Waiter");
    }

    @Override
    public String getRole() {
        return "Waiter";
    }

    /**
     * Seats a queued customer at a free table. The seating itself goes through
     * {@link Restaurant#seat(Customer, Table)}, which the Host will reuse in Stage 3.
     *
     * @param restaurant the restaurant that owns the queue and the tables
     * @param customer   the queued customer to seat
     * @param table      the table to seat them at
     * @throws InvalidActionException if the Waiter has already acted, or the seating breaks a rule
     */
    public void seat(Restaurant restaurant, Customer customer, Table table) throws InvalidActionException {
        // TODO 6: checkCanAct(); then restaurant.seat(customer, table); then markActed()
        throw new UnsupportedOperationException("TODO: Waiter.seat");
    }

    /**
     * Takes a seated customer's order. The GameController builds the Order
     * (choosing the dish and locking the price), so the Waiter never deals with prices.
     *
     * @param customer the seated customer
     * @param order    the order with its price already locked
     * @throws InvalidActionException if the Waiter has already acted, or the customer is not SEATED
     */
    public void takeOrder(Customer customer, Order order) throws InvalidActionException {
        // TODO 7: checkCanAct()
        // TODO 8: if the customer is not SEATED, throw with a reason
        //         (hint: compare customer.getState() with CustomerState.SEATED)
        // TODO 9: customer.placeOrder(order); then markActed()
        throw new UnsupportedOperationException("TODO: Waiter.takeOrder");
    }

    /**
     * Serves a customer whose order is READY.
     *
     * @param customer the customer to serve
     * @param turn     the current turn, recorded so the customer finishes eating next turn
     * @throws InvalidActionException if the Waiter has already acted, the customer has no order,
     *                                or the order is not READY
     */
    public void serve(Customer customer, int turn) throws InvalidActionException {
        // TODO 10: checkCanAct()
        // TODO 11: no order yet? throw. Order not READY? throw (say how many units are left)
        // TODO 12: customer.getOrder().markServed(); customer.markServed(turn); markActed()
        throw new UnsupportedOperationException("TODO: Waiter.serve");
    }

    /**
     * Uses up the Waiter's turn doing nothing.
     *
     * @throws InvalidActionException if the Waiter has already acted this turn
     */
    public void waitTurn() throws InvalidActionException {
        // TODO 13: checkCanAct(); markActed()
        throw new UnsupportedOperationException("TODO: Waiter.waitTurn");
    }
}
