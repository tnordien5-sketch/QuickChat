package com.mycompany;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StoredMessagesTest {

    private Message m1, m2, m3, m4, m5;

    // Runs before every test: loads the brief's five test messages
    @BeforeEach
    public void setUp() {
        Message.clearAll();
        Message.setCurrentSender("+27830000000");

        m1 = new Message("1000000001", 0, "+27834557896", "Did you get the cake?");
        m2 = new Message("1000000002", 1, "+27838884567",
                "Where are you? You are late! I have asked you to be on time.");
        m3 = new Message("1000000003", 2, "+27834484567", "Yohoooo, I am at your gate.");
        m4 = new Message("0838884567", 3, "0838884567", "It is dinner time!");
        m5 = new Message("1000000005", 4, "+27838884567", "Ok, I am leaving without you.");

        // Flags from the brief: 1 = Sent, 2 = Disregard, 3 = Stored
        m1.sentMessage(1);
        m2.sentMessage(3);
        m3.sentMessage(2);
        m4.sentMessage(1);
        m5.sentMessage(3);
    }

    @Test
    public void sentMessagesArrayPopulated() {
        ArrayList<Message> sent = Message.getSentMessages();
        assertEquals(2, sent.size());
        assertEquals("Did you get the cake?", sent.get(0).getMessageText());
        assertEquals("It is dinner time!", sent.get(1).getMessageText());
    }

    @Test
    public void otherArraysPopulated() {
        assertEquals(1, Message.getDisregardedMessages().size());
        assertEquals(2, Message.getStoredMessages().size());
        assertEquals(5, Message.getMessageHashes().size());
        assertEquals(5, Message.getMessageIDs().size());
    }

    @Test
    public void displayLongestMessage() {
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                Message.longestMessage());
    }

    @Test
    public void searchForMessageID() {
        Message found = Message.findByMessageID("0838884567");
        assertNotNull(found);
        assertEquals("It is dinner time!", found.getMessageText());
    }

    @Test
    public void searchByRecipient() {
        ArrayList<String> results = Message.searchByRecipient("+27838884567");
        assertEquals(2, results.size());
        assertEquals("Where are you? You are late! I have asked you to be on time.", results.get(0));
        assertEquals("Ok, I am leaving without you.", results.get(1));
    }

    @Test
    public void deleteMessageUsingHash() {
        String result = Message.deleteByHash(m2.getMessageHash());
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
                result);
        assertEquals(1, Message.getStoredMessages().size());
    }

    @Test
    public void displayReport() {
        String report = Message.displayReport();
        assertTrue(report.contains(m1.getMessageHash()));
        assertTrue(report.contains("+27834557896"));
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains(m4.getMessageHash()));
        assertTrue(report.contains("It is dinner time!"));
        assertFalse(report.contains("Where are you?"));   // stored messages are not in the sent report
    }
}