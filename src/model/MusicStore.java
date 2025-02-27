package model;

import java.util.ArrayList;

public class MusicStore {
	private ArrayList<Album> albums;
	private ArrayList<Playlist> playlists;
	
	public MusicStore() {
		albums = new ArrayList<Album>();
		playlists = new ArrayList<Playlist>();
	}
	
	public ArrayList<Playlist> getPlaylists() {
		return new ArrayList<Playlist>(playlists);
	}
	
	public ArrayList<Album> getAlbums() {
		return new ArrayList<Album>(albums);
	}
	
	public Playlist getPlaylist(String name) {
		for (Playlist p: playlists) {
			if (p.getName() == name) {
				return p;
			}
		}
		return null;
	}
	
	public Album getAlbum(String name) {
		for (Album a: albums) {
			if (a.getTitle() == name) {
				return new Album (a.getTitle(), a.getArtist(), a.getGenre(), a.getYear(), a.getSongs());
			}
		}
		return null;
	}
	
}

