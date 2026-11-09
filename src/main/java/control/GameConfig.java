package control;

import java.util.List;

/**
 * The setting for one game of Restaurant Rush: how many turns it runs, how many
 * tables the restaurant has, when customers arrive, and the targets the manager
 * must reach for victory.
 *
 * <p>A setting never changes once it is created. Use {@link #base()} for the
 * base setting, which the game always offers so that runs can be reproduced.
 *
 * @author CT2003 Group 3
 */
public final class GameConfig {

    private final int turns;
    private final int tables;
    private final List<Integer> arrivalTurns;
    private final int targetPaidCustomers;
    private final int targetRevenueCents;
    private final int targetAverageSatisfaction;

    /**
     * Creates a game setting.
     *
     * @param turns                     number of turns the game runs
     * @param tables                    number of tables in the restaurant
     * @param arrivalTurns              turns at whose start one customer arrives
     * @param targetPaidCustomers       fewest paid customers needed for victory
     * @param targetRevenueCents        least revenue needed for victory, in cents
     * @param targetAverageSatisfaction least average satisfaction of paid
     *                                  customers needed for victory
     */
    private GameConfig(int turns, int tables, List<Integer> arrivalTurns,
            int targetPaidCustomers, int targetRevenueCents, int targetAverageSatisfaction) {
        this.turns = turns;
        this.tables = tables;
        this.arrivalTurns = List.copyOf(arrivalTurns);
        this.targetPaidCustomers = targetPaidCustomers;
        this.targetRevenueCents = targetRevenueCents;
        this.targetAverageSatisfaction = targetAverageSatisfaction;
    }

    /**
     * Returns the base setting: 18 turns, 2 tables, one customer at the start of
     * turns 1, 4, 7, 10, 13 and 16, and victory with at least 4 paid customers,
     * $45.00 revenue and an average satisfaction of 60.
     *
     * @return the base game setting
     */
    public static GameConfig base() {
        return new GameConfig(18, 2, List.of(1, 4, 7, 10, 13, 16), 4, 4500, 60);
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
     * Returns the turns at whose start one customer arrives, in order.
     *
     * @return an unmodifiable list of arrival turns
     */
    public List<Integer> getArrivalTurns() {
        return arrivalTurns;
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
}
