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

import database.MusicStore;

public class LibraryModel {

    private HashMap<String, Playlist> userPlaylists = new HashMap<String, Playlist>();
	private HashMap<Song, Integer> songPlays;
	private HashMap<String, Integer> genreCount;
	private ArrayList<Song> allSongs = new ArrayList<Song>();
    
    public LibraryModel() {
		songPlays = new HashMap<Song, Integer>();
		genreCount = new HashMap<String, Integer>();
		
		//default playlists
		createPlaylist("Favorites");
		createPlaylist("Recently Played");
		createPlaylist("Top Rated");
		createPlaylist("Frequently Played");
		
    }
    
    //retrieves the songPlays for a song S
    public int getSongPlays(Song s) {
    	return songPlays.get(s);
    }
    
    // increases song plays or enters song into songPlays
    public void playSong(Song s) {
    	if (songPlays.containsKey(s)) {
    		songPlays.put(s, songPlays.get(s)+1); 
    	} else {songPlays.put(s,  1);}
    }
    
    public void addGenre(String g) {
    	if (genreCount.containsKey(g)) {
    		genreCount.put(g, genreCount.get(g)+1);
    	} else {genreCount.put(g, 1);}
    }
    
    //method is to be ran any time a song is added to the library and it wasn't already there
    //i.e. this method should not be ran if song is added to playlist when it is already in library
    public void addSong(Song s) {
    	String genre = s.getGenre();
    	addGenre(genre);
    	//if album in library, add to album
    	//else add album to library with only the given song
    	//song should also be added individually i guess
    	allSongs.add(s);
    }
    
    public ArrayList<Song> getAllSongs() {
        return (ArrayList<Song>) allSongs.clone();
    }
    
    // adding a whole album to our library
    public void addPlaylist(Playlist playlist) {
        playlist.setAlbum();
        userPlaylists.put(playlist.getName(), playlist);
        
        System.out.println("Playlist '" + playlist.getName() + "' added to library.");
    }
    //creating a new playlist
    public void createPlaylist(String name) {
        
            Playlist newPlaylist = new Playlist(name, new ArrayList<>());
			userPlaylists.put(newPlaylist.getName(), newPlaylist);
            
            System.out.println("Playlist '" + name + "' created.");
        
    }
    
    public void addSongToPlaylist(String playlistName, Song song) {
        Playlist playlist = userPlaylists.get(playlistName);
        if (song == null) {
            System.out.println("Song not found.");
            return;
        }
        playlist.addSong(song);
        System.out.println("Song '" + song.getName() + "' added to playlist '" + playlistName + "'.");
    }

    public Playlist getPlaylistByName(String name) {
        return userPlaylists.get(name);
    }
    
    //must receive actual song object which is expected to be within the playlist
    public void removeSongFromPlaylist(String playlistName, Song s) {
        Playlist playlist = getPlaylistByName(playlistName);
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
            userPlaylists.remove(playlist); 
            System.out.println("Playlist '" + playlistName + "' deleted.");
        } else {
            System.out.println("Playlist '" + playlistName + "' not found.");
        }
    }
    
}
