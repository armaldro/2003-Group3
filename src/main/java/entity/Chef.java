package entity;

/**
 * Chef cooks one unit of an order per turn, called by the controller class.
 */
public class Chef {

    /**
     * Method to cook one unit of an Order per call
     * A null order will be caught by Order class so no checks required in this class
     * Order status must be RECEIVED or NOT_READY
     * @param order an order of a menu item to be cooked
     */
    public void cookOneUnit(Order order) {
        order.cookOneUnit();
    }
}