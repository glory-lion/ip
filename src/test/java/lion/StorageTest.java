package lion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests conversion between tasks and their save-file representations.
 */
public class StorageTest {
    @Test
    void encodeThenDecode_deadline_preservesAllDetails() {
        Deadline original = new Deadline("return book", "30/8/2026 1800");
        original.markAsDone();

        Task restored = Storage.decode(Storage.encode(original));

        assertEquals(original.toString(), restored.toString());
    }

    @Test
    void encodeThenDecode_event_preservesAllDetails() {
        Event original = new Event("project meeting", "Monday 2pm", "Monday 4pm");

        Task restored = Storage.decode(Storage.encode(original));

        assertEquals(original.toString(), restored.toString());
    }
}
