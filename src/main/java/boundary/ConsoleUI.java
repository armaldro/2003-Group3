package boundary;

import java.util.List;

import entity.Customer;
import entity.Order;
import entity.Restaurant;
import entity.Table;
import enums.WaiterTask;
import util.Money;

/**
 * Handles all console output for Restaurant Rush.
 * Keeps the display separate from the game rules. All input is read by
 * {@link InputReader}, so the game has only one Scanner on {@code System.in}.
 *
 * <p>Every method here only prints. None of them changes the game.
 *
 * @author CT2003 Group 3
 */
public class ConsoleUI {

    /**
     * Creates the console display.
     */
    public ConsoleUI() {
    }

    /**
     * Displays the welcome message at the start of the game.
     */
    public void showWelcome() {
        System.out.println("=================================");
        System.out.println("   Welcome to Restaurant Rush!   ");
        System.out.println("=================================\n");
    }

    /**
     * Displays a header for the start of a new turn.
     *
     * @param currentTurn the current turn number
     * @param totalTurns the total number of turns in the game
     */
    public void showTurnHeader(int currentTurn, int totalTurns) {
        System.out.println("\n--- Turn " + currentTurn + " of " + totalTurns + " ---");
    }

    /**
     * Displays the running score at the start of a turn: revenue so far and
     * the average satisfaction of paid customers ("N/A" until someone pays).
     *
     * @param revenueCents    the revenue so far, in cents
     * @param paidCustomers   how many customers have paid so far
     * @param avgSatisfaction the average satisfaction of paid customers;
     *                        ignored when nobody has paid
     */
    public void showTurnStatus(int revenueCents, int paidCustomers, int avgSatisfaction) {
        System.out.println("Revenue: " + Money.format(revenueCents)
                + " | Average satisfaction of paid customers: "
                + averageText(paidCustomers, avgSatisfaction));
    }

    /**
     * Displays the results at the end of a turn: served count, unhappy
     * departures, revenue, average satisfaction of paid customers and turns used.
     *
     * @param turnsUsed       how many turns have been played, including this one
     * @param totalTurns      the number of turns in the game
     * @param served          how many customers have been served so far
     * @param departures      how many customers have left unhappy so far
     * @param revenueCents    the revenue so far, in cents
     * @param paidCustomers   how many customers have paid so far
     * @param avgSatisfaction the average satisfaction of paid customers;
     *                        ignored when nobody has paid
     */
    public void showTurnResults(int turnsUsed, int totalTurns, int served, int departures,
            int revenueCents, int paidCustomers, int avgSatisfaction) {
        System.out.println("End of turn " + turnsUsed + ": served " + served
                + ", unhappy departures " + departures
                + ", revenue " + Money.format(revenueCents)
                + ", average satisfaction " + averageText(paidCustomers, avgSatisfaction)
                + ", turns used " + turnsUsed + "/" + totalTurns);
    }

    /**
     * Displays a notification that a new customer has arrived.
     *
     * @param customer the customer who arrived
     */
    public void showCustomerArrived(Customer customer) {
        System.out.println("Ding! " + label(customer) + " has arrived.");
    }

    /**
     * Displays each table and who sits there, the waiting queue, and every
     * customer's state, satisfaction and order.
     *
     * @param restaurant the restaurant to describe
     */
    public void showRestaurantState(Restaurant restaurant) {
        System.out.println("Tables:");
        for (Table table : restaurant.getTables()) {
            String occupant = table.isFree() ? "free" : label(table.getOccupant());
            System.out.println("  Table " + table.getNumber() + ": " + occupant);
        }
        System.out.println("Waiting queue: " + customerIds(restaurant.getWaitingQueue()));
        System.out.println("Customers:");
        for (Customer customer : restaurant.getCustomers()) {
            System.out.println("  " + describe(customer));
        }
    }

    /**
     * Displays the Waiter tasks open this turn. Each task keeps the same number
     * every turn: its position in {@link WaiterTask} plus 1.
     *
     * @param available the tasks the Waiter can do this turn
     */
    public void showWaiterTasks(List<WaiterTask> available) {
        System.out.println("Waiter tasks:");
        for (WaiterTask task : available) {
            System.out.println("  " + (task.ordinal() + 1) + ". " + task.getLabel());
        }
    }

    /**
     * Displays a numbered list of customers to choose from, starting at 1.
     *
     * @param heading   the question shown above the list
     * @param customers the customers to choose from
     */
    public void showCustomerChoices(String heading, List<Customer> customers) {
        System.out.println(heading);
        for (int i = 0; i < customers.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + describe(customers.get(i)));
        }
    }

    /**
     * Displays a numbered list of free tables to choose from, starting at 1.
     *
     * @param tables the free tables
     */
    public void showTableChoices(List<Table> tables) {
        System.out.println("Which table?");
        for (int i = 0; i < tables.size(); i++) {
            System.out.println("  " + (i + 1) + ". Table " + tables.get(i).getNumber());
        }
    }

    /**
     * Displays the Chef's choices: 0 to wait, then each dish that can be cooked,
     * with the preparation units it still needs.
     *
     * @param customers the customers whose orders can be cooked
     */
    public void showCookChoices(List<Customer> customers) {
        System.out.println("Chef tasks:");
        System.out.println("  0. Wait");
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            Order order = customer.getOrder();
            System.out.println("  " + (i + 1) + ". Cook " + label(customer) + "'s "
                    + order.getItem().getName() + " (" + order.getUnitsLeft() + " unit(s) left)");
        }
    }

    /**
     * Displays that the Chef has no order to work on this turn.
     */
    public void showChefIdle() {
        System.out.println("The Chef has nothing to cook.");
    }

    /**
     * Displays why the manager's choice was refused.
     *
     * @param message the reason the choice is not allowed
     */
    public void showError(String message) {
        System.out.println("Not allowed: " + message);
    }

    /**
     * Displays that a customer has paid.
     *
     * @param customer    the customer who paid
     * @param amountCents the amount paid, in cents
     */
    public void showPayment(Customer customer, int amountCents) {
        System.out.println(label(customer) + " paid " + Money.format(amountCents) + ".");
    }

    /**
     * Displays that a customer ran out of patience and left without paying.
     *
     * @param customer the customer who left
     */
    public void showCustomerLeft(Customer customer) {
        System.out.println(label(customer) + " ran out of patience and left.");
    }

    /**
     * Displays the end game results and whether the player achieved victory.
     *
     * @param actualPaid the number of paid customers achieved
     * @param targetPaid the target number of paid customers
     * @param actualRev the actual revenue in cents
     * @param targetRev the target revenue in cents
     * @param actualSat the actual average satisfaction
     * @param targetSat the target average satisfaction
     * @param isVictory true if the player met all targets, false otherwise
     */
    public void showResults(int actualPaid, int targetPaid, int actualRev, int targetRev, int actualSat, int targetSat, boolean isVictory) {
        System.out.println("\n=================================");
        System.out.println("           GAME OVER             ");
        System.out.println("=================================");
        System.out.println("Final Stats:");
        System.out.println("- Paid Customers: " + actualPaid + " / " + targetPaid);
        System.out.println("- Revenue: " + Money.format(actualRev) + " / " + Money.format(targetRev));
        System.out.println("- Average Satisfaction: " + averageText(actualPaid, actualSat) + " / " + targetSat);
        System.out.println("Result: " + (isVictory ? "Victory" : "Defeat"));

        if (isVictory) {
            System.out.println("\nCongratulations! You met all the targets and won the game!");
        } else {
            System.out.println("\nYou did not meet all the required targets. Better luck next rush!");
        }
    }

    /**
     * Describes one customer on one line: id, state, satisfaction and order.
     *
     * @param customer the customer to describe
     * @return for example "Customer 3 (VIP): ORDERED, satisfaction 84, Pasta (Not ready)"
     */
    private String describe(Customer customer) {
        String text = label(customer) + ": " + customer.getState()
                + ", satisfaction " + customer.getSatisfaction();
        Order order = customer.getOrder();
        if (order != null) {
            text += ", " + order.getItem().getName() + " (" + order.getStatus().getLabel() + ")";
        }
        return text;
    }

    /**
     * Lists customers by id and type, for example "Customer 2 (VIP), Customer 3 (Critic)".
     *
     * @param customers the customers to list
     * @return the ids joined by commas, or "none" if the list is empty
     */
    private String customerIds(List<Customer> customers) {
        if (customers.isEmpty()) {
            return "none";
        }
        StringBuilder text = new StringBuilder();
        for (Customer customer : customers) {
            if (text.length() > 0) {
                text.append(", ");
            }
            text.append(label(customer));
        }
        return text.toString();
    }

    /**
     * Formats the average satisfaction of paid customers, or "N/A" when
     * nobody has paid yet, as the brief requires.
     *
     * @param paidCustomers   how many customers have paid
     * @param avgSatisfaction the average satisfaction of paid customers
     * @return the average as text, or "N/A"
     */
    private String averageText(int paidCustomers, int avgSatisfaction) {
        return paidCustomers == 0 ? "N/A" : String.valueOf(avgSatisfaction);
    }

    /**
     * Names a customer with their type, for example "Customer 2 (VIP)". The type
     * comes from the customer object itself, so a new customer type needs no
     * change here.
     *
     * @param customer the customer to name
     * @return the customer's number followed by their type in brackets
     */
    private String label(Customer customer) {
        return "Customer " + customer.getId() + " (" + customer.getTypeName() + ")";
    }
}
