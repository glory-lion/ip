package lion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Saves tasks to disk and restores them between application runs.
 */
public class Storage {
    private static final String DIRECTORY_PATH = "data";
    private static final String FILE_PATH = DIRECTORY_PATH + File.separator + "lion.txt";

    /** Creates a storage helper that uses the application's default data file. */
    public Storage() {
    }

    /**
     * Writes the active tasks to the application's data file.
     *
     * @param tasks array containing the tasks to save.
     * @param taskCount number of active tasks in the array.
     * @throws IOException if the data directory or file cannot be written.
     */
    public static void save(Task[] tasks, int taskCount) throws IOException {
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            for (int i = 0; i < taskCount; i++) {
                writer.write(encode(tasks[i]));
                writer.write(System.lineSeparator());
            }
        }
    }

    /**
     * Loads saved tasks into the supplied array.
     *
     * @param tasks destination array for restored tasks.
     * @return number of tasks loaded.
     * @throws IOException if the save file exists but cannot be read.
     */
    public static int load(Task[] tasks) throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return 0;
        }

        int count = 0;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine(); // T | 1 | buy boook
                tasks[count] = decode(line); // turn into task object
                count++;
            }
        } catch (FileNotFoundException e) {
            throw new IOException("Save file not found", e);
        }
        return count;
    }

    /**
     * Creates a task list containing all tasks currently stored on disk.
     *
     * @return restored task list, or an empty list when no save file exists.
     * @throws IOException if the save file cannot be read.
     */
    public static TaskList loadTaskList() throws IOException {
        Task[] tasks = new Task[100];
        int count = load(tasks);
        return new TaskList(tasks, count);
    }

    /**
     * Converts a task to the stable, machine-readable representation used in the save file.
     *
     * @param task task to encode.
     * @return one line suitable for the save file.
     */
    static String encode(Task task) {
        String prefix = task.getTypeIcon() + " | " + task.getStatusIcon()
                + " | " + task.getDescription();
        if (task instanceof Deadline) {
            return prefix + " | " + ((Deadline) task).getByForStorage();
        }
        if (task instanceof Event) {
            Event event = (Event) task;
            return prefix + " | " + event.getFrom() + " | " + event.getTo();
        }
        return prefix;
    }

    /**
     * Restores one task from its machine-readable save-file representation.
     *
     * @param line one encoded task from the save file.
     * @return restored task.
     */
    static Task decode(String line) {
        String[] parts = line.split("\\s*\\|\\s*", -1);
        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task;
        switch (type) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                task = new Deadline(description, parts[3]);
                break;
            case "E":
                task = new Event(description, parts[3], parts[4]);
                break;
            default:
                task = new Todo(description);
                break;
        }
        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
