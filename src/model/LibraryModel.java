/*
 * Authors: Lucas Hamacher and Harun Andeshmand
 * Functionality: 
 * 		holds a music store object and maintains storage of a user's music library
 * 		contains methods for getting, setting as well as creating playlists, adding songs, removing songs,
 * 		and deleting playlists altogether.
 */

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
    
    // adding a whole album to our library
    public void addPlaylist(Playlist playlist) {
        playlist.setAlbum();
        userPlaylists.add(playlist);
        musicStore.getPlaylists().add(playlist);  
        
        System.out.println("Playlist '" + playlist.getName() + "' added to library.");
    }
    //creating a new playlist
    public void createPlaylist(String name) {
        
            Playlist newPlaylist = new Playlist(name, new ArrayList<>());
            userPlaylists.add(newPlaylist);
            musicStore.addPlaylist(newPlaylist);
            
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
    
    public Song getSongByName(String songName) {
        for (Playlist playlist : userPlaylists) {
            for (Song song : playlist.getSongs()) {
                if (song.getName().equals(songName)) {
                    return song;
                }
            }
        }
        return null;  
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
