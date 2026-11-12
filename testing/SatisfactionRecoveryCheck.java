import entity.Customer;
import entity.RegularCustomer;
 
/**
 * Checks Customer.recoverSatisfaction(), used by Happy Hour in Stage 3:
 * +15 per Happy Hour turn while waiting, never above 100.
 */
public class SatisfactionRecoveryCheck {
 
    private static int failures = 0;
 
    public static void main(String[] args) {
        check("MAX_SATISFACTION is 100", Customer.MAX_SATISFACTION, 100);
 
        Customer customer = new RegularCustomer(1);
        customer.loseSatisfaction();
        customer.loseSatisfaction();
        check("after 2 waiting turns: 100 - 16 = 84", customer.getSatisfaction(), 84);
 
        customer.recoverSatisfaction(15);
        check("84 + 15 = 99", customer.getSatisfaction(), 99);
 
        customer.recoverSatisfaction(15);
        check("99 + 15 is capped at 100", customer.getSatisfaction(), 100);
 
        Customer full = new RegularCustomer(2);
        full.recoverSatisfaction(15);
        check("a customer already at 100 stays at 100", full.getSatisfaction(), 100);
 
        System.out.println(failures == 0 ? "\nAll checks passed." : "\n" + failures + " check(s) FAILED.");
        if (failures > 0) {
            System.exit(1);
        }
    }
 
    /** Compares an actual value with the expected one and prints OK or FAIL. */
    private static void check(String label, Object actual, Object expected) {
        boolean passed = expected.equals(actual);
        if (!passed) {
            failures++;
        }
        System.out.println((passed ? "OK   " : "FAIL ") + label
                + (passed ? "" : " (expected " + expected + ", actual " + actual + ")"));
    }
}
