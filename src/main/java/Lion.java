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

        String[] list = new String[100];
        int taskCount = 0;
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equals("bye")) {
            System.out.println(line);

            if(input.equals("list")) {
                for(int i = 0; i < taskCount; i++) {
                    System.out.println("    " + (i + 1) + ". " + list[i]);
                }
            }
            else {
                list[taskCount] = input;
                taskCount++;

                System.out.println("    added: " + input);
            }
            System.out.println(line);
            input = scanner.nextLine();
        }
        System.out.println(line);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(line);


    }
}
