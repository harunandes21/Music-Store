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
	
	public boolean isAlbum() {return isAlbum;} //immutable, no escape
	
	public String getName() {return name;} //immutable, no escape
	
	public void setName(String name) {this.name = name;}
	
	public ArrayList<Song> getSongs() {return (ArrayList<Song>) songs.clone();} //no escaping reference
	
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
	
	
}
