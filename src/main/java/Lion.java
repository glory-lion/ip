import java.util.Scanner;

public class Lion {
    public static void main(String[] args) {
        String banner = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        String line = " ________________________________";
        System.out.println(banner);
        System.out.println(line);
        System.out.println("    Hello! I'm Lion.");
        System.out.println("    What can I do for you?");
        System.out.println(line);

        Task[] list = new Task[100];
        int taskCount = 0;
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equals("bye")) {
            System.out.println(line);
            try {
                if (input.equals("list")) {
                    System.out.println("    Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println("    " + (i + 1) + "." + list[i]);
                    }
                } else if (input.startsWith("todo")) {
                    String details = input.substring(4).trim();
                    if (details.isEmpty()) {
                        throw new LionException("The description of a todo cannot be empty.");
                    }
                    list[taskCount] = new Todo(details);
                    taskCount++;

                    System.out.println("    Got it. I've added this task:");
                    System.out.println("      " + list[taskCount - 1]);
                    System.out.println("    Now you have " + taskCount + " tasks in the list");

                } else if (input.startsWith("deadline")) {
                    String details = input.substring(9);
                    String[] parts = details.split(" /by ");

                    String description = parts[0];
                    String by = parts[1];

                    list[taskCount] = new Deadline(description, by);
                    taskCount++;

                    System.out.println("    Got it. I've added this task:");
                    System.out.println("      " + list[taskCount - 1]);
                    System.out.println("    Now you have " + taskCount + " tasks in the list");
                } else if (input.startsWith("event")) {
                    String details = input.substring(5).trim();
                    String[] fromParts = details.split(" /from ");
                    String[] toParts = fromParts[1].split(" /to ");

                    String description = fromParts[0];
                    String from = toParts[0];
                    String to = toParts[1];


                    list[taskCount] = new Event(description, from, to);
                    taskCount++;

                    System.out.println("    Got it. I've added this task:");
                    System.out.println("      " + list[taskCount - 1]);
                    System.out.println("    Now you have " + taskCount + " tasks in the list");
                } else if (input.startsWith("mark")) {
                    String number = input.substring(5);
                    int taskNumber = Integer.parseInt(number) - 1;
                    list[taskNumber].markAsDone();

                    System.out.println("    Nice! I've marked this task as done:");
                    System.out.println("     [X] " + list[taskNumber].getDescription());

                } else if (input.startsWith("unmark")) {
                    String number = input.substring(7);
                    int taskNumber = Integer.parseInt(number) - 1;
                    list[taskNumber].markAsNotDone();

                    System.out.println("    OK! I've marked this task as not done yet:");
                    System.out.println("     [ ] " + list[taskNumber].getDescription());
                } else if(input.startsWith("delete")) {
                    String number = input.substring(7);
                    int taskNumber = Integer.parseInt(number) - 1;
                    Task deletedTask = list[taskNumber];
                    for(int i = taskNumber; i < taskCount - 1; i++) {
                        list[i] = list[i + 1];
                    }
                    taskCount--;
                    list[taskCount] = null;
                    System.out.println("    Noted. I've removed this task:");
                    System.out.println("      " + deletedTask);
                    System.out.println("    Now you have " + taskCount + " tasks in the list");
                }
                else {
                    throw new LionException("I'm sorry, but I don't know what that means :-(");
                }
            }
            catch (LionException e){
                System.out.println("    OOPS!!! " + e.getMessage());
            }

            System.out.println(line);
            input = scanner.nextLine();
        }
        System.out.println(line);
        System.out.println("    Bye. Hope to see you again soon!");
        System.out.println(line);


    }
}
