package com.mycompany;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    private Login make(String user, String pass, String cell) {
        Login l = new Login("Kyle", "Smith");
        l.setUsername(user);
        l.setPassword(pass);
        l.setCellNumber(cell);
        return l;
    }

    // ---------- assertEquals tests ----------
    @Test
    public void usernameCorrectMessage() {
        assertEquals("Username successfully captured.",
                make("kyl_1", "", "").usernameMessage());
    }

    @Test
    public void usernameIncorrectMessage() {
        assertEquals("Username is not correctly formatted; please ensure that your username "
                + "contains an underscore and is no more than five characters in length.",
                make("kyle!!!!!!!", "", "").usernameMessage());
    }

    @Test
    public void passwordCorrectMessage() {
        assertEquals("Password successfully captured.",
                make("", "Ch&&sec@ke99!", "").passwordMessage());
    }

    @Test
    public void passwordIncorrectMessage() {
        assertEquals("Password is not correctly formatted; please ensure that the password "
                + "contains at least eight characters, a capital letter, a number, and a special character.",
                make("", "password", "").passwordMessage());
    }

    @Test
    public void cellCorrectMessage() {
        assertEquals("Cell number successfully captured.",
                make("", "", "+27838968976").cellPhoneMessage());
    }

    @Test
    public void cellIncorrectMessage() {
        assertEquals("Cell number is incorrectly formatted or does not contain an international code; "
                + "please correct the number and try again.",
                make("", "", "08966553").cellPhoneMessage());
    }

    // ---------- assertTrue / assertFalse tests ----------
    @Test
    public void loginSuccessful() {
        Login l = make("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(l.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void loginFailed() {
        Login l = make("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(l.loginUser("kyl_1", "wrong"));
    }

    @Test
    public void usernameChecks() {
        assertTrue(make("kyl_1", "", "").checkUserName());
        assertFalse(make("kyle!!!!!!!", "", "").checkUserName());
    }

    @Test
    public void passwordChecks() {
        assertTrue(make("", "Ch&&sec@ke99!", "").checkPasswordComplexity());
        assertFalse(make("", "password", "").checkPasswordComplexity());
    }

    @Test
    public void cellChecks() {
        assertTrue(make("", "", "+27838968976").checkCellPhoneNumber());
        assertFalse(make("", "", "08966553").checkCellPhoneNumber());
    }
}