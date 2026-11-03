package entity;

import enums.CustomerState;

/**
 * Customer who visits the restaurant, this tracks thier satisfaction , id, and
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

}