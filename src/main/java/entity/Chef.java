package entity;

import util.InvalidActionException;

/**
 * The kitchen. Does one preparation unit per turn on one chosen order.
 *
 * <p>The Chef never decides WHICH order (that is the manager's choice, passed
 * in) and never prints anything (that is the boundary's job).
 */
public class Chef {

    /**
     * Advances the chosen order by one preparation unit.
     *
     * @param order the order the manager chose
     * @throws InvalidActionException if the order cannot be worked on, with a
     *                                reason the manager can read
     */
    public void prepare(Order order) throws InvalidActionException {
        // TODO (Danial): reject null / non-IN_PROGRESS orders, else order.advance().
        // Remember "one unit per turn": enforcing that limit is the
        // controller's job (it knows what a turn is), not the Chef's.
        throw new UnsupportedOperationException("TODO");
    }
}
