package model;

import java.util.ArrayList;

public class MusicStore {
	private ArrayList<Album> store;
	
	public MusicStore() {
		store = new ArrayList<Album>();
	}
	
	public ArrayList<Album> getStore() {
		return store;
	}
}

