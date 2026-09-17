package lion;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests event start/end range validation.
 */
public class EventTest {

    @Test
    void validateRange_startBeforeEnd_doesNotThrow() {
        assertDoesNotThrow(() -> Event.validateRange("2/12/2026 1800", "2/12/2026 2000"));
    }

    @Test
    void validateRange_startAfterEnd_throwsLionException() {
        assertThrows(LionException.class, () -> Event.validateRange("2/12/2026 2000", "2/12/2026 1800"));
    }

    @Test
    void validateRange_startEqualsEnd_throwsLionException() {
        assertThrows(LionException.class, () -> Event.validateRange("2/12/2026 1800", "2/12/2026 1800"));
    }

    @Test
    void validateRange_freeTextTimes_doesNotThrow() {
        // Event has always accepted arbitrary text for its start/end (e.g. "8pm"); since
        // that is not a comparable date-time, the range check must not reject it.
        assertDoesNotThrow(() -> Event.validateRange("8pm", "7pm"));
    }

    @Test
    void validateRange_oneSideFreeTextOneSideDate_doesNotThrow() {
        assertDoesNotThrow(() -> Event.validateRange("8pm", "2/12/2026 1800"));
    }

    @Test
    void validateRange_startIsNonExistentDate_treatedAsFreeTextAndDoesNotThrow() {
        // 30 February is not a real date, so it is not treated as a comparable
        // date-time — this matches how Deadline's own strict parser rejects it, but
        // here it simply falls back to being incomparable free text rather than an error.
        assertDoesNotThrow(() -> Event.validateRange("30/2/2026 1800", "2/12/2026 1800"));
    }
}
