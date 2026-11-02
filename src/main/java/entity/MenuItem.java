package entity;

/**
 * A dish on the menu: a name, a price in cents and a number of preparation
 * units the Chef must spend on it.
 *
 * <p>Stage 3 adds {@code ComboMeal}, which IS-A MenuItem (two components,
 * summed units, 90% price). Keep fields {@code private} and expose getters so
 * a subclass can compute its values and pass them to {@code super(...)}.
 *
 * <p>Design question for you: do Salad/Burger/Pasta need their own subclasses,
 * or are they just three MenuItem objects with different data? (Hint: they
 * differ only in data, not behaviour.)
 */
public class MenuItem {

    private final String name;
    private final int priceCents;
    private final int prepUnits;

    /**
     * @param name       display name
     * @param priceCents base price in cents
     * @param prepUnits  preparation units required (at least 1)
     */
    public MenuItem(String name, int priceCents, int prepUnits) {
        // TODO (Danial): validate arguments (non-empty name, price > 0,
        // prepUnits >= 1) and throw IllegalArgumentException otherwise.
        this.name = name;
        this.priceCents = priceCents;
        this.prepUnits = prepUnits;
    }

    /** @return display name */
    public String getName() {
        return name;
    }

    /** @return base price in cents */
    public int getPriceCents() {
        return priceCents;
    }

    /** @return preparation units required */
    public int getPrepUnits() {
        return prepUnits;
    }

    @Override
    public String toString() {
        // TODO (Danial): e.g. "Pasta ($18.00, 2 units)" using util.Money.
        return name;
    }
}
