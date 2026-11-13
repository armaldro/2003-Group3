import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
 * Checks Soon How's GameController: the Stage 1 turn sequence and scoring, the
 * Stage 2 arrival schedule with its VIP and Critic customers, and the Stage 3
 * Host, Happy Hour and combo menu.
 * Each check feeds scripted answers through a Scanner on a string, so no real
 * console is needed. The game's own output is hidden while a check plays.
 *
 * <p>Each turn the game asks, in this order: whether to start Happy Hour (1 Start,
 * 2 Not now; only while it is unused), the Host's choice (0 Wait, or who to seat;
 * only if someone is waiting and a table is free), the Waiter's task (1 Seat,
 * 2 Take order, 3 Serve, 4 Wait) with who and which table where asked, and the
 * Chef's choice (0 Wait, or which order to cook; only if there is something to cook).
 *
 * <p>Run from the repo root:
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
     * A winning Stage 3 game, one answer per line. Happy Hour starts on turn 7,
     * when the Critic arrives, and the Host seats each new customer so the
     * Waiter can take the order on the same turn.
     */
    private static final String STAGE3_VICTORY_MOVES = String.join("\n",
            "2", "1", "2", "1", "1",   // t1  no Happy Hour, Host seats C1, take C1's order, cook the Salad
            "2", "3", "1",             // t2  serve C1
            "2", "4",                  // t3  Waiter waits
            "2", "1", "2", "1", "1",   // t4  Host seats C2 (VIP), take the VIP's order, cook the Burger
            "2", "3", "1",             // t5  serve the VIP
            "2", "4",                  // t6  Waiter waits
            "1", "1", "2", "1", "1",   // t7  start Happy Hour, Host seats C3 (Critic), take the combo order, cook (1 of 3)
            "4", "1",                  // t8  Waiter waits, cook the combo (2 of 3)
            "4", "1",                  // t9  Waiter waits, cook the combo (3 of 3)
            "1", "3", "1",             // t10 Host seats C4, serve the Critic
            "2", "1", "1",             // t11 take C4's combo order, cook (1 of 3)
            "4", "1",                  // t12 Waiter waits, cook C4's combo (2 of 3)
            "1", "2", "1", "1",        // t13 Host seats C5, take C5's order, cook C4's combo (3 of 3)
            "3", "1", "1",             // t14 serve C4, cook C5's Salad
            "3", "1",                  // t15 serve C5
            "4",                       // t16 no free table for C6, so the Host is idle; Waiter waits
            "1", "2", "1", "1",        // t17 Host seats C6, take C6's order, cook the Burger
            "3", "1") + "\n";          // t18 serve C6

    /**
     * Runs every check and prints a summary.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        sameTurnServeCheck();
        paymentTimingCheck();
        onePaymentPerTurnCheck();
        satisfactionAndLeavingCheck();
        averageCountsPaidOnlyCheck();
        invalidChoiceCheck();
        arrivalScheduleCheck();
        vipCheck();
        criticCheck();
        hostCheck();
        happyHourCheck();
        happyHourRecoveryCheck();
        victoryAndDefeatCheck();

        System.out.println(failures == 0 ? "\nAll checks passed." : "\n" + failures + " check(s) FAILED.");
    }

    /** A dish cooked this turn cannot be served until the next turn. */
    private static void sameTurnServeCheck() {
        System.out.println("-- A dish cooked this turn is served next turn at the earliest");
        // t1: Host waits, Waiter seats C1 at table 1. t2: take C1's order, Chef cooks the Salad. t3: serve C1.
        GameController game = game(turn("0", "1", "1", "1") + turn("2", "1", "1") + turn("3", "1"));
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
        // Same as above, then the Waiter waits on turns 4 and 5 (the Host waits too, as the VIP arrives).
        GameController game = game(turn("0", "1", "1", "1") + turn("2", "1", "1") + turn("3", "1")
                + turn("0", "4") + turn("0", "4"));
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
        GameController game = game(turn("4"));
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
        // t1: seat C1. t2: take C1's order, Chef waits. Then everyone waits every turn.
        // From turn 4 someone is always in the queue with a table free, so the Host is asked too.
        StringBuilder script = new StringBuilder(turn("0", "1", "1", "1") + turn("2", "1", "0") + turn("4", "0"));
        for (int turn = 4; turn <= 13; turn++) {
            script.append(turn("0", "4", "0"));
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
        // No Happy Hour, Host waits, then to the Waiter: letters, out of range, empty line,
        // a hidden task (Take order), then seat C1 at table 1
        GameController game = game("2\n0\nabc\n9\n\n2\n1\n1\n1\n");
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
        int[] expectedLoss = {8, 5, 12, 8, 8, 8};
        List<Arrival> arrivals = GameConfig.base().getArrivals();
        for (int i = 0; i < arrivals.size(); i++) {
            Arrival arrival = arrivals.get(i);
            check("turn " + arrival.getTurn() + " brings a customer who loses " + expectedLoss[i] + " per turn",
                    arrival.createCustomer(i + 1).getSatisfactionLoss() == expectedLoss[i]);
        }
        check("arrivalsOn(4) gives exactly the turn 4 arrival", GameConfig.base().arrivalsOn(4).size() == 1
                && GameConfig.base().arrivalsOn(4).get(0).getTurn() == 4);
        check("arrivalsOn(5) is empty", GameConfig.base().arrivalsOn(5).isEmpty());

        // The Host and Waiter wait on turns 1 to 4, so both customers are still in the queue.
        GameController game = game(turn("0", "4") + turn("0", "4") + turn("0", "4") + turn("0", "4"));
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
        // No Happy Hour, and the Waiter does the seating. Customer 2 is the VIP: seated on
        // turn 4, orders a Burger on turn 5, served on turn 6, pays on turn 8.
        GameController game = game(turn("0", "1", "1", "1") + turn("2", "1", "1") + turn("3", "1")
                + turn("0", "1", "1", "1") + turn("2", "1", "1") + turn("3", "1")
                + turn("0", "1", "1", "1") + turn("2", "1", "1"));
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

    /**
     * The Critic arrives on turn 7, loses 12 per unserved turn, orders the
     * slowest dish (the Salad + Pasta combo, where a Regular customer 3 would
     * order a Pasta), stops losing once served, and gets no discount of its own.
     * In the victory moves its order is taken during Happy Hour.
     */
    private static void criticCheck() {
        System.out.println("-- The Critic on turn 7");
        GameController game = game(STAGE3_VICTORY_MOVES);
        play(game, 1, 7);
        List<Customer> customers = game.getRestaurant().getCustomers();
        check("the Critic has arrived by turn 7", customers.size() == 3);
        if (customers.size() < 3) {
            return;
        }
        Customer critic = customers.get(2);
        check("the Critic loses 12 per turn", critic.getSatisfactionLoss() == 12);
        check("the Critic drops to 88 after its first turn", critic.getSatisfaction() == 88);
        check("the Critic orders the Salad + Pasta combo",
                critic.getOrder().getItem().getName().equals("Salad + Pasta"));
        check("a Regular customer 3 would have ordered a Pasta",
                new RegularCustomer(3).chooseItem(Menu.withCombos()).getName().equals("Pasta"));
        check("the combo's full price is $24.30", critic.getOrder().getItem().getPriceCents() == 2430);

        play(game, 8, 9);
        check("the combo takes 3 turns to cook and is READY after turn 9",
                critic.getOrder().getStatus() == OrderStatus.READY);

        play(game, 10, 11);
        check("the Critic is served on turn 10 at 76 and stops losing",
                critic.getServedTurn() == 10 && critic.getSatisfaction() == 76);
        int revenueBefore = game.getRevenueCents();

        // Turn 12 is the Critic's payment turn, and nobody else pays on it.
        play(game, 12, 12);
        check("the Critic pays on turn 12", critic.getState() == CustomerState.PAID);
        check("turn 12 adds the Critic's price to revenue",
                game.getRevenueCents() - revenueBefore == critic.getOrder().getPriceCents());
    }

    /**
     * The Host acts before the Waiter: it seats one waiting customer at the first
     * free table, so the Waiter can take that customer's order on the same turn.
     * With nobody to seat, or no free table, the Host is idle and not asked.
     */
    private static void hostCheck() {
        System.out.println("-- The Host seats before the Waiter acts");
        GameController game = game(STAGE3_VICTORY_MOVES);
        play(game, 1, 1);
        Customer c1 = first(game);
        check("the Host is asked when someone is waiting and a table is free",
                output().contains("Host tasks:"));
        check("the Host seats C1 at table 1", c1.getTable() == game.getRestaurant().getTables().get(0));
        check("the Waiter takes C1's order on the same turn", c1.getState() == CustomerState.ORDERED);
        check("C1 lost only one turn of satisfaction", c1.getSatisfaction() == 92);

        play(game, 2, 2);
        check("the Host is idle when nobody is waiting", output().contains("The Host has nobody to seat."));

        play(game, 3, 16);
        List<Customer> customers = game.getRestaurant().getCustomers();
        Customer c6 = customers.get(customers.size() - 1);
        check("the Host is idle on turn 16 because both tables are taken",
                output().contains("The Host has nobody to seat."));
        check("C6 is still waiting after turn 16", c6.getId() == 6 && c6.getState() == CustomerState.WAITING);

        play(game, 17, 17);
        check("the Host seats C6 on turn 17, once a table is free", c6.getState() == CustomerState.ORDERED);
    }

    /**
     * Happy Hour is offered every turn until it is used, lasts 2 turns from the
     * turn it starts, and takes 20% off orders taken while it is on. Orders
     * taken outside it pay the normal price.
     */
    private static void happyHourCheck() {
        System.out.println("-- Happy Hour: offered until used, 2 turns, 20% off orders taken");
        GameController game = game(STAGE3_VICTORY_MOVES);
        play(game, 1, 6);
        check("Happy Hour is offered while it is unused", output().contains("Start Happy Hour?"));
        check("Happy Hour is shown as available", output().contains("Happy Hour: available (once per game)"));

        play(game, 7, 7);
        Customer critic = game.getRestaurant().getCustomers().get(2);
        check("Happy Hour starts on turn 7 with 2 turns left", output().contains("Happy Hour: ON, 2 turn(s) left"));
        check("the Critic's combo, taken in Happy Hour, is billed $19.44 (20% off $24.30)",
                critic.getOrder().getPriceCents() == 1944);

        play(game, 8, 8);
        check("turn 8 is the last Happy Hour turn", output().contains("Happy Hour: ON, 1 turn(s) left"));
        check("Happy Hour is not offered again once used", !output().contains("Start Happy Hour?"));

        play(game, 9, 11);
        check("Happy Hour is over from turn 9", output().contains("Happy Hour: used")
                && !output().contains("Happy Hour: ON"));
        Customer c4 = game.getRestaurant().getCustomers().get(3);
        check("C4's combo, taken on turn 11, is billed the full $24.30", c4.getOrder().getPriceCents() == 2430);
    }

    /**
     * During Happy Hour every customer still waiting for a table or food
     * recovers 15 at the start of the turn, before losing satisfaction at the
     * end of it. Satisfaction never goes above 100.
     */
    private static void happyHourRecoveryCheck() {
        System.out.println("-- Happy Hour: waiting customers recover 15 at the start of each Happy Hour turn");
        // Nobody is seated. Turns 1 to 3: no Happy Hour. Turn 4: start it. Host and Waiter always wait.
        GameController game = game(turn("0", "4") + turn("0", "4") + turn("0", "4")
                + "1\n0\n4\n" + "0\n4\n" + "0\n4\n");
        play(game, 1, 3);
        Customer c1 = first(game);
        check("C1 is at 76 after 3 turns waiting", c1.getSatisfaction() == 76);

        play(game, 4, 4);
        Customer vip = game.getRestaurant().getCustomers().get(1);
        check("turn 4: C1 recovers 15 then loses 8, ending at 83", c1.getSatisfaction() == 83);
        check("turn 4: the VIP arrives at 100, stays capped at 100, then loses 5", vip.getSatisfaction() == 95);

        play(game, 5, 5);
        check("turn 5: C1 recovers again, ending at 90", c1.getSatisfaction() == 90);

        play(game, 6, 6);
        check("turn 6: Happy Hour is over, so C1 only loses 8, ending at 82", c1.getSatisfaction() == 82);
    }

    /**
     * A winning Stage 3 game, and a game where nobody is served. In the winning
     * game customers pay on turns 4, 7, 12, 16 and 17: $9.00 + $13.50 (VIP) +
     * $19.44 (Critic, Happy Hour) + $24.30 (combo) + $9.00 = $75.24, with
     * satisfaction 92, 95, 76, 68 and 84, so the average is 83.
     */
    private static void victoryAndDefeatCheck() {
        System.out.println("-- Full Stage 3 games");
        GameController win = game(STAGE3_VICTORY_MOVES);
        play(win, 1, 18);
        check("victory moves: 5 paid", win.getPaidCustomers() == 5);
        check("victory moves: $75.24 revenue", win.getRevenueCents() == 7524);
        check("victory moves: average satisfaction 83", win.getAverageSatisfaction() == 83);
        check("victory moves: Victory", win.isVictory());

        // No Happy Hour, and the Host and Waiter wait every turn.
        StringBuilder waits = new StringBuilder();
        for (int turn = 1; turn <= 18; turn++) {
            waits.append(turn("0", "4"));
        }
        GameController lose = game(waits.toString());
        play(lose, 1, 18);
        List<Customer> customers = lose.getRestaurant().getCustomers();
        check("defeat moves: 0 paid", lose.getPaidCustomers() == 0);
        check("defeat moves: Defeat", !lose.isVictory());
        check("defeat moves: the Critic (customer 3) runs out of patience by turn 18",
                customers.size() >= 3 && customers.get(2).getState() == CustomerState.LEFT);
        check("defeat moves: the VIP (customer 2) is still waiting",
                customers.size() >= 2 && customers.get(1).getState() == CustomerState.WAITING);
    }

    // ----- helpers -----

    /**
     * Returns the answers for one turn while Happy Hour is still unused: "2"
     * (not now) to the Happy Hour offer, then the given answers.
     *
     * @param answers the answers after the Happy Hour offer, in the order asked
     * @return the answers, one per line
     */
    private static String turn(String... answers) {
        return "2\n" + String.join("\n", answers) + "\n";
    }

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
