package view;

import java.util.Scanner;
import database.MusicStore;
import model.Account;
import model.LibraryModel;
import model.Playlist;
import model.Song;

import java.util.ArrayList;
import java.util.HashMap;

public class View {

    private static MusicStore store;
    private static HashMap<String, Account> accounts = new HashMap<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            store = MusicStore.initializer("albums/albums.txt"); 
            homeScreen();
        } catch (Exception e) {
            System.out.println("Error initializing store: " + e.getMessage());
        }
    }

    private static void homeScreen() {
        while (true) {
            System.out.println("\n=====================");
            System.out.println("   Welcome to the Music Store");
            System.out.println("=====================");
            System.out.println("\n1. Log In");
            System.out.println("2. Create Account");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    login();
                    break;
                case "2":
                    createAccount();
                    break;
                case "3":
                    System.out.println("\nGoodbye! 🎶");
                    System.exit(0);
                default:
                    System.out.println("❌ Invalid choice. Please try again. ❌");
            }
        }
    }

    private static void createAccount() {
        System.out.println("\n=====================");
        System.out.print("Enter new username: ");
        String username = sc.nextLine();
        if (accounts.containsKey(username)) { // validation
            System.out.println("❌ Username already taken!");
            return;
        }
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        Account newAcc = new Account(username, password, new LibraryModel());
        System.out.println("🔐 Account created successfully!");
        accounts.put(username, newAcc);
        // Add new options after account creation
        System.out.println("\n1. Go to Log In");
        System.out.println("2. Go back to Main Menu");
        System.out.print("Select an option: ");
        String choice = sc.nextLine();
        if (choice.equals("1")) {
            login();
        } else {
            homeScreen();
        }
    }

    private static void login() {
        System.out.println("\n=====================");
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        Account acc = accounts.get(username);
        if (acc == null) {
            System.out.println("❌ No account found for that username.");
            return;
        }
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        if (acc.attemptLogin(password)) {
            System.out.println("🎉 Login successful. Welcome, " + username + " 👤!");
            userMenu(acc);
        } else {
            System.out.println("❌ Incorrect password.");
        }
    }

    private static void userMenu(Account acc) {
        while (true) {
            System.out.println("\n=====================");
            System.out.println(" 🎧 Main Menu,                     " + acc.getUsername() + " 👤 ");
            System.out.println("=====================");
            System.out.println("\n1. Go to The Music Store");
            System.out.println("2. Go to Your Library");
            System.out.println("3. Log Out");
            System.out.print("Select an option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    musicStore(acc);
                    break;
                case "2":
                    libraryMenu(acc);
                    break;
                case "3":
                    System.out.println("👋 Logging out...");
                    return;
                default:
                    System.out.println("❌ Invalid choice. Try again.");
            }
        }
    }

    private static void musicStore(Account acc) {
        System.out.println("\n=====================");
        System.out.println("🎶 Welcome to the Music Store,             " + acc.getUsername() + " 👤");
        System.out.println("=====================");
        System.out.println("\n1. View All Albums");
        System.out.println("2. Add Song to Your Library");
        System.out.println("3. Search Song by Name");
        System.out.println("4. Search Song by Singer");
        System.out.println("5. Search Song by Author");
        System.out.println("6. Search Album by Title");
        System.out.println("7. Search Album by Singer");
        System.out.println("8. Go Back to The Main Menu");
        
        System.out.print("Select an option: ");
        String choice = sc.nextLine();

        switch (choice) {
            case "1":
                viewAlbums(acc);
                break;
            case "2":
                addSongToLibrary(acc);
                break;
            case "3":
                // Placeholder for search logic
                System.out.println("Searching for Song by Name...");
                break;
            case "4":
                // Placeholder for search logic
                System.out.println("Searching for Song by Singer...");
                break;
            case "5":
                // Placeholder for search logic
                System.out.println("Searching for Song by Author...");
                break;
            case "6":
                // Placeholder for search logic
                System.out.println("Searching for Album by Title...");
                break;
            case "7":
                // Placeholder for search logic
                System.out.println("Searching for Album by Singer...");
                break;
            case "8":
                // Placeholder for search logic
            	userMenu(acc);
                break;
            default:
                System.out.println("❌ Invalid choice. Try again.");
        }
    }

    private static void viewAlbums(Account account) {
        System.out.println("\n=====================");
        System.out.println(" 🎶 Available Albums 🎶 ");
        System.out.println("=====================");
        store.getAlbums().forEach(album -> {
            System.out.println("🎵 " + album.getTitle() + " by " + album.getArtist());
        });
    }

    private static void addSongToLibrary(Account acc) {
        System.out.print("Enter the name of the song you want to add: ");
        String songName = sc.nextLine();
        Song song = store.searchSongByName(songName); // Assuming this method exists in MusicStore
        if (song != null) {
            acc.getLibrary().addSong(song);
            System.out.println("Added '" + song.getName() + "' to your library.");
        } else {
            System.out.println("❌ Song not found.");
        }
    }

    private static void libraryMenu(Account acc) {
        while (true) {
            System.out.println("\n=====================");
            System.out.println(" 📚 Your Library 🎶 ");
            System.out.println("=====================");
            System.out.println("1. View Library Songs");
            System.out.println("2. Manage Playlists");
            System.out.println("3. Go Back");
            System.out.print("Select an option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    viewLibrarySongs(acc);
                    break;
                case "2":
                    managePlaylists(acc);
                    break;
                case "3":
                    return;
                default:
                    System.out.println("❌ Invalid choice. Try again.");
            }
        }
    }

    private static void viewLibrarySongs(Account acc) {
        System.out.println("\n=====================");
        System.out.println(" 📚 Your Library Songs 📚 ");
        System.out.println("=====================");
        var songs = acc.getLibrary().getAllSongs();
        if (songs.isEmpty()) {
            System.out.println("🎧 Your library is empty.");
        } else {
            songs.forEach(song -> System.out.println("🎵 " + song.getName() + " by " + song.getArtist()));
        }
    }

    private static void managePlaylists(Account acc) {
        System.out.println("\n=====================");
        System.out.println(" 📀 Manage Your Playlists 📀 ");
        System.out.println("=====================");
        System.out.println("1. View Playlists");
        System.out.println("2. Create Playlist");
        System.out.println("3. Add Song to Playlist");
        System.out.println("4. Remove Song from Playlist");
        System.out.println("5. Delete Playlist");
        System.out.println("6. Back");
        System.out.print("Select an option: ");
        String option = sc.nextLine();

        switch (option) {
            case "1":
                ArrayList<Playlist> playlists = acc.getLibrary().getAllPlaylists();
                if (playlists.isEmpty()) {
                    System.out.println("🎶 No playlists found.");
                } else {
                    playlists.forEach(playlist -> System.out.println("🎵 Playlist: " + playlist.getName()));
                }
                break;
            case "2":
                System.out.print("Enter playlist name: ");
                String name = sc.nextLine();
                acc.getLibrary().createPlaylist(name);
                
                break;
            case "3":
                System.out.print("Playlist name: ");
                String plName = sc.nextLine();
                System.out.print("Song name: ");
                String songName = sc.nextLine();
                var song = acc.getLibrary().getSongByName(songName);
                if (song == null) song = store.searchSongByName(songName);
                acc.getLibrary().addSongToPlaylist(plName, song);
                break;
            case "4":
                System.out.print("Playlist name: ");
                String rplName = sc.nextLine();
                System.out.print("Song name: ");
                String rsongName = sc.nextLine();
                var rsong = acc.getLibrary().getSongByName(rsongName);
                if (rsong != null) acc.getLibrary().removeSongFromPlaylist(rplName, rsong);
                else System.out.println("❌ Song not found in library.");
                break;
            case "5":
                System.out.print("Playlist name: ");
                String delName = sc.nextLine();
                acc.getLibrary().deletePlaylist(delName);
                break;
            case "6":
                return;
            default:
                System.out.println("❌ Invalid input.");
        }
    }
}
