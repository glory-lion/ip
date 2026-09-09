package lion;

import java.io.IOException;

/**
 * Starts and coordinates the Lion task-management application.
 */
public class Lion {

    private TaskList tasks;
    private Parser parser;

    /**
     * Creates the application entry-point object.
     */
    public Lion() {
        parser = new Parser();

        try {
            tasks = Storage.loadTaskList();
        } catch (IOException e) {
            tasks = new TaskList();
        }
    }

    /**
     * Runs the command loop until the user enters the {@code bye} command.
     *
     * <p>Command handling itself lives in {@link #getResponse(String)} so that
     * the CLI and the GUI share exactly one implementation of each command;
     * this method only adds the CLI's four-space message margin on top of it.
     *
     * @param args command-line arguments; currently unused.
     */
    public static void main(String[] args) {
        String banner = " _     _             \n"
                + "| |   (_) ___  _ __  \n"
                + "| |   | |/ _ \\| '_ \\ \n"
                + "| |___| | (_) | | | |\n"
                + "|_____|_|\\___/|_| |_|\n";
        String line = " ________________________________";

        Ui ui = new Ui();
        ui.showWelcome(banner, line);

        Lion lion = new Lion();
        String input = ui.readCommand();

        while (CommandType.from(input) != CommandType.BYE) {
            ui.showLine(line);

            String response = lion.getResponse(input);
            ui.showMessage(indentForCli(response));

            ui.showLine(line);
            input = ui.readCommand();
        }

        ui.showGoodbye(line);
    }

    /**
     * Adds the CLI's four-space message margin to every line of a response.
     *
     * @param response response produced by {@link #getResponse(String)}.
     * @return each line of the response prefixed with a four-space margin.
     */
    private static String[] indentForCli(String response) {
        String[] lines = response.split("\n");
        String[] indentedLines = new String[lines.length];
        for (int i = 0; i < lines.length; i++) {
            indentedLines[i] = "    " + lines[i];
        }
        return indentedLines;
    }

    /**
     * Generates a response to the user's command for the GUI.
     *
     * @param input user's input
     * @return Lion's response
     */
    public String getResponse(String input) {
        try {
            CommandType command = parser.getCommandType(input);

            switch (command) {
                case LIST:
                    StringBuilder listResponse =
                            new StringBuilder("Here are the tasks in your list:");

                    for (int i = 0; i < tasks.size(); i++) {
                        listResponse.append("\n")
                                .append(i + 1)
                                .append(".")
                                .append(tasks.get(i));
                    }

                    return listResponse.toString();

                case TODO: {
                    String details = parser.getTodoDescription(input);

                    if (details.isEmpty()) {
                        throw new LionException(
                                "The description of a todo cannot be empty.");
                    }

                    Task newTask = new Todo(details);
                    tasks.add(newTask);

                    return "Got it. I've added this task:\n"
                            + newTask
                            + "\nNow you have "
                            + tasks.size()
                            + " tasks in the list"
                            + saveTasks();
                }

                case DEADLINE: {
                    String[] parts = parser.getDeadlineParts(input);

                    Task newTask = new Deadline(parts[0], parts[1]);
                    tasks.add(newTask);

                    return "Got it. I've added this task:\n"
                            + newTask
                            + "\nNow you have "
                            + tasks.size()
                            + " tasks in the list"
                            + saveTasks();
                }

                case EVENT: {
                    String[] parts = parser.getEventParts(input);

                    Task newTask = new Event(parts[0], parts[1], parts[2]);
                    tasks.add(newTask);

                    return "Got it. I've added this task:\n"
                            + newTask
                            + "\nNow you have "
                            + tasks.size()
                            + " tasks in the list"
                            + saveTasks();
                }

                case MARK: {
                    int taskNumber = parser.getTaskIndex(input, 5);
                    tasks.mark(taskNumber);

                    return "Nice! I've marked this task as done:\n"
                            + "[X] "
                            + tasks.get(taskNumber).getDescription()
                            + saveTasks();
                }

                case UNMARK: {
                    int taskNumber = parser.getTaskIndex(input, 7);
                    tasks.unmark(taskNumber);

                    return "OK! I've marked this task as not done yet:\n"
                            + "[ ] "
                            + tasks.get(taskNumber).getDescription()
                            + saveTasks();
                }

                case DELETE: {
                    int taskNumber = parser.getTaskIndex(input, 7);
                    Task deletedTask = tasks.delete(taskNumber);

                    return "Noted. I've removed this task:\n"
                            + deletedTask
                            + "\nNow you have "
                            + tasks.size()
                            + " tasks in the list"
                            + saveTasks();
                }

                case FIND: {
                    String keyword = parser.getFindKeyword(input);

                    if (keyword.isEmpty()) {
                        throw new LionException(
                                "The find keyword cannot be empty.");
                    }

                    TaskList matches = tasks.find(keyword);

                    StringBuilder findResponse =
                            new StringBuilder(
                                    "Here are the matching tasks in your list:");

                    for (int i = 0; i < matches.size(); i++) {
                        findResponse.append("\n")
                                .append(i + 1)
                                .append(".")
                                .append(matches.get(i));
                    }

                    return findResponse.toString();
                }

                case BYE:
                    return "Bye. Hope to see you again soon!";

                case UNKNOWN:
                    throw new LionException(
                            "I'm sorry, but I don't know what that means :-(");

                default:
                    throw new AssertionError(
                            "Unexpected command type: " + command);
            }

        } catch (LionException e) {
            return "OOPS!!! " + e.getMessage();
        }
    }

    /**
     * Saves the current task list.
     *
     * @return a save-failure notice to append to the response, or an empty
     *     string if the tasks were saved successfully.
     */
    private String saveTasks() {
        try {
            tasks.save();
            return "";
        } catch (IOException e) {
            return "\nOOPS!!! Failed to save tasks: " + e.getMessage();
        }
    }
}
