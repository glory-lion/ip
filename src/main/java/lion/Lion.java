package lion;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        return Arrays.stream(response.split("\n"))
                .map(responseLine -> "    " + responseLine)
                .toArray(String[]::new);
    }

    /**
     * Formats a task list as a one-based numbered list, one task per line.
     *
     * <p>Used by both the {@code list} and {@code find} commands, which only
     * differ in their header text and which task list they number.
     *
     * @param list tasks to format.
     * @return each task on its own line prefixed with "\n" and its 1-based
     *     number, or an empty string if the list has no tasks.
     */
    private static String formatNumberedList(TaskList list) {
        return IntStream.range(0, list.size())
                .mapToObj(i -> "\n" + (i + 1) + "." + list.get(i))
                .collect(Collectors.joining());
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
                    return "Here are the tasks in your list:" + formatNumberedList(tasks);

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
                    int taskNumber = parser.getTaskIndex(input, Parser.MARK_PREFIX_LENGTH);
                    tasks.mark(taskNumber);

                    return "Nice! I've marked this task as done:\n"
                            + "[X] "
                            + tasks.get(taskNumber).getDescription()
                            + saveTasks();
                }

                case UNMARK: {
                    int taskNumber = parser.getTaskIndex(input, Parser.UNMARK_OR_DELETE_PREFIX_LENGTH);
                    tasks.unmark(taskNumber);

                    return "OK! I've marked this task as not done yet:\n"
                            + "[ ] "
                            + tasks.get(taskNumber).getDescription()
                            + saveTasks();
                }

                case DELETE: {
                    int taskNumber = parser.getTaskIndex(input, Parser.UNMARK_OR_DELETE_PREFIX_LENGTH);
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

                    return "Here are the matching tasks in your list:" + formatNumberedList(matches);
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
