package enums;

/**
 * Lifecycle of an {@link entity.Order}.
 *
 * <p>Expected flow: {@code IN_PROGRESS -> READY -> SERVED -> PAID}.
 * An order can also end as {@code CANCELLED} when its customer abandons.
 *
 * <p>TODO (Danial): confirm these names with the team. The brief only fixes
 * READY, SERVED and PAID; the others are your design choice.
 */
public enum OrderStatus {
    /** Taken by the Waiter; the Chef has not finished all preparation units. */
    IN_PROGRESS,
    /** All preparation units done; waiting for the Waiter to serve it. */
    READY,
    /** Served to the customer. */
    SERVED,
    /** Paid at the Cashier. Counts towards revenue. */
    PAID,
    /** Customer left before paying. Never counts towards revenue. */
    CANCELLED
}
