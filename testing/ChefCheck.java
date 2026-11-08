import entity.Chef;
import entity.MenuItem;
import entity.Order;
import enums.OrderStatus;

public class ChefCheck {

    private static int failures = 0;

    public static void main(String[] args) {
        Chef chef = new Chef();

        // Pasta needs 2 units: NOT_READY after the first, READY after the second
        Order pastaOrder = new Order(new MenuItem("Pasta", 1800, 2), 1800);
        chef.cookOneUnit(pastaOrder);
        checkStatus("pasta after 1 call", pastaOrder, OrderStatus.NOT_READY);
        chef.cookOneUnit(pastaOrder);
        checkStatus("pasta after 2 calls", pastaOrder, OrderStatus.READY);

        // Burger needs 1 unit: READY after the first call
        Order burgerOrder = new Order(new MenuItem("Burger", 1500, 1), 1500);
        chef.cookOneUnit(burgerOrder);
        checkStatus("burger after 1 call", burgerOrder, OrderStatus.READY);

        // an order that is already READY cannot be cooked again
        checkThrows("cook a READY order", () -> chef.cookOneUnit(burgerOrder));

        // a cancelled order cannot be cooked. cancel() is how an order becomes CANCELLED
        Order cancelled = new Order(new MenuItem("Burger", 1500, 1), 1500);
        cancelled.cancel();
        checkThrows("cook a CANCELLED order", () -> chef.cookOneUnit(cancelled));

        System.out.println(failures == 0 ? "\nAll checks passed." : "\n" + failures + " check(s) FAILED.");
        if (failures > 0) {
            System.exit(1);
        }
    }

    /** Compares an order's status with the expected one. */
    private static void checkStatus(String label, Order order, OrderStatus expected) {
        boolean passed = order.getStatus() == expected;
        report(passed, label + " (expected " + expected + ", actual " + order.getStatus() + ")");
    }

    /** Expects the action to throw IllegalStateException. */
    private static void checkThrows(String label, Runnable action) {
        try {
            action.run();
            report(false, label + " (no exception thrown)");
        } catch (IllegalStateException e) {
            report(true, label + " throws: " + e.getMessage());
        }
    }

    private static void report(boolean passed, String label) {
        if (!passed) {
            failures++;
        }
        System.out.println((passed ? "OK   " : "FAIL ") + label);
    }
}