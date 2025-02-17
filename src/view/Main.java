package view;

import model.MusicStore;
import model.Album;
import model.Song;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		try {

			String path = "albums/albums.txt";

			MusicStore musicStore = MusicStore.Initializer(path);

			System.out.println("Music Store:");
			for (Album album : musicStore.getAlbums()) {
				System.out.println(album.getTitle() + "_" + album.getArtist());
			}

		} catch (IOException e) {
			System.err.println("Error loading music store: " + e.getMessage());
		}
	}
}
