/*
 * Authors: Lucas Hamacher and Harun Andeshmand
 * Functionality: 
 * 		holds a Song object, containing data like title, artist, and album.
 * 		contains methods for setting and getting attributes
 */

package model;

import java.util.Objects;

public class Song {
	
	public static enum Rating {UNRATED, ONE, TWO, THREE, FOUR, FIVE};
	private Rating rating;
	private String title;
	private String artist;
	private String albumTitle;
	private String genre;
	private boolean favorite = false;
	private int songId;
	
	/*
	 * @pre name, author, genre != null
	 */
	public Song(String name, String author, String genre, Album a, int id) {
		this.rating = Rating.UNRATED;
		title=name;
		artist=author;
		this.genre = genre.toLowerCase(); //case insensitive
		this.albumTitle = a.getName();
		songId=id;
	}
	public Song() {
        this.rating = Rating.UNRATED;
        this.title = "";
        this.artist = "";
        this.genre = "";
        this.albumTitle = "";
        this.songId=0;
    }
	public String getAlbumTitle() {
        return albumTitle;
    }
	public boolean setRating(String rateString) {
		if (rateString.equals("1")) rating = Rating.ONE;
		if (rateString.equals("2")) rating = Rating.TWO;
		if (rateString.equals("3")) rating = Rating.THREE;
		if (rateString.equals("4")) {rating = Rating.FOUR; return true;}
		if (rateString.equals("5")) {rating = Rating.FIVE; favorite = true; return true;}
		return false;
	}
	
	public void setFavorite(boolean b) {favorite = b;}
	
	public boolean getFavorite() {return (favorite || false);} //no escaping reference
	
	public String getGenre() {return genre;} //immutable
	
	public String getName() {return title;} //immutable
	
	
	public String getArtist() {return artist;} //immutable
	
	public Rating getRating() {
		if (rating.equals(Rating.ONE)) {return Rating.ONE;}
		if (rating.equals(Rating.TWO)) {return Rating.TWO;}
		if (rating.equals(Rating.THREE)) {return Rating.THREE;}
		if (rating.equals(Rating.FOUR)) {return Rating.FOUR;}
		if (rating.equals(Rating.FIVE)) {return Rating.FIVE;}
		return Rating.UNRATED;
	} //returns clone
	
	@Override
	public String toString() {
	    return " "+songId+" " + title + " by " + artist +", Album: "+albumTitle+"," + genre +", Rating:"+rating+"";
	}
	@Override
    public int hashCode() {
        return Objects.hash(title, artist, albumTitle);
    }
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(title, song.title) &&
               Objects.equals(artist, song.artist) &&
               Objects.equals(albumTitle, song.albumTitle);
    }
	public int getSongId() {
		
		return songId + 0;
	}

}
