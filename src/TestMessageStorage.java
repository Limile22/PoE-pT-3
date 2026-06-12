import MessageStorage.java.MessageStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Unit tests for MessageStorage class
 * Using JUnit 5 (Jupiter) only.
 * * Test data from the PoE:
 * Message 1: +27834557896, "Did you get the cake?",                              Sent
 * Message 2: +27838884567, "Where are you? You are late! I have asked you to...", Stored
 * Message 3: +27834484567, "Yohoooo, I am at your gate.",                        Disregard
 * Message 4: 0838884567,   "It is dinner time !",                                Sent
 * Message 5: +27838884567, "Ok, I am leaving without you.",                      Stored
 */
public class TestMessageStorage {

    private MessageStorage storage;

    @BeforeEach
    public void setUp() {
        storage = new MessageStorage();

        // PoE Test Data Setup

        // Message 1 - Sent
        storage.addSentMessage(
                "Did you get the cake?",
                "+27834557896",
                Integer.parseInt("00:0:DIDCAKE"),
                "1234567890"
        );

        // Message 4 - Sent
        storage.addSentMessage(
                "It is dinner time !",
                "0838884567",
                Integer.parseInt("11:1:ITTIME"),
                "0987654321"
        );

        // Message 3 - Disregarded
        storage.addDisregardedMessage("Yohoooo, I am at your gate.");

        // Messages 2 and 5 - Stored (Simulated safely using parallel tracking helper)
        storage.simulateLoadedStoredMessage(
                "Where are you? You are late! I have asked you to be on time.",
                "+27838884567",
                "22:2:LATEWHER",
                "5555555555"
        );

        storage.simulateLoadedStoredMessage(
                "Ok, I am leaving without you.",
                "+27838884567",
                "55:5:LEAVING",
                "7777777777"
        );
    }

    // Test: Sent messages array is correctly populated
    // Expected: contains message 1 and message 4
    @Test
    public void testSentMessagesArrayPopulated() {
        Assertions.assertTrue(storage.getSentMessages().contains("Did you get the cake?"),
                "Sent messages list should contain Message 1 text.");
        Assertions.assertTrue(storage.getSentMessages().contains("It is dinner time!"),
                "Sent messages list should contain Message 4 text.");
    }

    // Test: Display the longest message
    // From test data, longest is message 2
    @Test
    public void testDisplayLongestMessage() {
        String longest = storage.displayLongestMessage();
        Assertions.assertEquals(
                "Where are you? You are late! I have asked you to be on time.",
                longest,
                "The longest message identifier failed to pick out Message 2."
        );
    }

    // Test: Search for a message by ID
    // Message 4 has ID "0987654321"
    @Test
    public void testSearchByMessageID() {
        String result = storage.searchByMessageID("0987654321");
        Assertions.assertTrue(result.contains("It is dinner time!"),
                "Searching for Message 4's ID should return its message content.");
    }

    // Test: Search all messages for a particular recipient
    // +27838884567 has message 2 and message 5
    @Test
    public void testSearchByRecipient() {
        String result = storage.searchByRecipient("+27838884567");
        Assertions.assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."),
                "Recipient search output should contain Message 2.");
        Assertions.assertTrue(result.contains("Ok, I am leaving without you."),
                "Recipient search output should contain Message 5.");
    }

    // Test: Delete a message using its hash
    // Deleting message 1 using hash "00:0:DIDCAKE"
    @Test
    public void testDeleteMessageByHash() {
        String result = storage.deleteMessageByHash("00:0:DIDCAKE");
        Assertions.assertTrue(result.contains("successfully deleted"),
                "The deletion return status statement should explicitly confirm removal success.");
        // Make sure it's actually gone from the array
        Assertions.assertFalse(storage.getSentMessages().contains("Did you get the cake?"),
                "The targeted item text must completely clear from storage post-deletion.");
    }


    // Test: Disregarded messages array is populated correctly
    @Test
    public void testDisregardedMessagesPopulated() {
        Assertions.assertTrue(storage.getDisregardedMessages().contains("Yohoooo, I am at your gate."),
                "Disregarded collection missing Message 3.");
    }


    // Test: Display report shows all sent messages
    @Test
    public void testDisplayReport() {
        String report = storage.displayReport();
        Assertions.assertTrue(report.contains("Did you get the cake?"),
                "The aggregate report summary is missing Message 1 text.");
        Assertions.assertTrue(report.contains("It is dinner time!"),
                "The aggregate report summary is missing Message 4 text.");
    }
}
