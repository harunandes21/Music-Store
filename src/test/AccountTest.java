package test;

import model.Account;
import model.LibraryModel;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountTest {

    @Test
    public void testAccountConstructor() {
        // Create a new LibraryModel and Account
        LibraryModel libraryModel = new LibraryModel();
        Account account = new Account("testUser", "password123", libraryModel);

        // Test if the username is set correctly
        assertEquals("testUser", account.getUsername());
        
        // Test if the password is hashed correctly (you can't directly check the hashed password, but you can check if the password matches the hashed one)
        assertTrue(account.attemptLogin("password123"));
        assertFalse(account.attemptLogin("wrongPassword"));
    }

    @Test
    public void testAttemptLoginWithCorrectPassword() {
        // Create a new LibraryModel and Account
        LibraryModel libraryModel = new LibraryModel();
        Account account = new Account("testUser", "password123", libraryModel);
        
        // Test login attempt with correct password
        assertTrue(account.attemptLogin("password123"));
    }

    @Test
    public void testAttemptLoginWithIncorrectPassword() {
        // Create a new LibraryModel and Account
        LibraryModel libraryModel = new LibraryModel();
        Account account = new Account("testUser", "password123", libraryModel);

        // Test login attempt with incorrect password
        assertFalse(account.attemptLogin("wrongPassword"));
    }

    @Test
    public void testGetLibrary() {
        // Create a new LibraryModel and Account
        LibraryModel libraryModel = new LibraryModel();
        Account account = new Account("testUser", "password123", libraryModel);

        // Test that the library associated with the account is returned correctly
        assertNotNull(account.getLibrary());
    }
}
