/*
 * Authors: Lucas Hamacher and Harun Andeshmand
 * Functionality: 
 * 		represents a music store object with data like playlists, albums, and allsongs.
 * 		contains methods for getting, searching, adding, and reading in data from a file.
 */

package database;

import java.io.*;
import java.util.*;

import model.Album;
import model.LibraryModel;
import model.Song;

public class MusicStore {
	
	private ArrayList<Album> albums;
	private HashMap<String, LibraryModel> libraries = new HashMap<String, LibraryModel>(); 
	private ArrayList<Song> allSongs;
	private static int id=1;
	
	public MusicStore() {
		albums = new ArrayList<Album>();
		allSongs = new ArrayList<>();
	}
	
	public Album findAlbumBySong(Song s) {
		for (Album a: albums) {
			if (a.contains(s)) {
				return a;
			}
		}
		return null;
	}
	
	public Album findAlbum(String name) {
		for (Album a: albums) {
			if (a.getName().toLowerCase().equals(name.toLowerCase())) {
				return a;
			}
		}
		return null;
	}
	
	public LibraryModel getLibrary(String user) {
		return libraries.get(user);
	}
	
	public ArrayList<Album> getAlbums() {
		return new ArrayList<Album> (albums);
	}
	
	public ArrayList<Song> getAllSongs() {
	    return new ArrayList<Song>(allSongs); 
	}
	// used for library methods. searcing son while adding it to a playlist.
	public Song searchSongByName(String name) {
		
			for (Song s : allSongs) {
				if (s.getName().equalsIgnoreCase(name)) {
					System.out.println("Song found: Album - " + s.getAlbumTitle() + ", Title - "
							+ s.getName() + ", Artist - " + s.getArtist());
					return s;
				}
			}
		
		System.out.println("Song: " + name + " not found in database.");
		return null;
	}
	// opens the  albums txt, and then calls the album reader
	public static MusicStore initializer(String path) throws IOException {

		MusicStore store = new MusicStore();
		BufferedReader reader = new BufferedReader(new FileReader("albums/albums.txt"));
		 
		String line;
		while ((line = reader.readLine()) != null) {
			String[] parts = line.split(",");

			String albumTitle = parts[0];
			String artist = parts[1];
			String albumFilePath = "albums/" + albumTitle + "_" + artist + ".txt";
			Album album = albumReader(albumFilePath);
			store.albums.add(album);
			store.allSongs.addAll(album.getSongs());

		}
		reader.close();
		return store;
	}
	// takes the path from initializer, opens and reads albums, creates song and album lists.
	private static Album albumReader(String filePath) throws IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
		String data = reader.readLine();
		String[] parts = data.split(",");
		String title = parts[0];
		String artist = parts[1];
		String genre = parts[2];
		String year = parts[3];

		ArrayList<Song> songs = new ArrayList<>();
		String songTitle;
		
		Album album = new Album(title, artist, genre, year);
		
		while ((songTitle = reader.readLine()) != null) {
			
			songs.add(new Song(songTitle, artist, genre, album,id));
			id++;
		}
		
		//adds direct copy of song, which is not an escaping reference because Song is immutable
		for (Song s : songs) {
			album.addSong(s);
		}
		
		return album;
	    }

	}
	
	// In MusicStore class
	public ArrayList<Song> searchSongByGenreInStore(String genre) {
	    ArrayList<Song> results = new ArrayList<>();
	    
	    for (Song song : allSongs) {
	        if (song.getGenre().equalsIgnoreCase(genre)) {
	            results.add(song);
	        }
	    }
	    
	    return results;
	}

	
	public void addAlbum(Album a) {
		if (albums.contains(a)) {return;}
		albums.add(a);
		for (Song s: a.getSongs()) {
			if (!allSongs.contains(s)) {
				allSongs.add(s);
			}
		}
	}
}

