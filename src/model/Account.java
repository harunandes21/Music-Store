package model;
import org.mindrot.jbcrypt.BCrypt;


public class Account {

	private String username;
	private String hashedPassword;
	private LibraryModel library;
    
	public Account() {
    }
	
	public Account(String user, String pass, LibraryModel lib) {
		username = user;
		hashedPassword = BCrypt.hashpw(pass, BCrypt.gensalt());
		library = lib;
	}
	
	public boolean attemptLogin(String pass) {
		if (BCrypt.checkpw(pass,hashedPassword)) {;
		return true;
					}
		return false;
	}
	public String getUsername() {
		return username;
	}
	
//	public String getHashedPassword() {// for testing if hash works. to be removed later.
//		return hashedPassword;
//	}
//	
	public LibraryModel getLibrary() {
		return library;
	}
	
}
