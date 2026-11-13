import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import boundary.ConsoleUI;
import boundary.InputReader;
import control.Arrival;
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
 * Checks Soon How's GameController: the Stage 1 turn sequence and scoring, and
 * the Stage 2 arrival schedule with its VIP and Critic customers.
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
     * A winning Stage 2 game, one answer per line. Each turn: the Waiter's task
     * (1 Seat, 2 Take order, 3 Serve, 4 Wait), who and which table where asked,
     * then the Chef's choice (0 Wait, 1 cook the first order) when there is
     * something to cook. On turn 13 the Waiter seats customer 5 and the Chef
     * finishes the Critic's Pasta.
     */
    private static final String STAGE2_VICTORY_MOVES = String.join("\n",
            "1", "1", "1",          // t1  seat customer 1
            "2", "1", "1",          // t2  take customer 1's order, cook the Salad
            "3", "1",               // t3  serve customer 1
            "1", "1", "1",          // t4  seat customer 2 (VIP)
            "2", "1", "1",          // t5  take the VIP's order, cook the Burger
            "3", "1",               // t6  serve the VIP
            "1", "1", "1",          // t7  seat customer 3
            "2", "1", "1",          // t8  take customer 3's order, cook the Pasta (1 of 2)
            "4", "1",               // t9  Waiter waits, cook the Pasta (2 of 2)
            "3", "1",               // t10 serve customer 3
            "1", "1", "1",          // t11 seat customer 4 (Critic)
            "2", "1", "1",          // t12 take the Critic's order, cook the Pasta (1 of 2)
            "1", "1", "1", "1",     // t13 seat customer 5, cook the Critic's Pasta (2 of 2)
            "3", "1",               // t14 serve the Critic
            "2", "1", "1",          // t15 take customer 5's order, cook the Burger
            "3", "1",               // t16 serve customer 5
            "1", "1", "1",          // t17 seat customer 6
            "4") + "\n";            // t18 Waiter waits

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
        arrivalScheduleCheck();
        vipCheck();
        criticCheck();
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

    /**
     * A winning Stage 2 game. The stage 1 moves (5 paid, $66.00, 79 at the
     * stage-1 tag) no longer fit, because the Critic on turn 10 orders a Pasta
     * that needs a second unit of cooking. Customers pay on turns 5, 8, 12, 16
     * and 18: $9.00 + $13.50 (VIP) + $18.00 + $18.00 (Critic) + $15.00 = $73.50,
     * with satisfaction 84, 90, 76, 52 and 76, so the average is 75.6, rounded to 76.
     */
    private static void victoryAndDefeatCheck() throws Exception {
        System.out.println("-- Full Stage 2 games");
        GameController win = game(STAGE2_VICTORY_MOVES);
        play(win, 1, 18);
        check("victory moves: 5 paid", win.getPaidCustomers() == 5);
        check("victory moves: $73.50 revenue", win.getRevenueCents() == 7350);
        check("victory moves: average satisfaction 76", win.getAverageSatisfaction() == 76);
        check("victory moves: Victory", win.isVictory());

        // The Waiter and Chef wait every turn.
        GameController lose = game(Files.readString(Path.of("testing/stage1-defeat-input.txt")));
        play(lose, 1, 18);
        List<Customer> customers = lose.getRestaurant().getCustomers();
        check("defeat moves: 0 paid", lose.getPaidCustomers() == 0);
        check("defeat moves: Defeat", !lose.isVictory());
        check("defeat moves: the Critic (customer 4) runs out of patience by turn 18",
                customers.size() >= 4 && customers.get(3).getState() == CustomerState.LEFT);
        check("defeat moves: the VIP (customer 2) is still waiting",
                customers.size() >= 2 && customers.get(1).getState() == CustomerState.WAITING);
    }

    /**
     * The Critic arrives on turn 10, loses 12 per unserved turn, orders the
     * slowest dish (a Pasta, where a Regular customer 4 would order a Salad),
     * stops losing once served, and pays full price.
     */
    private static void criticCheck() {
        System.out.println("-- The Critic on turn 10");
        GameController game = game(STAGE2_VICTORY_MOVES);
        play(game, 1, 10);
        List<Customer> customers = game.getRestaurant().getCustomers();
        check("the Critic has arrived by turn 10", customers.size() == 4);
        if (customers.size() < 4) {
            return;
        }
        Customer critic = customers.get(3);
        check("the Critic drops to 88 after its first turn waiting", critic.getSatisfaction() == 88);

        play(game, 11, 12);
        check("the Critic drops to 64 after turns 11 and 12", critic.getSatisfaction() == 64);
        check("the Critic orders a Pasta", critic.getOrder().getItem().getName().equals("Pasta"));
        check("a Regular customer 4 would have ordered a Salad",
                new RegularCustomer(4).chooseItem(Menu.standard()).getName().equals("Salad"));
        check("the Critic's Pasta is billed full price, $18.00", critic.getOrder().getPriceCents() == 1800);

        play(game, 13, 15);
        check("the Critic is at 52 and stops losing once served on turn 14", critic.getSatisfaction() == 52);
        int revenueBefore = game.getRevenueCents();

        // Turn 16 is the Critic's payment turn, and nobody else pays on it.
        play(game, 16, 16);
        check("the Critic pays on turn 16", critic.getState() == CustomerState.PAID);
        check("turn 16 adds $18.00 to revenue", game.getRevenueCents() - revenueBefore == 1800);
    }

    /**
     * The base schedule brings the right customer on the right turn. The type is
     * recognised by its behaviour (satisfaction lost per turn), not by its class.
     */
    private static void arrivalScheduleCheck() {
        System.out.println("-- The arrival schedule creates the right customer on the right turn");
        List<Integer> turns = new ArrayList<>();
        for (Arrival arrival : GameConfig.base().getArrivals()) {
            turns.add(arrival.getTurn());
        }
        check("6 arrivals on turns 1, 4, 7, 10, 13 and 16", turns.equals(List.of(1, 4, 7, 10, 13, 16)));

        // Satisfaction lost per turn for each scheduled arrival: 8 Regular, 5 VIP, 12 Critic.
        int[] expectedLoss = {8, 5, 8, 12, 8, 8};
        List<Arrival> arrivals = GameConfig.base().getArrivals();
        for (int i = 0; i < arrivals.size(); i++) {
            Arrival arrival = arrivals.get(i);
            check("turn " + arrival.getTurn() + " brings a customer who loses " + expectedLoss[i] + " per turn",
                    arrival.createCustomer(i + 1).getSatisfactionLoss() == expectedLoss[i]);
        }
        check("arrivalsOn(4) gives exactly the turn 4 arrival", GameConfig.base().arrivalsOn(4).size() == 1
                && GameConfig.base().arrivalsOn(4).get(0).getTurn() == 4);
        check("arrivalsOn(5) is empty", GameConfig.base().arrivalsOn(5).isEmpty());

        // The Waiter waits on turns 1 to 4, so both customers are still in the queue.
        GameController game = game("4\n4\n4\n4\n");
        play(game, 1, 4);
        List<Customer> customers = game.getRestaurant().getCustomers();
        check("two customers have arrived by turn 4", customers.size() == 2);
        if (customers.size() < 2) {
            return;
        }
        check("turn 1 brings a Regular (loses 8 per turn)", customers.get(0).getSatisfactionLoss() == 8);
        check("turn 4 brings a VIP (loses 5 per turn)", customers.get(1).getSatisfactionLoss() == 5);
        check("customer ids follow arrival order", customers.get(0).getId() == 1 && customers.get(1).getId() == 2);
    }

    /** A VIP loses 5 per unserved turn, is billed 10% less, and revenue counts the VIP price. */
    private static void vipCheck() {
        System.out.println("-- A VIP loses 5 per unserved turn and pays 10% less");
        // The stage 1 victory moves for turns 1 to 8. Customer 2 is the VIP: seated on
        // turn 4, orders a Burger on turn 5, served on turn 6, pays on turn 8.
        GameController game = game("1\n1\n1\n" + "2\n1\n1\n" + "3\n1\n"
                + "1\n1\n1\n" + "2\n1\n1\n" + "3\n1\n"
                + "1\n1\n1\n" + "2\n1\n1\n");
        play(game, 1, 4);
        List<Customer> customers = game.getRestaurant().getCustomers();
        check("the VIP has arrived by turn 4", customers.size() == 2);
        if (customers.size() < 2) {
            return;
        }
        Customer vip = customers.get(1);
        check("VIP drops to 95 after one turn seated", vip.getSatisfaction() == 95);

        play(game, 5, 5);
        check("VIP drops to 90 after a turn waiting for the Burger", vip.getSatisfaction() == 90);
        check("VIP's Burger is billed $13.50 (1350 cents)", vip.getOrder().getPriceCents() == 1350);

        play(game, 6, 7);
        check("VIP stays at 90 once served", vip.getSatisfaction() == 90);
        int revenueBefore = game.getRevenueCents();

        // Turn 8 is the VIP's payment turn, and nobody else pays on it.
        play(game, 8, 8);
        check("VIP pays on turn 8", vip.getState() == CustomerState.PAID);
        check("turn 8 adds $13.50 to revenue, not $15.00", game.getRevenueCents() - revenueBefore == 1350);
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
