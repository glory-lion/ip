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

            if(input.equals("list")) {
                System.out.println("    Here are the tasks in your list:");
                for(int i = 0; i < taskCount; i++) {
                    System.out.println("    " + (i + 1) + ".["  + list[i].getStatusIcon() + "] " + list[i].getDescription());
                }
            }
            else if(input.startsWith("mark")) {
                String number = input.substring(5);
                int taskNumber = Integer.parseInt(number) - 1;
                list[taskNumber].markAsDone();

                System.out.println("    Nice! I've marked this task as done:");
                System.out.println("     [X] " + list[taskNumber].getDescription());

            }
            else if(input.startsWith("unmark")) {
                String number = input.substring(7);
                int taskNumber = Integer.parseInt(number) - 1;
                list[taskNumber].markAsNotDone();

                System.out.println("    OK! I've marked this task as not done yet:");
                System.out.println("     [ ] " + list[taskNumber].getDescription());
            }
            else {
                list[taskCount] = new Task(input);
                taskCount++;

                System.out.println("    added: " + input);
            }
            System.out.println(line);
            input = scanner.nextLine();
        }
        System.out.println(line);
        System.out.println("    Bye. Hope to see you again soon!");
        System.out.println(line);


    }
}
