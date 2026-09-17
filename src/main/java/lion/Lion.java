package lion;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Starts and coordinates the Lion task-management application.
 */
public class Lion {

    /** Prefix every error response begins with; see {@link #isErrorResponse(String)}. */
    private static final String ERROR_PREFIX = "ROAR!!! ";

    /** Header the {@code list} command's response begins with. */
    private static final String LIST_HEADER = "Here's what's in your pride:";

    /** Header the {@code find} command's response begins with. */
    private static final String FIND_HEADER = "Lion tracked down these matching tasks:";

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
                .mapToObj(i -> "\n" + (i + 1) + ". " + list.get(i))
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
            CommandType commandType = parser.getCommandType(input);

            // A switch expression, not a switch statement: the compiler checks this
            // covers every CommandType constant, so there is no default case, and
            // adding a new constant without a matching case here fails to compile
            // rather than throwing at runtime.
            return switch (commandType) {
                case LIST -> LIST_HEADER + formatNumberedList(tasks);
                case TODO -> handleTodo(input);
                case DEADLINE -> handleDeadline(input);
                case EVENT -> handleEvent(input);
                case MARK -> handleMark(input);
                case UNMARK -> handleUnmark(input);
                case DELETE -> handleDelete(input);
                case FIND -> handleFind(input);
                case HELP -> handleHelp();
                case BYE -> "Roar! Until next time.";
                case UNKNOWN -> throw new LionException(
                        "Lion didn't quite catch that.\n"
                        + "Type 'help' to see what I can do.");
            };

        } catch (LionException e) {
            return ERROR_PREFIX + e.getMessage();
        }
    }

    /**
     * Handles the {@code todo} command.
     *
     * @param input full user input.
     * @return response confirming the new task was added.
     * @throws LionException if the todo description is empty.
     */
    private String handleTodo(String input) throws LionException {
        String details = parser.getTodoDescription(input);

        if (details.isEmpty()) {
            throw new LionException("Even Lion needs something to chase — the todo description can't be empty.");
        }

        Task newTask = new Todo(details);
        tasks.add(newTask);

        return formatTaskAddedMessage(newTask);
    }

    /**
     * Handles the {@code deadline} command.
     *
     * @param input full user input.
     * @return response confirming the new task was added.
     */
    private String handleDeadline(String input) {
        String[] parts = parser.getDeadlineParts(input);

        Task newTask = new Deadline(parts[0], parts[1]);
        tasks.add(newTask);

        return formatTaskAddedMessage(newTask);
    }

    /**
     * Handles the {@code event} command.
     *
     * @param input full user input.
     * @return response confirming the new task was added.
     */
    private String handleEvent(String input) {
        String[] parts = parser.getEventParts(input);

        Task newTask = new Event(parts[0], parts[1], parts[2]);
        tasks.add(newTask);

        return formatTaskAddedMessage(newTask);
    }

    /**
     * Handles the {@code mark} command.
     *
     * @param input full user input.
     * @return response confirming the task was marked as done.
     */
    private String handleMark(String input) {
        int taskNumber = parser.getTaskIndex(input, Parser.MARK_PREFIX_LENGTH);
        tasks.mark(taskNumber);

        return "Lion's proud of you — task conquered:\n"
                + "[X] "
                + tasks.get(taskNumber).getDescription()
                + saveTasks();
    }

    /**
     * Handles the {@code unmark} command.
     *
     * @param input full user input.
     * @return response confirming the task was marked as not done.
     */
    private String handleUnmark(String input) {
        int taskNumber = parser.getTaskIndex(input, Parser.UNMARK_OR_DELETE_PREFIX_LENGTH);
        tasks.unmark(taskNumber);

        return "Back to the hunt — task reopened:\n"
                + "[ ] "
                + tasks.get(taskNumber).getDescription()
                + saveTasks();
    }

    /**
     * Handles the {@code delete} command.
     *
     * @param input full user input.
     * @return response confirming the task was removed.
     */
    private String handleDelete(String input) {
        int taskNumber = parser.getTaskIndex(input, Parser.UNMARK_OR_DELETE_PREFIX_LENGTH);
        Task deletedTask = tasks.delete(taskNumber);

        return "Lion's let this one go:\n"
                + deletedTask
                + "\nYour pride now has "
                + tasks.size()
                + " tasks"
                + saveTasks();
    }

    /**
     * Handles the {@code find} command.
     *
     * @param input full user input.
     * @return response listing the matching tasks.
     * @throws LionException if the find keyword is empty.
     */
    private String handleFind(String input) throws LionException {
        String keyword = parser.getFindKeyword(input);

        if (keyword.isEmpty()) {
            throw new LionException("Give Lion a scent to follow — the search keyword can't be empty.");
        }

        TaskList matches = tasks.find(keyword);

        return FIND_HEADER + formatNumberedList(matches);
    }

    /**
     * Handles the {@code help} command.
     *
     * @return response listing every available command and its description.
     */
    private static String handleHelp() {
        return "Here are the available commands:" + Arrays.stream(CommandType.values())
                .filter(type -> type != CommandType.UNKNOWN)
                .map(type -> "\n" + type.name().toLowerCase() + " - " + type.getDescription())
                .collect(Collectors.joining());
    }

    /**
     * Formats the confirmation message shown after a task is added.
     *
     * @param newTask task that was just added.
     * @return confirmation message, including a save-failure notice if saving failed.
     */
    private String formatTaskAddedMessage(Task newTask) {
        return "Roar! Added to your pride:\n"
                + newTask
                + "\nYour pride now has "
                + tasks.size()
                + " tasks"
                + saveTasks();
    }

    /**
     * Returns whether a response produced by {@link #getResponse(String)} represents an error.
     *
     * @param response response text to check.
     * @return true if the response is a standalone error message, i.e. it begins with the
     *     standard error prefix rather than merely mentioning a failure partway through
     *     an otherwise successful response (e.g. a save failure appended after a task
     *     was still added).
     */
    public static boolean isErrorResponse(String response) {
        return response.startsWith(ERROR_PREFIX);
    }

    /**
     * Returns whether a response produced by {@link #getResponse(String)} is a numbered
     * task list (from {@code list} or {@code find}), so the GUI can give it a visually
     * distinct, scan-friendly style from ordinary conversational replies.
     *
     * @param response response text to check.
     * @return true if the response begins with the {@code list} or {@code find} header.
     */
    public static boolean isTaskListResponse(String response) {
        return response.startsWith(LIST_HEADER) || response.startsWith(FIND_HEADER);
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
            return "\n" + ERROR_PREFIX + "Lion couldn't stash your tasks safely: " + e.getMessage();
        }
    }
}
