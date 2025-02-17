package model;

public class Song {
	
	private boolean favorite;
	private double rating;

	private String title;
	private String artist;

	public Song(String name, String author) {
		title = name;
		artist = author;
	}
	
	public void setRating(double rating) {
		if (rating == 5.0) {
			favorite = true;
		}
		this.rating = rating;
	}
	
	public void setFavorite(boolean b) {
		favorite = b;
	}
	
	public String getName() {
		return title;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public double getRating() {
		return rating;
	}
	
	public boolean getFavorite() {
		return favorite;
	}
	
}
