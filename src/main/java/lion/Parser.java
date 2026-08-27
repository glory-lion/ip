package lion;

/**
 * Extracts command types and task details from user input.
 */
public class Parser {
    /** Creates a parser for Lion commands. */
    public Parser() {
    }

    /**
     * Identifies the command type represented by the input.
     *
     * @param input complete command entered by the user.
     * @return matching command type, or {@link CommandType#UNKNOWN}.
     */
    public CommandType getCommandType(String input) {
        return CommandType.from(input);
    }

    /**
     * Extracts and trims the description from a todo command.
     *
     * @param input todo command entered by the user.
     * @return todo description, possibly empty.
     */
    public String getTodoDescription(String input) {
        return input.substring(4).trim();
    }

    /**
     * Separates a deadline command into its description and due date.
     *
     * @param input deadline command containing a {@code /by} separator.
     * @return description at index 0 and deadline text at index 1.
     */
    public String[] getDeadlineParts(String input) {
        String details = input.substring(9);
        return details.split(" /by ", 2);
    }

    /**
     * Separates an event command into its description, start, and end.
     *
     * @param input event command containing {@code /from} and {@code /to}.
     * @return description, start, and end in that order.
     */
    public String[] getEventParts(String input) {
        String details = input.substring(5).trim();
        String[] fromParts = details.split(" /from ", 2);
        String[] toParts = fromParts[1].split(" /to ", 2);

        String description = fromParts[0];
        String from = toParts[0];
        String to = toParts[1];

        return new String[] { description, from, to };
    }

    /**
     * Converts a one-based task number in a command to an array index.
     *
     * @param input command containing the task number.
     * @param prefixLength number of command-prefix characters to skip.
     * @return zero-based task index.
     */
    public int getTaskIndex(String input, int prefixLength) {
        String number = input.substring(prefixLength).trim();
        return Integer.parseInt(number) - 1;
    }
}
