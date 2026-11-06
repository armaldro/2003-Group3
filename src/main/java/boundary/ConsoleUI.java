package boundary;

import java.util.Scanner;

/**
 * Handles all console input and output for Restaurant Rush.
 * Keeps the UI logic separate from the core game logic.
 * 
 * @author CT2003 Group 3
 */
public class ConsoleUI {
    
    private final Scanner scanner;

    /**
     * Creates a new ConsoleUI and initializes the input scanner.
     */
    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message at the start of the game.
     */
    public void showWelcome() {
        System.out.println("=================================");
        System.out.println("   Welcome to Restaurant Rush!   ");
        System.out.println("=================================\n");
    }

    /**
     * Displays a header for the start of a new turn.
     * 
     * @param currentTurn the current turn number
     * @param totalTurns the total number of turns in the game
     */
    public void showTurnHeader(int currentTurn, int totalTurns) {
        System.out.println("\n--- Turn " + currentTurn + " of " + totalTurns + " ---");
    }

    /**
     * Displays a notification that a new customer has arrived.
     */
    public void showCustomerArrived() {
        System.out.println("Ding! A new customer has arrived.");
    }

    /**
     * Prompts the user for their next action.
     * 
     * @return the string entered by the user, or an empty string if the input stream is exhausted
     */
    public String promptAction() {
        System.out.print("Enter action: ");
        if (scanner.hasNextLine()) {
            return scanner.nextLine().trim();
        }
        return "";
    }

    /**
     * Displays the end game results and whether the player achieved victory.
     * 
     * @param actualPaid the number of paid customers achieved
     * @param targetPaid the target number of paid customers
     * @param actualRev the actual revenue in cents
     * @param targetRev the target revenue in cents
     * @param actualSat the actual average satisfaction
     * @param targetSat the target average satisfaction
     * @param isVictory true if the player met all targets, false otherwise
     */
    public void showResults(int actualPaid, int targetPaid, int actualRev, int targetRev, int actualSat, int targetSat, boolean isVictory) {
        System.out.println("\n=================================");
        System.out.println("           GAME OVER             ");
        System.out.println("=================================");
        System.out.println("Final Stats:");
        System.out.println("- Paid Customers: " + actualPaid + " / " + targetPaid);
        System.out.println("- Revenue (cents): " + actualRev + " / " + targetRev);
        System.out.println("- Average Satisfaction: " + actualSat + " / " + targetSat);
        
        if (isVictory) {
            System.out.println("\nCongratulations! You met all the targets and won the game!");
        } else {
            System.out.println("\nYou did not meet all the required targets. Better luck next rush!");
        }
    }
}