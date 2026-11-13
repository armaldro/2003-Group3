import entity.Cashier;
import entity.Customer;
import entity.MenuItem;
import entity.Order;
import entity.RegularCustomer;
import enums.CustomerState;
import enums.OrderStatus;
import java.util.ArrayList;
import java.util.List;

public class CashierCheck {

    private static int failures = 0;

    public static void main(String[] args) {
        Cashier cashier = new Cashier();

        // nobody can pay: an empty list, and a customer who is only WAITING
        Customer waiting = new RegularCustomer(1);
        check("empty list -> nobody pays",
                cashier.collectPayment(new ArrayList<>(), 5) == null);
        check("only a WAITING customer -> nobody pays",
                cashier.collectPayment(listOf(waiting), 5) == null);

        // became ready THIS turn: cannot pay yet, and nothing changes
        Customer readyNow = readyToPayCustomer(2, 5);
        check("ready THIS turn -> nobody pays",
                cashier.collectPayment(listOf(readyNow), 5) == null);
        check("  ...and the customer is still READY_TO_PAY",
                readyNow.getState() == CustomerState.READY_TO_PAY);

        // became ready on an earlier turn: pays, and both customer and order are PAID
        Customer readyEarlier = readyToPayCustomer(3, 5);
        Customer paid = cashier.collectPayment(listOf(readyEarlier), 6);
        check("ready on an earlier turn -> that customer pays", paid == readyEarlier);
        check("  ...customer is PAID", readyEarlier.getState() == CustomerState.PAID);
        check("  ...order is PAID", readyEarlier.getOrder().getStatus() == OrderStatus.PAID);

        // an ineligible customer earlier in the list must not block an eligible one
        Customer eating = new RegularCustomer(4);
        Customer eligible = readyToPayCustomer(5, 5);
        check("ineligible first does not block eligible second",
                cashier.collectPayment(listOf(eating, eligible), 6) == eligible);

        // oldest first, and only one customer pays per call
        Customer newer = readyToPayCustomer(6, 7);
        Customer older = readyToPayCustomer(7, 5);
        Customer oldestPaid = cashier.collectPayment(listOf(newer, older), 8);
        check("two ready -> the OLDEST pays", oldestPaid == older);
        check("  ...the other is still READY_TO_PAY", newer.getState() == CustomerState.READY_TO_PAY);

        // tie-break: same turn, so the one earlier in the list pays
        Customer first = readyToPayCustomer(8, 5);
        Customer second = readyToPayCustomer(9, 5);
        check("same turn -> the first in the list pays",
                cashier.collectPayment(listOf(first, second), 6) == first);

        System.out.println(failures == 0 ? "\nAll checks passed." : "\n" + failures + " check(s) FAILED.");
        if (failures > 0) {
            System.exit(1);
        }
    }

    /**
     * Builds a customer who is READY_TO_PAY from the given turn. Follows the
     * real flow: order cooked and served, then the customer eats.
     */
    private static Customer readyToPayCustomer(int id, int readyToPayTurn) {
        Customer customer = new RegularCustomer(id);
        Order order = new Order(new MenuItem("Burger", 1500, 1), 1500);
        order.cookOneUnit();
        order.markServed();
        customer.placeOrder(order);
        customer.markServed(readyToPayTurn - 1);
        customer.markReadyToPay(readyToPayTurn);
        return customer;
    }

    private static List<Customer> listOf(Customer... customers) {
        return new ArrayList<>(List.of(customers));
    }

    private static void check(String label, boolean passed) {
        if (!passed) {
            failures++;
        }
        System.out.println((passed ? "OK   " : "FAIL ") + label);
    }
}