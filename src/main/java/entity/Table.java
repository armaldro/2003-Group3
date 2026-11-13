package entity;

/**
 * One table in the restaurant. A table holds at most one customer and stays
 * occupied until that customer pays or leaves.
 *
 * <p>Only {@link Restaurant#seat(Customer, Table)} should call {@link #seat(Customer)},
 * so that the table and the customer always agree about who sits where.
 */
public class Table {

    private final int number;
    private Customer occupant;

    /**
     * Creates an empty table.
     *
     * @param number the table number shown to the manager, starting at 1
     * @throws IllegalArgumentException if number is less than 1
     */
    public Table(int number) {
        if (number < 1) {
            throw new IllegalArgumentException("table number must be at least 1");
        }
        this.number = number;
        this.occupant = null;
    }

    /**
     * Returns the table number.
     *
     * @return the table number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Returns the customer at this table, or null if the table is free.
     *
     * @return the customer at this table, or null if the table is free
     */
    public Customer getOccupant() {
        return occupant;
    }

    /**
     * Returns true if nobody is sitting at this table.
     *
     * @return true if nobody is sitting at this table
     */
    public boolean isFree() {
        return occupant == null;
    }

    /**
     * Puts a customer at this table.
     *
     * @param customer the customer to seat
     * @throws IllegalArgumentException if customer is null
     * @throws IllegalStateException if the table is already occupied
     */
    public void seat(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("customer cannot be null");
        }
        if (!isFree()) {
            throw new IllegalStateException("table " + number + " is already occupied");
        }
        occupant = customer;
    }

    /**
     * Frees the table, called when its customer pays or leaves.
     * Freeing a table that is already free does nothing.
     */
    public void free() {
        occupant = null;
    }
}
