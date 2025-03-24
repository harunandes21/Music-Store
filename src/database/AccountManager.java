package database;

import model.Account;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AccountManager {

    private static final String ACCOUNT_DIRECTORY = "accounts";  // A directory to store individual account files

    
    public static void saveAccount(Account account) {
    	if (account.getLibrary().getAllPlaylists().isEmpty()) {
            account.getLibrary().createDefaultPlaylists();
        }
    	File dir = new File(ACCOUNT_DIRECTORY);
    	if (!dir.exists()) {
    	    if (dir.mkdirs()) {
    	        System.out.println("Created accounts directory: " + dir.getAbsolutePath());
    	    } else {
    	        System.out.println("Failed to create accounts directory.");
    	    }
    	}
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File accountFile = new File(ACCOUNT_DIRECTORY, account.getUsername() + ".json");  // File named by username
        try (FileWriter writer = new FileWriter(accountFile)) {
            gson.toJson(account, writer);  // Serialize account to JSON and write to file
            System.out.println("Account saved: " + account.getUsername());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    // load an account by username
    public static Account loadAccount(String username) {
        File accountFile = new File(ACCOUNT_DIRECTORY, username + ".json");  // look for a file named by username
        if (!accountFile.exists()) {
            return null;  
        }

        Gson gson = new Gson();
        try (FileReader reader = new FileReader(accountFile)) {
            return gson.fromJson(reader, Account.class);  
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;  
    }
    public static void updateAccount(Account acc) {
       
        File accountFile = new File(ACCOUNT_DIRECTORY, acc.getUsername() + ".json");

        // Check if the file exists
        if (!accountFile.exists()) {
            System.out.println("Account file does not exist.");
            return;
        }

        
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(accountFile, false)) { 
            gson.toJson(acc, writer);  
            System.out.println("Account data updated and saved successfully.");
        } catch (IOException e) {
            System.out.println(" Error saving account data to JSON: " + e.getMessage());
        }
    }




    
    public static boolean accountExists(String username) {// search if that username exists and return if it does
        return loadAccount(username) != null;  // 
    }
}
