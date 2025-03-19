package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Account {

	private String username;
	private String password;
	private LibraryModel library;
	
	public Account(String user, String pass, LibraryModel lib) {
		username = user;
		password = pass;
		library = lib;
	}
	
	public boolean attemptLogin(String pass) {
		if (password.equals(pass)) return true;
		return false;
	}
	
	public String getUsername() {
		return username;
	}
	
	public LibraryModel getLibrary() {
		return library;
	}
	
}
