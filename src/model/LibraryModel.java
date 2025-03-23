/*
 * Authors: Lucas Hamacher and Harun Andeshmand
 * Functionality: 
 * 		holds a music store object and maintains storage of a user's music library
 * 		contains methods for getting, setting as well as creating playlists, adding songs, removing songs,
 * 		and deleting playlists altogether.
 */

package model;

import java.util.ArrayList;
import java.util.HashMap;

import database.AccountManager;

public class LibraryModel {

    private HashMap<String, Playlist> userPlaylists = new HashMap<String, Playlist>();
	private HashMap<Song, Integer> songPlays;
	private HashMap<String, Integer> genreCount;
	private ArrayList<Song> songList;
	private ArrayList<Song> allSongs;
	private ArrayList<Song> recentlyPlayed;
	private ArrayList<Song> frequentlyPlayed;
	 
    public LibraryModel() {
		songPlays = new HashMap<Song, Integer>();
		genreCount = new HashMap<String, Integer>();
		recentlyPlayed = new ArrayList<>();
		createDefaultPlaylists();
		allSongs=  new ArrayList<Song>();
		userPlaylists = new HashMap<String, Playlist>();
		
    }
    
    //retrieves the songPlays for a song S
    public int getSongPlays(Song s) {
    	return songPlays.get(s);
    }
    
    //this is a boolean so when it is called within view, you can print message if song is already in allSongs
    public boolean addSongToLibrary(Song s) {
    	if (!allSongs.contains(s)) {allSongs.add(s);}
    	if (!songList.contains(s)) {
    		songList.add(s);
    		return true;
    	}
    	return false;
    }
    
    //boolean for message handling in view
    public boolean removeSongFromLib(Song s) {
    	if (songList.contains(s)) {
    		for(int i = 0; i < songList.size(); i++) {
    			if (songList.get(i).equals(s)) {
    				songList.remove(i);
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    public ArrayList<Song> getSongList() {
    	return new ArrayList<Song>(songList);
    }
    
    public void rateSong(Song s, Double rate) {
    	if (rate > 5) rate = 5.0; //sets max possible rating
    	s.setRating(rate);
    	//both will execute if rating = 5
    	if (rate >= 4) {
    		userPlaylists.get("Top Rated").addSong(s);
    	}
    	if (rate == 5) {
    		userPlaylists.get("Favorites").addSong(s);
    	}
    }
    
    private void createDefaultPlaylists() {	
    	createPlaylist("Favorites");
		createPlaylist("Recently Played");
		createPlaylist("Top Rated");
		createPlaylist("Frequently Played");
    }
    
    // increases song plays or enters song into songPlays
    // also updates frequently played
    public void playSong(Song s) {
    	if (songPlays.containsKey(s)) {
    		songPlays.put(s, songPlays.get(s)+1); 
    	} else {songPlays.put(s,  1);}
    	if (recentlyPlayed.size() == 10) {
            recentlyPlayed.remove(recentlyPlayed.size() - 1); // remove the least recently played song
            recentlyPlayed.add(0, s);
        } else {recentlyPlayed.add(0, s);} //adds song if recently played isnt 10 yet
    	if (frequentlyPlayed.size() < 10) {
    		frequentlyPlayed.add(frequentlyPlayed.size()-1,s); //add song at end
    		frequentlyPlayed.sort((a, b) -> songPlays.get(a).compareTo(songPlays.get(b))); 
    	} else {
    		if (songPlays.get(s) > songPlays.get(frequentlyPlayed.get(9))) {
    			frequentlyPlayed.remove(9);
    			frequentlyPlayed.add(9, s);
    			frequentlyPlayed.sort((a, b) -> songPlays.get(a).compareTo(songPlays.get(b))); //sort frequently played every time it is updated
    		}
    	}
    }
    
    public void addGenre(String g) {
    	if (genreCount.containsKey(g)) {
    		genreCount.put(g, genreCount.get(g)+1);
    	} else {genreCount.put(g, 1);}
    }
    
    private void createPlaylistForGenre(String genre) {
        Playlist genrePlaylist = new Playlist(genre, new ArrayList<>());
        userPlaylists.put(genre, genrePlaylist);
        System.out.println("Playlist for genre '" + genre + "' created.");
    }
    
    //method is to be ran any time a song is added to the library and it wasn't already there
    //i.e. this method should not be ran if song is added to playlist when it is already in library
    public void addSongToLibrary(Song s, Account acc) {
    	// check if song is already in library
    	String genre = s.getGenre();
    	addGenre(genre);
    	//if album in library, add to album
    	//else add album to library with only the given song
    	//song should also be added individually i guess
    	allSongs.add(s);
    	if (genreCount.get(genre) >= 10 && !userPlaylists.containsKey(genre)) {
            createPlaylistForGenre(genre);}
    	AccountManager.updateAccount(acc);
    	
    	
    	
    	return;
    }
    // Use this method when adding to library, use addSongToPlaylist when adding to playlist. This will increase the genre count.
    
    public ArrayList<Song> getAllSongs() {
        return new ArrayList<Song>(allSongs);
    }
    public ArrayList<Song> getRecentlyPlayed() {
        return new ArrayList<>(recentlyPlayed);
    }
    
    // adding a whole album to our library
    public void addPlaylist(Playlist playlist) {
        playlist.setAlbum();
        userPlaylists.put(playlist.getName(), playlist);
        
        System.out.println("Playlist '" + playlist.getName() + "' added to library.");
    }
    //creating a new playlist
    public void createPlaylist(String name) {
    	ArrayList<String> defaultPlaylists = new ArrayList<>();
        defaultPlaylists.add("Favorites");
        defaultPlaylists.add("Recently Played");
        defaultPlaylists.add("Top Rated");
        defaultPlaylists.add("Frequently Played");
        // avoided to print this at the beginning.
        Playlist newPlaylist = new Playlist(name, new ArrayList<>());
		userPlaylists.put(newPlaylist.getName(), newPlaylist);
        if(!defaultPlaylists.contains(name)) 
             System.out.println("Playlist '" + name + "' created.");
        
        
    }
    // song has to be in library to add id to any playlist!! 
    public void addSongToPlaylist(String playlistName, Song song) {
        Playlist playlist = userPlaylists.get(playlistName);
        if (playlist == null) {
            System.out.println("Playlist '" + playlistName + "' not found.");
            return;
        }
        else if (song == null) {
            System.out.println("Song not found.");
            return;
        }
        else 
        playlist.addSong(song);
        System.out.println("Song '" + song.getName() + "' added to playlist '" + playlistName + "'.");
    }

    public Playlist getPlaylistByName(String name) {
        return userPlaylists.get(name);
    }
    
    //must receive actual song object which is expected to be within the playlist
    public void removeSongFromPlaylist(String playlistName, Song s) {
        Playlist playlist = getPlaylistByName(playlistName);
        if (playlist == null) {
            System.out.println("Playlist '" + playlistName + "' not found.");
            return;
        }
        else if (s == null) {
            System.out.println("Song not found.");
            return;
        }
        else 
        playlist.removeSong(s); //logic is within playlist class
    }
    
    public Song getSongByName(String songName) {
        for (Playlist playlist : userPlaylists.values()) {
            for (Song song : playlist.getSongs()) {
                if (song.getName().equals(songName)) {
                    return song;
                }
            }
        }
        return null;  
    }

    public ArrayList<Playlist> getAllPlaylists() {
        return new ArrayList<>(userPlaylists.values());
    }
    public void deletePlaylist(String playlistName) {
        Playlist playlist = getPlaylistByName(playlistName);
        if (playlist != null) {
            userPlaylists.remove(playlistName); //the hash map keeps keys as names (Strings)
            System.out.println("Playlist '" + playlistName + "' deleted.");
        } else {
            System.out.println("Playlist '" + playlistName + "' not found.");
        }
    }
    
}
