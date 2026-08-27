package lion;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests the parsing of user commands into values used by the application.
 */
public class ParserTest {
    private final Parser parser = new Parser();

    @Test
    void getTodoDescription_descriptionWithSpaces_returnsTrimmedDescription() {
        assertEquals("read a book", parser.getTodoDescription("todo   read a book  "));
    }

    @Test
    void getTodoDescription_missingDescription_returnsEmptyString() {
        assertEquals("", parser.getTodoDescription("todo"));
    }

    @Test
    void getDeadlineParts_validCommand_returnsDescriptionAndDeadline() {
        assertArrayEquals(
                new String[] {"return book", "2/12/2019 1800"},
                parser.getDeadlineParts("deadline return book /by 2/12/2019 1800"));
    }

    @Test
    void getDeadlineParts_deadlineContainsSeparator_splitsOnlyOnce() {
        assertArrayEquals(
                new String[] {"submit report", "Monday /by 1800"},
                parser.getDeadlineParts("deadline submit report /by Monday /by 1800"));
    }

    @Test
    void getEventParts_validCommand_returnsDescriptionStartAndEnd() {
        assertArrayEquals(
                new String[] {"project meeting", "Monday 2pm", "Monday 4pm"},
                parser.getEventParts("event project meeting /from Monday 2pm /to Monday 4pm"));
    }

    @Test
    void getTaskIndex_oneBasedTaskNumber_returnsZeroBasedIndex() {
        assertEquals(0, parser.getTaskIndex("mark 1", 5));
        assertEquals(11, parser.getTaskIndex("delete   12  ", 7));
    }

    @Test
    void getFindKeyword_keywordWithSpaces_returnsTrimmedKeyword() {
        assertEquals("read book", parser.getFindKeyword("find   read book  "));
    }
}
