package lion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void decode_unrecognizedTaskType_fallsBackToTodo() {
        Task restored = Storage.decode("X | 0 | mystery task");

        assertEquals("[T] [ ] mystery task", restored.toString());
    }

    @Test
    void decode_tooFewFields_throwsIllegalArgumentException() {
        // A hand-edited or truncated save-file line should be rejected outright, not
        // silently indexed out of bounds (which relying on disabled-by-default asserts
        // used to risk in a normally-run/packaged build).
        assertThrows(IllegalArgumentException.class, () -> Storage.decode("T | 0"));
    }

    @Test
    void decode_deadlineMissingByField_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Storage.decode("D | 0 | return book"));
    }

    @Test
    void decode_eventMissingToField_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Storage.decode("E | 0 | meeting | Monday 2pm"));
    }

    @Test
    void encode_taskDescriptionWithPipeCharacter_wouldCorruptTheField() {
        // Documents the storage-format limitation that motivates Lion.getResponse()
        // rejecting '|' in user input up front: the pipe is also the field separator,
        // so a description containing one desyncs decode()'s field count on reload.
        Task task = new Todo("buy milk | bread");

        String encoded = Storage.encode(task);

        assertTrue(encoded.split("\\s*\\|\\s*", -1).length > 3);
    }
}
