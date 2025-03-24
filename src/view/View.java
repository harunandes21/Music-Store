package view;

import database.AccountManager;
import database.MusicStore;
import model.Account;
import model.LibraryModel;
import model.Playlist;
import model.Song;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.Scanner;

public class View {

    private static MusicStore store;
    private static HashMap<String, Account> accounts = new HashMap<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
    	System.out.println("Current working directory: " + System.getProperty("user.dir"));

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
        if (AccountManager.accountExists(username)) { // validation
            System.out.println("❌ Username already taken!");
            return;
        }
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        Account newAcc = new Account(username, password, new LibraryModel());
        System.out.println("🔐 Account created successfully!");
        AccountManager.saveAccount(newAcc);
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
        Account acc = AccountManager.loadAccount(username);
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
        System.out.println("8. Search Song by Genre");
        System.out.println("9. Go Back to The Main Menu");
        
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
               
                System.out.println("Searching for Song by Name...");
                break;
            case "4":
               
                System.out.println("Searching for Song by Singer...");
                break;
            case "5":
                
                System.out.println("Searching for Song by Author...");
                break;
            case "6":
               
                System.out.println("Searching for Album by Title...");
                break;
            case "7":
                
                System.out.println("Searching for Album by Singer...");
                break;
            case "8":
                
            	searchAndAddSongsByGenre(acc);
                break;
            case "9":
                
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
          if(  acc.getLibrary().addSongToLibrary(song,acc))
            System.out.println("Added '" + song.getName() + "' to your library.");
          else System.out.println("❌ Song is already in your library");
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
            System.out.println("3. Search a song");
            System.out.println("4. Go Back");
            System.out.print("Select an option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    if (! viewLibrarySongs(acc,"Songs are sorted by the order of being added to library"))
                    break;
                    
                    boolean songsMenu = true;
                    while (songsMenu) {
                        System.out.println("\nWould you like to sort your library?");
                        System.out.println("1. Sort by Song Title");
                        System.out.println("2. Sort by Artist");
                        System.out.println("3. Sort by Rating");
                        System.out.println("4. Shuffle Songs");
                        System.out.println("5. Remove a song from your library");
                        System.out.println("6. Rate a song");
                        
                        System.out.println("7. Go Back");

                        System.out.print("Select an option: ");
                        String option = sc.nextLine();

                        switch (option) {
                            case "1":
                                sortSongsByTitle(acc);
                                break;
                            case "2":
                                sortSongsByArtist(acc);
                                break;
                            case "3":
                            	 sortSongsByRating(acc);
                                
                                break;
                            case "4":
                            	shuffle(acc);
                                break;
                            case "5":
                            	System.out.println("Enter Song ID to remove");
                            	 String songid = sc.nextLine();
                            	 deleteSongFromLib(acc,Integer.parseInt(songid));
                            	
                                break;
                            case "7":
                                songsMenu = false; 
                                break;
                            default:
                                System.out.println("❌ Invalid option. Try again.");
                        }
                    }
                    break;
                case "2":
                    managePlaylists(acc);
                    break;
                case "3":
                    searchInLibrary(acc);
                    break;
                case "4":
                    return;
                default:
                    System.out.println("❌ Invalid choice. Try again.");
            }
        }
    }
    private static void shuffle(Account acc)
    {
    	acc.getLibrary().shuffle();
    	
    	viewLibrarySongs(acc,"Songs are being shown in a random order");
    	
    }
    private static void sortSongsByRating(Account acc) {
        acc.getLibrary().sortByRating();
        
        viewLibrarySongs(acc,"Sorted by Rating");
    }
    private static void sortSongsByArtist(Account acc) {
        acc.getLibrary().sortByArtist();
        
        viewLibrarySongs(acc,"Sorted by Artist Name");
    }
    private static void sortSongsByTitle(Account acc) {
        acc.getLibrary().sortByName(); // Calls sortByName() on user's library
       
        viewLibrarySongs(acc,"Sorted by Song Title"); // Show updated sorted list
    }

    private static boolean viewLibrarySongs(Account acc, String viewType) {
        System.out.println("\n=====================");
        
        System.out.println("=====================");
        ArrayList<Song> songs = new ArrayList<>(acc.getLibrary().getAllSongs());
        if (songs.isEmpty()) {
            System.out.println("🎧 Your library is empty.");
            return false;
        } else {
        	System.out.println(" 📚 Your Library Songs( "+viewType+") ");
        	for(Song song:songs)
        		System.out.println(song);
        }
        return true;
    }
    private static void deleteSongFromLib(Account acc,int id) {
    		String songName=acc.getLibrary().getSongById(id).getName();
    	if(acc.getLibrary().removeSongFromLib(id, acc)) {
    		System.out.println(""+songName +" Has been Removed");
    		 viewLibrarySongs(acc,"Songs are sorted by the order of being added to library");
    	}
    	else 
    		{System.out.println("Song not found");
    	 viewLibrarySongs(acc,"Songs are sorted by the order of being added to library");
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
                acc.getLibrary().addSongToPlaylist(plName, song,acc);
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
    public static void searchInLibrary(Account acc)
    {
    	System.out.println("1. Search a Song by Name.");
    	System.out.println("2. Search a Song by Genre.");
    	System.out.print("Enter your Choice: ");
        String choice = sc.nextLine();
        switch (choice) {
        case "1":
        	System.out.print("Enter the Song Name: ");
        	String songName = sc.nextLine();
        	acc.getLibrary().getSongByName(songName);
        	
            break;
        case "2":
        	System.out.print("Please type in one genre to search: ");
        	System.out.print("Pop---Rock---Alternative---Latin---Traditional Country---6.Singer/SongWriter  ");
        	String genre = sc.nextLine();
        	ArrayList<Song> results= new ArrayList<>(acc.getLibrary().searchSongByGenre(genre));
        	for(Song song: results)
        	System.out.println(song);
        	
            break;
        case "4":
            return;
        default:
            System.out.println("❌ Invalid choice. Try again.");
        	}
        }
    private static void searchAndAddSongsByGenre(Account acc) {
        System.out.print("Enter the genre you want to search for: ");
        String genre = sc.nextLine();
        
        
        ArrayList<Song> genreSongs = store.searchSongByGenreInStore(genre);
        
        if (genreSongs.isEmpty()) {
            System.out.println("No songs found for the genre: " + genre);
            return;
        }

        
        System.out.println("\nSongs found for genre: " + genre);
        for(Song song:genreSongs)
        	System.out.println(song);
//        for (int i = 0; i < genreSongs.size(); i++) {
//            Song song = genreSongs.get(i);
//            System.out.println((i + 1) + ". " + song.getName() + " by " + song.getArtist());
//        }

        System.out.print("\nEnter the ID's of the songs you want to add (comma separated): ");
        String input = sc.nextLine();
        String[] songNumbers = input.split(",");

       
        for (String number : songNumbers) {
            try {
                int songId = Integer.parseInt(number.trim()); 
                boolean songFound = false;

                
                for (Song song : genreSongs) {
                    if (song.getSongId() == songId) {
                        songFound = true;

                        
                        if (acc.getLibrary().addSongToLibrary(song, acc)) {
                            System.out.println("Added '" + song.getName() + "' to your library.");
                        } else {
                            System.out.println("❌ Song '" + song.getName() + "' is already in your library.");
                        }
                        break;
                    }
                }

                if (!songFound) {
                    System.out.println("❌ Song with ID " + songId + " not found.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter valid song IDs.");
            }
        }

    }

}
