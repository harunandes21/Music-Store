package view;

import java.util.Scanner;
import database.MusicStore;
import model.Account;
import model.LibraryModel;
import model.Playlist;

import java.util.ArrayList;
import java.util.HashMap;
import org.mindrot.jbcrypt.BCrypt;

public class View {

    private static MusicStore store;
    private static HashMap<String, Account> accounts = new HashMap<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            store = MusicStore.initializer("albums/albums.txt"); 
            System.out.println("Welcome to the Music Store!");
            homeScreen();
        } catch (Exception e) {
            System.out.println("Error initializing store: " + e.getMessage());
        }
    }

    private static void homeScreen() {
        while (true) {
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
                    System.out.println("Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void createAccount() {
        System.out.print("Enter new username: ");
        String username = sc.nextLine();
        if (accounts.containsKey(username)) { // validation
            System.out.println("Username already taken!");
            return;
        }
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        Account newAcc = new Account(username, password, new LibraryModel());
        System.out.println(newAcc.getHashedPassword()); //debuggin if hash works
        accounts.put(username, newAcc);
        System.out.println("Account created successfully!");
    }

    private static void login() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        Account acc = accounts.get(username);
        if (acc == null) {
            System.out.println("No account found for that username.");
            return;
        }
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        System.out.println(acc.getHashedPassword());// debugging if hash works
        String enteredPasswordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println(enteredPasswordHash);// compare if entered hash= to saved hash
        if (acc.attemptLogin(password)) {
            System.out.println("Login successful. Welcome, " + username + "!");
            userMenu(acc);
        } else {
            System.out.println("Incorrect password.");
        }
    }

    private static void userMenu(Account acc) {
        while (true) {
            System.out.println("\n1. View All Albums in Store");
            System.out.println("2. Add Song from Store to Library");
            System.out.println("3. View Library Songs");
            System.out.println("4. Manage Playlists");
            System.out.println("5. Logout");

            System.out.print("Select an option: ");
            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    viewAlbums();
                    break;
                case "2":
                    addSongToLibrary(acc);
                    break;
                case "3":
                    viewLibrarySongs(acc);
                    break;
                case "4":
                    managePlaylists(acc);
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void viewAlbums() {
        store.getAlbums().forEach(album -> {
            System.out.println("Album: " + album.getTitle() + " by " + album.getArtist());
        });
    }

    private static void addSongToLibrary(Account acc) {
        System.out.print("Enter the name of the song you want to add: ");
        String songName = sc.nextLine();
        var song = store.searchSongByName(songName);
        if (song != null) {
            acc.getLibrary().addSong(song);
            System.out.println("Added '" + song.getName() + "' to your library.");
        }
    }

    private static void viewLibrarySongs(Account acc) {
        var songs = acc.getLibrary().getAllSongs();
        if (songs.isEmpty()) {
            System.out.println("Your library is empty.");
        } else {
            songs.forEach(song -> System.out.println(song.getName() + " by " + song.getArtist()));
        }
    }

    private static void managePlaylists(Account acc) {
        System.out.println("\n1. View Playlists");
        System.out.println("2. Create Playlist");
        System.out.println("3. Add Song to Playlist");
        System.out.println("4. Remove Song from Playlist");
        System.out.println("5. Delete Playlist");
        System.out.println("6. Back");
        String option = sc.nextLine();
        
        switch (option) {
            case "1":
            	ArrayList<Playlist> playlists=acc.getLibrary().getAllPlaylists();
                for (Playlist playlist:playlists)
                	 System.out.println(playlist.getName());
            	
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
                else System.out.println("Song not found in library.");
                break;
            case "5":
                System.out.print("Playlist name: ");
                String delName = sc.nextLine();
                acc.getLibrary().deletePlaylist(delName);
                break;
            case "6":
                return;
            default:
                System.out.println("Invalid input.");
        }
    }
}
