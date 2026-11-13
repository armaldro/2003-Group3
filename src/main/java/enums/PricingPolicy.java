package enums;

import util.Money;

/**
 * How the price of a new order is worked out. The Waiter asks the current
 * policy for the price when it takes an order, and the price is then locked
 * in the Order.
 *
 * <p>Brief (Stage 3, Happy Hour): orders taken while Happy Hour is active get
 * 20% off. Apply the customer's own discount (e.g. VIP) FIRST, then this one.
 * Example: a VIP orders a $15.00 Burger in Happy Hour: 1500 -> 1350 -> 1080.
 */
public enum PricingPolicy {

    /** Normal pricing: the customer pays the full price. */
    NORMAL(100),

    /** Happy Hour pricing: the customer pays 80% of the full price */
    HAPPY_HOUR(80);

    // The percent of the price the customer pays under this policy. Never changes.
    private final int percentPayable;

    PricingPolicy(int percentPayable) {
        this.percentPayable = percentPayable;
    }

    /**
     * Works out the price to charge under this policy.
     *
     * @param priceCents the price in cents BEFORE this policy (already
     *                   discounted for the customer, if they get one)
     * @return the price in cents after this policy
     */
    public int apply(int priceCents) {
        return Money.percentOf(priceCents, percentPayable);
    }

    /**
     * Picks the policy for the current turn.
     *
     * @param happyHourActive true while Happy Hour is running
     * @return HAPPY_HOUR when active, otherwise NORMAL
     */
    public static PricingPolicy of(boolean happyHourActive) {
        if (happyHourActive) {
            return HAPPY_HOUR;
        }
        return NORMAL;
    }
}
