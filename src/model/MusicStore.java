package model;

import java.util.HashMap;

public class MusicStore {
	private HashMap<String, Album> store;
	
	public MusicStore() {
		store = new HashMap<String, Album>();
	}
	
	public HashMap<String, Album> getStore() {
		return store;
	}
	
	public Album getAlbum(String name) {
		return store.get(name);
	}
	
}

