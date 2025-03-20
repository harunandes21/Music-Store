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
	
	// SORTING METHODS
	
	public void shuffle() {
		Collections.shuffle(songs);
	}
	
	public void sortByName() {
		songs.sort((a,b) -> a.getName().compareTo(b.getName()));
	}
	
	public void sortByArtist() {
		songs.sort((a,b) -> a.getArtist().compareTo(b.getArtist()));
	}
	
	public void sortByRating() {
		songs.sort((a,b) -> Double.compare(a.getRating(), b.getRating()));
	}
}
