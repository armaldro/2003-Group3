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

    /**
     * True when the input is piped from a file. Nothing is typed in a piped run,
     * so each choice read is printed after its prompt to keep the output readable.
     */
    private final boolean echo;

    /**
     * Reads from the keyboard, or from a file piped in. When the input is piped
     * (there is no interactive console), each choice read is printed back.
     */
    public InputReader() {
        this(new Scanner(System.in), System.console() == null);
    }

    /**
     * Reads from the given Scanner, so tests can pass in text such as
     * {@code new Scanner("abc\n9\n2\n")}. Choices are not printed back.
     *
     * @param scanner where to read from
     */
    public InputReader(Scanner scanner) {
        this(scanner, false);
    }

    /**
     * Reads from the given Scanner, optionally printing back each line it reads.
     *
     * @param scanner where to read from
     * @param echo    true to print each line read, for piped replay runs
     */
    public InputReader(Scanner scanner, boolean echo) {
        this.scanner = scanner;
        this.echo = echo;
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
            if (echo) {
                System.out.println(line);
            }

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
