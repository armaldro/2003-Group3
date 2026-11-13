package control;

import java.util.ArrayList;
import java.util.List;

import entity.CriticCustomer;
import entity.Menu;
import entity.RegularCustomer;
import entity.VIPCustomer;

/**
 * The setting for one game of Restaurant Rush: how many turns it runs, how many
 * tables the restaurant has, which customers arrive and when, and the targets
 * the manager must reach for victory.
 *
 * <p>A setting never changes once it is created. Use {@link #base()} for the
 * base setting, which the game always offers so that runs can be reproduced.
 *
 * @author CT2003 Group 3
 */
public final class GameConfig {

    private final Menu menu;
    private final int happyHourTurns;
    private final int happyHourRecovery;
    private final int turns;
    private final int tables;
    private final List<Arrival> arrivals;
    private final int targetPaidCustomers;
    private final int targetRevenueCents;
    private final int targetAverageSatisfaction;

    /**
     * Creates a game setting.
     *
     * @param turns                     number of turns the game runs
     * @param tables                    number of tables in the restaurant
     * @param arrivals                  who arrives at the start of which turn, in turn order
     * @param menu                      the dishes and combos customers can order
     * @param happyHourTurns            how many turns Happy Hour lasts once started
     * @param happyHourRecovery         satisfaction an unserved customer recovers
     *                                  each Happy Hour turn
     * @param targetPaidCustomers       fewest paid customers needed for victory
     * @param targetRevenueCents        least revenue needed for victory, in cents
     * @param targetAverageSatisfaction least average satisfaction of paid
     *                                  customers needed for victory
     * @throws IllegalArgumentException if there is no turn, the arrivals are not
     *                                  in turn order, or one arrives after the last turn
     */
    private GameConfig(int turns, int tables, List<Arrival> arrivals, Menu menu,
            int happyHourTurns, int happyHourRecovery,
            int targetPaidCustomers, int targetRevenueCents, int targetAverageSatisfaction) {
        this.arrivals = List.copyOf(arrivals);
        checkArrivals(this.arrivals, turns);
        this.turns = turns;
        this.tables = tables;
        this.menu = menu;
        this.happyHourTurns = happyHourTurns;
        this.happyHourRecovery = happyHourRecovery;
        this.targetPaidCustomers = targetPaidCustomers;
        this.targetRevenueCents = targetRevenueCents;
        this.targetAverageSatisfaction = targetAverageSatisfaction;
    }

    /**
     * Returns the base setting: 18 turns, 2 tables, and one customer at the start
     * of turns 1, 4, 7, 10, 13 and 16. The customer on turn 4 is a VIP, the one on
     * turn 7 is a Critic, and the rest are Regular. Victory needs at least 4 paid
     * customers, $45.00 revenue and an average satisfaction of 60.
     *
     * <p>The menu has the three dishes and the Salad + Pasta combo. Happy Hour lasts
     * 2 turns, and each Happy Hour turn an unserved customer recovers 15 satisfaction.
     *
     * <p>The Critic arrives on turn 7 as customer 3. A Regular customer 3 would
     * order the Pasta, but a Critic always orders the dish that takes longest to
     * cook, the Salad + Pasta combo, so the Critic's twist shows in the game.
     *
     * <p>The schedule is fixed, so every run with the same input gives the same result.
     *
     * @return the base game setting
     */
    public static GameConfig base() {
        List<Arrival> arrivals = List.of(
                new Arrival(1, RegularCustomer::new),
                new Arrival(4, VIPCustomer::new),
                new Arrival(7, CriticCustomer::new),
                new Arrival(10, RegularCustomer::new),
                new Arrival(13, RegularCustomer::new),
                new Arrival(16, RegularCustomer::new));
        return new GameConfig(18, 2, arrivals, Menu.withCombos(), 2, 15, 4, 4500, 60);
    }

    /**
     * Returns the menu for this game, including the combo meal.
     *
     * @return the menu
     */
    public Menu getMenu() {
        return menu;
    }

    /**
     * Returns how many turns Happy Hour lasts once it starts.
     *
     * @return the Happy Hour duration, in turns
     */
    public int getHappyHourTurns() {
        return happyHourTurns;
    }

    /**
     * Returns the satisfaction an unserved customer recovers each Happy Hour turn.
     *
     * @return the recovery points per turn
     */
    public int getHappyHourRecovery() {
        return happyHourRecovery;
    }

    /**
     * Returns the number of turns the game runs. The game always runs all of them.
     *
     * @return the number of turns
     */
    public int getTurns() {
        return turns;
    }

    /**
     * Returns the number of tables in the restaurant.
     *
     * @return the number of tables
     */
    public int getTables() {
        return tables;
    }

    /**
     * Returns the arrival schedule: who arrives at the start of which turn, in turn order.
     *
     * @return an unmodifiable list of arrivals
     */
    public List<Arrival> getArrivals() {
        return arrivals;
    }

    /**
     * Returns the arrivals due at the start of one turn.
     *
     * @param turn the turn number
     * @return the arrivals on that turn, in schedule order (read-only); empty if nobody arrives
     */
    public List<Arrival> arrivalsOn(int turn) {
        List<Arrival> result = new ArrayList<>();
        for (Arrival arrival : arrivals) {
            if (arrival.getTurn() == turn) {
                result.add(arrival);
            }
        }
        return List.copyOf(result);
    }

    /**
     * Returns the fewest paid customers needed for victory.
     *
     * @return the paid-customer target
     */
    public int getTargetPaidCustomers() {
        return targetPaidCustomers;
    }

    /**
     * Returns the least revenue needed for victory.
     *
     * @return the revenue target, in cents
     */
    public int getTargetRevenueCents() {
        return targetRevenueCents;
    }

    /**
     * Returns the least average satisfaction of paid customers needed for victory.
     *
     * @return the average-satisfaction target, from 0 to 100
     */
    public int getTargetAverageSatisfaction() {
        return targetAverageSatisfaction;
    }

    /**
     * Stops a setting whose schedule could never play out as written.
     *
     * @param arrivals the arrival schedule to check
     * @param turns    the number of turns the game runs
     * @throws IllegalArgumentException if there is no turn, the arrivals are not
     *                                  in turn order, or one arrives after the last turn
     */
    private static void checkArrivals(List<Arrival> arrivals, int turns) {
        if (turns < 1) {
            throw new IllegalArgumentException("a game needs at least 1 turn");
        }
        int previousTurn = 0;
        for (Arrival arrival : arrivals) {
            if (arrival.getTurn() < previousTurn) {
                throw new IllegalArgumentException("arrivals must be in turn order, but turn "
                        + arrival.getTurn() + " comes after turn " + previousTurn);
            }
            if (arrival.getTurn() > turns) {
                throw new IllegalArgumentException("an arrival on turn " + arrival.getTurn()
                        + " can never happen in a " + turns + "-turn game");
            }
            previousTurn = arrival.getTurn();
        }
    }
}