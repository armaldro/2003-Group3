import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import boundary.ConsoleUI;
import boundary.InputReader;
import control.GameConfig;
import control.GameController;
import entity.Customer;
import entity.Menu;
import entity.Order;
import entity.RegularCustomer;
import entity.Restaurant;
import enums.CustomerState;
import enums.OrderStatus;

/**
 * Checks Soon How's Stage 1 GameController: the turn sequence and the scoring.
 * Each check feeds scripted answers through a Scanner on a string, so no real
 * console is needed. The game's own output is hidden while a check plays.
 * Run from the repo root:
 * <pre>
 * javac -d out $(find src/main/java -name "*.java") testing/GameControllerCheck.java
 * java -cp out GameControllerCheck
 * </pre>
 */
public class GameControllerCheck {

    private static int failures = 0;
    private static final PrintStream REAL_OUT = System.out;
    private static ByteArrayOutputStream captured;

    /**
     * Runs every check and prints a summary.
     *
     * @param args not used
     * @throws Exception if the victory input file cannot be read
     */
    public static void main(String[] args) throws Exception {
        sameTurnServeCheck();
        paymentTimingCheck();
        onePaymentPerTurnCheck();
        satisfactionAndLeavingCheck();
        averageCountsPaidOnlyCheck();
        invalidChoiceCheck();
        victoryAndDefeatCheck();

        System.out.println(failures == 0 ? "\nAll checks passed." : "\n" + failures + " check(s) FAILED.");
    }

    /** A dish cooked this turn cannot be served until the next turn. */
    private static void sameTurnServeCheck() {
        System.out.println("-- A dish cooked this turn is served next turn at the earliest");
        // t1: seat C1 at table 1. t2: take C1's order, Chef cooks the Salad. t3: serve C1.
        GameController game = game("1\n1\n1\n" + "2\n1\n1\n" + "3\n1\n");
        Customer c1;

        play(game, 1, 2);
        c1 = first(game);
        check("Salad is READY at the end of turn 2", c1.getOrder().getStatus() == OrderStatus.READY);
        check("C1 is not served on the turn the Salad was cooked", c1.getState() == CustomerState.ORDERED);

        play(game, 3, 3);
        check("C1 is served on turn 3", c1.getState() == CustomerState.EATING && c1.getServedTurn() == 3);
    }

    /** Served on turn t: READY_TO_PAY on t+1, pays on t+2 at the earliest. */
    private static void paymentTimingCheck() {
        System.out.println("-- Served on turn t, ready to pay on t+1, pays on t+2");
        // Same as above, then the Waiter waits on turns 4 and 5.
        GameController game = game("1\n1\n1\n" + "2\n1\n1\n" + "3\n1\n" + "4\n" + "4\n");
        play(game, 1, 3);
        Customer c1 = first(game);

        play(game, 4, 4);
        check("C1 is READY_TO_PAY on turn 4", c1.getState() == CustomerState.READY_TO_PAY
                && c1.getReadyToPayTurn() == 4);
        check("C1 has not paid on turn 4", game.getPaidCustomers() == 0);

        play(game, 5, 5);
        check("C1 pays on turn 5", c1.getState() == CustomerState.PAID);
        check("revenue is $9.00 (900 cents)", game.getRevenueCents() == 900);
        check("C1's table is freed after paying", c1.getTable().isFree());
    }

    /** Two customers ready to pay: only the one who waited longest pays this turn. */
    private static void onePaymentPerTurnCheck() {
        System.out.println("-- Only one payment per turn");
        GameController game = game("4\n");
        Restaurant restaurant = game.getRestaurant();
        Customer c1 = readyToPay(restaurant, 1, 0);
        Customer c2 = readyToPay(restaurant, 2, 1);

        play(game, 3, 3);
        check("exactly one customer paid on turn 3", game.getPaidCustomers() == 1);
        check("the one who waited longest (C1) paid", c1.getState() == CustomerState.PAID);
        check("C2 is still waiting to pay", c2.getState() == CustomerState.READY_TO_PAY);
    }

    /** Unserved customers lose 8 per turn; at 0 they leave, free the table and never pay. */
    private static void satisfactionAndLeavingCheck() {
        System.out.println("-- Satisfaction drops 8 per turn while unserved; at 0 the customer leaves");
        // t1: seat C1. t2: take C1's order, Chef waits. Then Waiter and Chef wait every turn.
        StringBuilder script = new StringBuilder("1\n1\n1\n" + "2\n1\n0\n");
        for (int turn = 3; turn <= 13; turn++) {
            script.append("4\n0\n");
        }
        GameController game = game(script.toString());

        play(game, 1, 1);
        Customer c1 = first(game);
        check("SEATED customer drops to 92 after turn 1", c1.getSatisfaction() == 92);
        play(game, 2, 2);
        check("ORDERED customer drops to 84 after turn 2", c1.getSatisfaction() == 84);
        play(game, 3, 12);
        check("C1 is still there at 4 after turn 12", c1.getState() == CustomerState.ORDERED
                && c1.getSatisfaction() == 4);

        play(game, 13, 13);
        check("C1 reaches 0 and leaves on turn 13", c1.getState() == CustomerState.LEFT
                && c1.getSatisfaction() == 0);
        check("C1's order is cancelled", c1.getOrder().getStatus() == OrderStatus.CANCELLED);
        check("C1's table is freed", c1.getTable().isFree());
        check("C1 never paid", game.getPaidCustomers() == 0 && game.getRevenueCents() == 0);
        check("the game says C1 left", output().contains("Customer 1 (Regular) ran out of patience and left."));
    }

    /** Customers who left do not pull the average down; nobody paid gives 0. */
    private static void averageCountsPaidOnlyCheck() {
        System.out.println("-- Average satisfaction counts paid customers only");
        GameController game = game("");
        check("average is 0 when nobody has paid", game.getAverageSatisfaction() == 0);

        Restaurant restaurant = game.getRestaurant();
        Customer paid = readyToPay(restaurant, 1, 0);
        paid.getOrder().markPaid();
        paid.markPaid();
        Customer left = new RegularCustomer(2);
        restaurant.addArrival(left);
        for (int i = 0; i < 13; i++) {
            left.loseSatisfaction();
        }
        left.leave();

        check("average is 100 (the paid customer), not 50", game.getAverageSatisfaction() == 100);
    }

    /** A bad or hidden choice is rejected, and the Waiter is asked again in the same turn. */
    private static void invalidChoiceCheck() {
        System.out.println("-- Invalid choices are rejected and the turn does not move on");
        // letters, out of range, empty line, a hidden task (Take order), then seat C1 at table 1
        GameController game = game("abc\n9\n\n2\n1\n1\n1\n");
        play(game, 1, 1);
        Customer c1 = first(game);
        String shown = output();

        check("letters are refused", shown.contains("Please enter a number."));
        check("an out-of-range number is refused", shown.contains("Please enter a number from 1 to 4."));
        check("a hidden task is refused", shown.contains("\"Take an order\" is not possible this turn."));
        check("the Waiter still seats C1 on turn 1", c1.getState() == CustomerState.SEATED);
        check("only one turn of satisfaction was lost", c1.getSatisfaction() == 92);
    }

    /** The full victory and defeat replays end with the expected scores. */
    private static void victoryAndDefeatCheck() throws Exception {
        System.out.println("-- Full games from the replay files");
        GameController win = game(Files.readString(Path.of("testing/stage1-victory-input.txt")));
        play(win, 1, 18);
        check("victory run: 5 paid", win.getPaidCustomers() == 5);
        check("victory run: $66.00 revenue", win.getRevenueCents() == 6600);
        check("victory run: average satisfaction 79", win.getAverageSatisfaction() == 79);
        check("victory run: Victory", win.isVictory());

        GameController lose = game(Files.readString(Path.of("testing/stage1-defeat-input.txt")));
        play(lose, 1, 18);
        check("defeat run: 0 paid", lose.getPaidCustomers() == 0);
        check("defeat run: Defeat", !lose.isVictory());
    }

    // ----- helpers -----

    /**
     * Creates a game on the base setting that reads the given answers.
     *
     * @param script the answers, one per line
     * @return the new game
     */
    private static GameController game(String script) {
        return new GameController(new ConsoleUI(), new InputReader(new Scanner(script)), GameConfig.base());
    }

    /**
     * Plays turns from..to with the game's output hidden.
     *
     * @param game the game to play
     * @param from the first turn to play
     * @param to   the last turn to play
     */
    private static void play(GameController game, int from, int to) {
        captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured));
        try {
            for (int turn = from; turn <= to; turn++) {
                game.playTurn(turn);
            }
        } finally {
            System.setOut(REAL_OUT);
        }
    }

    /**
     * Returns what the game printed during the last {@link #play} call.
     *
     * @return the hidden output
     */
    private static String output() {
        return captured.toString();
    }

    /**
     * Returns the first customer who arrived.
     *
     * @param game the game
     * @return customer 1
     */
    private static Customer first(GameController game) {
        return game.getRestaurant().getCustomers().get(0);
    }

    /**
     * Puts a customer straight into READY_TO_PAY with a Salad, without playing turns.
     *
     * @param restaurant the restaurant to add them to
     * @param id         the customer id
     * @param table      the index of the table to seat them at
     * @return the customer
     */
    private static Customer readyToPay(Restaurant restaurant, int id, int table) {
        Customer customer = new RegularCustomer(id);
        restaurant.addArrival(customer);
        try {
            restaurant.seat(customer, restaurant.getTables().get(table));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        Order order = new Order(Menu.standard().getItems().get(0), 900);
        customer.placeOrder(order);
        order.cookOneUnit();
        order.markServed();
        customer.markServed(1);
        customer.markReadyToPay(2);
        return customer;
    }

    /**
     * Prints OK or FAIL for one condition.
     *
     * @param label what is being checked
     * @param ok    whether it held
     */
    private static void check(String label, boolean ok) {
        if (ok) {
            System.out.println("OK   " + label);
        } else {
            System.out.println("FAIL " + label);
            failures++;
        }
    }
}
