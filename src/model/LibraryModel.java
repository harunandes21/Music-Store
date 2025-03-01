package model;
import java.util.ArrayList;
import database.MusicStore;

public class LibraryModel {

	private MusicStore musicStore;
    private ArrayList<Playlist> userPlaylists;
    
    public LibraryModel(MusicStore musicStore) {
        this.musicStore = musicStore;
        this.userPlaylists = new ArrayList<>();
    }
    
    public ArrayList<Song> getAllSongs() {
        return musicStore.getAllSongs();
    }
    
    
    public void createPlaylist(String name) {
        
            Playlist newPlaylist = new Playlist(name, new ArrayList<>());
            userPlaylists.add(newPlaylist);
            musicStore.getPlaylists().add(newPlaylist);
            
            System.out.println("Playlist '" + name + "' created.");
        
    }
    
    public void addSongToPlaylist(String playlistName, String songName) {
        Playlist playlist = musicStore.getPlaylist(playlistName);
        Song song = musicStore.searchSongByName(songName);
        if (song == null) {
            System.out.println("Song '" + songName + "' not found.");
            return;
        }
        playlist.addSong(song);
        System.out.println("Song '" + songName + "' added to playlist '" + playlistName + "'.");
    }
    
    public void listAllPlaylists() {
        ArrayList<Playlist> playlists = musicStore.getPlaylists();
        if (playlists.isEmpty()) {
            System.out.println("No playlists available.");
            return;
        }
        System.out.println("Playlists:");
        for (Playlist p : playlists) {
            System.out.println("Playlist: " + p.getName());
            for (Song s : p.getSongs()) {
                System.out.println(" - " + s.getName() + " by " + s.getArtist());
            }
        }
    }
    
    public Playlist getPlaylistByName(String name) {
        return musicStore.getPlaylist(name);
    }
    
    public void removeSongFromPlaylist(String playlistName, String songName) {
        Playlist playlist = getPlaylistByName(playlistName);
        if (playlist != null) {
            Song songToRemove = null;
            for (Song song : playlist.getSongs()) {
                if (song.getName().equals(songName)) {
                    songToRemove = song;
                    break;
                }
            }
            if (songToRemove != null) {
                playlist.getSongs().remove(songToRemove);
            }
        }
    }

    public void deletePlaylist(String playlistName) {
        Playlist playlist = getPlaylistByName(playlistName);
        if (playlist != null) {
            userPlaylists.remove(playlist); 
            musicStore.getPlaylists().remove(playlist); 
            System.out.println("Playlist '" + playlistName + "' deleted.");
        } else {
            System.out.println("Playlist '" + playlistName + "' not found.");
        }
    }
    
}
