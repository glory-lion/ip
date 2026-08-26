import lion.*;

import java.io.IOException;

public class Lion {
    public static void main(String[] args) {
        String banner = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        String line = " ________________________________";

        Ui ui = new Ui();
        ui.showWelcome(banner, line);

        TaskList tasks;
        try{
            tasks = Storage.loadTaskList();
        }
        catch(IOException e) {
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
                        throw new LionException("The description of a todo cannot be empty.");
                    }
                    Task newTask = new Todo(details);
                    tasks.add(newTask);

                    System.out.println("    Got it. I've added this task:");
                    System.out.println("      " + newTask);
                    System.out.println("    Now you have " + tasks.size() + " tasks in the list");

                    try {
                        tasks.save();
                    } catch (IOException e) {
                        System.out.println("    OOPS!!! Failed to save tasks: " + e.getMessage());
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
                    System.out.println("    Now you have " + tasks.size() + " tasks in the list");

                    try {
                        tasks.save();
                    } catch (IOException e) {
                        System.out.println("    OOPS!!! Failed to save tasks: " + e.getMessage());
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
                    System.out.println("    Now you have " + tasks.size() + " tasks in the list");

                    try {
                        tasks.save();
                    } catch (IOException e) {
                        System.out.println("    OOPS!!! Failed to save tasks: " + e.getMessage());
                    }
                    break;
                }
                case MARK: {
                    int taskNumber = parser.getTaskIndex(input, 5);
                    tasks.mark(taskNumber);

                    System.out.println("    Nice! I've marked this task as done:");
                    System.out.println("     [X] " + tasks.get(taskNumber).getDescription());

                    try {
                        tasks.save();
                    } catch (IOException e) {
                        System.out.println("    OOPS!!! Failed to save tasks: " + e.getMessage());
                    }
                    break;

                }
                case UNMARK: {
                    int taskNumber = parser.getTaskIndex(input, 7);
                    tasks.unmark(taskNumber);

                    System.out.println("    OK! I've marked this task as not done yet:");
                    System.out.println("     [ ] " + tasks.get(taskNumber).getDescription());

                    try {
                        tasks.save();
                    } catch (IOException e) {
                        System.out.println("    OOPS!!! Failed to save tasks: " + e.getMessage());
                    }
                    break;
                }
                case DELETE: {
                    int taskNumber = parser.getTaskIndex(input, 7);
                    Task deletedTask = tasks.delete(taskNumber);

                    System.out.println("    Noted. I've removed this task:");
                    System.out.println("      " + deletedTask);
                    System.out.println("    Now you have " + tasks.size() + " tasks in the list");
                    try {
                        tasks.save();
                    } catch (IOException e) {
                        System.out.println("    OOPS!!! Failed to save tasks: " + e.getMessage());
                    }
                    break;
                }
                case UNKNOWN:
                    throw new LionException("I'm sorry, but I don't know what that means :-(");
                case BYE:
                    break;
                }
            } catch (LionException e) {
                System.out.println("    OOPS!!! " + e.getMessage());
            }

            ui.showLine(line);
            input = ui.readCommand();
        }
        ui.showGoodbye(line);


    }
}
