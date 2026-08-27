package lion;

public class Event extends Task {
    protected String from;
    protected String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }
    @Override
    public String getTypeIcon() {
        return "E";
    }

    /**
     * Returns the event's start text for saving and restoring the event.
     *
     * @return event start text
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the event's end text for saving and restoring the event.
     *
     * @return event end text
     */
    public String getTo() {
        return to;
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + this.from + " to: " + to + ")";
    }
}
