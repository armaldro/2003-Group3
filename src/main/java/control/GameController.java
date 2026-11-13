package control;

import java.util.ArrayList;
import java.util.List;

import boundary.ConsoleUI;
import boundary.InputReader;
import entity.Customer;
import entity.MenuItem;
import entity.Order;
import entity.Restaurant;
import entity.Table;
import enums.CustomerState;
import enums.PricingPolicy;
import enums.WaiterTask;
import util.InvalidActionException;

/**
 * Runs a game of Restaurant Rush: owns the game state and the turn sequence.
 *
 * <p>Each turn follows the order set out in the README: arrivals, the
 * restaurant state, the Happy Hour offer and recovery, the Host, the Waiter,
 * the Chef (always after the Waiter), eating and payment, then waiting and
 * abandonment. After the last turn the result is
 * Victory if every target in {@link GameConfig} is met, otherwise Defeat.
 *
 * <p>This class never prints or reads the console itself. It shows things
 * through {@link ConsoleUI} and reads choices through {@link InputReader}.
 *
 * @author CT2003 Group 3
 */
public class GameController {

    private final ConsoleUI ui;
    private final InputReader input;
    private final GameConfig config;
    private final Restaurant restaurant;
    private final HappyHour happyHour;
    private int nextCustomerId = 1;

    /**
     * Creates a controller for one game, with an empty restaurant that has the
     * number of tables and the menu in the setting, and an unused Happy Hour.
     *
     * @param ui     the console boundary used for all output
     * @param input  reads the manager's numbered choices
     * @param config the game setting to play
     */
    public GameController(ConsoleUI ui, InputReader input, GameConfig config) {
        this.ui = ui;
        this.input = input;
        this.config = config;
        this.restaurant = new Restaurant(config.getTables(), config.getMenu());
        this.happyHour = new HappyHour(config.getHappyHourTurns());
    }

    /**
     * Plays the whole game: every turn in the setting, then the result.
     */
    public void start() {
        ui.showWelcome();
        for (int turn = 1; turn <= config.getTurns(); turn++) {
            playTurn(turn);
        }
        showFinalResult();
    }

    /**
     * Runs one full turn in the order set out in the README.
     *
     * <p>The Waiter always acts before the Chef. So when the Waiter serves, any
     * dish that is READY was finished on an earlier turn, and a dish the Chef
     * finishes this turn can only be served next turn. This is how the rule
     * from TODO 15 in {@code Order} is enforced; no extra field is needed.
     *
     * <p>{@link #start()} is the normal way to play. This method is public so
     * that checks can play one turn at a time.
     *
     * @param turn the turn number, from 1 to {@code config.getTurns()}
     */
    public void playTurn(int turn) {
        restaurant.getWaiter().startTurn();
        restaurant.getHost().startTurn();
        ui.showTurnHeader(turn, config.getTurns());
        handleArrivals(turn);
        ui.showTurnStatus(getRevenueCents(), getPaidCustomers(), getAverageSatisfaction());
        ui.showRestaurantState(restaurant);
        happyHourPhase(turn);
        hostAction();
        waiterAction(turn);
        chefAction();
        eatingAndPayment(turn);
        waitingAndAbandonment();
        ui.showTurnResults(turn, config.getTurns(), getServedCustomers(), getUnhappyDepartures(),
                getRevenueCents(), getPaidCustomers(), getAverageSatisfaction());
    }

    /**
     * Returns how many customers have paid.
     *
     * @return the number of customers in state PAID
     */
    public int getPaidCustomers() {
        return paidCustomers().size();
    }

    /**
     * Returns how many customers have been served so far: those eating, ready
     * to pay or paid.
     *
     * @return the number of customers served
     */
    public int getServedCustomers() {
        int served = 0;
        for (Customer customer : restaurant.getCustomers()) {
            CustomerState state = customer.getState();
            if (state == CustomerState.EATING || state == CustomerState.READY_TO_PAY
                    || state == CustomerState.PAID) {
                served++;
            }
        }
        return served;
    }

    /**
     * Returns how many customers ran out of patience and left without paying.
     *
     * @return the number of unhappy departures
     */
    public int getUnhappyDepartures() {
        int departures = 0;
        for (Customer customer : restaurant.getCustomers()) {
            if (customer.getState() == CustomerState.LEFT) {
                departures++;
            }
        }
        return departures;
    }

    /**
     * Returns the total paid so far: the locked price of every paid order.
     *
     * @return the revenue, in cents
     */
    public int getRevenueCents() {
        int total = 0;
        for (Customer customer : paidCustomers()) {
            total += customer.getOrder().getPriceCents();
        }
        return total;
    }

    /**
     * Returns the average satisfaction of paid customers only. Customers who
     * left or are still in the restaurant do not count.
     *
     * @return the mean satisfaction of paid customers, rounded, or 0 if none paid
     */
    public int getAverageSatisfaction() {
        List<Customer> paid = paidCustomers();
        if (paid.isEmpty()) {
            return 0;
        }
        int total = 0;
        for (Customer customer : paid) {
            total += customer.getSatisfaction();
        }
        return (int) Math.round((double) total / paid.size());
    }

    /**
     * Returns whether every target in the setting is met.
     *
     * @return true for Victory, false for Defeat
     */
    public boolean isVictory() {
        return getPaidCustomers() >= config.getTargetPaidCustomers()
                && getRevenueCents() >= config.getTargetRevenueCents()
                && getAverageSatisfaction() >= config.getTargetAverageSatisfaction();
    }

    /**
     * Returns the restaurant this game is played in, so checks can inspect it.
     *
     * @return the restaurant
     */
    public Restaurant getRestaurant() {
        return restaurant;
    }

    /**
     * Phase 1: every customer the schedule in {@link GameConfig} has due this
     * turn joins the waiting queue. The schedule decides the customer's type, so
     * this method never needs to change when a new type of customer is added.
     * Customer ids start at 1 and go up by one per arrival.
     *
     * @param turn the current turn number
     */
    private void handleArrivals(int turn) {
        for (Arrival arrival : config.arrivalsOn(turn)) {
            Customer customer = arrival.createCustomer(nextCustomerId++);
            restaurant.addArrival(customer);
            ui.showCustomerArrived(customer);
        }
    }

    /**
     * Offers to start Happy Hour while it is still unused, then shows its status.
     * The offer comes after the restaurant state, so the manager can see who is
     * waiting before deciding. Happy Hour lasts the setting's number of turns,
     * starting with this one. The discount shown is read from
     * {@link PricingPolicy}, so it is written in one place only.
     *
     * <p>On every Happy Hour turn, each customer still waiting for a table or
     * food recovers the setting's recovery points here, at the start of the
     * turn and before any staff act, as {@code Customer.recoverSatisfaction}
     * describes. Satisfaction never goes above the maximum. The restaurant
     * state is then shown again, so the manager sees the recovered numbers.
     *
     * @param turn the current turn number
     */
    private void happyHourPhase(int turn) {
        if (happyHour.isAvailable()) {
            int discountPercent = 100 - PricingPolicy.HAPPY_HOUR.apply(100);
            ui.showHappyHourOffer(discountPercent, config.getHappyHourRecovery(),
                    config.getHappyHourTurns());
            if (input.readChoice("Happy Hour action: ", 1, 2) == 1) {
                happyHour.start(turn);
            }
        }
        ui.showHappyHourStatus(happyHour.turnsLeft(turn), !happyHour.isAvailable());
        if (happyHour.isActive(turn)) {
            for (Customer customer : restaurant.getCustomers()) {
                if (isUnserved(customer)) {
                    customer.recoverSatisfaction(config.getHappyHourRecovery());
                }
            }
            ui.showRestaurantState(restaurant);
        }
    }

    /**
     * Lets the Host seat one waiting customer at the first free table, or wait.
     * This runs before the Waiter, so the Waiter can still seat another customer
     * in the same turn. If nobody is waiting or no table is free, the Host is idle.
     */
    private void hostAction() {
        List<Customer> queue = restaurant.getWaitingQueue();
        List<Table> freeTables = restaurant.freeTables();
        if (queue.isEmpty() || freeTables.isEmpty()) {
            ui.showHostIdle();
            return;
        }
        while (true) {
            ui.showHostChoices(queue);
            int choice = input.readChoice("Host action: ", 0, queue.size());
            try {
                if (choice == 0) {
                    restaurant.getHost().waitTurn();
                } else {
                    restaurant.getHost().seat(restaurant, queue.get(choice - 1), freeTables.get(0));
                }
                return;
            } catch (InvalidActionException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Phases 2 and 3 for the Waiter: asks for one task and carries it out.
     *
     * <p>Only the tasks possible this turn are listed, but each keeps its fixed
     * number. If the manager picks a hidden task, or the action breaks a rule,
     * the reason is shown and the Waiter is asked again. The turn does not move
     * on and the Chef is not asked until the Waiter's action has succeeded.
     *
     * @param turn the current turn number, recorded as the served turn when a dish is served
     */
    private void waiterAction(int turn) {
        while (true) {
            List<WaiterTask> available = availableWaiterTasks();
            ui.showWaiterTasks(available);
            int choice = input.readChoice("Waiter action: ", 1, WaiterTask.values().length);
            WaiterTask task = WaiterTask.values()[choice - 1];

            if (!available.contains(task)) {
                ui.showError("\"" + task.getLabel() + "\" is not possible this turn.");
                continue;
            }
            try {
                doWaiterTask(task, turn);
                return;
            } catch (InvalidActionException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Works out which Waiter tasks are possible now. Wait is always possible.
     *
     * @return the possible tasks, in {@link WaiterTask} order
     */
    private List<WaiterTask> availableWaiterTasks() {
        List<WaiterTask> tasks = new ArrayList<>();
        if (!restaurant.getWaitingQueue().isEmpty() && !restaurant.freeTables().isEmpty()) {
            tasks.add(WaiterTask.SEAT);
        }
        if (!restaurant.customersToTakeOrder().isEmpty()) {
            tasks.add(WaiterTask.TAKE_ORDER);
        }
        if (!restaurant.customersToServe().isEmpty()) {
            tasks.add(WaiterTask.SERVE);
        }
        tasks.add(WaiterTask.WAIT);
        return tasks;
    }

    /**
     * Carries out one Waiter task.
     *
     * @param task the task the manager chose
     * @param turn the current turn number
     * @throws InvalidActionException if the action breaks a game rule
     */
    private void doWaiterTask(WaiterTask task, int turn) throws InvalidActionException {
        switch (task) {
            case SEAT -> seatCustomer();
            case TAKE_ORDER -> takeOrder(turn);
            case SERVE -> serveCustomer(turn);
            case WAIT -> restaurant.getWaiter().waitTurn();
        }
    }

    /**
     * Asks which waiting customer to seat and at which free table, then seats them.
     *
     * @throws InvalidActionException if the seating breaks a game rule
     */
    private void seatCustomer() throws InvalidActionException {
        Customer customer = chooseCustomer("Who should be seated?", restaurant.getWaitingQueue());
        List<Table> freeTables = restaurant.freeTables();
        ui.showTableChoices(freeTables);
        Table table = freeTables.get(input.readChoice("Table: ", 1, freeTables.size()) - 1);
        restaurant.getWaiter().seat(restaurant, customer, table);
    }

    /**
     * Asks whose order to take, then takes it. The customer picks the dish, and
     * the price is locked now: the customer's own discount first (a VIP pays 90%),
     * then the Happy Hour discount if Happy Hour is on this turn. An order taken
     * before Happy Hour keeps its price.
     *
     * @param turn the current turn number, used to check Happy Hour
     * @throws InvalidActionException if the customer cannot order now
     */
    private void takeOrder(int turn) throws InvalidActionException {
        Customer customer = chooseCustomer("Whose order should the Waiter take?",
                restaurant.customersToTakeOrder());
        MenuItem item = customer.chooseItem(restaurant.getMenu());
        int price = customer.applyDiscount(item.getPriceCents());
        price = PricingPolicy.of(happyHour.isActive(turn)).apply(price);
        Order order = new Order(item, price);
        restaurant.getWaiter().takeOrder(customer, order);
    }

    /**
     * Asks which customer with a READY dish to serve, then serves them.
     *
     * @param turn the current turn number, recorded as the served turn
     * @throws InvalidActionException if the dish cannot be served now
     */
    private void serveCustomer(int turn) throws InvalidActionException {
        Customer customer = chooseCustomer("Who should be served?", restaurant.customersToServe());
        restaurant.getWaiter().serve(customer, turn);
    }

    /**
     * Shows a numbered list of customers and returns the one the manager picks.
     *
     * @param heading   the question shown above the list
     * @param customers the customers to choose from; must not be empty
     * @return the chosen customer
     */
    private Customer chooseCustomer(String heading, List<Customer> customers) {
        ui.showCustomerChoices(heading, customers);
        return customers.get(input.readChoice("Customer: ", 1, customers.size()) - 1);
    }

    /**
     * Phases 2 and 3 for the Chef: advances one order by one preparation unit,
     * or waits. 0 is Wait, because the list of orders changes length every turn
     * and 0 is the only number that always means Wait. If there is nothing to
     * cook, the Chef is not asked.
     */
    private void chefAction() {
        List<Customer> cooking = customersWithOrdersToCook();
        if (cooking.isEmpty()) {
            ui.showChefIdle();
            return;
        }
        ui.showCookChoices(cooking);
        int choice = input.readChoice("Chef action: ", 0, cooking.size());
        if (choice > 0) {
            restaurant.getChef().cookOneUnit(cooking.get(choice - 1).getOrder());
        }
    }

    /**
     * Finds the customers whose orders the Chef can work on, using
     * {@link Restaurant#ordersToCook()} so the rule lives in one place.
     *
     * @return those customers, in arrival order
     */
    private List<Customer> customersWithOrdersToCook() {
        List<Order> orders = restaurant.ordersToCook();
        List<Customer> result = new ArrayList<>();
        for (Customer customer : restaurant.getCustomers()) {
            if (orders.contains(customer.getOrder())) {
                result.add(customer);
            }
        }
        return result;
    }

    /**
     * Phase 4: a customer served on an earlier turn finishes eating and becomes
     * READY_TO_PAY. Then the Cashier takes at most one payment, oldest first.
     * The Cashier only accepts customers who became ready before this turn, so
     * a customer served on turn t is ready on turn t+1 and pays on turn t+2 at
     * the earliest. The payer's table is freed.
     *
     * @param turn the current turn number
     */
    private void eatingAndPayment(int turn) {
        for (Customer customer : restaurant.getCustomers()) {
            if (customer.getState() == CustomerState.EATING && customer.getServedTurn() < turn) {
                customer.markReadyToPay(turn);
            }
        }

        Customer payer = restaurant.getCashier().collectPayment(restaurant.getCustomers(), turn);
        if (payer != null) {
            payer.getTable().free();
            ui.showPayment(payer, payer.getOrder().getPriceCents());
        }
    }

    /**
     * Phase 5: every customer not yet served (WAITING, SEATED or ORDERED) loses
     * satisfaction. Served customers lose no more. A customer who reaches 0
     * leaves for good. (Happy Hour recovery happens earlier, at the start of
     * the turn, in {@link #happyHourPhase(int)}.)
     */
    private void waitingAndAbandonment() {
        for (Customer customer : restaurant.getCustomers()) {
            if (isUnserved(customer)) {
                customer.loseSatisfaction();
                if (customer.getSatisfaction() == 0) {
                    removeLeavingCustomer(customer);
                }
            }
        }
    }

    /**
     * Says whether a customer is still waiting to be served.
     *
     * @param customer the customer to check
     * @return true if the customer is WAITING, SEATED or ORDERED
     */
    private boolean isUnserved(Customer customer) {
        CustomerState state = customer.getState();
        return state == CustomerState.WAITING
                || state == CustomerState.SEATED
                || state == CustomerState.ORDERED;
    }

    /**
     * Removes a customer who ran out of patience: cancels their order if they
     * have one, frees their table if they have one, takes them out of the
     * queue, and marks them as left. They can never pay.
     *
     * @param customer the customer who is leaving
     */
    private void removeLeavingCustomer(Customer customer) {
        if (customer.getOrder() != null) {
            customer.getOrder().cancel();
        }
        if (customer.getTable() != null) {
            customer.getTable().free();
        }
        restaurant.removeFromQueue(customer);
        customer.leave();
        ui.showCustomerLeft(customer);
    }

    /**
     * Finds the customers who have paid.
     *
     * @return the customers in state PAID, in arrival order
     */
    private List<Customer> paidCustomers() {
        List<Customer> paid = new ArrayList<>();
        for (Customer customer : restaurant.getCustomers()) {
            if (customer.getState() == CustomerState.PAID) {
                paid.add(customer);
            }
        }
        return paid;
    }

    /**
     * Shows the final scores against the targets, and Victory or Defeat.
     */
    private void showFinalResult() {
        ui.showResults(
                getPaidCustomers(), config.getTargetPaidCustomers(),
                getRevenueCents(), config.getTargetRevenueCents(),
                getAverageSatisfaction(), config.getTargetAverageSatisfaction(),
                isVictory());
    }
}