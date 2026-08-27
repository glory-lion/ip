package lion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a task that must be completed by a specific date and time.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter STORAGE_FORMAT =
            DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    /** Due date and time stored as a value that can be formatted when needed. */
    protected LocalDateTime by;

    /**
     * Creates a deadline from its description and date-time text.
     *
     * @param description description of the task.
     * @param byText deadline formatted as {@code d/M/yyyy HHmm}.
     */
    public Deadline(String description, String byText) {
        super(description);
        try {
            this.by = LocalDateTime.parse(byText.trim(), STORAGE_FORMAT);
        } catch (DateTimeParseException e) {
            this.by = LocalDateTime.now();
        }
    }

    /**
     * Returns the one-letter icon used to identify deadline tasks.
     *
     * @return {@code D}.
     */
    @Override
    public String getTypeIcon() {
        return "D";
    }

    /**
     * Returns the deadline in the stable format used in the save file.
     *
     * @return deadline formatted as {@code d/M/yyyy HHmm}.
     */
    public String getByForStorage() {
        return by.format(STORAGE_FORMAT);
    }

    /**
     * Returns a display-friendly representation of this deadline.
     *
     * @return deadline status, description, and formatted due date.
     */
    @Override
    public String toString() {
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("MMM dd yyyy h:mm a");
        String formattedDateTime = by.format(outputFormat);
        return super.toString() + " (by: " + formattedDateTime + ")";
    }
}
