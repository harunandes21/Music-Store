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
	private ArrayList<String> songs;

	/*
	 * @pre name, songs != nil
	 */
	public Playlist(String name, ArrayList<String> songs) {
		this.name = name;
		this.songs = songs;
	}
	
	public String getName() {return name;}
	
	public void setName(String name) {this.name = name;}
	
	public ArrayList<String> getSongs() {return new ArrayList<String>(songs);}
	
	public void addSong(String name) {songs.add(name);}
	
	public void removeSong(String name) {
		for (int i = 0; i < songs.size(); i++) {
			if (songs.get(i) == name) {
				songs.remove(i);
				return;
			}
		}
	}
	
	public void shuffle() {
		Collections.shuffle(songs);
	}
	
	public void sortByNames() {
		Collections.sort(songs);
	}
}
