package lion;

import java.util.Scanner;

/**
 * Handles terminal input and output for the Lion application.
 */
public class Ui {
    private final Scanner scanner;

    /** Creates a user interface that reads commands from standard input. */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the application banner and welcome message.
     *
     * @param banner application banner.
     * @param line separator used around messages.
     */
    public void showWelcome(String banner, String line) {
        System.out.println(banner);
        System.out.println(line);
        System.out.println("    Hello! I'm lion.Lion.");
        System.out.println("    What can I do for you?");
        System.out.println(line);
    }

    /**
     * Displays a message separator.
     *
     * @param line separator to display.
     */
    public void showLine(String line) {
        System.out.println(line);
    }

    /**
     * Displays a message to the user.
     *
     * @param message message to display.
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Reads the next complete command from standard input.
     *
     * @return command entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the farewell message.
     *
     * @param line separator used around the message.
     */
    public void showGoodbye(String line) {
        System.out.println(line);
        System.out.println("    Bye. Hope to see you again soon!");
        System.out.println(line);
    }
}
