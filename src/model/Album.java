/*
 * Authors: Lucas Hamacher and Harun Andeshmand
 * Functionality: 
 * 		holds an album object with data like artist, year, title, genre, and songs.
 * 		contains methods for getting and adding songs to internal list.
 */
package model;

import java.util.ArrayList;

public class Album {

	private String artist;
	private String year;
	private String title;
	private String genre;
	private ArrayList<Song> songs = new ArrayList<Song>();
	
	/*
	 * @pre title, artis, genre, year, songs != null
	 */

	public Album(String title, String artist, String genre, String year) {
		this.title = title;
		this.artist = artist;
		this.genre = genre;
		this.year = year;
	}
	public Album() {
        this.title = "";
        this.artist = "";
        this.genre = "";
        this.year = "";
        this.songs = new ArrayList<>();  // Initialize songs list
    }
	

	public String getTitle() { //immutable
		return title;
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

	public ArrayList<Song> getSongs() {
		return (ArrayList<Song>) songs.clone(); //clone = immutable
	}
	
	public void addSong(Song song) {
		songs.add(song);
	}
}