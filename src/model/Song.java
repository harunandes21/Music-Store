package model;

public class Song {
	
	private boolean favorite;
	private double rating;

	private String title;
	private String artist;


	public Song(String name, String author) {
		title=name;
		artist=author;
		
	}

	private Album album;
	
	/*
	 * @pre name, author != null
	 */
	public Song(String name, String author, Album album) {

		title = name;
		artist = author;
		this.album = album;
	}
	
	public void setRating(double rating) {
		if (rating == 5.0) {
			favorite = true;
		}
		this.rating = rating;
	}
	
	public void setFavorite(boolean b) {favorite = b;}
	
	public String getName() {return title;}
	
	public Album getAlbum() {return album;}
	
	public String getArtist() {return artist;}
	
	public double getRating() {return rating;}
	
	public boolean getFavorite() {return favorite;}
	
	public boolean equals(Song song) {return ((song.getName() == title) && (song.getArtist() == artist) && (song.getAlbum() == album));}
	
}
