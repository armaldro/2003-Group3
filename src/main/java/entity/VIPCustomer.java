package entity;

import util.Money;

/**
 * A VIP customer. VIPs are more patient than regular customers, losing
 * 5 satisfaction per waiting turn, and get 10% off their bill.
 */
public class VIPCustomer extends Customer {

    /** Percentage of the menu price a VIP pays (10% off). */
    private static final int PRICE_PERCENT = 90;

    /**
     * Creates a VIP customer who has just arrived.
     *
     * @param id the customer's number
     */
    public VIPCustomer(int id) {
        super(id);
    }

    /**
     * Returns the satisfaction a VIP loses each turn they wait unserved.
     *
     * @return 5
     */
    @Override
    public int getSatisfactionLoss() {
        return 5;
    }

    /**
     * Returns the price a VIP pays: 10% off, rounded to the nearest cent.
     * For example, a $15.00 Burger costs a VIP $13.50.
     *
     * @param priceCents the standard menu price, in cents
     * @return the discounted price, in cents
     */
    @Override
    public int applyDiscount(int priceCents) {
        return Money.percentOf(priceCents, PRICE_PERCENT);
    }
}