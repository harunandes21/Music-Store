/*
 * Authors: Lucas Hamacher and Harun Andeshmand
 * Functionality: 
 * 		holds a music store object and maintains storage of a user's music library
 * 		contains methods for getting, setting as well as creating playlists, adding songs, removing songs,
 * 		and deleting playlists altogether.
 */

package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import database.AccountManager;

public class LibraryModel {

    private HashMap<String, Playlist> userPlaylists = new HashMap<String, Playlist>();
	private HashMap<Song, Integer> songPlays = new HashMap<>();
	private HashMap<String, Integer> genreCount = new HashMap<>();;
	private ArrayList<Song> songList = new ArrayList<Song>();
	private ArrayList<Song> allSongs = new ArrayList<Song>();;
	
	private ArrayList<String> defaultPlaylists = new ArrayList<>();
	 
    public LibraryModel() {
		songPlays = new HashMap<Song, Integer>();
		genreCount = new HashMap<String, Integer>();
		
		defaultPlaylists.add("Favorites");
        defaultPlaylists.add("Recently Played");
        defaultPlaylists.add("Top Rated");
        defaultPlaylists.add("Frequently Played");
        createDefaultPlaylists();
        
		allSongs =  new ArrayList<Song>();
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
    public Song getSongById(int songId) {
        for (Song song : allSongs) {
            if (song.getSongId() == songId) {
                return song; 
            }
        }
        return null; 
    }

    
    
    public boolean removeSongFromLib(int id, Account acc) {
    	
    	Song songToBeRemoved=getSongById(id);
    	if (songToBeRemoved!=null) {
    		allSongs.remove(songToBeRemoved);
    		AccountManager.updateAccount(acc);
    		return true;
    		
    	}
    	
    	return false;
    }
    
    public ArrayList<Song> getSongList() {
    	return new ArrayList<Song>(songList);
    }
    
    public void rateSong(Song s, String rate) {
    	boolean add = s.setRating(rate);
    	if (add) {
    		if (s.getFavorite()) {
    			userPlaylists.get("Favorites").addSong(s);
    		}
    		userPlaylists.get("Top Rated").addSong(s);
    	}
    }
    
    public void createDefaultPlaylists() {	
    	createPlaylist("Favorites");
		createPlaylist("Recently Played");
		createPlaylist("Top Rated");
		createPlaylist("Frequently Played");
    }
    
    // increases song plays or enters song into songPlays
    // also updates frequently played
    public void playSong(Song s) {
    	Playlist recentlyPlayed = userPlaylists.get("Recently Played");
    	Playlist frequentlyPlayed = userPlaylists.get("Frequently Played");
    	if (songPlays.containsKey(s)) {
    		songPlays.put(s, songPlays.get(s)+1); 
    	} else {songPlays.put(s,  1);}
    	if (recentlyPlayed.getSize() == 10) {
    		if (recentlyPlayed.contains(s)) {
    			recentlyPlayed.removeSong(s);
    		} else {
	            recentlyPlayed.trimEnd(); // remove the least recently played song
	            recentlyPlayed.addAtBeginning(s);
    		}
        } else {recentlyPlayed.addAtBeginning(s);} //adds song if recently played isnt 10 yet
    	if (frequentlyPlayed.getSize() < 10) {
    		if (!frequentlyPlayed.contains(s)) {
    			frequentlyPlayed.addSong(s);
    		}
    	} else {
    		if (songPlays.get(s) > songPlays.get(frequentlyPlayed.getEnd())) {
    			if (!frequentlyPlayed.contains(s)) {    				
    				frequentlyPlayed.trimEnd();
    				frequentlyPlayed.addSong(s);
    			}
    		}
    	}
    	frequentlyPlayed.sortByPlays(songPlays);
    	s.play();
    }
    
    public void addGenre(String g) {
    	if (genreCount.containsKey(g)) {
    		genreCount.put(g, genreCount.get(g)+1);
    	} else {genreCount.put(g, 1);}
    }
    
    private void createPlaylistForGenre(String genre,Account acc) {
        Playlist genrePlaylist = new Playlist(genre, new ArrayList<>());
        userPlaylists.put(genre, genrePlaylist);
        AccountManager.updateAccount(acc);
    }
    
    public void addSongToPlaylist(String playlistName, Song song,Account acc) {
        Playlist playlist = userPlaylists.get(playlistName);
        if (playlist == null) {
            return;
        }
        else if (song == null) {
            return;
        }
        else 
        playlist.addSong(song);
        AccountManager.updateAccount(acc);
        
    }
    
    //method is to be ran any time a song is added to the library and it wasn't already there
    //i.e. this method should not be ran if song is added to playlist when it is already in library
    public boolean addSongToLibrary(Song s, Album a, Account acc) {
    	if(!allSongs.contains(s)) {
    	// check if song is already in library
    		if (userPlaylists.containsKey(a.getName())) {
    			if (!userPlaylists.get(a.getName()).contains(s)) {
    				userPlaylists.get(a.getName()).addSong(s);
    			}
    		} else {
    			Playlist halfAlbum = new Playlist(a.getName(), new ArrayList<Song>());
    			halfAlbum.addSong(s);
    			userPlaylists.put(a.getName(), halfAlbum);
    		}
	    	String genre = s.getGenre();
	    	addGenre(genre);
	    	//if album in library, add to album
	    	//else add album to library with only the given song
	    	//song should also be added individually i guess
	    	allSongs.add(s);
	    	if (genreCount.get(genre) >= 10 && !userPlaylists.containsKey(genre)) {
	            createPlaylistForGenre(genre,acc);
	            
	            }
	    	AccountManager.updateAccount(acc);
	    	ArrayList<Song> genreSongs = searchSongByGenre(genre); 
	        Playlist genrePlaylist = userPlaylists.get(genre); 
	        
	        if(genrePlaylist!=null) {
		        for (Song song : genreSongs) {
		             if (!genrePlaylist.contains(song)) 
		               addSongToPlaylist(genre,song,acc); 
		        }
	    	
		        return true;
	    	}
	        return true;
	    	}
    	else 
    		
    	
		return false;
    }
    // Use this method when adding to library, use addSongToPlaylist when adding to playlist. This will increase the genre count.
    
    public ArrayList<Song> getAllSongs() {
        return new ArrayList<Song>(allSongs);
    }
    
    // adding a whole album to our library
    public void addPlaylist(Playlist playlist) {
        playlist.setAlbum();
        userPlaylists.put(playlist.getName(), playlist);
    }
    //creating a new playlist
    public void createPlaylist(String name) {
        // avoided to print this at the beginning.
        Playlist newPlaylist = new Playlist(name, new ArrayList<>());
		userPlaylists.put(newPlaylist.getName(), newPlaylist);     
    }

    public Playlist getPlaylistByName(String name) {
    	for (String s : userPlaylists.keySet()) {
    		if (name.toLowerCase().equals(s.toLowerCase())) {
    			return userPlaylists.get(s);
    		}
    	}
    	return null;
    }
    
    //must receive actual song object which is expected to be within the playlist
    public void removeSongFromPlaylist(String playlistName, Song s) {
        Playlist playlist = getPlaylistByName(playlistName);
        if (playlist == null) {
            return;
        }
        else if (s == null) {
            return;
        }
        else 
        playlist.removeSong(s); //logic is within playlist class
    }
    
    public Song getSongByName(String songName) {
            for (Song song : allSongs) {
                if (song.getName().equalsIgnoreCase(songName)) {
                    return song;
                }
            }
        
        return null;  
    }
    
    public ArrayList<Song> searchSongByTitle(String title){
    	ArrayList<Song> results = new ArrayList<>();
    	
    	for (Song song: allSongs) {
    		if(song.getName().toLowerCase().contains(title)) {
    			results.add(song);
    		}
    	}
    	
    	return results;
    }
    
    public ArrayList<Song> searchSongByGenre(String genre)
    {
    	ArrayList<Song> results= new ArrayList<>();
    	
    	for(Song song: allSongs) {
    		if(genre.equalsIgnoreCase(song.getGenre()))
    			results.add(song);
    			
    	}
    	
    	return results;
    }
    

    public HashMap<String, Playlist> getPlaylistMap() {
    	return new HashMap<>(userPlaylists);
    }

    public ArrayList<Playlist> getAllPlaylists() {
        return new ArrayList<Playlist>(userPlaylists.values());
    }
    public void deletePlaylist(String playlistName) {
        Playlist playlist = getPlaylistByName(playlistName);
        if (playlist != null) {
            userPlaylists.remove(playlistName); //the hash map keeps keys as names (Strings)
        }
    }
    
    public void shuffle() {
		Collections.shuffle(allSongs);
	}
	
 // sort for a playlist
    public void sortByName(ArrayList<Song> songs) {
        songs.sort((a, b) -> a.getName().compareTo(b.getName()));
    }

    public void sortByName() {
        allSongs.sort((a, b) -> a.getName().compareTo(b.getName()));
    }

    // sort for a playlist
    public void sortByArtist(ArrayList<Song> songs) {
        songs.sort((a, b) -> a.getArtist().compareTo(b.getArtist()));
    }

    public void sortByArtist() {
        allSongs.sort((a, b) -> a.getArtist().compareTo(b.getArtist()));
    }

    // sort for a playlist
    public void sortByRating(ArrayList<Song> songs) {
        songs.sort(Comparator.comparing(a -> a.getRating()));
    }

    public void sortByRating() {
        allSongs.sort(Comparator.comparing(a -> a.getRating()));
    }

    
}
