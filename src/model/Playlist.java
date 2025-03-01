

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
	
	public boolean isAlbum() {return isAlbum;}
	
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
	
	
}
