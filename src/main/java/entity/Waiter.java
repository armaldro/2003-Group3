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

    /**
     * Returns this staff member's role.
     *
     * @return {@code "Waiter"}
     */
    @Override
    public String getRole() {
        return "Waiter";
    }

    /**
     * Seats a queued customer at a free table. The seating itself goes through
     * {@link Restaurant#seat(Customer, Table)}.
     *
     * @param restaurant the restaurant that owns the queue and the tables
     * @param customer   the queued customer to seat
     * @param table      the table to seat them at
     * @throws InvalidActionException if the Waiter has already acted, or the seating breaks a rule
     */
    public void seat(Restaurant restaurant, Customer customer, Table table) throws InvalidActionException {
        checkCanAct();
        restaurant.seat(customer, table);
        markActed();
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
        checkCanAct();
        if (customer.getState() != CustomerState.SEATED) {
            throw new InvalidActionException("Customer " + customer.getId()
                    + " cannot order: they are " + customer.getState() + ", not SEATED.");
        }

        customer.placeOrder(order);
        markActed();
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
        checkCanAct();
        Order order = customer.getOrder();
        if (order == null) {
            throw new InvalidActionException("Customer " + customer.getId() + " has not ordered yet.");
        }
        if (order.getStatus() != OrderStatus.READY) {
            String dish = order.getItem().getName();
            if (order.getStatus() == OrderStatus.RECEIVED || order.getStatus() == OrderStatus.NOT_READY) {
                throw new InvalidActionException("Customer " + customer.getId() + "'s " + dish
                        + " is not ready: " + order.getUnitsLeft() + " unit(s) left to cook.");
            }
            throw new InvalidActionException("Customer " + customer.getId() + "'s " + dish
                    + " cannot be served: it is already " + order.getStatus().getLabel() + ".");
        }

        order.markServed();
        customer.markServed(turn);
        markActed();
    }

    /**
     * Uses up the Waiter's turn doing nothing.
     *
     * @throws InvalidActionException if the Waiter has already acted this turn
     */
    public void waitTurn() throws InvalidActionException {
        checkCanAct();
        markActed();
    }
}