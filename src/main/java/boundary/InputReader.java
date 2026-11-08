package boundary;

import java.util.Scanner;

/**
 * Reads the manager's choices from the console, or from a file piped in with
 * {@code java -cp out Main < testing/stage1-victory.txt}.
 *
 * <p>It only checks that the input is a number in range. Whether the choice is
 * allowed by the game rules is decided by the entity and control classes.
 */
public class InputReader {

    private final Scanner scanner;

    /** Reads from the keyboard (or a piped file). */
    public InputReader() {
        this(new Scanner(System.in));
    }

    /**
     * Reads from the given Scanner, so tests can pass in text such as
     * {@code new Scanner("abc\n9\n2\n")}.
     *
     * @param scanner where to read from
     */
    public InputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Asks for a whole number from min to max, and keeps asking until it gets one.
     *
     * @param prompt the question to show
     * @param min    the smallest allowed number
     * @param max    the largest allowed number
     * @return a number from min to max
     * @throws IllegalStateException if the input runs out (e.g. a replay file is too short)
     */
    public int readChoice(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                throw new IllegalStateException("Input ended before the game finished");
            }
            String line = scanner.nextLine().trim();

            int choice;
            try {
                choice = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
                continue;
            }

            if (choice < min || choice > max) {
                System.out.println("Please enter a number from " + min + " to " + max + ".");
                continue;
            }
            return choice;
        }
    }
}
