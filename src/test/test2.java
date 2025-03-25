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
		
		lib.deletePlaylist("test playlist");
		Assert.assertFalse(lib.getPlaylistMap().containsKey("test playlist"));
		
	
		
		
		
	}

}
