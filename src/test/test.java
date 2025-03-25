/*
 * Authors: Lucas Hamacher and Harun Andeshmand
 * Functionality: 
 * 		tests all classes of the project within model and database with 100% statement coverage
 */

package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import model.Song;
import model.Account;
import model.Album;
import model.LibraryModel;
import model.Playlist;

import org.junit.Test;

import database.AccountManager;
import database.MusicStore;


public class test {

	@Test
	public void testOne() throws IOException {
		String user = "lucas";
		MusicStore store = MusicStore.initializer("albums/albums.txt"); 
		
		Account acc = AccountManager.loadAccount(user);
		
		acc.attemptLogin("1");
		
	}
}
