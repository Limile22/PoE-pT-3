import java.util.Scanner;

/**
 * Main.java - QuickChat entry point
 * Features: Send Messages menu, Stored Messages menu, input validation, and report handling.
 * Console only.
 */
public class Main {

     static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // One MessageStorage object for the whole session
        MessageStorage.java.MessageStorage storage = new MessageStorage.java.MessageStorage();

        System.out.println();
        System.out.println("Welcome to QuickChat.");
        System.out.println();

        // Ask how many messages the user wants to send this session (with input validation)
        int numMessages = promptForInt(scanner, "How many messages would you like to send? ");

        int messageCounter = 0; // tracks which message number we're on
        boolean running = true;

        // Load any previously stored messages from JSON
        storage.loadStoredMessagesFromJSON();

        while (running) {
            System.out.println();
            System.out.println("Main Menu");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Stored Messages");
            System.out.println("4) Quit");

            int menuChoice = promptForInt(scanner, "Choose: ");

            // OPTION 1: SEND MESSAGES
            if (menuChoice == 1) {
                String messageText;
                String recipient;

                for (int i = 0; i < numMessages; i++) {
                    System.out.println();
                    System.out.println("Message" + (i + 1) + " of " + numMessages + " ");

                    // Get recipient number
                    System.out.print("Enter recipient cell number (with international code e.g. +27...): ");
                    recipient = scanner.nextLine();

                    // Get message text
                    System.out.print("Enter your message (max 250 characters): ");
                    messageText = scanner.nextLine();

                    // Check message length first
                    if (messageText.length() > 250) {
                        int overBy = messageText.length() - 250;
                        System.out.println("Message exceeds 250 characters by " + overBy + "; please reduce.");
                        i--; // redo this message
                        continue;
                    } else {
                        System.out.println("Message ready to send.");
                    }

                    // Create the message object
                    Message newMessage = new Message(recipient, messageText, messageCounter);

                    // Check recipient cell number
                    String cellCheck = newMessage.checkRecipientCell();
                    System.out.println(cellCheck);

                    if (!cellCheck.equals("Cell phone number successfully captured.")) {
                        i--; // redo this message
                        continue;
                    }

                    // Show ID and Hash
                    System.out.println("Message ID: " + newMessage.getMessageID());
                    System.out.println("Message Hash: " + newMessage.getMessageHash());

                    // Ask what to do with the message
                    System.out.println();
                    System.out.println("What would you like to do?");
                    System.out.println("1) Send Message");
                    System.out.println("2) Disregard Message");
                    System.out.println("3) Store Message to send later");

                    int sendChoice = promptForInt(scanner, "Choose: ");
                    String sendResult = newMessage.sentMessage(sendChoice);
                    System.out.println(sendResult);

                    // Add to correct array based on choice
                    if (sendChoice == 1) {
                        storage.addSentMessage(
                                newMessage.getMessageText(),
                                newMessage.getRecipientCell(),
                                newMessage.getMessageHash(),
                                newMessage.getMessageID()
                        );
                    } else if (sendChoice == 2) {
                        storage.addDisregardedMessage(newMessage.getMessageText());
                    } else if (sendChoice == 3) {
                        // Crucial fix: link choice 3 directly to the JSON pipeline
                        storage.addStoredMessageDirectly();
                    }

                    // Print full message details
                    System.out.println();
                    System.out.println("Message Details");
                    System.out.println(newMessage.printMessages());

                    messageCounter++;
                }

                // Show total messages sent
                System.out.println();
                System.out.println("Total messages sent: " + Message.getTotalMessagesSent());

                // OPTION 2: COMING SOON
            } else if (menuChoice == 2) {
                System.out.println("Coming Soon.");

                // OPTION 3: STORED MESSAGES MENU
            } else if (menuChoice == 3) {
                boolean storedRunning = true;

                while (storedRunning) {
                    System.out.println();
                    System.out.println("Stored Messages Menu");
                    System.out.println("a) Display sender and recipient of all stored messages");
                    System.out.println("b) Display the longest stored message");
                    System.out.println("c) Search for a message by ID");
                    System.out.println("d) Search all messages for a particular recipient");
                    System.out.println("e) Delete a message using message hash");
                    System.out.println("f) Display full report");
                    System.out.println("g) Back to Main Menu");
                    System.out.print("Choose: ");

                    String subChoice = scanner.nextLine().trim().toLowerCase();

                    switch (subChoice) {
                        case "a" -> {
                            System.out.println();
                            System.out.println(storage.displayAllSendersAndRecipients());
                        }
                        case "b" -> {
                            System.out.println();
                            System.out.println("Longest message: " + storage.displayLongestMessage());
                        }
                        case "c" -> {
                            System.out.print("Enter Message ID to search: ");
                            String searchID = scanner.nextLine();
                            System.out.println();
                            System.out.println(storage.searchByMessageID(searchID));
                        }
                        case "d" -> {
                            System.out.print("Enter recipient cell number to search: ");
                            String searchRecipient = scanner.nextLine();
                            System.out.println();
                            System.out.println(storage.searchByRecipient(searchRecipient));
                        }
                        case "e" -> {
                            System.out.print("Enter message hash to delete: ");
                            String deleteHash = scanner.nextLine();
                            System.out.println();
                            System.out.println(storage.deleteMessageByHash(deleteHash));
                        }
                        case "f" -> {
                            System.out.println();
                            System.out.println(storage.displayReport());
                        }
                        case "g" -> storedRunning = false;
                        default -> System.out.println("Invalid choice. Please pick a-g.");
                    }
                }

                // OPTION 4: QUIT
            } else if (menuChoice == 4) {
                System.out.println("Goodbye! Thanks for using QuickChat.");
                running = false;

            } else {
                System.out.println("Invalid option. Please choose 1, 2, 3, or 4.");
            }
        }

        scanner.close();
    }

    /**
     * Helper method to safely prompt for and read an integer from console.
     * Prevents program termination caused by non-numeric inputs.
     */
    private static int promptForInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}
