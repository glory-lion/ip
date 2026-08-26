package lion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {
    protected LocalDateTime by; //store as a real date

    public Deadline(String description, String byText) {
        super(description);
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
        try {
            this.by = LocalDateTime.parse(byText.trim(), inputFormat);
        } catch (DateTimeParseException e) {
            this.by = LocalDateTime.now();
        }
    }
    @Override
    public String getTypeIcon() {
        return "D";
    }

    @Override
    public String toString() {
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("MMM dd yyyy h:mm a");
        String formattedDateTime = by.format(outputFormat);
        return super.toString() + " (by: " + formattedDateTime + ")";
    }
}
