package lion;

/**
 * Represents a task that takes place between a specified start and end.
 */
public class Event extends Task {
    /** User-provided event start details. */
    protected String from;
    /** User-provided event end details. */
    protected String to;

    /**
     * Creates an event with its description, start, and end details.
     *
     * @param description description of the event
     * @param from event start text
     * @param to event end text
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the one-letter icon used to identify event tasks.
     *
     * @return {@code E}
     */
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

    /**
     * Returns a display-friendly representation of this event.
     *
     * @return event status, description, start, and end
     */
    @Override
    public String toString() {
        return super.toString() + " (from: " + this.from + " to: " + to + ")";
    }
}
