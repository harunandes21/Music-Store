/*
 * Authors: Lucas Hamacher and Harun Andeshmand
 * Functionality: 
 * 		holds a playlist object with data like name, songs, and whether or not it is an album. 
 * 		contains methods for setting, getting, and adding and removing song objects
 * 		from internal list.
 */

package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Playlist {
	
	private String name;
	private ArrayList<Song> songs;
	private boolean isAlbum = false;

	/*
	 * @pre name, songs != null
	 */
	public Playlist(String name, ArrayList<Song> songs) {
		this.name = name;
		this.songs = songs;
	}
	
	public Playlist() {
        this.songs = new ArrayList<>();
    }
	
	public void setAlbum() {
		isAlbum = true;
	}
	
	/*
	 * @pre: n is a correct song id
	 */
	public Song getSongById(int n) {
		for (Song s: songs) {
			if (s.getSongId() == n) {
				return s;
			}
		}
		return null;
	}
	
	public boolean isAlbum() {return isAlbum;} //immutable, no escape
	
	public String getName() {return name;} //immutable, no escape
	
	public void setName(String name) {this.name = name;}
	
	public ArrayList<Song> getSongs() {return new ArrayList<Song> (songs); } //no escaping reference
	
	public void addSong(Song song) {songs.add(song);}
	
	public void removeSong(Song song) {
		for (int i = 0; i < songs.size(); i++) {
			if (songs.get(i).equals(song)) {
				songs.remove(i);
			}
		}
	}
	public boolean contains(Song song) {
        
        for (Song s : songs) {
            if (s.getName().equalsIgnoreCase(song.getName()) && s.getArtist().equalsIgnoreCase(song.getArtist())) {
                return true; // Song is already in the playlist
            }
        }
        return false; 
    }
	
	// SORTING METHODS
	
    public void shuffle() {
		Collections.shuffle(songs);
	}

    public void sortByName() {
    	songs.sort((a, b) -> a.getName().compareTo(b.getName()));
    }

    public void sortByArtist() {
    	songs.sort((a, b) -> a.getArtist().compareTo(b.getArtist()));
    }

    // sort for a playlist
    public void sortByRating() {
    	 songs.sort(Comparator.comparing(a -> a.getRating()));
    }
}
