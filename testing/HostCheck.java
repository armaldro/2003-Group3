import entity.Customer;
import entity.Host;
import entity.Menu;
import entity.RegularCustomer;
import entity.Restaurant;
import entity.Table;
import entity.Waiter;
import enums.CustomerState;
import util.InvalidActionException;

/**
 * Checks the Stage 3 Host: it seats at most one queued customer per turn, on
 * top of the Waiter's task, through the same seating rules as the Waiter.
 * Run from the repo root:
 * <pre>
 * javac -d out $(find src/main/java -name "*.java") testing/HostCheck.java
 * java -cp out HostCheck
 * </pre>
 */
public class HostCheck {

    private static int failures = 0;

    /** An action that may throw a checked exception. */
    private interface Action {
        void run() throws Exception;
    }

    public static void main(String[] args) {
        try {
            hostChecks();
            hostAndWaiterChecks();
        } catch (Exception e) {
            report(false, "unexpected " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        System.out.println(failures == 0 ? "\nAll checks passed." : "\n" + failures + " check(s) FAILED.");
        if (failures > 0) {
            System.exit(1);
        }
    }

    private static void hostChecks() throws Exception {
        System.out.println("\n== Host");
        Restaurant r = new Restaurant(2, Menu.standard());
        Host host = r.getHost();
        report(host != null && host == r.getHost(), "the restaurant has one Host");
        report(host.getRole().equals("Host"), "the Host's role is \"Host\"");

        Customer c1 = new RegularCustomer(1);
        Customer c2 = new RegularCustomer(2);
        r.addArrival(c1);
        r.addArrival(c2);
        Table t1 = r.getTables().get(0);
        Table t2 = r.getTables().get(1);

        host.startTurn();
        host.seat(r, c1, t1);
        report(c1.getState() == CustomerState.SEATED && t1.getOccupant() == c1, "the Host seats a queued customer");
        report(r.getWaitingQueue().size() == 1, "the seated customer left the queue");
        report(host.hasActed(), "seating uses up the Host's turn");
        expect("a second seat by the Host in the same turn", InvalidActionException.class,
                () -> host.seat(r, c2, t2));

        host.startTurn();
        expect("the Host seating at an occupied table", InvalidActionException.class,
                () -> host.seat(r, c2, t1));
        report(!host.hasActed(), "a refused seat does NOT use up the Host's turn");
        host.waitTurn();
        report(host.hasActed(), "waiting uses up the Host's turn");
    }

    private static void hostAndWaiterChecks() throws Exception {
        System.out.println("\n== Host and Waiter in one turn");
        Restaurant r = new Restaurant(2, Menu.standard());
        Host host = r.getHost();
        Waiter waiter = r.getWaiter();
        Customer c1 = new RegularCustomer(1);
        Customer c2 = new RegularCustomer(2);
        r.addArrival(c1);
        r.addArrival(c2);

        host.startTurn();
        waiter.startTurn();
        host.seat(r, c1, r.getTables().get(0));
        waiter.seat(r, c2, r.getTables().get(1));
        report(c1.getState() == CustomerState.SEATED && c2.getState() == CustomerState.SEATED,
                "the Host and the Waiter both seat someone: 2 customers seated");
        report(r.getWaitingQueue().isEmpty() && r.freeTables().isEmpty(), "queue empty and both tables taken");
        report(host.hasActed() && waiter.hasActed(), "each used their own turn, not each other's");
    }

    // ----- helpers -----

    private static void expect(String label, Class<? extends Exception> type, Action action) {
        try {
            action.run();
            report(false, label + " (no exception thrown)");
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
