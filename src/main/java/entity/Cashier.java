package entity;

import enums.CustomerState;
import java.util.List;

/**
 * The cashier: takes payment from customers who are ready to pay.
 *
 * <p>At most ONE payment per turn, the customer who has waited longest
 * (oldest first), and a customer who became READY_TO_PAY this turn pays next
 * turn at the earliest.
 */
public class Cashier {

    /**
     * Collects payment from at most one customer this turn.
     *
     * <p>If two customers became ready on the same turn, the one earlier in
     * the list pays first.
     *
     * @param customers   every customer in the restaurant
     * @param currentTurn the turn number now (1 to 18)
     * @return the customer who paid, or null if nobody could pay this turn
     */
    public Customer collectPayment(List<Customer> customers, int currentTurn) {
        Customer oldest = null;

        for (Customer customer : customers) {
            boolean readyToPay = customer.getState() == CustomerState.READY_TO_PAY;
            boolean readyBeforeThisTurn = customer.getReadyToPayTurn() < currentTurn;

            if (!readyToPay || !readyBeforeThisTurn) {
                continue;   // not eligible: skip, keep looking
            }
            if (oldest == null || customer.getReadyToPayTurn() < oldest.getReadyToPayTurn()) {
                oldest = customer;
            }
        }

        if (oldest == null) {
            return null;
        }

        oldest.getOrder().markPaid();
        oldest.markPaid();
        return oldest;
    }
}