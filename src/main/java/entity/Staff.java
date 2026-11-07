package entity;

import util.InvalidActionException;

/**
 * A member of staff who can do at most one action per turn.
 *
 * <p>Waiter extends this in Stage 1 (and Host in Stage 3). Chef and Cashier can
 * extend it too, if Danial agrees.
 *
 * <p>The "one action per turn" rule lives here once, so no subclass repeats it.
 */
public abstract class Staff {

    private final String name;
    private boolean acted;

    /**
     * @param name the name shown to the manager, e.g. "Waiter"
     */
    protected Staff(String name) {
        // TODO 1: store the name; a new staff member has not acted yet
        this.name = name;
    }

    /** @return the name shown to the manager */
    public String getName() {
        return name;
    }

    /**
     * Returns what this staff member does, e.g. "Waiter" or "Host".
     * Each subclass must say what it is, which is why Staff is abstract.
     *
     * @return the role name
     */
    public abstract String getRole();

    /** Gets this staff member ready for a new turn: they have not acted yet. */
    public void startTurn() {
        // TODO 2: reset the flag
        throw new UnsupportedOperationException("TODO: Staff.startTurn");
    }

    /** @return true if this staff member has already used their action this turn */
    public boolean hasActed() {
        // TODO 3
        return false;
    }

    /**
     * Records that the action for this turn is used up. Subclasses call this
     * only after an action has succeeded.
     */
    protected void markActed() {
        // TODO 4
        throw new UnsupportedOperationException("TODO: Staff.markActed");
    }

    /**
     * Stops a second action in the same turn.
     *
     * @throws InvalidActionException if this staff member has already acted this turn
     */
    protected void checkCanAct() throws InvalidActionException {
        // TODO 5: if already acted, throw new InvalidActionException(<a reason the manager understands>)
        throw new UnsupportedOperationException("TODO: Staff.checkCanAct");
    }
}
