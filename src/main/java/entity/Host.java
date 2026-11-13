package entity;

import util.InvalidActionException;

/**
 * The Host (Stage 3): seats at most one queued customer per turn, in addition
 * to the Waiter's task. The Waiter can still seat customers too.
 *
 * <p>Like the Waiter, the Host extends {@link Staff}, so the one-action-per-turn
 * rule comes from there. The seating itself goes through
 * {@link Restaurant#seat(Customer, Table)}, the same method the Waiter uses, so
 * the seating rules are written once.
 */
public class Host extends Staff {

    /** Creates the Host. */
    public Host() {
        super("Host");
    }

    /**
     * Returns this staff member's role.
     *
     * @return {@code "Host"}
     */
    @Override
    public String getRole() {
        return "Host";
    }

    /**
     * Seats a queued customer at a free table.
     *
     * @param restaurant the restaurant that owns the queue and the tables
     * @param customer   the queued customer to seat
     * @param table      the table to seat them at
     * @throws InvalidActionException if the Host has already acted this turn, or
     *                                the seating breaks a rule (customer not
     *                                queued, or the table is occupied)
     */
    public void seat(Restaurant restaurant, Customer customer, Table table) throws InvalidActionException {
        checkCanAct();
        restaurant.seat(customer, table);
        markActed();
    }

    /**
     * Uses up the Host's turn without seating anyone.
     *
     * @throws InvalidActionException if the Host has already acted this turn
     */
    public void waitTurn() throws InvalidActionException {
        checkCanAct();
        markActed();
    }
}
