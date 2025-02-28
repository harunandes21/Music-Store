package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import model.Song;
import model.Album;
import model.Playlist;
import database.MusicStore;

public class MusicLibraryView {

    private MusicStore musicStore;
    private DefaultListModel<String> playlistListModel;
    private JList<String> playlistListView;
    private JTextField searchField;
    private DefaultListModel<String> songListModel; // Changed to DefaultListModel for JList

    public MusicLibraryView(MusicStore musicStore) {
        this.musicStore = musicStore;
        playlistListModel = new DefaultListModel<>();
        songListModel = new DefaultListModel<>();
    }

    public void createAndShowGUI() {
        // Set up the frame with a smaller size
        JFrame frame = new JFrame("Music Store");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400); // Smaller window size

        // Set up the main panel with BoxLayout (vertical)
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Set up the playlist list view (smaller JList)
        playlistListView = new JList<>(playlistListModel);
        playlistListView.setPreferredSize(new Dimension(300, 100)); // Smaller playlist section (height reduced further)
        playlistListView.setMaximumSize(new Dimension(300, 100)); // Prevent it from growing larger
        playlistListView.setMinimumSize(new Dimension(250, 100)); // Set a minimum size for consistency
        updatePlaylistList();

        // Add mouse listener to handle playlist clicks
        playlistListView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    String selectedPlaylistName = playlistListView.getSelectedValue();
                    showSongsForPlaylist(selectedPlaylistName);
                }
            }
        });

        // Search field (smaller, centered)
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30)); // Reasonable size for the text field
        searchField.setMaximumSize(new Dimension(200, 30)); // Prevent it from getting larger
        searchField.setMinimumSize(new Dimension(150, 30)); // Prevent it from getting too small
        searchField.setAlignmentX(Component.CENTER_ALIGNMENT); // Centered
        searchField.setToolTipText("Enter song or album name...");

        // Create a JList for displaying songs (one song per row)
        JList<String> songListView = new JList<>(songListModel);
        songListView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Only allow single selection
        JScrollPane songListScrollPane = new JScrollPane(songListView);
        songListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        songListScrollPane.setPreferredSize(new Dimension(500, 200));

        // Add mouse listener to handle song clicks
        songListView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    String selectedSong = songListView.getSelectedValue();
                    showSongDetails(selectedSong);
                }
            }
        });

        // Buttons for search
        JButton searchSongButton = new JButton("Search Song");
        searchSongButton.addActionListener(e -> searchSongs());

        JButton searchAlbumButton = new JButton("Search Album");
        searchAlbumButton.addActionListener(e -> searchAlbums());

        // Buttons for playlist actions
        JButton createPlaylistButton = new JButton("Create Playlist");
        createPlaylistButton.addActionListener(e -> createPlaylist());

        // Arrange the layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(searchSongButton);
        buttonPanel.add(searchAlbumButton);

        panel.add(new JLabel("Playlists:"));
        panel.add(new JScrollPane(playlistListView));
        panel.add(Box.createRigidArea(new Dimension(-5, 10))); // Add space between elements
        panel.add(searchField);
        panel.add(buttonPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space between elements
        panel.add(songListScrollPane); // Add scrollable song list
        panel.add(createPlaylistButton);

        // Add panel to frame and display
        frame.add(panel);
        frame.setVisible(true);
    }

    private void updatePlaylistList() {
        // Update the playlist list view with the playlists from MusicStore
        playlistListModel.clear();
        for (Playlist playlist : musicStore.getPlaylists()) {
            playlistListModel.addElement(playlist.getName());
        }
    }

    private void showSongsForPlaylist(String playlistName) {
        // Find the playlist and display its songs
        songListModel.clear();
        Playlist playlist = musicStore.getPlaylist(playlistName);
        if (playlist != null) {
            for (Song song : playlist.getSongs()) {
                songListModel.addElement(song.getName());
            }
        }
    }

    private void searchSongs() {
        String query = searchField.getText().toLowerCase();
        songListModel.clear();

        // Search for songs by name
        ArrayList<String> results = new ArrayList<>();
        for (Song song : musicStore.getAllSongs()) {
            if (song.getName().toLowerCase().contains(query)) {
                String result = "Song: " + song.getName() + " | Artist: " + song.getArtist() + " | Album: " + song.getAlbum().getTitle();
                results.add(result);
            }
        }

        // Display results or message
        if (results.isEmpty()) {
            songListModel.addElement("No songs found.");
        } else {
            for (String result : results) {
                songListModel.addElement(result);
            }
        }
    }

    private void searchAlbums() {
        String query = searchField.getText().toLowerCase();
        songListModel.clear();

        // Search for albums by name
        ArrayList<String> results = new ArrayList<>();
        boolean found = false;
        for (Album album : musicStore.getAlbums()) {
            if (album.getTitle().toLowerCase().contains(query)) {
                StringBuilder albumInfo = new StringBuilder();
                albumInfo.append("Album: ").append(album.getTitle())
                          .append(" | Artist: ").append(album.getArtist())
                          .append(" | Year: ").append(album.getYear()).append("\nSongs:\n");

                // Add songs in the album to the list
                for (Song song : album.getSongs()) {
                    albumInfo.append(" - ").append(song.getName()).append("\n");
                }
                results.add(albumInfo.toString());
                found = true;
            }
        }

        // Display results or message
        if (!found) {
            songListModel.addElement("No albums found.");
        } else {
            for (String result : results) {
                songListModel.addElement(result);
            }
        }
    }

    private void showSongDetails(String selectedSong) {
        // For now, just show a simple message with the selected song
        JOptionPane.showMessageDialog(null, "You selected: " + selectedSong);
    }

    private void createPlaylist() {
        String playlistName = JOptionPane.showInputDialog("Enter Playlist Name:");
        if (playlistName != null && !playlistName.trim().isEmpty()) {
            Playlist newPlaylist = new Playlist(playlistName, new ArrayList<>());
            musicStore.getPlaylists().add(newPlaylist);

            updatePlaylistList();
            JOptionPane.showMessageDialog(null, "Playlist '" + playlistName + "' created.");
        }
    }

    public static void main(String[] args) throws IOException {
        // Create MusicStore and MusicLibraryView, and show GUI
        String path = "albums/albums.txt";
        MusicStore musicStore = MusicStore.initializer(path);
        SwingUtilities.invokeLater(() -> {
            new MusicLibraryView(musicStore).createAndShowGUI();
        });
    }
}
