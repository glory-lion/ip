package lion;

import java.io.IOException;

/**
 * Manages the ordered collection of tasks used by the application.
 */
public class TaskList {
    /** Maximum number of tasks the fixed-size backing array can hold. */
    public static final int MAX_TASKS = 100;

    private final Task[] tasks;
    private int size;

    /** Creates an empty task list with capacity for {@value #MAX_TASKS} tasks. */
    public TaskList() {
        this.tasks = new Task[MAX_TASKS];
        this.size = 0;
    }

    /**
     * Creates a task list backed by an existing task array.
     *
     * @param tasks array containing existing tasks.
     * @param size number of active tasks in the array.
     */
    public TaskList(Task[] tasks, int size) {
        // Callers (currently only Storage.loadTaskList) are expected to pass a
        // matching array and count; a mismatch would mean the loader is broken.
        assert tasks != null : "backing task array must not be null";
        assert size >= 0 && size <= tasks.length
                : "size must be between 0 and the backing array's capacity";

        this.tasks = tasks;
        this.size = size;
    }

    /**
     * Returns the number of active tasks.
     *
     * @return number of tasks.
     */
    public int size() {
        return size;
    }

    /**
     * Returns the task at the specified zero-based index.
     *
     * @param index zero-based task index.
     * @return task at the index.
     */
    public Task get(int index) {
        // Callers are expected to only request indices within the active range;
        // an out-of-range index within array capacity would otherwise silently
        // return null instead of failing loudly.
        assert index >= 0 && index < size : "index out of range for current tasks";

        return tasks[index];
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task task to add.
     */
    public void add(Task task) {
        // A null task would be added silently and only fail much later (e.g. when
        // its description is read), far from the real cause. Also document the
        // fixed-array capacity assumption relied on throughout this class.
        assert task != null : "task to add must not be null";
        assert size < tasks.length : "task list is already at capacity";

        tasks[size] = task;
        size++;
    }

    /**
     * Removes and returns a task while keeping the remaining tasks contiguous.
     *
     * @param index zero-based index of the task to remove.
     * @return removed task.
     */
    public Task delete(int index) {
        assert index >= 0 && index < size : "index out of range for current tasks";

        Task deleted = tasks[index];
        for (int i = index; i < size - 1; i++) {
            tasks[i] = tasks[i + 1];
        }
        size--;
        tasks[size] = null;
        return deleted;
    }

    /**
     * Marks the task at the specified index as complete.
     *
     * @param index zero-based task index.
     */
    public void mark(int index) {
        // Without this, an out-of-range index that still falls within array
        // capacity would fail with a confusing NullPointerException instead.
        assert index >= 0 && index < size : "index out of range for current tasks";

        tasks[index].markAsDone();
    }

    /**
     * Marks the task at the specified index as incomplete.
     *
     * @param index zero-based task index.
     */
    public void unmark(int index) {
        assert index >= 0 && index < size : "index out of range for current tasks";

        tasks[index].markAsNotDone();
    }

    /**
     * Returns tasks whose descriptions contain the given keyword.
     * Matching is case-sensitive and preserves the original task order.
     *
     * @param keyword keyword to find in task descriptions.
     * @return task list containing the matching tasks.
     */
    public TaskList find(String keyword) {
        assert keyword != null : "search keyword must not be null";

        TaskList matches = new TaskList();
        for (int i = 0; i < size; i++) {
            if (tasks[i].getDescription().contains(keyword)) {
                matches.add(tasks[i]);
            }
        }
        return matches;
    }

    /**
     * Saves the active tasks to disk.
     *
     * @throws IOException if the tasks cannot be written.
     */
    public void save() throws IOException {
        Storage.save(tasks, size);
    }

}
