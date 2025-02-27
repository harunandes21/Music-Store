/*
 * Author: Lucas Hamacher
 * Functionality:
 * 		Creates an object for an ablum, storing songs as well as data specific to an individual album.
 * 		Class contains methods for retrieval and adding to its internal list.
 */

package model;

import java.util.ArrayList;

public class Album {

	private String artist;
	private String year;
	private String title;
	private String genre;
	private ArrayList<Song> songs;


	
	
	/*
	 * @pre title, artis, genre, year, songs != null
	 */

	public Album(String title, String artist, String genre, String year, ArrayList<Song> songs) {
		this.title = title;
		this.artist = artist;
		this.genre = genre;
		this.year = year;
		this.songs = songs;
	}

	public String getTitle() {
		return title;
	}

	public String getArtist() {
		return artist;
	}

	public String getGenre() {
		return genre;
	}

	public String getYear() {
		return year;
	}

	public ArrayList<Song> getSongs() {
		return songs;
	}
	
	public void addSong(Song song) {
		songs.add(song);
	}
}