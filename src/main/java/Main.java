import boundary.ConsoleUI;
import control.GameConfig;
import control.GameController;

/**
 * Entry point for Restaurant Rush, a turn-based restaurant management game
 * played on the command line.
 *
 * <p>CT2003 Object-Oriented Design &amp; Programming, 26S1, lab group COMP1, Group 3.
 *
 * <p>This class only starts the game. Game rules belong in the {@code control}
 * package, and all console input and output belongs in the {@code boundary} package.
 *
 * @author CT2003 Group 3
 */
public class Main {

    /**
     * Creates the console boundary and the base game setting, passes both to a
     * new {@link GameController}, and starts the game.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            ConsoleUI ui = new ConsoleUI();
            GameConfig config = GameConfig.base();
            GameController controller = new GameController(ui, config);
            controller.start();
        } catch (RuntimeException e) {
            System.err.println("Restaurant Rush stopped unexpectedly: " + e.getMessage());
        }
    }
}
