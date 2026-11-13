package entity;

/**
 * 
 * RegularCustomer losses 8 satisfaction per turn
 */

public class RegularCustomer extends Customer {

    /**
     * Creates a regular customer
     * 
     * @param id the customer's number
     */

    public RegularCustomer(int id) {
        super(id);
    }

    @Override
    public int getSatisfactionLoss() {
        return 8;
    }
    
    /**
     * Returns the name of this customer type, for display.
     *
     * @return "Regular"
     */
    @Override
    public String getTypeName() {
        return "Regular";
    }
}