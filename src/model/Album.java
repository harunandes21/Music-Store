/*
 * Authors: Lucas Hamacher and Harun Andeshmand
 * Functionality: 
 * 		holds an album object with data like artist, year, title, genre, and songs.
 * 		contains methods for getting and adding songs to internal list.
 */
package model;

import java.util.ArrayList;

public class Album extends Playlist{

	private String artist;
	private String year;
	private String genre;
	
	/*
	 * @pre title, artis, genre, year, songs != null
	 */

	public Album(String title, String artist, String genre, String year) {
		this.name = title;
		this.artist = artist;
		this.genre = genre;
		this.year = year;
		setAlbum();
	}
	public Album() {
        this.name = "";
        this.artist = "";
        this.genre = "";
        this.year = "";
        this.songs = new ArrayList<>();  // Initialize songs list
        setAlbum();
    }

	public String getArtist() { //immutable
		return artist;
	}

	public String getGenre() { //immutable
		return genre;
	}

	public String getYear() { //immutable
		return year;
	}
}