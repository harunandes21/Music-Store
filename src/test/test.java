/*
 * Authors: Lucas Hamacher and Harun Andeshmand
 * Functionality: 
 * 		tests all classes of the project within model and database with 100% statement coverage
 */

package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import model.Song;
import model.Album;
import model.LibraryModel;
import model.Playlist;

import org.junit.Test;

import database.MusicStore;


public class test {

	@Test
	/*
	 * Achieves 100% coverage of Song class, passed
	 */
	public void testSong() {
		Song exSong = new Song("Sicko Mode", "Travis Scott");
		Album astroworld = new Album("Astroworld", "Travis Scott", "Rap", "2018", new ArrayList<Song>());
		exSong.setAlbum(astroworld);
		exSong.setRating(5);
		assertTrue(exSong.getRating() == 5);
		
		exSong.setRating(5);
		assertTrue(exSong.getRating() == 5);
		assertTrue(exSong.getRating() == 5);

		assertTrue((exSong.getName().equals("Sicko Mode")) && (exSong.getAlbum().getTitle().equals("Astroworld")));
		assertTrue(exSong.getArtist().equals("Travis Scott"));
		Song secondSong = new Song("Sicko Mode", "Travis Scott");
		secondSong.setAlbum(astroworld);
		assertTrue(exSong.equals(secondSong));
	}
	
	@Test
	/*
	 * Acheives 100% statement coverage of Album class, passed.
	 */
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
	
	@Test
	/*
	 * Acheives 100% statement coverage of Playlist class, passed.
	 */
	public void testPlaylist() {
		Playlist exPlaylist = new Playlist("2025", new ArrayList<Song>());
		exPlaylist.setAlbum();
		assertTrue(exPlaylist.isAlbum() == true);
		assertTrue(exPlaylist.getName().equals("2025"));
		exPlaylist.setName("2026");
		assertTrue(exPlaylist.getName().equals("2026"));
		Song exSong = new Song("King For A Day", "Pierce the Veil");
		Album exAlbum = new Album("Rock Album", "Pierce the Veil", "Rock", "2001", new ArrayList<Song>());
		exSong.setAlbum(exAlbum);
		ArrayList<Song> songList = new ArrayList<Song>();
		songList.add(exSong);
		exPlaylist.addSong(exSong);
		assertTrue(exPlaylist.getSongs().equals(songList));
		Song exSong1 = new Song("Bank Account", "21 Savage");
		Album exAlbum1 = new Album("Rap Album", "21 Savage", "Rap", "2002", new ArrayList<Song>());
		exSong.setAlbum(exAlbum1);
		exPlaylist.addSong(exSong1);
		Song exSong2 = new Song("Mine Again", "Fetty Wapp");
		Album exAlbum2 = new Album("Rap Album2", "Fetty Wapp", "Rap", "2003", new ArrayList<Song>());
		exSong.setAlbum(exAlbum2);
		exPlaylist.addSong(exSong2);	
	}
	
	@Test
	/*
	 * Acheives 100% statement coverage of MusicStore class excluding reader and view to controller only methods, passed.
	 */
	public void testMusicStore() {
		MusicStore exStore = new MusicStore();
		Playlist exPlaylist = new Playlist("Playlist", new ArrayList<Song>());
		Album exAlbum = new Album("Title", "Artist", "Genre", "Year", new ArrayList<Song>());
		Song exSong = new Song("Title", "Artist");
		exAlbum.addSong(exSong);
		exSong.setAlbum(exAlbum);
		exStore.addAlbum(exAlbum);
		exPlaylist.addSong(exSong);
		exStore.addPlaylist(exPlaylist);
		exStore.addAlbum(exAlbum);
		assertTrue(exStore.getPlaylists().contains(exPlaylist));
		assertTrue(exStore.getAlbums().contains(exAlbum));
		ArrayList<Song> testList = new ArrayList<Song>();
		testList.add(exSong);
		assertTrue(exStore.getAllSongs().equals(testList));
		ArrayList<Album> testList2 = new ArrayList<Album>();
		testList2.add(exAlbum);
		assertTrue(exStore.getAlbums().equals(testList2));
		assertTrue(exStore.getPlaylists().contains(exPlaylist));
		assertTrue(exStore.getPlaylist("Playlist").equals(exPlaylist));
		assertTrue(exStore.searchSongByName("Title").equals(exSong));
		assertTrue(exStore.searchSongByName("x") == null);
		ArrayList<Song> search1 = exStore.performSearch("Title", "Song");
		ArrayList<Song> search2 = exStore.performSearch("Artist", "Album");
		assertTrue(search1.contains(exSong));
		assertTrue(search2.contains(exSong));
	}
	
	@Test
	public void testLibrary() {
		MusicStore musicStore = new MusicStore();
		Album album = new Album("Title", "Artist", "Genre", "Year", new ArrayList<Song>());
		Song song = new Song("Title", "Artist");
		album.addSong(song);
		song.setAlbum(album);
		musicStore.addAlbum(album);
		LibraryModel library = new LibraryModel(musicStore);
		ArrayList<Song> allSongs = new ArrayList<Song>();
		allSongs.add(song);
		assertTrue(library.getAllSongs().equals(allSongs));
		library.createPlaylist("Title");
		library.addSongToPlaylist("Title", "Title");
		assertTrue(library.getSongByName("Title").getArtist().equals("Artist"));
		assertTrue(library.getPlaylistByName("Title").getName().equals("Title"));
		library.removeSongFromPlaylist("Title", "Title");
		assertTrue(library.getSongByName("Title") == null);
		library.deletePlaylist("Title");
		assertTrue(library.getPlaylistByName("Title") == null);
		
		
		
	}

}
