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

}