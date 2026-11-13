package entity;

import util.Money;


/**
 * A combo meal: two existing menu items sold together at a discount.
 *
 * <p>Brief (Stage 3): preparation units = the sum of the two components, and
 * the price = 90% of the components' combined price, rounded to the nearest
 * cent. Example: Salad ($9.00) + Pasta ($18.00) = $24.30 and 3 units.
 */
public class ComboMeal extends MenuItem {

    // The two dishes this combo is made of. Never change, so fields are final.
    private final MenuItem first;
    private final MenuItem second;

    /**
     * Creates a combo from two menu items.
     * nameOf runs first and validates both menu items, so other helpers do not need it
     * @param first  the first component
     * @param second the second component
     * @throws IllegalArgumentException if a component is null
     */
    public ComboMeal(MenuItem first, MenuItem second) {
        // The parent constructor must be the FIRST statement, so the name,
        // price and units are worked out by small static helper methods below.
        super(nameOf(first, second), priceOf(first, second), unitsOf(first, second));
        this.first = first;
        this.second = second;
    }

    /** 
     * returns the first menu item
     * @return the first component */
    public MenuItem getFirst() {
        return first;
    }

    /** 
     * returns the second menu item 
     * @return the second component */
    public MenuItem getSecond() {
        return second;
    }

    // ----- helpers used by the constructor -----

    /**
     * nameOf(): returns String name of combo meal consisting of two menu items
     * @param first first menu item object in the combo meal
     * @param second second menu item object in the combo meal
     * @return String name of the combo meal.
     * @throws IllegalArgumentException if the first item is null
     * @throws IllegalArgumentException if the second item is null
     * @throws IllegalArgumentException if argument is a ComboMeal item 
     */
    private static String nameOf(MenuItem first, MenuItem second) {
        
        if (first == null) {
            throw new IllegalArgumentException("first menu item cannot be null");
        }
        if (second == null) {
            throw new IllegalArgumentException("second menu item cannot be null");
        }
        if (first instanceof ComboMeal || second instanceof ComboMeal) {
            throw new IllegalArgumentException("component cannot be part of a combo meal");
        }
        
        String name = first.getName() + " + " + second.getName();
        return name;
    }

    /**
     *  priceOf(): returns an int of 90% of the prices of both menu items added together
     * e.g. 90% of (500 cents + 600 cents)
     * components are assumed valid
     * @param first first menu item object in the combo meal
     * @param second second menu item object in the combo meal
     * @return returns an int of 90% of the price of the two menu items added together
     */
    private static int priceOf(MenuItem first, MenuItem second) {
        
        int comboPrice = Money.percentOf((first.getPriceCents() + second.getPriceCents()), 90);
        return comboPrice;
    }

    /**
     * unitsOf(): returns an int of the total prep units needed to cook both menu items
     * components are assumed to be valid
     * @param first the first menu item object in the combo meal
     * @param second the second menu item object in the combo meal
     * @return an int of the number of prep units needed to cook both menu items
     */
    private static int unitsOf(MenuItem first, MenuItem second) {

        //the two preparation-unit counts added together.
        return first.getPrepUnits() + second.getPrepUnits();
    }

}