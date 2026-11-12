package entity;

import java.util.List;

import enums.CustomerState;

/**
 * Customer who visits the restaurant, this tracks their satisfaction , id, and
 * state.
 */
public abstract class Customer {

    private int id;
    private int satisfaction;
    private CustomerState state;
    private Table table;
    private Order order;
    private int servedTurn;
    private int readyToPayTurn;

    /**
     * This creates a customer who just arrived and is waiting for a table.
     * 
     * @param id
     */
    public Customer(int id) {
        this.id = id;
        this.satisfaction = 100;
        this.state = CustomerState.WAITING;
    }

    /**
     * Returns the customer id
     * 
     * @return the customer id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the customer satisfaction
     * 
     * @return the customer satisfaction
     */
    public int getSatisfaction() {
        return satisfaction;
    }

    /**
     * Returns the customer state
     * 
     * @return the customer state
     */
    public CustomerState getState() {
        return state;
    }

    /**
     * Returns the table the customer is seated at
     * 
     * @return the customers table
     */
    public Table getTable() {
        return table;
    }

    /**
     * Returns the order the customer placed
     * 
     * @return the order of the customer
     */
    public Order getOrder() {
        return order;
    }

    /**
     * Returns the turn the customer got served
     * 
     * @return the turn customer is served
     */
    public int getServedTurn() {
        return servedTurn;
    }

    /**
     * Returns the turn the customer is ready to pay
     * 
     * @return the turn customer is ready to pay
     */
    public int getReadyToPayTurn() {
        return readyToPayTurn;
    }

    /**
     * Returns the amount of satisfaction lost each turn of not being served
     * 
     * @return the amount of satisfaction lost.
     */
    public abstract int getSatisfactionLoss();

    /**
     * Reduces the customer's satisfaction by the loss per turn
     * never go below 0
     */
    public void loseSatisfaction() {
        satisfaction = satisfaction - getSatisfactionLoss();
        if (satisfaction < 0) {
            satisfaction = 0;
        }
    }

    /**
     * Seats the customers in the given table
     * 
     * @param table the table the customer is seated in
     */
    public void seat(Table table) {
        this.table = table;
        this.state = CustomerState.SEATED;
    }

    /**
     * Records the order placed by the customer
     * 
     * @param order the food the customer ordered
     */
    public void placeOrder(Order order) {
        this.order = order;
        this.state = CustomerState.ORDERED;
    }

    /**
     * Records that the customer has been served and is eating
     * 
     * @param turn the turn the customer was served
     */
    public void markServed(int turn) {
        this.servedTurn = turn;
        this.state = CustomerState.EATING;
    }

    /**
     * Records that the customer have finished eating and is ready to pay
     * 
     * @param turn the turn the customer is ready to pay
     */
    public void markReadyToPay(int turn) {
        this.readyToPayTurn = turn;
        this.state = CustomerState.READY_TO_PAY;
    }

    /**
     * Records that the customer has paid
     */
    public void markPaid() {
        this.state = CustomerState.PAID;
    }

    /**
     * Records that the customer has left
     */
    public void leave() {
        this.state = CustomerState.LEFT;
    }

    /**
     * Returns the price the customer have to pay
     * 
     * @param priceCents the standard price of the food
     * @return the price of the order
     */
    public int applyDiscount(int priceCents) {
        return priceCents;
    }

    /**
     * Chooses this customer's dish from the menu using a fixed rule, so runs
     * can be reproduced: customer n orders item (n - 1) mod the menu size.
     * With the standard menu, customer 1 orders Salad, customer 2 Burger,
     * customer 3 Pasta, customer 4 Salad again, and so on.
     *
     * @param menu the menu to choose from
     * @return the menu item this customer orders
     */
    public MenuItem chooseItem(Menu menu) {
        List<MenuItem> items = menu.getItems();
        return items.get((id - 1) % items.size());
    }
}