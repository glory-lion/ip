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
        // input.substring(4) assumes the command word is exactly "todo" (4 chars).
        // That is only guaranteed once getCommandType has classified the input as
        // TODO, which Lion.java always checks before calling this method.
        assert getCommandType(input) == CommandType.TODO
                : "getTodoDescription should only be called for todo commands";

        return input.substring(4).trim();
    }

    /**
     * Separates a deadline command into its description and due date.
     *
     * @param input deadline command containing a {@code /by} separator.
     * @return description at index 0 and deadline text at index 1.
     */
    public String[] getDeadlineParts(String input) {
        // input.substring(9) assumes the command word is exactly "deadline "
        // (8 letters + 1 space), which only holds once getCommandType has
        // classified the input as DEADLINE.
        assert getCommandType(input) == CommandType.DEADLINE
                : "getDeadlineParts should only be called for deadline commands";

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
        // input.substring(5) assumes the command word is exactly "event"
        // (5 chars), which only holds once getCommandType has classified the
        // input as EVENT.
        assert getCommandType(input) == CommandType.EVENT
                : "getEventParts should only be called for event commands";

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
        // Callers pass the length of their own command word plus one space:
        // "mark " (5) or "unmark "/"delete " (7). Any other value would mean a
        // caller is using this shared helper incorrectly.
        assert prefixLength == 5 || prefixLength == 7
                : "prefixLength must match the 'mark '/'unmark '/'delete ' prefix";

        String number = input.substring(prefixLength).trim();
        return Integer.parseInt(number) - 1;
    }

    /**
     * Extracts and trims the keyword from a find command.
     *
     * @param input find command entered by the user.
     * @return search keyword, possibly empty.
     */
    public String getFindKeyword(String input) {
        // input.substring(4) assumes the command word is exactly "find"
        // (4 chars), which only holds once getCommandType has classified the
        // input as FIND.
        assert getCommandType(input) == CommandType.FIND
                : "getFindKeyword should only be called for find commands";

        return input.substring(4).trim();
    }
}
