import java.util.Scanner;

import boundary.InputReader;
import entity.Customer;
import entity.Menu;
import entity.MenuItem;
import entity.Order;
import entity.RegularCustomer;
import entity.Restaurant;
import entity.Table;
import entity.Waiter;
import enums.CustomerState;
import enums.OrderStatus;
import util.InvalidActionException;

/**
 * Checks Kenz's Stage 1 classes: Table, Staff, Waiter, Restaurant and InputReader.
 * Run from the repo root:
 * <pre>
 * javac -d out $(find src/main/java -name "*.java") testing/WaiterCheck.java
 * java -cp out WaiterCheck
 * </pre>
 */
public class WaiterCheck {

    private static int failures = 0;

    /** An action that may throw a checked exception. */
    private interface Action {
        void run() throws Exception;
    }

    public static void main(String[] args) {
        run("Table", WaiterCheck::tableChecks);
        run("Restaurant", WaiterCheck::restaurantChecks);
        run("Waiter: seat", WaiterCheck::seatChecks);
        run("Waiter: take order and serve", WaiterCheck::orderAndServeChecks);
        run("InputReader", WaiterCheck::inputChecks);

        System.out.println(failures == 0 ? "\nAll checks passed." : "\n" + failures + " check(s) FAILED.");
        if (failures > 0) {
            System.exit(1);
        }
    }

    private static void tableChecks() {
        Table t = new Table(1);
        report(t.isFree(), "a new table is free");
        Customer c = new RegularCustomer(1);
        t.seat(c);
        report(!t.isFree() && t.getOccupant() == c, "after seat(), the table holds that customer");
        expect("seat at an occupied table", IllegalStateException.class, () -> t.seat(new RegularCustomer(2)));
        t.free();
        report(t.isFree(), "free() empties the table");
    }

    private static void restaurantChecks() {
        Restaurant r = new Restaurant(2, Menu.standard());
        report(r.getTables().size() == 2, "2 tables are created");
        report(r.freeTables().size() == 2, "both tables start free");
        Customer c = new RegularCustomer(1);
        r.addArrival(c);
        report(r.getWaitingQueue().size() == 1, "an arrival joins the waiting queue");
        report(r.getCustomers().size() == 1, "an arrival is recorded in customers");
    }

    private static void seatChecks() throws Exception {
        Restaurant r = new Restaurant(2, Menu.standard());
        Waiter w = r.getWaiter();
        Customer c1 = new RegularCustomer(1);
        Customer c2 = new RegularCustomer(2);
        r.addArrival(c1);
        r.addArrival(c2);
        Table t1 = r.getTables().get(0);
        w.startTurn();

        w.seat(r, c1, t1);
        report(c1.getState() == CustomerState.SEATED, "seated customer is SEATED");
        report(c1.getTable() == t1 && t1.getOccupant() == c1, "customer and table point at each other");
        report(r.getWaitingQueue().size() == 1, "seated customer left the queue");
        report(w.hasActed(), "a successful seat uses up the Waiter's task");

        expect("a second action in the same turn", InvalidActionException.class, () -> w.waitTurn());

        w.startTurn();
        expect("seat at an occupied table", InvalidActionException.class, () -> w.seat(r, c2, t1));
        report(!w.hasActed(), "an invalid choice does NOT use up the task");
        expect("seat someone who is not in the queue", InvalidActionException.class,
                () -> w.seat(r, c1, r.getTables().get(1)));
    }

    private static void orderAndServeChecks() throws Exception {
        Restaurant r = new Restaurant(2, Menu.standard());
        Waiter w = r.getWaiter();
        Customer c = new RegularCustomer(1);
        r.addArrival(c);
        MenuItem pasta = new MenuItem("Pasta", 1800, 2);

        w.startTurn();
        expect("take the order of a customer still in the queue", InvalidActionException.class,
                () -> w.takeOrder(c, new Order(pasta, 1800)));

        w.seat(r, c, r.getTables().get(0));             // turn 1
        report(r.customersToTakeOrder().contains(c), "a SEATED customer is listed for ordering");

        w.startTurn();                                   // turn 2
        w.takeOrder(c, new Order(pasta, 1800));
        report(c.getState() == CustomerState.ORDERED, "after takeOrder() the customer is ORDERED");
        report(r.ordersToCook().size() == 1, "the new order is listed for the Chef");
        r.getChef().cookOneUnit(c.getOrder());          // Chef acts after the Waiter

        w.startTurn();                                   // turn 3
        expect("serve a dish that is not READY yet", InvalidActionException.class, () -> w.serve(c, 3));
        report(!w.hasActed(), "the failed serve did not use up the task");
        w.waitTurn();
        r.getChef().cookOneUnit(c.getOrder());          // unit 2 of 2: READY after the Waiter acted
        report(r.customersToServe().contains(c), "a customer with a READY order is listed for serving");

        w.startTurn();                                   // turn 4
        w.serve(c, 4);
        report(c.getState() == CustomerState.EATING, "served customer is EATING");
        report(c.getOrder().getStatus() == OrderStatus.SERVED, "served order is SERVED");
        report(c.getServedTurn() == 4, "the serve turn is recorded");
    }

    private static void inputChecks() {
        InputReader in = new InputReader(new Scanner("abc\n9\n2\n"));
        int choice = in.readChoice("Pick 1-4: ", 1, 4);
        report(choice == 2, "readChoice skips 'abc' and 9, and returns 2 (got " + choice + ")");
        InputReader empty = new InputReader(new Scanner(""));
        expect("input runs out", IllegalStateException.class, () -> empty.readChoice("Pick: ", 1, 4));
    }

    // ----- helpers -----

    private static void run(String group, Action checks) {
        System.out.println("\n== " + group);
        try {
            checks.run();
        } catch (UnsupportedOperationException e) {
            report(false, "not written yet: " + e.getMessage());
        } catch (Exception e) {
            report(false, "unexpected " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private static void expect(String label, Class<? extends Exception> type, Action action) {
        try {
            action.run();
            report(false, label + " (no exception thrown)");
        } catch (UnsupportedOperationException e) {
            report(false, label + " (not written yet: " + e.getMessage() + ")");
        } catch (Exception e) {
            boolean ok = type.isInstance(e);
            report(ok, label + (ok ? " is refused: " + e.getMessage()
                    : " threw " + e.getClass().getSimpleName() + ", expected " + type.getSimpleName()));
        }
    }

    private static void report(boolean passed, String label) {
        if (!passed) {
            failures++;
        }
        System.out.println((passed ? "OK   " : "FAIL ") + label);
    }
}
