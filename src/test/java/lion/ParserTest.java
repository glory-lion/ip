package lion;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void getDeadlineParts_validCommand_returnsDescriptionAndDeadline() throws LionException {
        assertArrayEquals(
                new String[] {"return book", "2/12/2019 1800"},
                parser.getDeadlineParts("deadline return book /by 2/12/2019 1800"));
    }

    @Test
    void getDeadlineParts_deadlineContainsSeparator_splitsOnlyOnce() throws LionException {
        assertArrayEquals(
                new String[] {"submit report", "Monday /by 1800"},
                parser.getDeadlineParts("deadline submit report /by Monday /by 1800"));
    }

    @Test
    void getDeadlineParts_extraSpacesAroundSeparator_stillSplitsCorrectly() throws LionException {
        assertArrayEquals(
                new String[] {"return book", "2/12/2019 1800"},
                parser.getDeadlineParts("deadline return book   /by   2/12/2019 1800"));
    }

    @Test
    void getDeadlineParts_missingBySeparator_throwsLionException() {
        assertThrows(LionException.class, () -> parser.getDeadlineParts("deadline return book"));
    }

    @Test
    void getDeadlineParts_bareCommandWord_throwsLionExceptionInsteadOfCrashing() {
        // "deadline" alone is shorter than the "deadline " prefix length, which used to
        // throw StringIndexOutOfBoundsException instead of a user-facing error.
        assertThrows(LionException.class, () -> parser.getDeadlineParts("deadline"));
    }

    @Test
    void getDeadlineParts_emptyDescription_throwsLionException() {
        assertThrows(LionException.class, () -> parser.getDeadlineParts("deadline /by 2/12/2019 1800"));
    }

    @Test
    void getEventParts_validCommand_returnsDescriptionStartAndEnd() throws LionException {
        assertArrayEquals(
                new String[] {"project meeting", "Monday 2pm", "Monday 4pm"},
                parser.getEventParts("event project meeting /from Monday 2pm /to Monday 4pm"));
    }

    @Test
    void getEventParts_missingToSeparator_throwsLionExceptionInsteadOfCrashing() {
        // Previously threw ArrayIndexOutOfBoundsException instead of a user-facing error.
        assertThrows(LionException.class, () -> parser.getEventParts("event party /from 8pm"));
    }

    @Test
    void getEventParts_bareCommandWord_throwsLionException() {
        assertThrows(LionException.class, () -> parser.getEventParts("event"));
    }

    @Test
    void getTaskIndex_oneBasedTaskNumber_returnsZeroBasedIndex() throws LionException {
        assertEquals(0, parser.getTaskIndex("mark 1", 5));
        assertEquals(11, parser.getTaskIndex("delete   12  ", 7));
    }

    @Test
    void getTaskIndex_missingNumber_throwsLionExceptionInsteadOfCrashing() {
        // "mark" alone is shorter than the "mark " prefix length, which used to throw
        // StringIndexOutOfBoundsException instead of a user-facing error.
        assertThrows(LionException.class, () -> parser.getTaskIndex("mark", 5));
    }

    @Test
    void getTaskIndex_nonNumericText_throwsLionExceptionInsteadOfCrashing() {
        // Previously threw NumberFormatException instead of a user-facing error.
        assertThrows(LionException.class, () -> parser.getTaskIndex("mark abc", 5));
    }

    @Test
    void getFindKeyword_keywordWithSpaces_returnsTrimmedKeyword() {
        assertEquals("read book", parser.getFindKeyword("find   read book  "));
    }
}
