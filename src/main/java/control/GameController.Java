package control;
 
import boundary.ConsoleUI;
 
/**
 * Runs a game of Restaurant Rush: owns the game state and the turn sequence.
 *
 * @author CT2003 Group 3
 */
public class GameController {
 
    private final ConsoleUI ui;
    private final GameConfig config;
 
    /**
     * Creates a controller for one game.
     *
     * @param ui     the console boundary used for all input and output
     * @param config the game setting to play
     */
    public GameController(ConsoleUI ui, GameConfig config) {
        this.ui = ui;
        this.config = config;
    }
 
    /** Starts the game. */
    public void start() {
        ui.showWelcome();
        // TODO: turn loop for config.getTurns() turns.
    }
}
 