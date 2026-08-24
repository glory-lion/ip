public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "1" : "0");
    }
    public String getDescription() {
        return this.description;
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    public abstract String getTypeIcon();

    @Override
    public String toString() {
        return  getTypeIcon() + " | " + getStatusIcon() + " | " + description;
    }
 }
