import java.util.Objects;

public class Message {
    private String recipientCell;
    private String messageText;
    private String messageID;
    private int messageHash;

    private static int totalMessagesSent = 0;

    // Default constructor for JSON
    public Message() {}

    public Message(String recipientCell, String messageText, int messageCounter) {
        this.recipientCell = recipientCell;
        this.messageText = messageText;
        this.messageID = "MSG-" + String.format("%04d", messageCounter);
        this.messageHash = Objects.hash(recipientCell, messageText, this.messageID);
    }

    public String checkRecipientCell() {
        if (recipientCell != null && recipientCell.startsWith("+") && recipientCell.length() >= 10) {
            return "Cell phone number successfully captured.";
        }
        return "Invalid cell phone number format. Must start with international code (e.g., +27).";
    }

    public String sentMessage(int choice) {
        return switch (choice) {
            case 1 -> {
                totalMessagesSent++;
                yield "Message successfully sent to " + recipientCell;
            }
            case 2 -> "Message successfully disregarded.";
            case 3 -> "Message successfully stored to JSON pipeline.";
            default -> "Unknown action selected.";
        };
    }

    public String printMessages() {
        return "ID: " + messageID + "\n" +
                "To: " + recipientCell + "\n" +
                "Hash: " + messageHash + "\n" +
                "Content: \"" + messageText + "\"";
    }

    // Getters and Setters
    public String getRecipientCell() { return recipientCell; }
    public void setRecipientCell(String recipientCell) { this.recipientCell = recipientCell; }

    public String getMessageText() { return messageText; }
    public void setMessageText(String messageText) { this.messageText = messageText; }

    public String getMessageID() { return messageID; }
    public void setMessageID(String messageID) { this.messageID = messageID; }

    public int getMessageHash() { return messageHash; }
    public void setMessageHash(int messageHash) { this.messageHash = messageHash; }

    public static int getTotalMessagesSent() { return totalMessagesSent; }
