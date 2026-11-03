package util;

/**
  utility class to format a value into dollars and cents for UI 
*/
public final class Money {

    // Private constructor to prevent instantiating
    private Money() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    /**
     * format method: takes a integer value and formats it
       example use: String value = Money.format(1080)
       value is now a string that looks like $10.80

       for negative values in billing, convert value to positive, then return with "-" before $ to show negative value
     * @param value the amount in cents
     * @return amount as a string e.g. "$10.80"
     */
    public static String format(int value) {

        if (value < 0) {
            value = value * -1;
            int cents = value % 100;
            int dollars = value / 100;
            return String.format("-$%d.%02d", dollars, cents);
        }
        int cents = value % 100;
        int dollars = value / 100;
        return String.format("$%d.%02d", dollars, cents);
    }

    /**
     * percentOf method: returns an integer of the percent of an input value
     * e.g. 10% of 100 cents = 10
     * @param cents the input amount in cents
     * @param pct the percentage of cents to return
     * @return amount obtained from percentage of cents
     */
    public static int percentOf(int cents, int pct) {
        return (int) Math.round(cents * (pct / 100.0));
    }
}
