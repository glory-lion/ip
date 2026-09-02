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

        TaskList tasks;
        try {
            tasks = Storage.loadTaskList();
        } catch (IOException e) {
            System.out.println("    OOPS!!! Failed to load previous tasks: " + e.getMessage());
            tasks = new TaskList();
        }

        Parser parser = new Parser();
        String input = ui.readCommand();

        while (parser.getCommandType(input) != CommandType.BYE) {
            ui.showLine(line);

            try {
                CommandType command = parser.getCommandType(input);

                switch (command) {
                    case LIST:
                        System.out.println("    Here are the tasks in your list:");
                        for (int i = 0; i < tasks.size(); i++) {
                            System.out.println("    " + (i + 1) + "." + tasks.get(i));
                        }
                        break;

                    case TODO: {
                        String details = parser.getTodoDescription(input);

                        if (details.isEmpty()) {
                            throw new LionException(
                                    "The description of a todo cannot be empty.");
                        }

                        Task newTask = new Todo(details);
                        tasks.add(newTask);

                        System.out.println("    Got it. I've added this task:");
                        System.out.println("      " + newTask);
                        System.out.println(
                                "    Now you have " + tasks.size() + " tasks in the list");

                        try {
                            tasks.save();
                        } catch (IOException e) {
                            System.out.println(
                                    "    OOPS!!! Failed to save tasks: " + e.getMessage());
                        }
                        break;
                    }

                    case DEADLINE: {
                        String[] parts = parser.getDeadlineParts(input);
                        String description = parts[0];
                        String by = parts[1];

                        Task newTask = new Deadline(description, by);
                        tasks.add(newTask);

                        System.out.println("    Got it. I've added this task:");
                        System.out.println("      " + newTask);
                        System.out.println(
                                "    Now you have " + tasks.size() + " tasks in the list");

                        try {
                            tasks.save();
                        } catch (IOException e) {
                            System.out.println(
                                    "    OOPS!!! Failed to save tasks: " + e.getMessage());
                        }
                        break;
                    }

                    case EVENT: {
                        String[] parts = parser.getEventParts(input);

                        String description = parts[0];
                        String from = parts[1];
                        String to = parts[2];

                        Task newTask = new Event(description, from, to);
                        tasks.add(newTask);

                        System.out.println("    Got it. I've added this task:");
                        System.out.println("      " + newTask);
                        System.out.println(
                                "    Now you have " + tasks.size() + " tasks in the list");

                        try {
                            tasks.save();
                        } catch (IOException e) {
                            System.out.println(
                                    "    OOPS!!! Failed to save tasks: " + e.getMessage());
                        }
                        break;
                    }

                    case MARK: {
                        int taskNumber = parser.getTaskIndex(input, 5);
                        tasks.mark(taskNumber);

                        System.out.println("    Nice! I've marked this task as done:");
                        System.out.println(
                                "     [X] " + tasks.get(taskNumber).getDescription());

                        try {
                            tasks.save();
                        } catch (IOException e) {
                            System.out.println(
                                    "    OOPS!!! Failed to save tasks: " + e.getMessage());
                        }
                        break;
                    }

                    case UNMARK: {
                        int taskNumber = parser.getTaskIndex(input, 7);
                        tasks.unmark(taskNumber);

                        System.out.println(
                                "    OK! I've marked this task as not done yet:");
                        System.out.println(
                                "     [ ] " + tasks.get(taskNumber).getDescription());

                        try {
                            tasks.save();
                        } catch (IOException e) {
                            System.out.println(
                                    "    OOPS!!! Failed to save tasks: " + e.getMessage());
                        }
                        break;
                    }

                    case DELETE: {
                        int taskNumber = parser.getTaskIndex(input, 7);
                        Task deletedTask = tasks.delete(taskNumber);

                        System.out.println("    Noted. I've removed this task:");
                        System.out.println("      " + deletedTask);
                        System.out.println(
                                "    Now you have " + tasks.size() + " tasks in the list");

                        try {
                            tasks.save();
                        } catch (IOException e) {
                            System.out.println(
                                    "    OOPS!!! Failed to save tasks: " + e.getMessage());
                        }
                        break;
                    }

                    case FIND: {
                        String keyword = parser.getFindKeyword(input);

                        if (keyword.isEmpty()) {
                            throw new LionException("The find keyword cannot be empty.");
                        }

                        TaskList matches = tasks.find(keyword);

                        System.out.println(
                                "    Here are the matching tasks in your list:");

                        for (int i = 0; i < matches.size(); i++) {
                            System.out.println(
                                    "    " + (i + 1) + "." + matches.get(i));
                        }
                        break;
                    }

                    case UNKNOWN:
                        throw new LionException(
                                "I'm sorry, but I don't know what that means :-(");

                    case BYE:
                        break;

                    default:
                        throw new AssertionError(
                                "Unexpected command type: " + command);
                }
            } catch (LionException e) {
                System.out.println("    OOPS!!! " + e.getMessage());
            }

            ui.showLine(line);
            input = ui.readCommand();
        }

        ui.showGoodbye(line);
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
                    saveTasks();

                    return "Got it. I've added this task:\n"
                            + newTask
                            + "\nNow you have "
                            + tasks.size()
                            + " tasks in the list";
                }

                case DEADLINE: {
                    String[] parts = parser.getDeadlineParts(input);

                    Task newTask = new Deadline(parts[0], parts[1]);
                    tasks.add(newTask);
                    saveTasks();

                    return "Got it. I've added this task:\n"
                            + newTask
                            + "\nNow you have "
                            + tasks.size()
                            + " tasks in the list";
                }

                case EVENT: {
                    String[] parts = parser.getEventParts(input);

                    Task newTask = new Event(parts[0], parts[1], parts[2]);
                    tasks.add(newTask);
                    saveTasks();

                    return "Got it. I've added this task:\n"
                            + newTask
                            + "\nNow you have "
                            + tasks.size()
                            + " tasks in the list";
                }

                case MARK: {
                    int taskNumber = parser.getTaskIndex(input, 5);
                    tasks.mark(taskNumber);
                    saveTasks();

                    return "Nice! I've marked this task as done:\n"
                            + "[X] "
                            + tasks.get(taskNumber).getDescription();
                }

                case UNMARK: {
                    int taskNumber = parser.getTaskIndex(input, 7);
                    tasks.unmark(taskNumber);
                    saveTasks();

                    return "OK! I've marked this task as not done yet:\n"
                            + "[ ] "
                            + tasks.get(taskNumber).getDescription();
                }

                case DELETE: {
                    int taskNumber = parser.getTaskIndex(input, 7);
                    Task deletedTask = tasks.delete(taskNumber);
                    saveTasks();

                    return "Noted. I've removed this task:\n"
                            + deletedTask
                            + "\nNow you have "
                            + tasks.size()
                            + " tasks in the list";
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
     */
    private void saveTasks() {
        try {
            tasks.save();
        } catch (IOException e) {
            // Prevent the GUI from crashing if saving fails.
        }
    }
}
