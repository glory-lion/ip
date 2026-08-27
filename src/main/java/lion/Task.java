package lion;

/**
 * Defines the shared description and completion state of every task type.
 */
public abstract class Task {
    /** Human-readable details describing the task. */
    protected String description;
    /** Whether the task has been completed. */
    protected boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the storage-friendly completion indicator.
     *
     * @return {@code 1} when complete, otherwise {@code 0}
     */
    public String getStatusIcon() {
        return (isDone ? "1" : "0");
    }
    /**
     * Returns the task description.
     *
     * @return task description
     */
    public String getDescription() {
        return this.description;
    }

    /** Marks this task as complete. */
    public void markAsDone() {
        this.isDone = true;
    }

    /** Marks this task as incomplete. */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns the one-letter icon identifying the concrete task type.
     *
     * @return task type icon
     */
    public abstract String getTypeIcon();

    /**
     * Returns the task's storage-friendly base representation.
     *
     * @return type, completion status, and description
     */
    @Override
    public String toString() {
        return  getTypeIcon() + " | " + getStatusIcon() + " | " + description;
    }
 }
