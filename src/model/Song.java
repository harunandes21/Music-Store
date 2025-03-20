/*
 * Authors: Lucas Hamacher and Harun Andeshmand
 * Functionality: 
 * 		holds a Song object, containing data like title, artist, and album.
 * 		contains methods for setting and getting attributes
 */

package model;

public class Song {
	
	private double rating;
	private String title;
	private String artist;
	private Album album;
	private String genre;
	/*
	 * @pre name, author, genre != null
	 */
	public Song(String name, String author, String genre, Album a) {
		title=name;
		artist=author;
		rating = 0.0;
		this.genre = genre.toLowerCase(); //case insensitive
		album = a;
	}
	
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	public String getGenre() {return genre;}
	
	public String getName() {return title;}
	
	public Album getAlbum() {return album;}
	
	public String getArtist() {return artist;}
	
	public double getRating() {return rating;}
}
