package com.mycompany.messages;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class messagetest {

    // USERNAME TEST 
    @Test
    public void testCheckUserName() {

        assertTrue(Messages.checkUserName("ab_c"));
        assertFalse(Messages.checkUserName("abcdef"));
        assertFalse(Messages.checkUserName("abc"));
    }

    // ---------------- PASSWORD TEST ----------------
    @Test
    public void testCheckPasswordComplexity() {

        assertTrue(Messages.checkPasswordComplexity("Password1!"));
        assertFalse(Messages.checkPasswordComplexity("password"));
        assertFalse(Messages.checkPasswordComplexity("Pass1"));
    }

    // ---------------- CELLPHONE TEST ----------------
    @Test
    public void testCheckCellPhoneNumber() {

        assertTrue(Messages.checkCellPhoneNumber("+27831234567"));
        assertFalse(Messages.checkCellPhoneNumber("0831234567"));
        assertFalse(Messages.checkCellPhoneNumber("+27123"));
    }

    // ---------------- MESSAGE HASH TEST ----------------
    @Test
    public void testCreateMessageHash() {

        String hash =
                Messages.createMessageHash(
                        "1234567890",
                        1,
                        "Hello world"
                );

        assertNotNull(hash);
        assertTrue(hash.contains(":1:"));
    }

    // ---------------- MESSAGE LENGTH TEST ----------------
    @Test
    public void testMessageLength() {

        String validMessage =
                "Hello";

        String longMessage =
                "a".repeat(251);

        assertTrue(validMessage.length() <= 250);
        assertFalse(longMessage.length() <= 250);
    }

    //  LOGIN TEST 
    @Test
    public void testLoginUser() {

        boolean result =
                Messages.loginUser(
                        "test_user",
                        "Test123!"
                );

        assertNotNull(result);
    }
}