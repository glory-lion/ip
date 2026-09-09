package lion;

/**
 * Extracts command types and task details from user input.
 */
public class Parser {
    /** Length of the "mark " command word, including its one trailing space. */
    public static final int MARK_PREFIX_LENGTH = 5;
    /** Length of the "unmark "/"delete " command words, including their trailing space. */
    public static final int UNMARK_OR_DELETE_PREFIX_LENGTH = 7;

    /** Length of the "todo" command word, before any trailing whitespace is trimmed. */
    private static final int TODO_PREFIX_LENGTH = 4;
    /** Length of the "deadline " command word, including its one trailing space. */
    private static final int DEADLINE_PREFIX_LENGTH = 9;
    /** Length of the "event" command word, before any trailing whitespace is trimmed. */
    private static final int EVENT_PREFIX_LENGTH = 5;
    /** Length of the "find" command word, before any trailing whitespace is trimmed. */
    private static final int FIND_PREFIX_LENGTH = 4;

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
        // Skipping exactly TODO_PREFIX_LENGTH characters assumes the command
        // word is "todo", which is only guaranteed once getCommandType has
        // classified the input as TODO, which Lion.java always checks before
        // calling this method.
        assert getCommandType(input) == CommandType.TODO
                : "getTodoDescription should only be called for todo commands";

        return input.substring(TODO_PREFIX_LENGTH).trim();
    }

    /**
     * Separates a deadline command into its description and due date.
     *
     * @param input deadline command containing a {@code /by} separator.
     * @return description at index 0 and deadline text at index 1.
     */
    public String[] getDeadlineParts(String input) {
        // Skipping exactly DEADLINE_PREFIX_LENGTH characters assumes the
        // command word is "deadline ", which only holds once getCommandType
        // has classified the input as DEADLINE.
        assert getCommandType(input) == CommandType.DEADLINE
                : "getDeadlineParts should only be called for deadline commands";

        String details = input.substring(DEADLINE_PREFIX_LENGTH);
        return details.split(" /by ", 2);
    }

    /**
     * Separates an event command into its description, start, and end.
     *
     * @param input event command containing {@code /from} and {@code /to}.
     * @return description, start, and end in that order.
     */
    public String[] getEventParts(String input) {
        // Skipping exactly EVENT_PREFIX_LENGTH characters assumes the command
        // word is "event", which only holds once getCommandType has
        // classified the input as EVENT.
        assert getCommandType(input) == CommandType.EVENT
                : "getEventParts should only be called for event commands";

        String details = input.substring(EVENT_PREFIX_LENGTH).trim();
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
        // Callers are expected to pass one of this class's own prefix-length
        // constants. Any other value would mean a caller is using this shared
        // helper incorrectly.
        assert prefixLength == MARK_PREFIX_LENGTH || prefixLength == UNMARK_OR_DELETE_PREFIX_LENGTH
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
        // Skipping exactly FIND_PREFIX_LENGTH characters assumes the command
        // word is "find", which only holds once getCommandType has
        // classified the input as FIND.
        assert getCommandType(input) == CommandType.FIND
                : "getFindKeyword should only be called for find commands";

        return input.substring(FIND_PREFIX_LENGTH).trim();
    }
}
