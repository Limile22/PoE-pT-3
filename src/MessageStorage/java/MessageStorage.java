package MessageStorage.java;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// MessageStorage class - handles all array storage for Part 3
// Stores sent, disregarded, and stored messages in separate ArrayLists
public class MessageStorage {

    // Separated lists to prevent cross-contamination and index shifting bugs
    private final ArrayList<String> sentMessages = new ArrayList<>();
    private final ArrayList<String> disregardedMessages = new ArrayList<>();
    private final ArrayList<String> storedMessages = new ArrayList<>();

    private final ArrayList<String> sentHashes = new ArrayList<>();
    private final ArrayList<String> storedHashes = new ArrayList<>();

    private final ArrayList<String> sentIDs = new ArrayList<>();
    private final ArrayList<String> storedIDs = new ArrayList<>();

    private final ArrayList<String> sentRecipients = new ArrayList<>();
    private final ArrayList<String> storedRecipients = new ArrayList<>();

    // Adds a message to the send messages array
    public void addSentMessage(String message, String recipient, int  hash, String id) {
        sentMessages.add(message);
        sentRecipients.add(recipient);
        sentHashes.add(String.valueOf(hash));
        sentIDs.add(id);
    }

    // Adds a message to the disregarded messages array
    public void addDisregardedMessage(String message) {
        disregardedMessages.add(message);
    }

    // Reads storedMessages.json and loads entries into the storedMessages array
    public void loadStoredMessagesFromJSON() {
        try (BufferedReader reader = new BufferedReader(new FileReader("storedMessages.json"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String message = extractValue(line, "message");
                String recipient = extractValue(line, "recipient");
                String hash = extractValue(line, "messageHash");
                String id = extractValue(line, "messageID");

                if (message != null && recipient != null) {
                    storedMessages.add(message);
                    storedRecipients.add(recipient);
                    if (hash != null) storedHashes.add(hash);
                    if (id != null) storedIDs.add(id);
                }
            }
        } catch (IOException e) {
            System.out.println("No stored messages file found yet.");
        }
    }

    // Helper - pulls a value out of a JSON string by key
    private String extractValue(String json, String key) {
        String search = "\"" + key + "\": \"";
        int start = json.indexOf(search);
        if (start == -1) return null;
        start += search.length();
        int end = json.indexOf("\"", start);
        if (end == -1) return null;
        return json.substring(start, end);
    }

    // Displays sender and recipient of all stored messages
    public String displayAllSendersAndRecipients() {
        if (storedMessages.isEmpty()) {
            return "No stored messages to display.";
        }
        StringBuilder result = new StringBuilder();
        result.append("Stored Messages \n");
        for (int i = 0; i < storedMessages.size(); i++) {
            result.append("Message").append(i + 1).append(":\n");
            result.append("Recipient").append(storedRecipients.get(i)).append("\n");
            result.append("Message").append(storedMessages.get(i)).append("\n");
        }
        return result.toString();
    }

    // Finds and returns the longest message from sent and stored arrays
    public String displayLongestMessage() {
        ArrayList<String> allMessages = new ArrayList<>();
        allMessages.addAll(sentMessages);
        allMessages.addAll(storedMessages);

        if (allMessages.isEmpty()) {
            return "No messages available.";
        }

        String longest = allMessages.getFirst();
        for (int i = 1; i < allMessages.size(); i++) {
            if (allMessages.get(i).length() > longest.length()) {
                longest = allMessages.get(i);
            }
        }
        return longest;
    }

    // Searches for a message by its ID safely using isolated indices
    public String searchByMessageID(String searchID) {
        // Search Sent track first
        for (int i = 0; i < sentIDs.size(); i++) {
            if (sentIDs.get(i).equals(searchID)) {
                return "Recipient: " + sentRecipients.get(i) + "\nMessage: " + sentMessages.get(i);
            }
        }
        // Search Stored track next
        for (int i = 0; i < storedIDs.size(); i++) {
            if (storedIDs.get(i).equals(searchID)) {
                return "Recipient: " + storedRecipients.get(i) + "\nMessage: " + storedMessages.get(i);
            }
        }
        return "Message ID not found.";
    }

    // Returns all messages sent or stored for a specific recipient number
    public String searchByRecipient(String recipientNumber) {
        StringBuilder result = new StringBuilder();
        boolean found = false;

        for (int i = 0; i < sentRecipients.size(); i++) {
            if (sentRecipients.get(i).equals(recipientNumber)) {
                result.append("Sent: ").append(sentMessages.get(i)).append("\n");
                found = true;
            }
        }
        for (int i = 0; i < storedRecipients.size(); i++) {
            if (storedRecipients.get(i).equals(recipientNumber)) {
                result.append("Stored: ").append(storedMessages.get(i)).append("\n");
                found = true;
            }
        }

        if (!found) {
            return "No messages found for: " + recipientNumber;
        }
        return result.toString();
    }

    // Deletes a message from the arrays cleanly using its isolated hash
    public String deleteMessageByHash(String hash) {
        // Look through sent items
        for (int i = 0; i < sentHashes.size(); i++) {
            if (sentHashes.get(i).equals(hash)) {
                String deleted = sentMessages.get(i);
                sentMessages.remove(i);
                sentRecipients.remove(i);
                sentHashes.remove(i);
                sentIDs.remove(i);
                return "Message: \"" + deleted + "\" successfully deleted from Sent messages.";
            }
        }
        // Look through stored items
        for (int i = 0; i < storedHashes.size(); i++) {
            if (storedHashes.get(i).equals(hash)) {
                String deleted = storedMessages.get(i);
                storedMessages.remove(i);
                storedRecipients.remove(i);
                storedHashes.remove(i);
                storedIDs.remove(i);
                return "Message: \"" + deleted + "\" successfully deleted from Stored messages.";
            }
        }
        return "Hash not found. No message deleted.";
    }

    // Full report of all sent messages showing Hash, Recipient, Message
    public String displayReport() {
        if (sentMessages.isEmpty()) {
            return "No sent messages to report.";
        }
        StringBuilder report = new StringBuilder();
        report.append("QuickChat Message Report \n\n");
        for (int i = 0; i < sentMessages.size(); i++) {
            report.append("Message").append(i + 1).append("\n");
            report.append("Message Hash").append(sentHashes.get(i)).append("\n");
            report.append("Recipient").append(sentRecipients.get(i)).append("\n");
            report.append("Message").append(sentMessages.get(i)).append("\n\n");
        }
        report.append("Total sent").append(sentMessages.size()).append("\n");
        report.append("\n");
        return report.toString();
    }

    // Unit Testing Getters (Aggregated on the fly to match original signatures)
    public ArrayList<String> getSentMessages() { return sentMessages; }
    public ArrayList<String> getDisregardedMessages() { return disregardedMessages; }
    public ArrayList<String> getStoredMessages() { return storedMessages; }
    public ArrayList<String> getSentRecipients() { return sentRecipients; }
    public ArrayList<String> getStoredRecipients() { return storedRecipients; }

    public ArrayList<String> getMessageHashes() {
        ArrayList<String> combined = new ArrayList<>(sentHashes);
        combined.addAll(storedHashes);
        return combined;
    }

    public ArrayList<String> getMessageIDs() {
        ArrayList<String> combined = new ArrayList<>(sentIDs);
        combined.addAll(storedIDs);
        return combined;
    }

    public void simulateLoadedStoredMessage(String s, String s1, String s2, String number) {
    }

    public void addStoredMessageDirectly() {
    }
}