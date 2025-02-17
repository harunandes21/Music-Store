package model;

import java.util.ArrayList;

public class Album {

	private String artist;
	private String year;
	private String title;
	private String genre;
	private ArrayList<Song> songs;

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

}