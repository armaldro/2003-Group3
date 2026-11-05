package entity;

import java.util.List;

/**
 * The restaurant's menu: the fixed list of dishes a customer can order as well
 * as a custom menu that the caller can create. 
 */
public class Menu {

    private final List<MenuItem> items;

    /**
     * Allows caller to create their own list of menu items
     * stores a copy of the original list
     * <p>e.g. {@code List<MenuItem> items = List.of(lasagna, steak, fries);
     *                Menu customMenu = new Menu(items); }
     * @param items a list of MenuItems
     */
    public Menu(List<MenuItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("custom list cannot be empty");
        }
        this.items = List.copyOf(items);
    }

    /**
     * populate the items list with standard menu items: Salad, Burger and Pasta.
     * (Prices in cents; see section 2.1 of the brief.)
     * @return Menu made up of standard items
     */
    public static Menu standard() {
        MenuItem salad = new MenuItem("Salad", 900, 1);
        MenuItem burger = new MenuItem("Burger", 1500, 1);
        MenuItem pasta = new MenuItem("Pasta", 1800, 2);

        List<MenuItem> items = List.of(salad, burger, pasta);
        return new Menu(items);
    }

    /**
     * caller receives items, which is a copy of the original list
     * to prevent alteration of original list
     * @return the dishes on the menu, in menu order
     */
    public List<MenuItem> getItems() {
        return items;
    }

    // TODO 5 (later, after talking to Rifqi): who decides what a customer orders?
}