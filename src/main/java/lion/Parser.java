package lion;

public class Parser {
    public CommandType getCommandType(String input) {
        return CommandType.from(input);
    }

    public String getTodoDescription(String input) {
        return input.substring(4).trim();
    }

    public String[] getDeadlineParts(String input) {
        String details = input.substring(9);
        return details.split(" /by ", 2);
    }

    public String[] getEventParts(String input) {
        String details = input.substring(5).trim();
        String[] fromParts = details.split(" /from ", 2);
        String[] toParts = fromParts[1].split(" /to ", 2);

        String description = fromParts[0];
        String from = toParts[0];
        String to = toParts[1];

        return new String[] { description, from, to };
    }

    public int getTaskIndex(String input, int prefixLength) {
        String number = input.substring(prefixLength).trim();
        return Integer.parseInt(number) - 1;
    }
}
