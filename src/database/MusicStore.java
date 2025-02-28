package database;


import java.io.*;
import java.util.*;

import model.Album;
import model.Playlist;
import model.Song;







public class MusicStore {
	private ArrayList<Album> albums;
	private ArrayList<Playlist> playlists;
	private ArrayList<Song> allSongs;
	public MusicStore() {
		albums = new ArrayList<Album>();
		playlists = new ArrayList<Playlist>();
		allSongs = new ArrayList<>();
	}
	
	public ArrayList<Playlist> getPlaylists() {
		return playlists;
	}
	
	public ArrayList<Album> getAlbums() {
		return albums;
	}
	
	public Playlist getPlaylist(String name) {
		for (Playlist p: playlists) {
			if (p.getName().equalsIgnoreCase(name)) {
				return p;
			}
		}
		return null;
	}
	public ArrayList<Song> getAllSongs() {
	    return allSongs; 
	}
	
	public Song searchSongByName(String name) {
		
			for (Song s : allSongs) {
				if (s.getName().equalsIgnoreCase(name)) {
					System.out.println("Song found: Album - " + s.getAlbum().getTitle() + ", Title - "
							+ s.getName() + ", Artist - " + s.getArtist());
					return s;
				}
			}
		
		System.out.println("Song: " + name + " not found in database.");
		return null;
	}
	
	public Song searchSongByArtist(String name) {
		
			for (Song s : allSongs) {
				if (s.getArtist().equalsIgnoreCase(name)) {
					System.out.println("Song found: Album - " + s.getAlbum().getTitle() + ", Title - "
							+ s.getName() + ", Artist - " + s.getArtist());
					return s;
				}
			}
		
		System.out.println("No songs by " + name + " were found in database.");
		return null;
	}
	
	public Album searchAlbumByName(String name) {
		for (Album a : albums) {
			if (a.getTitle().equalsIgnoreCase(name)) {
				String msg = "Artist: " + a.getArtist() + "\nAlbum: " + a.getTitle() + "\nSongs: \n";
				for (Song song : a.getSongs()) {
					msg += song.getName() + "\n";
				}
				System.out.print(msg);
				return a;
			}
		}
		System.out.println("Album: " + name + " not found in database.");	
		return null;
	}
	
	public Album searchAlbumByArtist(String name) {
		for (Album a : albums) {
			 if (a.getArtist().equalsIgnoreCase(name)) {
				String msg = "Artist: " + a.getArtist() + "\nAlbum: " + a.getTitle() + "\nSongs: \n";
				for (Song song : a.getSongs()) {
					msg += song.getName() + "\n";
				}
				System.out.print(msg);
				return a;
			}
		}
		System.out.println("No albums by " + name + " were found in database.");		 
		return null;

	}
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
		while ((songTitle = reader.readLine()) != null) {
			songs.add(new Song(songTitle, artist));
		}
		
		Album album = new Album(title, artist, genre, year, songs);
		
		for (Song s : album.getSongs()) {
			s.setAlbum(album);
		}
		
		return album;
	    }

	}
	
	
}

