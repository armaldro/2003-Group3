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

    /**
     * Starts the game turn loop, processing events until all configured turns are completed.
     */
    public void start() {
        ui.showWelcome();
        
        int totalTurns = config.getTurns();
        
        for (int currentTurn = 1; currentTurn <= totalTurns; currentTurn++) {
            
            ui.showTurnHeader(currentTurn, totalTurns);
            
            if (config.getArrivalTurns().contains(currentTurn)) {
                ui.showCustomerArrived();
            }
            
            boolean turnComplete = false;
            while (!turnComplete) {
                String action = ui.promptAction();
                
                turnComplete = true; 
            }
        }
        
        evaluateAndShowResults();
    }

    /**
     * Evaluates the player's performance against the configuration targets
     * and delegates displaying the final game results to the UI.
     */
    private void evaluateAndShowResults() {
        int actualPaidCustomers = 0; 
        int actualRevenueCents = 0;
        int actualAverageSatisfaction = 0;

        boolean wonPaid = actualPaidCustomers >= config.getTargetPaidCustomers();
        boolean wonRevenue = actualRevenueCents >= config.getTargetRevenueCents();
        boolean wonSatisfaction = actualAverageSatisfaction >= config.getTargetAverageSatisfaction();

        boolean isVictory = wonPaid && wonRevenue && wonSatisfaction;

        ui.showResults(
            actualPaidCustomers, config.getTargetPaidCustomers(),
            actualRevenueCents, config.getTargetRevenueCents(),
            actualAverageSatisfaction, config.getTargetAverageSatisfaction(),
            isVictory
        );
    }
}