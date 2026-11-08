package entity;

import enums.CustomerState;
import enums.OrderStatus;
import util.InvalidActionException;

import java.util.ArrayList;
import java.util.List;

/**
 * The restaurant: its tables, the waiting queue, every customer who has arrived,
 * the menu and the staff.
 *
 * <p>It answers questions the controller and the console need, such as
 * "which tables are free?", and it is the only place that seats a customer.
 */
public class Restaurant {

    private final List<Table> tables = new ArrayList<>();
    private final List<Customer> waitingQueue = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();
    private final Menu menu;
    private final Waiter waiter = new Waiter();
    private final Chef chef = new Chef();
    // TODO (when Danial pushes Cashier): add the Cashier here, plus getCashier()

    /**
     * Creates a restaurant with empty tables numbered 1 to tableCount.
     *
     * @param tableCount how many tables (2 in the base setting, from GameConfig.getTables())
     * @param menu       the menu, e.g. Menu.standard()
     */
    public Restaurant(int tableCount, Menu menu) {
        this.menu = menu;
        for (int i = 1; i <= tableCount; i++) {
            tables.add(new Table(i));
        }
    }

    /**
     * A new customer arrives: they join the end of the waiting queue.
     *
     * @param customer the customer who arrived
     */
    public void addArrival(Customer customer) {
        waitingQueue.add(customer);
        customers.add(customer);
    }

    /**
     * Seats a queued customer at a free table. This is the only method that
     * seats anyone, so the Waiter or future Host both call it.
     *
     * <p>Every rule is checked before anything changes, so a refused seating
     * leaves the queue, the table and the customer exactly as they were.
     *
     * @param customer the customer to seat
     * @param table    the table to use
     * @throws InvalidActionException if the customer is not waiting in the queue,
     *                                no table is given, or the table is occupied
     */
    public void seat(Customer customer, Table table) throws InvalidActionException {
        if (!waitingQueue.contains(customer)) {
            throw new InvalidActionException("That customer is not waiting for a table.");
        }
        if (table == null) {
            throw new InvalidActionException("Choose a table to seat the customer at.");
        }
        if (!table.isFree()) {
            throw new InvalidActionException("Table " + table.getNumber() + " is already occupied by customer "
                    + table.getOccupant().getId() + ".");
        }

        table.seat(customer);
        customer.seat(table);
        waitingQueue.remove(customer);
    }

    /**
     * Removes a customer from the queue, called when a waiting customer gives up and leaves.
     *
     * @param customer the customer who left
     */
    public void removeFromQueue(Customer customer) {
        waitingQueue.remove(customer);
    }

    /** @return the tables, in number order (read-only) */
    public List<Table> getTables() {
        return List.copyOf(tables);
    }

    /** @return the customers waiting for a table, first arrival first (read-only) */
    public List<Customer> getWaitingQueue() {
        return List.copyOf(waitingQueue);
    }

    /** @return every customer who has arrived so far, in arrival order (read-only) */
    public List<Customer> getCustomers() {
        return List.copyOf(customers);
    }

    /** @return the tables nobody is sitting at, in number order */
    public List<Table> freeTables() {
        List<Table> result = new ArrayList<>();
        for (Table table : tables) {
            if (table.isFree()) {
                result.add(table);
            }
        }
        return result;
    }

    /** @return seated customers who have not ordered yet (state SEATED), in arrival order */
    public List<Customer> customersToTakeOrder() {
        List<Customer> result = new ArrayList<>();
        for (Customer customer : customers) {
            if (customer.getState() == CustomerState.SEATED) {
                result.add(customer);
            }
        }
        return result;
    }

    /** @return customers whose order is READY to be served, in arrival order */
    public List<Customer> customersToServe() {
        List<Customer> result = new ArrayList<>();
        for (Customer customer : customers) {
            if (customer.getState() == CustomerState.ORDERED
                    && customer.getOrder().getStatus() == OrderStatus.READY) {
                result.add(customer);
            }
        }
        return result;
    }

    /** @return orders the Chef can work on (status RECEIVED or NOT_READY), in arrival order */
    public List<Order> ordersToCook() {
        List<Order> result = new ArrayList<>();
        for (Customer customer : customers) {
            if (customer.getState() != CustomerState.ORDERED) {
                continue;
            }
            OrderStatus status = customer.getOrder().getStatus();
            if (status == OrderStatus.RECEIVED || status == OrderStatus.NOT_READY) {
                result.add(customer.getOrder());
            }
        }
        return result;
    }

    /** @return the menu */
    public Menu getMenu() {
        return menu;
    }

    /** @return the Waiter */
    public Waiter getWaiter() {
        return waiter;
    }

    /** @return the Chef */
    public Chef getChef() {
        return chef;
    }
}