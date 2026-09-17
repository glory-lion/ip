package lion;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests deadline date-time validation.
 */
public class DeadlineTest {

    @Test
    void validateByText_validDate_doesNotThrow() {
        assertDoesNotThrow(() -> Deadline.validateByText("2/12/2026 1800"));
    }

    @Test
    void validateByText_leapYearFeb29_doesNotThrow() {
        assertDoesNotThrow(() -> Deadline.validateByText("29/2/2028 1800"));
    }

    @Test
    void validateByText_nonExistentDate_throwsLionException() {
        // 30 February does not exist on any calendar.
        assertThrows(LionException.class, () -> Deadline.validateByText("30/2/2026 1800"));
    }

    @Test
    void validateByText_nonLeapYearFeb29_throwsLionException() {
        assertThrows(LionException.class, () -> Deadline.validateByText("29/2/2026 1800"));
    }

    @Test
    void validateByText_wrongFormat_throwsLionException() {
        assertThrows(LionException.class, () -> Deadline.validateByText("next Monday"));
    }
}
