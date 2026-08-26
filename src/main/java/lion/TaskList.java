package lion;

import java.io.IOException;

public class TaskList {
    private final Task[] tasks;
    private int size;

    public TaskList() {
        this.tasks = new Task[100];
        this.size = 0;
    }

    public TaskList(Task[] tasks, int size) {
        this.tasks = tasks;
        this.size = size;
    }

    public int size() {
        return size;
    }

    public Task get(int index) {
        return tasks[index];
    }

    public void add(Task task) {
        tasks[size] = task;
        size++;
    }

    public Task delete(int index) {
        Task deleted = tasks[index];
        for(int i = index; i < size - 1; i++) {
            tasks[i] = tasks[i + 1];
        }
        size--;
        tasks[size] = null;
        return deleted;
    }

    public void mark(int index) {
        tasks[index].markAsDone();
    }

    public void unmark(int index) {
        tasks[index].markAsNotDone();
    }

    public void save() throws IOException {
        Storage.save(tasks, size);
    }

}
