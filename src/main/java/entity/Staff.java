package entity;

import util.InvalidActionException;

/**
 * A member of staff who can do at most one action per turn.
 *
 * <p>Waiter extends this in Stage 1. Chef and Cashier can
 * extend it too, if Danial agrees.
 *
 * <p>The "one action per turn" rule lives here once, so no subclass repeats it.
 */
public abstract class Staff {

    private final String name;
    private boolean acted;

    /**
     * Creates a staff member who has not acted yet.
     * @param name the name shown to the manager, e.g. "Waiter"
     */
    protected Staff(String name) {
        this.name = name;
        this.acted = false;
    }

    /** @return the name shown to the manager */
    public String getName() {
        return name;
    }

    /**
     * Returns what this staff member does.
     * Each subclass must say what it is, which is why Staff is abstract.
     *
     * @return the role name
     */
    public abstract String getRole();

    /** Gets this staff member ready for a new turn: they have not acted yet. */
    public void startTurn() {
        this.acted = false;
    }

    /** @return true if this staff member has already used their action this turn */
    public boolean hasActed() {
        return acted;
    }

    /**
     * Records that the action for this turn is used up. Subclasses call this
     * only after an action has succeeded.
     */
    protected void markActed() {
        this.acted = true;
    }

    /**
     * Stops a second action in the same turn.
     *
     * @throws InvalidActionException if this staff member has already acted this turn
     */
    protected void checkCanAct() throws InvalidActionException {
        if (acted) {
            throw new InvalidActionException("The " + this.name + " has already acted this turn!");
        }
    }
}
