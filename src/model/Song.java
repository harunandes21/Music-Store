/*
 * Authors: Lucas Hamacher and Harun Andeshmand
 * Functionality: 
 * 		holds a Song object, containing data like title, artist, and album.
 * 		contains methods for setting and getting attributes
 */

package model;

public class Song {
	
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
	}

	public void setAlbum(Album album) {
		this.album = album;
	}
	
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	
	public String getName() {return title;}
	
	public Album getAlbum() {return album;}
	
	public String getArtist() {return artist;}
	
	public int getRating() {return rating;}
	
	
	
	
}
