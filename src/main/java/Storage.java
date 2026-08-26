import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Storage {
    private static final String DIRECTORY_PATH = "data";
    private static final String FILE_PATH = DIRECTORY_PATH + File.separator + "lion.txt";

    public static void save(Task[] tasks, int taskCount) throws IOException {
        File directory = new File(DIRECTORY_PATH);
        if(!directory.exists()) {
            directory.mkdirs();
        }

        try(FileWriter writer = new FileWriter(FILE_PATH)) {
            for(int i = 0; i < taskCount; i++) {
                writer.write(tasks[i].toString());
                writer.write(System.lineSeparator());
            }
        }
    }

    public static int load(Task[] tasks) throws IOException {
        File file = new File(FILE_PATH);
        if(!file.exists()) {
            return 0;
        }

        int count = 0;
        try(Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine(); // T | 1 | buy boook
                tasks[count] = decode(line); // turn into task object
                count++;
            }
        }
        catch (FileNotFoundException e) {
            throw new IOException("Save file not found", e);
        }
        return count;
    }

    public static TaskList loadTaskList() throws IOException {
        Task[] tasks = new Task[100];
        int count = load(tasks);
        return new TaskList(tasks, count);
    }

    private static Task decode(String line) {
        String[] parts = line.split("\\s*\\|\\s*");
        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task;
        switch(type) {
            case "T": task = new Todo(description); break;
            case "D": task = new Deadline(description, ""); break;
            case "E": task = new Event(description, "", ""); break;
            default:  task = new Todo(description);
        }
        if(isDone) {
            task.markAsDone();
        }
        return task;
    }
}

