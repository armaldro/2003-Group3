import control.HappyHour;

/**
 * Checks Soon How's HappyHour timing: unused at first, on for its duration from
 * the turn it starts, counting down, then off, and usable once per game only.
 * Run from the repo root:
 * <pre>
 * javac -d out $(find src/main/java -name "*.java") testing/HappyHourCheck.java
 * java -cp out HappyHourCheck
 * </pre>
 */
public class HappyHourCheck {

    private static int failures = 0;

    /**
     * Runs every check and prints a summary.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        System.out.println("-- A new Happy Hour is unused");
        HappyHour happyHour = new HappyHour(2);
        check("it can be started", happyHour.isAvailable());
        check("it is not on", !happyHour.isActive(1));
        check("it has no turns left", happyHour.turnsLeft(1) == 0);

        System.out.println("-- Started on turn 5, it covers turns 5 and 6");
        happyHour.start(5);
        check("it can no longer be started", !happyHour.isAvailable());
        check("it is not on before it starts (turn 4)", !happyHour.isActive(4));
        check("it is on during turn 5, with 2 turns left", happyHour.isActive(5) && happyHour.turnsLeft(5) == 2);
        check("it is on during turn 6, with 1 turn left", happyHour.isActive(6) && happyHour.turnsLeft(6) == 1);
        check("it is off from turn 7", !happyHour.isActive(7) && happyHour.turnsLeft(7) == 0);

        System.out.println("-- It can be used once per game, from turn 1");
        check("starting it again is refused", throwsException(() -> happyHour.start(8)));
        check("a start before turn 1 is refused", throwsException(() -> new HappyHour(2).start(0)));
        check("a duration below 1 turn is refused", throwsException(() -> new HappyHour(0)));

        System.out.println(failures == 0 ? "\nAll checks passed." : "\n" + failures + " check(s) FAILED.");
    }

    /**
     * Says whether running the action throws an exception.
     *
     * @param action the action to run
     * @return true if it threw
     */
    private static boolean throwsException(Runnable action) {
        try {
            action.run();
            return false;
        } catch (RuntimeException e) {
            return true;
        }
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
