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

        assertTrue(response.startsWith("OOPS!!!"));
        assertTrue(response.contains("help"));
    }
}
