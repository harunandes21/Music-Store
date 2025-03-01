/*
 * Author: Lucas Hamacher
 * Functionality:
 * 		Creates an object representing a playlist, which contists an Array of Strings (songs).
 * 		Contains methods for setting and getting as well as sorting and shuffling.
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
	
	public boolean getIsAlbum() {return isAlbum;}
	
	public String getName() {return name;}
	
	public void setName(String name) {this.name = name;}
	
	public ArrayList<Song> getSongs() {return songs;}
	
	public void addSong(Song song) {songs.add(song);}
	
	public void removeSong(Song song) {
		for (int i = 0; i < songs.size(); i++) {
			if (songs.get(i).equals(song)) {
				songs.remove(i);
			}
		}
	}
	
	public void shuffle() {
		Collections.shuffle(songs);
	}
	
	public void sortByNames() {
		songs.sort((a,b) -> (a.getName().compareTo(b.getName())));
	}
	
	public void sortByYear() {
		songs.sort((a,b) -> (a.getAlbum().getYear().compareTo(b.getAlbum().getYear())));
	}
}
