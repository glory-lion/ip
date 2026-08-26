package lion;

/**
 * Represents the commands that Lion understands.
 */
public enum CommandType {
    LIST,
    TODO,
    DEADLINE,
    EVENT,
    MARK,
    UNMARK,
    DELETE,
    BYE,
    UNKNOWN;

    /**
     * Identifies the command represented by the first word of the user's input.
     *
     * @param input full line entered by the user
     * @return the matching command, or {@link #UNKNOWN} if there is no match
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
