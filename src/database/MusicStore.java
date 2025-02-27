package database;


import java.io.*;
import java.util.*;

import model.Album;
import model.Playlist;
import model.Song;







public class MusicStore {
	private ArrayList<Album> albums;
	private ArrayList<Playlist> playlists;
	
	public MusicStore() {
		albums = new ArrayList<Album>();
		playlists = new ArrayList<Playlist>();
	}
	
	public ArrayList<Playlist> getPlaylists() {
		return playlists;
	}
	
	public ArrayList<Album> getAlbums() {
		return albums;
	}
	
	public Playlist getPlaylist(String name) {
		for (Playlist p: playlists) {
			if (p.getName() == name) {
				return p;
			}
		}
		return null;
	}
	
	public Song searchSongByName(String name) {
		for (Album a : albums) {
			for (Song s : a.getSongs()) {
				if (s.getName() == name) {
					System.out.println("Song found: Album - " + s.getAlbum() + ", Title - "
							+ s.getName() + ", Artist - " + s.getArtist());
					return s;
				}
			}
		}
		System.out.println("Song: " + name + " not found in database.");
		return null;
	}
	
	public Song searchSongByArtist(String name) {
		for (Album a : albums) {
			for (Song s : a.getSongs()) {
				if (s.getArtist() == name) {
					System.out.println("Song found: Album - " + s.getAlbum() + ", Title - "
							+ s.getName() + ", Artist - " + s.getArtist());
					return s;
				}
			}
		}
		System.out.println("No songs by " + name + " were found in database.");
		return null;
	}
	
	public Album searchAlbumByName(String name) {
		for (Album a : albums) {
			if (a.getTitle() == name) {
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
			if (a.getArtist() == name) {
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
	public static MusicStore Initializer(String path) throws IOException {

		MusicStore store = new MusicStore();
		BufferedReader reader = new BufferedReader(new FileReader("albums/albums.txt"));

		String line;
		while ((line = reader.readLine()) != null) {
			String[] parts = line.split(",");

			String albumTitle = parts[0];
			String artist = parts[1];
			String albumFilePath = "albums/" + albumTitle + "_" + artist + ".txt";
			Album album = AlbumReader(albumFilePath);
			store.albums.add(album);

		}
		reader.close();
		return store;
	}
	
	private static Album AlbumReader(String filePath) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filePath));

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
		return new Album(title, artist, genre, year, songs);
	}

}

