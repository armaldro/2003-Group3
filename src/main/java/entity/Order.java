package entity;

import enums.OrderStatus;

/**
 * One order placed by a customer: which dish, how much cooking is done,
 * the price locked when the order was taken, and its current status.
 */
public class Order {

    // The dish this order is for. It never changes, so we make it final
    private final MenuItem item;

    // The price locked when the order was taken, in cents. final
    private final int priceCents;

    // How many preparation units the Chef has done so far. Starts at 0.
    private int unitsDone;

    // The current status. It starts as RECEIVED.
    private OrderStatus status;

    /**
     * Creates an order when the Waiter takes it.
     *
     * @param item       the dish ordered
     * @param priceCents the price locked now, in cents
     * @throws IllegalArgumentException if item is null
     * @throws IllegalArgumentException if price is {@code <= 0} 
     */
    public Order(MenuItem item, int priceCents) {

        if (item == null) {
            throw new IllegalArgumentException("item cannot be null");
        }
        if (priceCents <= 0) {
            throw new IllegalArgumentException("price cannot be negative or 0");
        }
        
        this.item = item;
        this.priceCents = priceCents;
        unitsDone = 0;
        status = OrderStatus.RECEIVED;


        
    }

    // ----- getters (simple) -----

    /** 
     * returns the dish ordered
     * @return the dish ordered */
    public MenuItem getItem() {
        
        return item;
    }

    /**
     * returns the locked price in cents
     *  @return the locked price in cents */
    public int getPriceCents() {
        
        return priceCents;
    }

    /** 
     * returns how many prep units are done
     * @return how many preparation units are done */
    public int getUnitsDone() {
        
        return unitsDone;
    }

    /** 
     * returns the current order status 
     * @return the current status */
    public OrderStatus getStatus() {
        
        return status;
    }

    /**
     * return how many prep units are still left
     * @return how many preparation units are still left
     */
    public int getUnitsLeft() {
        
        return (item.getPrepUnits() - unitsDone);
    }

    // ----- actions that change the status -----

    /**
     * Does one preparation unit (called when the Chef works on this order).
     * When the last unit is done, the order becomes READY. Before that it is
     * NOT_READY.
     *
     * @throws IllegalStateException if the order cannot be cooked now
     */
    public void cookOneUnit() {
    
        if (status != OrderStatus.RECEIVED && status != OrderStatus.NOT_READY) {
            throw new IllegalStateException("order status must be 'RECEIVED' or 'NOT_READY'");
        }
        
        unitsDone += 1;
        
        if (unitsDone >= item.getPrepUnits()) {
            status = OrderStatus.READY;
        } else {
            status = OrderStatus.NOT_READY;
        }
    }

    /**
     * Marks the order as served (called when the Waiter serves it).
     *
     * @throws IllegalStateException if the order is not READY
     */
    public void markServed() {

        if (status != OrderStatus.READY) {
            throw new IllegalStateException("order must be ready before being served");
        }
        status = OrderStatus.SERVED;
    }

    /**
     * Marks the order as paid (called by the Cashier).
     *
     * @throws IllegalStateException if the order is not SERVED
     */
    public void markPaid() {

        if (status != OrderStatus.SERVED) {
            throw new IllegalStateException("order must be served before it can be paid");
        }
        status = OrderStatus.PAID;
    }

    /**
     * Cancels the order when its customer leaves without paying.
     * Only an order that has not been served can be cancelled.
     *
     * @throws IllegalStateException if the order is already SERVED, PAID or CANCELLED
     */
    public void cancel() {
        
        if (status == OrderStatus.SERVED) {
            throw new IllegalStateException("order cannot be cancelled if already served");
        }
        if (status == OrderStatus.PAID) {
            throw new IllegalStateException("order cannot be cancelled if already paid");
        }
        if (status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("order cannot be cancelled if already cancelled");
        }

        status = OrderStatus.CANCELLED;
    }

    // TODO 15 (later, ask Teo): a dish that turns READY this turn must not be
    //   served until next turn. Where should that rule live: here, or in the
    //   turn controller? Think about it before you decide.
}