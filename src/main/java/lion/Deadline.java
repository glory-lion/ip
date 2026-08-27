package lion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {
    private static final DateTimeFormatter STORAGE_FORMAT =
            DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    protected LocalDateTime by; //store as a real date

    public Deadline(String description, String byText) {
        super(description);
        try {
            this.by = LocalDateTime.parse(byText.trim(), STORAGE_FORMAT);
        } catch (DateTimeParseException e) {
            this.by = LocalDateTime.now();
        }
    }
    @Override
    public String getTypeIcon() {
        return "D";
    }

    /**
     * Returns the deadline in the stable format used in the save file.
     *
     * @return deadline formatted as {@code d/M/yyyy HHmm}
     */
    public String getByForStorage() {
        return by.format(STORAGE_FORMAT);
    }

    @Override
    public String toString() {
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("MMM dd yyyy h:mm a");
        String formattedDateTime = by.format(outputFormat);
        return super.toString() + " (by: " + formattedDateTime + ")";
    }
}
