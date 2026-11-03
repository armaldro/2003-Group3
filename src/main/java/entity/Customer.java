package entity;

import enums.CustomerState;

/**
 * Customer who visits the restaurant, this tracks thier satisfaction , id, and state.
 */
public abstract class Customer {

    private int id;
    private int satisfaction;
    private CustomerState state;
    private Table table;
    private Order order;
    private int servedTurn;
    private int readyToPayTurn;
}