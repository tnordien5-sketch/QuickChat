package com.mycompany;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    // Test data from the brief (IDs are fixed here so the hash is predictable)
    private Message message1;
    private Message message2;

    @BeforeEach
    public void setUp() {
        Message.clearAll();   // start every test with empty lists
        message1 = new Message("0012345678", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        message2 = new Message("0087654321", 1, "08575975889",
                "Hi Keegan, did you receive the payment?");
    }

    // ---------- Message length ----------
    @Test
    public void messageLengthSuccess() {
        assertEquals("Message ready to send.", message1.checkMessageLength());
    }

    @Test
    public void messageLengthFailure() {
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 260; i++) {
            longText.append("a");
        }
        Message tooLong = new Message("0012345678", 0, "+27718693002", longText.toString());
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                tooLong.checkMessageLength());
    }

    // ---------- Recipient number ----------
    @Test
    public void recipientSuccess() {
        assertEquals("Cell phone number successfully captured.", message1.checkRecipientCell());
    }

    @Test
    public void recipientFailure() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. "
                + "Please correct the number and try again.", message2.checkRecipientCell());
    }

    // ---------- Message hash ----------
    @Test
    public void messageHashTestCase1() {
        assertEquals("00:0:HITONIGHT", message1.getMessageHash());
    }

    @Test
    public void messageHashesInALoop() {
        Message[] messages = { message1, message2 };
        String[] expected = { "00:0:HITONIGHT", "00:1:HIPAYMENT" };
        for (int i = 0; i < messages.length; i++) {
            assertEquals(expected[i], messages[i].getMessageHash());
        }
    }

    // ---------- Message ID ----------
    @Test
    public void messageIDCreated() {
        String id = Message.generateMessageID();
        Message m = new Message(id, 0, "+27718693002", "Hello there");
        assertEquals("Message ID generated: " + id, m.messageIDGenerated());
        assertEquals(10, id.length());
        assertTrue(m.checkMessageID());
    }

    // ---------- Message sent (send / disregard / store) ----------
    @Test
    public void messageSentOptions() {
        assertEquals("Message successfully sent.", message1.sentMessage(1));
        assertEquals("Press 0 to delete the message.", message2.sentMessage(2));
        assertEquals("Message successfully stored.", message1.sentMessage(3));
    }

    // ---------- Total messages sent ----------
    @Test
    public void totalMessagesSent() {
        message1.sentMessage(1);   // sent
        message2.sentMessage(2);   // disregarded, so not counted
        assertEquals(1, Message.returnTotalMessages());
    }
}