package entity;

import java.util.List;

/**
 * Holds the restaurant's menu. Customers pick from it; nothing else owns the
 * list of dishes.
 *
 * <p>Base menu (brief section 2.1): Salad $9.00 / 1 unit, Burger $15.00 /
 * 1 unit, Pasta $18.00 / 2 units.
 */
public class Menu {

    private final List<MenuItem> items;

    /** Creates the base menu. */
    public Menu() {
        // TODO (Danial): create the three items and store them
        // (List.of(...) is fine; the list should not be modifiable from outside).
        this.items = List.of();
    }

    /** @return the dishes in menu order (unmodifiable) */
    public List<MenuItem> getItems() {
        return items;
    }

    /**
     * Picks a dish for the n-th customer using a documented deterministic rule
     * (brief section 2.2: customers choose, the manager never does).
     *
     * @param customerNumber 1-based arrival number of the customer
     * @return the dish this customer orders
     */
    public MenuItem choiceFor(int customerNumber) {
        // TODO (Danial / Rifqi): decide WHO owns this rule. Suggestion from
        // the team's notes: customer n orders item (n - 1) mod 3. Note that
        // Stage 2 wants a Critic with a different preference, which hints the
        // choice may belong on the Customer instead. Agree with Rifqi first.
        throw new UnsupportedOperationException("TODO");
    }
}
