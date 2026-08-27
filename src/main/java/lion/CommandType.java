package lion;

/**
 * Represents the commands that lion.Lion understands.
 */
public enum CommandType {
    /** Displays all saved tasks. */
    LIST,
    /** Adds a task without date or time details. */
    TODO,
    /** Adds a task with a due date and time. */
    DEADLINE,
    /** Adds a task with start and end details. */
    EVENT,
    /** Marks a task as complete. */
    MARK,
    /** Marks a task as incomplete. */
    UNMARK,
    /** Removes a task. */
    DELETE,
    /** Exits the application. */
    BYE,
    /** Represents input that does not match a supported command. */
    UNKNOWN;

    /**
     * Identifies the command represented by the first word of the user's input.
     *
     * @param input full line entered by the user.
     * @return the matching command, or {@link #UNKNOWN} if there is no match.
     */
    public static CommandType from(String input) {
        String trimmedInput = input.trim();
        if (trimmedInput.isEmpty()) {
            return UNKNOWN;
        }

        String commandWord = trimmedInput.split("\\s+", 2)[0];
        try {
            return CommandType.valueOf(commandWord.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
