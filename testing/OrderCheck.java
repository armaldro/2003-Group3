import entity.MenuItem;
import entity.Order;
import enums.OrderStatus;

public class OrderCheck {

    private static int failures = 0;

    public static void main(String[] args) {
        // ... your Pasta flow checks go here (inside main) ...

        checkBurgerSkipsNotReady();
        checkIllegalMoves();
        checkCancelFromEarlyStates();

        System.out.println(failures == 0 ? "\nAll checks passed." : "\n" + failures + " check(s) FAILED.");
        if (failures > 0) {
            System.exit(1);
        }
    }

    /** A 1-unit dish goes straight from RECEIVED to READY. */
    private static void checkBurgerSkipsNotReady() {
        Order burgerOrder = new Order(new MenuItem("Burger", 1500, 1), 1500);
        burgerOrder.cookOneUnit();
        checkStatus("burger READY after 1 unit", burgerOrder, OrderStatus.READY);
    }

    /** Each illegal move must throw IllegalStateException. */
    private static void checkIllegalMoves() {
        // serving or paying a brand-new order (still RECEIVED)
        Order fresh = newSalad();
        checkThrows("serve a RECEIVED order", () -> fresh.markServed());
        checkThrows("pay a RECEIVED order", () -> fresh.markPaid());

        // cooking an order that is already READY
        Order ready = newSalad();
        ready.cookOneUnit();
        checkThrows("cook a READY order", () -> ready.cookOneUnit());

        // paying an order that is READY but not yet SERVED
        checkThrows("pay a READY order", () -> ready.markPaid());

        // cancelling an order that has already been served
        Order served = newSalad();
        served.cookOneUnit();
        served.markServed();
        checkThrows("cancel a SERVED order", () -> served.cancel());
    }

    /** Cancelling is allowed before the order is served. */
    private static void checkCancelFromEarlyStates() {
        Order received = newSalad();
        received.cancel();
        checkStatus("cancel from RECEIVED", received, OrderStatus.CANCELLED);

        Order ready = newSalad();
        ready.cookOneUnit();
        ready.cancel();
        checkStatus("cancel from READY", ready, OrderStatus.CANCELLED);

        // a cancelled order cannot be cooked again
        checkThrows("cook a CANCELLED order", () -> received.cookOneUnit());
    }

    private static Order newSalad() {
        return new Order(new MenuItem("Salad", 900, 1), 900);
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