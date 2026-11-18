package com.mycompany;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Message {
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String flag = "";

    // Lists shared by all messages while the program runs
    private static ArrayList<Message> sentMessages = new ArrayList<>();
    private static ArrayList<Message> disregardedMessages = new ArrayList<>();
    private static ArrayList<Message> storedMessages = new ArrayList<>();

    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    public String getMessageID() { return messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
    public String getFlag() { return flag; }

    // Makes a random 10-digit ID
    public static String generateMessageID() {
        Random random = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(random.nextInt(10));
        }
        return id.toString();
    }

    // The ID must be no more than 10 characters
    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    // Reuses the phone check from Part 1
    public String checkRecipientCell() {
        Login helper = new Login("", "");
        helper.setCellNumber(recipient);
        if (helper.checkCellPhoneNumber()) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. "
             + "Please correct the number and try again.";
    }

    // The message must be 250 characters or fewer
    public String checkMessageLength() {
        if (messageText.length() <= 250) {
            return "Message ready to send.";
        }
        int extra = messageText.length() - 250;
        return "Message exceeds 250 characters by " + extra + "; please reduce the size.";
    }

    // Keeps only letters and digits, so "tonight?" becomes "tonight"
    private String cleanWord(String word) {
        StringBuilder clean = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                clean.append(c);
            }
        }
        return clean.toString();
    }

    // Hash = first two ID digits : message number : first word + last word (all caps)
    public String createMessageHash() {
        String[] words = messageText.split(" ");
        String firstWord = cleanWord(words[0]);
        String lastWord = cleanWord(words[words.length - 1]);
        return (messageID.substring(0, 2) + ":" + messageNumber + ":"
                + firstWord + lastWord).toUpperCase();
    }

    // choice 1 = send, 2 = disregard, 3 = store
    public String sentMessage(int choice) {
        if (choice == 1) {
            flag = "Sent";
            sentMessages.add(this);
            return "Message successfully sent.";
        } else if (choice == 2) {
            flag = "Disregard";
            disregardedMessages.add(this);
            return "Press 0 to delete the message.";
        } else if (choice == 3) {
            flag = "Stored";
            storedMessages.add(this);
            return "Message successfully stored.";
        }
        return "Invalid choice.";
    }

    // Shows every sent message: ID, hash, recipient, message
    public static String printMessages() {
        String result = "";
        for (int i = 0; i < sentMessages.size(); i++) {
            Message m = sentMessages.get(i);
            result += "Message ID: " + m.messageID + "\n"
                    + "Message Hash: " + m.messageHash + "\n"
                    + "Recipient: " + m.recipient + "\n"
                    + "Message: " + m.messageText + "\n\n";
        }
        return result;
    }

    public static int returnTotalMessages() {
        return sentMessages.size();
    }

    // Saves all stored messages into messages.json
    // Reference: Google (n.d.) Gson. Available at: https://github.com/google/gson (Accessed: 18 November 2026).
    public static void storeMessage() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter writer = new FileWriter("messages.json");
            gson.toJson(storedMessages, writer);
            writer.close();
        } catch (IOException e) {
            System.out.println("Could not save the message file.");
        }
    }

    // Used by the unit tests so each test starts fresh
    public static void clearAll() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
    }
}