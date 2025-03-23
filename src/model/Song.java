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
	private String albumTitle;
	private String genre;
	
	/*
	 * @pre name, author, genre != null
	 */
	public Song(String name, String author, String genre, Album a) {
		title=name;
		artist=author;
		rating = 0.0;
		this.genre = genre.toLowerCase(); //case insensitive
		this.albumTitle = a.getTitle();
	}
	public Song() {
        this.rating = 0.0;
        this.title = "";
        this.artist = "";
        this.genre = "";
        this.albumTitle = ""; 
    }
	public String getAlbumTitle() {
        return albumTitle;
    }
	public void setRating(double rating) {
		this.rating = rating;
	}
	
	public String getGenre() {return genre;} //immutable
	
	public String getName() {return title;} //immutable
	
	
	public String getArtist() {return artist;} //immutable
	
	public double getRating() {double r = rating; return r;} //returns clone
}
