package entity;

import enums.OrderStatus;
import util.InvalidActionException;

/**
 * One customer's order: which dish, how much preparation is done, its status
 * and the bill locked at the moment the order was taken.
 *
 * <p>Encapsulation: only Order changes its own status, through the methods
 * below, so illegal jumps (e.g. SERVED before READY) are impossible from
 * outside. Each method should throw {@link InvalidActionException} with a
 * clear reason when the move is not allowed.
 *
 * <p>The bill is passed in already computed (the customer type decides any
 * discount in Stage 2, Happy Hour in Stage 3), so Order stays simple.
 */
public class Order {

    private final MenuItem item;
    private final int billCents;
    private int unitsDone;
    private OrderStatus status;

    /**
     * @param item      the dish ordered
     * @param billCents price locked now (never changes afterwards)
     */
    public Order(MenuItem item, int billCents) {
        this.item = item;
        this.billCents = billCents;
        this.unitsDone = 0;
        this.status = OrderStatus.IN_PROGRESS;
    }

    /** @return the dish ordered */
    public MenuItem getItem() {
        return item;
    }

    /** @return the locked bill in cents */
    public int getBillCents() {
        return billCents;
    }

    /** @return preparation units completed so far */
    public int getUnitsDone() {
        return unitsDone;
    }

    /** @return current status */
    public OrderStatus getStatus() {
        return status;
    }

    /**
     * Applies one preparation unit (called by the Chef). When the last unit is
     * done the status becomes READY.
     *
     * @throws InvalidActionException if the order is not IN_PROGRESS
     */
    public void advance() throws InvalidActionException {
        // TODO (Danial)
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Marks the order SERVED (called via the Waiter).
     *
     * @throws InvalidActionException if the order is not READY
     */
    public void serve() throws InvalidActionException {
        // TODO (Danial)
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Marks the order PAID (called by the Cashier).
     *
     * @throws InvalidActionException if the order is not SERVED
     */
    public void markPaid() throws InvalidActionException {
        // TODO (Danial)
        throw new UnsupportedOperationException("TODO");
    }

    /** Cancels an unfinished order when its customer abandons. */
    public void cancel() {
        // TODO (Danial): which statuses may still be cancelled?
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Timing rule (brief step 3): a dish that turns READY this turn must not be
     * served this turn. Think about how the controller will know. One option is
     * a flag here that the controller clears at end of turn; another is to let
     * the controller compare turn numbers. Pick one and justify it in the report.
     */
}
