package view;

import model.Album;
import model.LibraryModel;
import model.Song;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import database.MusicStore;

public class Main {

	public static void main(String[] args) {
		try {

			String path = "albums/albums.txt";

			MusicStore musicStore = MusicStore.initializer(path);
			LibraryModel UserLibrary= new LibraryModel(musicStore);
			/*
			 * System.out.println("Albums:"); for(Album album: musicStore.getAlbums()) {
			 * System.out.println(album.getTitle() + "_" + album.getArtist());
			 * 
			 * 
			 * 
			 * } System.out.println("All Songs:"); for (Song song :
			 * musicStore.getAllSongs()) { System.out.println("Song: " + song.getName());
			 * System.out.println("Artist: " + song.getArtist());
			 * System.out.println("Album: " + song.getAlbum().getTitle());F
			 * System.out.println(); }
			 */
			Scanner scanner = new Scanner(System.in);
	        String plName;
	        
	        
			System.out.println("name for the new playlist:");
	        plName = scanner.nextLine();
	        UserLibrary.createPlaylist(plName);
	        
	        System.out.print("\nEnter the name of the album you want to search for: ");
            String albumName = scanner.nextLine();
            Album album = musicStore.searchAlbumByName(albumName);
            if (album != null) {
               
                System.out.println("\nSongs in the album '" + album.getTitle() + "':");
                List<Song> songs = album.getSongs();
                for (int i = 0; i < songs.size(); i++) {
                    System.out.println((i + 1) + ". " + songs.get(i).getName());
                }
            }
            System.out.println("\name of the song which will be added to playlist:");
            String songName = scanner.nextLine();
            musicStore.searchSongByName(songName);
            UserLibrary.addSongToPlaylist(plName, songName);
            
            System.out.println("\name of the  second song which will be added to playlist:");
            songName = scanner.nextLine();
            musicStore.searchSongByName(songName);
            UserLibrary.addSongToPlaylist(plName, songName);
            
            System.out.println("\nSongs in the playlist '" + plName + "':");
            UserLibrary.listAllPlaylists();
            
            
            
            
            
	        
		} catch (IOException e) {
			System.err.println("Error loading music store: " + e.getMessage());
		}
		
		
	}
}
