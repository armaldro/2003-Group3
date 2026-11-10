import enums.PricingPolicy;
import util.Money;

public class PricingPolicyCheck {

    private static int failures = 0;

    public static void main(String[] args) {

        // normal pricing leaves the price unchanged
        check("NORMAL.apply(1500) is 1500", PricingPolicy.NORMAL.apply(1500) == 1500);
        check("NORMAL.apply(900) is 900", PricingPolicy.NORMAL.apply(900) == 900);

        // Happy Hour is 20% off, so the customer pays 80%
        check("HAPPY_HOUR.apply(1500) is 1200", PricingPolicy.HAPPY_HOUR.apply(1500) == 1200);
        check("HAPPY_HOUR.apply(1800) is 1440", PricingPolicy.HAPPY_HOUR.apply(1800) == 1440);

        // the brief's example: a VIP orders a $15.00 Burger in Happy Hour = $10.80
        // (VIP discount first: 90%, then Happy Hour: 80%)
        int vipPrice = Money.percentOf(1500, 90);
        check("VIP then Happy Hour on 1500 is 1080", PricingPolicy.HAPPY_HOUR.apply(vipPrice) == 1080);

        // a combo during Happy Hour: Salad + Pasta is 2430, then 80% = 1944
        check("Happy Hour on a 2430 combo is 1944", PricingPolicy.HAPPY_HOUR.apply(2430) == 1944);

        // rounding to the nearest cent: 80% of 5 cents is 4, 80% of 1 cent is 1 (0.8 rounds up)
        check("HAPPY_HOUR.apply(5) is 4", PricingPolicy.HAPPY_HOUR.apply(5) == 4);
        check("HAPPY_HOUR.apply(1) is 1", PricingPolicy.HAPPY_HOUR.apply(1) == 1);

        // choosing the policy from the active flag
        check("of(true) is HAPPY_HOUR", PricingPolicy.of(true) == PricingPolicy.HAPPY_HOUR);
        check("of(false) is NORMAL", PricingPolicy.of(false) == PricingPolicy.NORMAL);

        // a negative price is rejected (Money.percentOf does this, so it passes through)
        try {
            PricingPolicy.HAPPY_HOUR.apply(-1);
            check("negative price is rejected (no exception thrown)", false);
        } catch (IllegalArgumentException e) {
            check("negative price is rejected -> " + e.getMessage(), true);
        }

        System.out.println(failures == 0 ? "\nAll checks passed." : "\n" + failures + " check(s) FAILED.");
        if (failures > 0) {
            System.exit(1);
        }
    }

    private static void check(String label, boolean passed) {
        if (!passed) {
            failures++;
        }
        System.out.println((passed ? "OK   " : "FAIL ") + label);
    }
}
