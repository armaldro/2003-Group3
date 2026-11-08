package entity;

import java.util.ArrayList;
import java.util.List;

import enums.CustomerState;
import enums.OrderStatus;
import util.InvalidActionException;

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
        // TODO 14: add tableCount new Table objects, numbered from 1
    }

    /**
     * A new customer arrives: they join the end of the waiting queue.
     *
     * @param customer the customer who arrived
     */
    public void addArrival(Customer customer) {
        // TODO 15: add to waitingQueue AND to customers (customers keeps everyone, for the end-of-game score)
        throw new UnsupportedOperationException("TODO: Restaurant.addArrival");
    }

    /**
     * Seats a queued customer at a free table. This is the only method that
     * seats anyone, so the Waiter (and the Host in Stage 3) both call it.
     *
     * @param customer the customer to seat
     * @param table    the table to use
     * @throws InvalidActionException if the customer is not waiting in the queue, or the table is occupied
     */
    public void seat(Customer customer, Table table) throws InvalidActionException {
        // TODO 16: check the customer is in waitingQueue; if not, throw with a reason
        // TODO 17: check table.isFree(); if not, throw with a reason
        // TODO 18: link both sides: table.seat(customer) and customer.seat(table)
        // TODO 19: remove the customer from waitingQueue
        throw new UnsupportedOperationException("TODO: Restaurant.seat");
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

    /** @return the tables nobody is sitting at */
    public List<Table> freeTables() {
        // TODO 20: loop over tables, keep the ones where isFree() is true
        return new ArrayList<>();
    }

    /** @return seated customers who have not ordered yet (state SEATED) */
    public List<Customer> customersToTakeOrder() {
        // TODO 21
        return new ArrayList<>();
    }

    /** @return customers whose order is READY to be served */
    public List<Customer> customersToServe() {
        // TODO 22: state ORDERED and getOrder().getStatus() == OrderStatus.READY
        return new ArrayList<>();
    }

    /** @return orders the Chef can work on (status RECEIVED or NOT_READY) */
    public List<Order> ordersToCook() {
        // TODO 23: look through customers who are ORDERED
        return new ArrayList<>();
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
