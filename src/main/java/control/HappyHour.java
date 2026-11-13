package control;

/**
 * Tracks the game's one Happy Hour: whether it has been used, when it started,
 * and how many turns it has left. Happy Hour can be started once per game and
 * lasts for the turn it starts plus the turns after it, up to its duration.
 *
 * <p>This class only handles timing. The discount comes from
 * {@link enums.PricingPolicy}, and the satisfaction recovery is applied by
 * {@link GameController}.
 *
 * @author CT2003 Group 3
 */
public class HappyHour {

    private final int duration;
    private int startTurn;

    /**
     * Creates an unused Happy Hour.
     *
     * @param duration how many turns Happy Hour lasts once started
     * @throws IllegalArgumentException if the duration is less than 1
     */
    public HappyHour(int duration) {
        if (duration < 1) {
            throw new IllegalArgumentException("Happy Hour must last at least 1 turn");
        }
        this.duration = duration;
        this.startTurn = 0;
    }

    /**
     * Returns whether Happy Hour can still be started this game.
     *
     * @return true if it has not been used yet
     */
    public boolean isAvailable() {
        return startTurn == 0;
    }

    /**
     * Starts Happy Hour on the given turn.
     *
     * @param turn the turn Happy Hour starts on, 1 or more
     * @throws IllegalArgumentException if the turn is less than 1
     * @throws IllegalStateException    if Happy Hour has already been used
     */
    public void start(int turn) {
        if (turn < 1) {
            throw new IllegalArgumentException("Happy Hour must start on turn 1 or later");
        }
        if (!isAvailable()) {
            throw new IllegalStateException("Happy Hour can only be used once per game");
        }
        startTurn = turn;
    }

    /**
     * Returns whether Happy Hour is on during the given turn.
     *
     * @param turn the turn to check
     * @return true if the turn falls inside Happy Hour
     */
    public boolean isActive(int turn) {
        return !isAvailable() && turn >= startTurn && turn < startTurn + duration;
    }

    /**
     * Returns how many turns of Happy Hour are left, counting the given turn.
     *
     * @param turn the current turn
     * @return the turns left, or 0 if Happy Hour is not on
     */
    public int turnsLeft(int turn) {
        if (!isActive(turn)) {
            return 0;
        }
        return startTurn + duration - turn;
    }
}