package com.mycompany;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Message {
    private String messageID;
    private int messageNumber;
    private String sender;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String flag = "";

    // The five arrays from the brief
    private static ArrayList<Message> sentMessages = new ArrayList<>();
    private static ArrayList<Message> disregardedMessages = new ArrayList<>();
    private static ArrayList<Message> storedMessages = new ArrayList<>();
    private static ArrayList<String> messageHashes = new ArrayList<>();
    private static ArrayList<String> messageIDs = new ArrayList<>();

    // The logged-in user's number, copied onto every new message
    private static String currentSender = "Unknown";

    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.sender = currentSender;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    public static void setCurrentSender(String senderNumber) { currentSender = senderNumber; }

    public String getMessageID() { return messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getSender() { return sender; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
    public String getFlag() { return flag; }

    public static ArrayList<Message> getSentMessages() { return sentMessages; }
    public static ArrayList<Message> getDisregardedMessages() { return disregardedMessages; }
    public static ArrayList<Message> getStoredMessages() { return storedMessages; }
    public static ArrayList<String> getMessageHashes() { return messageHashes; }
    public static ArrayList<String> getMessageIDs() { return messageIDs; }

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

    // Used by the "Message ID is created" test
    public String messageIDGenerated() {
        return "Message ID generated: " + messageID;
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

    // Adds the hash and ID of every message in a list to the two index arrays
    private static void addHashesAndIDs(ArrayList<Message> list) {
        for (int i = 0; i < list.size(); i++) {
            messageHashes.add(list.get(i).messageHash);
            messageIDs.add(list.get(i).messageID);
        }
    }

    // Rebuilds the hash and ID arrays so they always match the three message lists
    private static void rebuildIndexLists() {
        messageHashes = new ArrayList<>();
        messageIDs = new ArrayList<>();
        addHashesAndIDs(sentMessages);
        addHashesAndIDs(disregardedMessages);
        addHashesAndIDs(storedMessages);
    }

    // choice 1 = send, 2 = disregard, 3 = store
    public String sentMessage(int choice) {
        String result = "Invalid choice.";
        if (choice == 1) {
            flag = "Sent";
            sentMessages.add(this);
            result = "Message successfully sent.";
        } else if (choice == 2) {
            flag = "Disregard";
            disregardedMessages.add(this);
            result = "Press 0 to delete the message.";
        } else if (choice == 3) {
            flag = "Stored";
            storedMessages.add(this);
            result = "Message successfully stored.";
        }
        rebuildIndexLists();
        return result;
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

    // Reads messages.json back into the stored messages array
    // Reference: Google (n.d.) Gson. Available at: https://github.com/google/gson (Accessed: 18 November 2026).
    public static void loadStoredMessages() {
        storedMessages = new ArrayList<>();
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader("messages.json");
            Message[] loaded = gson.fromJson(reader, Message[].class);
            reader.close();
            if (loaded != null) {
                for (int i = 0; i < loaded.length; i++) {
                    storedMessages.add(loaded[i]);
                }
            }
        } catch (IOException e) {
            // No file yet, so there is nothing to load
        }
        rebuildIndexLists();
    }

    // Used by the unit tests so each test starts fresh
    public static void clearAll() {
        sentMessages = new ArrayList<>();
        disregardedMessages = new ArrayList<>();
        storedMessages = new ArrayList<>();
        rebuildIndexLists();
    }
}