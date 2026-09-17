package lion;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests responses produced by {@link Lion#getResponse(String)}.
 */
public class LionTest {
    private final Lion lion = new Lion();

    @Test
    void getResponse_help_listsAllCommandsWithDescriptions() {
        String response = lion.getResponse("help");

        assertTrue(response.startsWith("Here are the available commands:"));
        assertTrue(response.contains("todo - " + CommandType.TODO.getDescription()));
        assertTrue(response.contains("help - " + CommandType.HELP.getDescription()));
        assertTrue(response.contains("bye - " + CommandType.BYE.getDescription()));
        assertFalse(response.contains("unknown"));
    }

    @Test
    void getResponse_unknownCommand_mentionsHelp() {
        String response = lion.getResponse("gibberish");

        assertTrue(response.startsWith("ROAR!!!"));
        assertTrue(response.contains("help"));
    }

    // The remaining tests below all exercise rejection paths that fail before reaching
    // TaskList.add()/save(), so they never touch the real save file that this class's
    // Lion instance loads from and writes to.

    @Test
    void getResponse_markOutOfRange_isRejectedAsError() {
        String response = lion.getResponse("mark 999999");

        assertTrue(Lion.isErrorResponse(response));
    }

    @Test
    void getResponse_markZeroOrNegative_isRejectedAsError() {
        assertTrue(Lion.isErrorResponse(lion.getResponse("mark 0")));
        assertTrue(Lion.isErrorResponse(lion.getResponse("mark -1")));
    }

    @Test
    void getResponse_markNonNumeric_isRejectedAsError() {
        String response = lion.getResponse("mark abc");

        assertTrue(Lion.isErrorResponse(response));
    }

    @Test
    void getResponse_markMissingNumber_isRejectedAsError() {
        String response = lion.getResponse("mark");

        assertTrue(Lion.isErrorResponse(response));
    }

    @Test
    void getResponse_commandContainingPipeCharacter_isRejectedAsError() {
        String response = lion.getResponse("todo buy milk | bread");

        assertTrue(Lion.isErrorResponse(response));
    }

    @Test
    void getResponse_deadlineWithNonExistentDate_isRejectedAsError() {
        // 30 February does not exist.
        String response = lion.getResponse("deadline return book /by 30/2/2026 1800");

        assertTrue(Lion.isErrorResponse(response));
    }

    @Test
    void getResponse_eventStartAtOrAfterEnd_isRejectedAsError() {
        String sameTime = lion.getResponse("event clash /from 2/12/2026 1800 /to 2/12/2026 1800");
        String startAfterEnd = lion.getResponse("event clash /from 2/12/2026 1900 /to 2/12/2026 1800");

        assertTrue(Lion.isErrorResponse(sameTime));
        assertTrue(Lion.isErrorResponse(startAfterEnd));
    }
}
