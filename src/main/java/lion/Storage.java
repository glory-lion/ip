package lion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Saves tasks to disk and restores them between application runs.
 *
 * <p>All members are static; this class is not meant to be instantiated.
 */
public final class Storage {
    private static final String DIRECTORY_PATH = "data";
    private static final String FILE_PATH = DIRECTORY_PATH + File.separator + "lion.txt";

    private Storage() {
    }

    /**
     * Writes the active tasks to the application's data file.
     *
     * @param tasks tasks to save.
     * @throws IOException if the data directory or file cannot be written.
     */
    public static void save(List<Task> tasks) throws IOException {
        assert tasks != null : "task list must not be null";

        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Built as one string, rather than writing each line separately, so that
        // the only place in this method that can throw IOException is the single
        // write() call below; encode() itself never throws.
        String content = tasks.stream()
                .map(task -> encode(task) + System.lineSeparator())
                .collect(Collectors.joining());

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(content);
        }
    }

    /**
     * Loads all tasks saved in the application's data file.
     *
     * @return restored tasks, in the order they were saved, or an empty list
     *     if no save file exists.
     * @throws IOException if the save file exists but cannot be read.
     */
    public static List<Task> load() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        List<Task> tasks = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                tasks.add(decode(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            throw new IOException("Save file not found", e);
        }
        return tasks;
    }

    /**
     * Creates a task list containing all tasks currently stored on disk.
     *
     * @return restored task list, or an empty list when no save file exists.
     * @throws IOException if the save file cannot be read.
     */
    public static TaskList loadTaskList() throws IOException {
        return new TaskList(load());
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
     * <p>Each line is pipe-delimited as produced by {@link #encode(Task)},
     * e.g. {@code T | 1 | buy book} for a completed todo.
     *
     * @param line one encoded task from the save file.
     * @return restored task.
     */
    static Task decode(String line) {
        String[] parts = line.split("\\s*\\|\\s*", -1);
        // Every line decoded here was written by encode() in this same class, so
        // it must have at least the type, status, and description fields. A
        // shorter line means the save file was corrupted or edited by hand.
        assert parts.length >= 3
                : "encoded task line must have at least type, status, and description fields";

        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task;
        switch (type) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                assert parts.length >= 4 : "encoded deadline must include a 'by' field";
                task = new Deadline(description, parts[3]);
                break;
            case "E":
                assert parts.length >= 5 : "encoded event must include 'from' and 'to' fields";
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
