package entity;

import java.util.List;

/**
 * A food critic. Critics are the least patient customers, losing 12
 * satisfaction per waiting turn, and pay full price.
 *
 * <p>Twist: a critic always orders the dish that takes longest to cook, to
 * test the kitchen. With the standard menu that is Pasta.
 */
public class CriticCustomer extends Customer {

    /**
     * Creates a critic who has just arrived.
     *
     * @param id the customer's number
     */
    public CriticCustomer(int id) {
        super(id);
    }

    /**
     * Returns the satisfaction a critic loses each turn they wait unserved.
     *
     * @return 12
     */
    @Override
    public int getSatisfactionLoss() {
        return 12;
    }

    /**
     * Chooses the menu item with the most preparation units. If two items
     * tie, the one listed first on the menu wins, so runs are reproducible.
     * With the standard menu this is always Pasta (2 units).
     *
     * @param menu the menu to choose from
     * @return the slowest item to cook
     */
    @Override
    public MenuItem chooseItem(Menu menu) {
        List<MenuItem> items = menu.getItems();
        MenuItem slowest = items.get(0);
        for (MenuItem item : items) {
            if (item.getPrepUnits() > slowest.getPrepUnits()) {
                slowest = item;
            }
        }
        return slowest;
    }
  
    /**
     * Returns the name of this customer type, for display.
     *
     * @return "Critic"
     */
    @Override
    public String getTypeName() {
        return "Critic";
    }
}