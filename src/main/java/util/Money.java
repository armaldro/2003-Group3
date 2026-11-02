package util;

/**
 * Helpers for money. All amounts in the game are stored as integer cents
 * (brief section 2.2) and only converted to dollars for display.
 */
public final class Money {

    private Money() {
        // utility class, no instances
    }

    /**
     * Formats cents as dollars with two decimals, e.g. 1080 -> "$10.80".
     *
     * @param cents amount in cents
     * @return formatted dollar string
     */
    public static String format(int cents) {
        // TODO (Danial): build "$d.dd" from cents. Avoid double arithmetic.
        // Hint: cents / 100 and cents % 100, or String.format with %02d.
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Applies a percentage discount and rounds to the nearest cent, using
     * integer maths only.
     *
     * @param cents          original amount in cents
     * @param percentPayable percent still payable (90 means 10% off)
     * @return discounted amount in cents
     */
    public static int applyPercent(int cents, int percentPayable) {
        // TODO (Danial): cents * percentPayable / 100, rounded to nearest cent.
        // Stage 3 needs this for Combo (90%) and Happy Hour (80%), and the
        // order matters: VIP first, then Happy Hour. Think about rounding
        // when you chain two discounts.
        throw new UnsupportedOperationException("TODO");
    }
}
