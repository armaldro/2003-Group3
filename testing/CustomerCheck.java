import entity.Customer;
import entity.Menu;
import entity.MenuItem;
import entity.Order;
import entity.RegularCustomer;
import entity.Table;
import enums.CustomerState;
 
/**
 * Checks the shared Customer behaviour, using a RegularCustomer: starting
 * values, satisfaction loss, the state changes, full price and the food rule.
 */
public class CustomerCheck {
 
    private static int failures = 0;
 
    public static void main(String[] args) {
        // a new customer starts at 100, WAITING
        Customer customer = new RegularCustomer(1);
        check("id is 1", customer.getId(), 1);
        check("starts at satisfaction 100", customer.getSatisfaction(), 100);
        check("starts WAITING", customer.getState(), CustomerState.WAITING);
 
        // a Regular loses 8 per waiting turn
        check("Regular loses 8 per turn", customer.getSatisfactionLoss(), 8);
        customer.loseSatisfaction();
        check("100 - 8 = 92", customer.getSatisfaction(), 92);
 
        // satisfaction never goes below 0 (13 turns x 8 = 104)
        Customer patient = new RegularCustomer(2);
        for (int i = 0; i < 13; i++) {
            patient.loseSatisfaction();
        }
        check("satisfaction stops at 0", patient.getSatisfaction(), 0);
 
        // the state changes, in game order
        Table table = new Table(1);
        customer.seat(table);
        check("seat() -> SEATED", customer.getState(), CustomerState.SEATED);
        check("seat() records the table", customer.getTable(), table);
 
        Order order = new Order(new MenuItem("Burger", 1500, 1), 1500);
        customer.placeOrder(order);
        check("placeOrder() -> ORDERED", customer.getState(), CustomerState.ORDERED);
        check("placeOrder() records the order", customer.getOrder(), order);
 
        customer.markServed(5);
        check("markServed() -> EATING", customer.getState(), CustomerState.EATING);
        check("markServed() records turn 5", customer.getServedTurn(), 5);
 
        customer.markReadyToPay(6);
        check("markReadyToPay() -> READY_TO_PAY", customer.getState(), CustomerState.READY_TO_PAY);
        check("markReadyToPay() records turn 6", customer.getReadyToPayTurn(), 6);
        check("markReadyToPay() keeps the served turn", customer.getServedTurn(), 5);
 
        customer.markPaid();
        check("markPaid() -> PAID", customer.getState(), CustomerState.PAID);
 
        patient.leave();
        check("leave() -> LEFT", patient.getState(), CustomerState.LEFT);
 
        // a Regular pays full price
        check("Regular pays full price for a $15 Burger", customer.applyDiscount(1500), 1500);
 
        // food rule: customer n orders item (n - 1) mod menu size
        Menu menu = Menu.standard();
        String[] expected = {"Salad", "Burger", "Pasta", "Salad", "Burger", "Pasta"};
        for (int id = 1; id <= 6; id++) {
            String chosen = new RegularCustomer(id).chooseItem(menu).getName();
            check("customer " + id + " orders " + expected[id - 1], chosen, expected[id - 1]);
        }
 
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