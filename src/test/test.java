package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import model.Song;
import model.Album;

import org.junit.Test;


public class test {

	@Test
	/*
	 * Achieves 100% coverage of Song class, passed
	 */
	public void testSong() {
		Song exSong = new Song("Sicko Mode", "Travis Scott");
		Album astroworld = new Album("Astroworld", "Travis Scott", "Rap", "2018", new ArrayList<Song>());
		exSong.setAlbum(astroworld);
		exSong.setRating(5.0);
		assertTrue((exSong.getRating() == 5) && (exSong.getFavorite() == true));
		exSong.setFavorite(false);
		assertTrue((exSong.getFavorite() == false) && (exSong.getRating() == 5));
		assertTrue((exSong.getName().equals("Sicko Mode")) && (exSong.getAlbum().getTitle().equals("Astroworld")));
		assertTrue(exSong.getArtist().equals("Travis Scott"));
		Song secondSong = new Song("Sicko Mode", "Travis Scott");
		secondSong.setAlbum(astroworld);
		assertTrue(exSong.equals(secondSong));
	}
	
	@Test
	public void testAlbum() {
		Album exAlbum = new Album("The Forever Story", "JID", "Rap", "2023", new ArrayList<Song>());
		assertTrue(exAlbum.getArtist().equals("JID"));
		assertTrue(exAlbum.getTitle().equals("The Forever Story"));
		assertTrue(exAlbum.getYear().equals("2023"));
		assertTrue(exAlbum.getGenre().equals("Rap"));
		Song exSong = new Song("2007", "JID");
		ArrayList<Song> songList = new ArrayList<Song>();
		songList.add(exSong);
		exAlbum.addSong(exSong);
		assertTrue(exAlbum.getSongs().equals(songList));
	}

}
