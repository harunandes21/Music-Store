package model;

import java.io.*;
import java.util.*;

public class MusicStore {
	private ArrayList<Album> albums;

	public MusicStore() {
		this.albums = new ArrayList<>();
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

	public ArrayList<Album> getAlbums() {
		return albums;
	}

}
