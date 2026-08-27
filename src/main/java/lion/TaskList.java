package lion;

import java.io.IOException;

/**
 * Manages the ordered collection of tasks used by the application.
 */
public class TaskList {
    private final Task[] tasks;
    private int size;

    /** Creates an empty task list with capacity for 100 tasks. */
    public TaskList() {
        this.tasks = new Task[100];
        this.size = 0;
    }

    /**
     * Creates a task list backed by an existing task array.
     *
     * @param tasks array containing existing tasks
     * @param size number of active tasks in the array
     */
    public TaskList(Task[] tasks, int size) {
        this.tasks = tasks;
        this.size = size;
    }

    /**
     * Returns the number of active tasks.
     *
     * @return number of tasks
     */
    public int size() {
        return size;
    }

    /**
     * Returns the task at the specified zero-based index.
     *
     * @param index zero-based task index
     * @return task at the index
     */
    public Task get(int index) {
        return tasks[index];
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task task to add
     */
    public void add(Task task) {
        tasks[size] = task;
        size++;
    }

    /**
     * Removes and returns a task while keeping the remaining tasks contiguous.
     *
     * @param index zero-based index of the task to remove
     * @return removed task
     */
    public Task delete(int index) {
        Task deleted = tasks[index];
        for(int i = index; i < size - 1; i++) {
            tasks[i] = tasks[i + 1];
        }
        size--;
        tasks[size] = null;
        return deleted;
    }

    /**
     * Marks the task at the specified index as complete.
     *
     * @param index zero-based task index
     */
    public void mark(int index) {
        tasks[index].markAsDone();
    }

    /**
     * Marks the task at the specified index as incomplete.
     *
     * @param index zero-based task index
     */
    public void unmark(int index) {
        tasks[index].markAsNotDone();
    }

    /**
     * Saves the active tasks to disk.
     *
     * @throws IOException if the tasks cannot be written
     */
    public void save() throws IOException {
        Storage.save(tasks, size);
    }

}
