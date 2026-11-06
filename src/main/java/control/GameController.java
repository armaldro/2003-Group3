package control;

import boundary.ConsoleUI;

public class GameController {

    private final ConsoleUI ui;
    private final GameConfig config;

    public GameController(ConsoleUI ui, GameConfig config) {
        this.ui = ui;
        this.config = config;
    }

    public void start() {
        ui.showMessage("=================================");
        ui.showMessage("   Welcome to Restaurant Rush!   ");
        ui.showMessage("=================================\n");
        
        int totalTurns = config.getTurns();
        
        for (int currentTurn = 1; currentTurn <= totalTurns; currentTurn++) {
            
            ui.showMessage("\n--- Turn " + currentTurn + " of " + totalTurns + " ---");
            
            if (config.getArrivalTurns().contains(currentTurn)) {
                ui.showMessage("Ding! A new customer has arrived.");
            }
            
            boolean turnComplete = false;
            while (!turnComplete) {
                String action = ui.getInput("Enter action: ");
                
                turnComplete = true; 
            }
        }
        
        evaluateAndShowResults();
    }

    private void evaluateAndShowResults() {
        int actualPaidCustomers = 0; 
        int actualRevenueCents = 0;
        int actualAverageSatisfaction = 0;

        boolean wonPaid = actualPaidCustomers >= config.getTargetPaidCustomers();
        boolean wonRevenue = actualRevenueCents >= config.getTargetRevenueCents();
        boolean wonSatisfaction = actualAverageSatisfaction >= config.getTargetAverageSatisfaction();

        boolean isVictory = wonPaid && wonRevenue && wonSatisfaction;

        ui.showMessage("\n=================================");
        ui.showMessage("           GAME OVER             ");
        ui.showMessage("=================================");
        ui.showMessage("Final Stats:");
        ui.showMessage("- Paid Customers: " + actualPaidCustomers + " / " + config.getTargetPaidCustomers());
        ui.showMessage("- Revenue (cents): " + actualRevenueCents + " / " + config.getTargetRevenueCents());
        ui.showMessage("- Average Satisfaction: " + actualAverageSatisfaction + " / " + config.getTargetAverageSatisfaction());
        
        if (isVictory) {
            ui.showMessage("\nCongratulations! You met all the targets and won the game!");
        } else {
            ui.showMessage("\nYou did not meet all the required targets. Better luck next rush!");
        }
    }
}