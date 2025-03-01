package model;

public class Song {
	
	private boolean favorite;
	private int rating;

	private String title;
	private String artist;
	private Album album;

	/*
	 * @pre name, author != null
	 */
	public Song(String name, String author) {
		title=name;
		artist=author;
		
		rating = 0;
		favorite = false;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}
	
	public void setRating(int rating) {
		if (rating == 5) {
			favorite = true;
		}
		this.rating = rating;
	}
	
	public void setFavorite(boolean b) {favorite = b;}
	
	public String getName() {return title;}
	
	public Album getAlbum() {return album;}
	
	public String getArtist() {return artist;}
	
	public int getRating() {return rating;}
	
	public boolean getFavorite() {return favorite;}
	
	public boolean equals(Song song) {return ((song.getName() == title) && (song.getArtist() == artist) && (song.getAlbum() == album));}
	
}
