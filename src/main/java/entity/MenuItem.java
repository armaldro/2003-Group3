package entity;

/**
 * entity class to create menu items
 */
public class MenuItem {

    // fields declared when creating a menuItem

    /** a field to store the name of the dish */
    private final String name;

    /** a field to store the price of the dish in cents */
    private final int priceCents;

    /** a field to store units of chef work needed to prep the dish */
    private final int prepUnits;

    /**
     * a constructor for MenuItem. Takes in the name of the dish, the price as well as how much work is needed to prepare it
     * 
     * @param name the name of the item
     * @param priceCents the price of the item in cents
     * @param prepUnits units of Chef work needed to prepare the dish
     * @throws IllegalArgumentException if name is null or blank, price is negative or 0, or prepUnits is less than 1
     */
    public MenuItem(String name, int priceCents, int prepUnits) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be empty");
        }
        if (priceCents <= 0) {
            throw new IllegalArgumentException("price cannot be negative or 0");
        }
        if (prepUnits < 1) {
            throw new IllegalArgumentException("must have at least 1 prep unit");
        }

        this.name = name;
        this.priceCents = priceCents;
        this.prepUnits = prepUnits;
    }
    
    /**
     * returns the name of the dish
     * 
     * @return the name of the dish
     */
    public String getName() {
        return name;
    }

    /**
     * returns the price of the dish
     * 
     * @return the price of the dish in cents
     */
    public int getPriceCents() {
        return priceCents;
    }

    /**
     * returns the unit of chef work needed to prep the dish
     * 
     * @return the unit of chef work needed to prep the dish
     */
    public int getPrepUnits() {
        return prepUnits;
    }
}
