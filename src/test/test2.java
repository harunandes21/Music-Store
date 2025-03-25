package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import database.AccountManager;
import database.MusicStore;
import model.Account;
import model.Album;
import model.LibraryModel;
import model.Playlist;
import model.Song;

class test2 {

	@Test
	public void testOne() throws IOException {
		String user = "test";
		MusicStore store = MusicStore.initializer("albums/albums.txt"); 
		
		Account acc = new Account(user, "123", new LibraryModel());
		
		AccountManager.saveAccount(acc);
		
		acc.attemptLogin("123");
		
		LibraryModel lib = acc.getLibrary();
		
		lib.createPlaylist("test playlist");
		
		Album a = store.findAlbum("19");
		
		lib.addPlaylist(a);
		
		Song s = a.getSongs().get(0);
		Song s2 = a.getSongs().get(1);
		
		lib.addSongToLibrary(s,  a,  acc);
		lib.addSongToLibrary(s2);
		
		Assert.assertTrue(lib.getAllPlaylists().contains(a));
		Assert.assertTrue(lib.getAllSongs().contains(s));
		Assert.assertTrue(lib.getSongById(1).equals(s));
		Assert.assertTrue(lib.getSongByName("Daydreamer").equals(s));
		Assert.assertTrue(lib.getPlaylistByName("19").equals(a));
		
		lib.addSongToPlaylist("test playlist", s2, acc);
		Assert.assertTrue(lib.getPlaylistByName("test playlist").contains(s2));
		Assert.assertTrue(lib.getSongList().contains(s2));
		
		lib.getAllPlaylists().forEach(p -> System.out.print(p.getName()));
		lib.getPlaylistByName("Recently Played").addSong(s);
		lib.playSong(s2);
		Assert.assertTrue(lib.getSongPlays(s2) == 1);
		
		lib.addSongToPlaylist("test playlist", s, acc);
		
		lib.removeSongFromLib(s.getSongId(), acc);
		Assert.assertFalse(lib.getAllSongs().contains(s));
		
		lib.removeSongFromPlaylist("test playlist", s);
		Assert.assertFalse(lib.getPlaylistMap().get("test playlist").contains(s));
		
		lib.deletePlaylist("test playlist");
		Assert.assertFalse(lib.getPlaylistMap().containsKey("test playlist"));
		
		lib.shuffle();
		lib.sortByArtist();
		lib.sortByArtist(lib.getPlaylistByName("19").getSongs());
		lib.sortByName();
		lib.sortByName(lib.getPlaylistByName("19").getSongs());
		lib.sortByRating();
		lib.sortByRating(lib.getPlaylistByName("19").getSongs());
		
		lib.playSong(s2);
		lib.playSong(s);
		
		Assert.assertTrue(lib.getPlaylistByName("19").getName().equals("19"));
		
		Album blank = new Album();
		Assert.assertTrue(blank.getName().equals(""));
		Assert.assertFalse(a.getArtist().equals(null));
		Assert.assertFalse(a.getGenre().equals(null));
		Assert.assertFalse(a.getName().equals(null));
		Assert.assertFalse(a.getYear().equals(null));
	
		lib.rateSong(s, "five");
		Song s3 = new Song();
		
		Assert.assertTrue(s.getAlbumTitle().equals("19"));
		s3.setFavorite(true);
		Assert.assertTrue(s3.getFavorite() == true);
		s3.setRating("1");
		s3.setRating("2");
		s3.setRating("3");
		s3.setRating("4");
		s3.setRating("5");
		
		lib.createPlaylist("test");
		Playlist p = lib.getPlaylistByName("test");
		
		p.addSong(s);
		p.addSong(s2);
		
		p.sortByArtist();
		p.sortByName();
		p.sortByRating();
		p.shuffle();
		
		Assert.assertTrue(a.isAlbum());
		
		p.setName("testy");
		p.getEnd();
		
		Song s4 = p.getSongById(1);
		
		lib.addGenre("pop");
		lib.addGenre("pop");
		lib.addGenre("pop");
		lib.addGenre("pop");
		lib.addGenre("pop");
		lib.addGenre("pop");
		lib.addGenre("pop");
		lib.addGenre("pop");
		lib.addGenre("pop");
		lib.addGenre("pop");
		
		Song s5 = store.searchSongByName("Love Song");
		lib.deletePlaylist(a.getName());
		
		lib.addSongToLibrary(s);
		
		lib.addPlaylist(a);
		a.getSongs().forEach(song -> lib.playSong(song));
		
		Album a2 = store.findAlbum("21");
		a2.getSongs().forEach(song -> lib.addSongToLibrary(song, a2, acc));
		
		lib.rateSong(s2, "5");
		
		lib.searchSongByTitle("Daydreamer");
		
	}

}
