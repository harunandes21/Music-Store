package test;

import static org.junit.Assert.*;

import model.Account;
import model.LibraryModel;
import org.junit.Test;
import database.AccountManager;

import java.io.File;

public class AccountManagerTest {

    private static final String ACCOUNT_DIRECTORY = "accounts";  // Directory where account files are saved

    @Test
    public void testAccountManagerMethods() {
        // Test saveAccount method
        LibraryModel libraryModel = new LibraryModel();  // Create a new LibraryModel
        Account testAccount = new Account("testUser", "password123", libraryModel);
        
        // Ensure the accounts directory exists
        File directory = new File(ACCOUNT_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();  // Create the directory if it doesn't exist
        }

        // Save account
        AccountManager.saveAccount(testAccount);

        // Test if the file is created in the correct directory
        File file = new File(directory, "testUser.json");
        System.out.println("Saving account to: " + file.getAbsolutePath());  // Print file path for debugging
        assertTrue(file.exists());  // Assert file exists after saving

        // Test loadAccount method
        Account loadedAccount = AccountManager.loadAccount("testUser");
        assertNotNull(loadedAccount);  // Assert loaded account is not null
        assertEquals("testUser", loadedAccount.getUsername());  // Assert the username matches

        // Test accountExists method
        assertTrue(AccountManager.accountExists("testUser"));  // Account should exist
        assertFalse(AccountManager.accountExists("nonExistentUser"));  // Non-existent account should return false

        // Test loadAccount method when account doesn't exist
        Account nonExistentAccount = AccountManager.loadAccount("doesNotExist");
        assertNull(nonExistentAccount);  // Should return null if account doesn't exist

        // Test updateAccount method when account exists
        Account updatedAccount = new Account("testUser", "newPassword123", new LibraryModel());
        AccountManager.updateAccount(updatedAccount);  // Attempt to update the existing account

        // Verify that the file was updated
        File updatedFile = new File(directory, "testUser.json");
        assertTrue(updatedFile.exists());  // File should still exist after update
        Account loadedUpdatedAccount = AccountManager.loadAccount("testUser");
        assertNotNull(loadedUpdatedAccount);
        assertEquals("testUser", loadedUpdatedAccount.getUsername());  // Verify the username is correct

        // Test updateAccount method when account doesn't exist
        Account fakeAccount = new Account("fakeUser", "password123", new LibraryModel());
        AccountManager.updateAccount(fakeAccount);  // Attempt to update non-existent account
        assertFalse(AccountManager.accountExists("fakeUser"));  // Account should not exist after update

        // Test saveAccount method with default playlists
        Account accountWithDefaultPlaylists = new Account("testUserWithPlaylists", "password123", new LibraryModel());
        accountWithDefaultPlaylists.getLibrary().createDefaultPlaylists();
        AccountManager.saveAccount(accountWithDefaultPlaylists);

        // Verify that the file is created with default playlists
        File fileWithPlaylists = new File(directory, "testUserWithPlaylists.json");
        assertTrue(fileWithPlaylists.exists());  // File should exist after saving with playlists
    }
}