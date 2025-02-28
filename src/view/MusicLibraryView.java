package view;

import javax.swing.*;
import java.awt.*;
import model.LibraryModel;
import database.MusicStore;
import model.Song;
import model.Playlist;

public class MusicLibraryView {

    private MusicStore musicStore;
    private LibraryModel libraryModel;

    // GUI components
    private JList<String> songListView;
    private JList<String> playlistListView;
    private JTextField searchField;
    private JTextField playlistNameField;
    private JButton searchButton;
    private JButton addToPlaylistButton;
    private JButton createPlaylistButton;
    private DefaultListModel<String> songListModel;
    private DefaultListModel<String> playlistListModel;

    public MusicLibraryView() {
        // Initialize the music store and library model
        musicStore = new MusicStore();
        libraryModel = new LibraryModel(musicStore);

        // Initialize the models for the list views
        songListModel = new DefaultListModel<>();
        playlistListModel = new DefaultListModel<>();
    }

    public void createAndShowGUI() {
        // Set up the frame
        JFrame frame = new JFrame("Music Library Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);

        // Set up the main panel with BoxLayout (vertical)
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Set up the song list view
        songListView = new JList<>(songListModel);
        songListView.setPreferredSize(new Dimension(450, 200));
        updateSongList();

        // Set up the playlist list view
        playlistListView = new JList<>(playlistListModel);
        playlistListView.setPreferredSize(new Dimension(450, 200));

        // Search field and button
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(450, 30));
        searchField.setToolTipText("Enter song name...");
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchSongs());

        // Playlist name field and buttons
        playlistNameField = new JTextField();
        playlistNameField.setPreferredSize(new Dimension(450, 30));
        playlistNameField.setToolTipText("Enter playlist name...");
        createPlaylistButton = new JButton("Create Playlist");
        createPlaylistButton.addActionListener(e -> createPlaylist());
        addToPlaylistButton = new JButton("Add to Playlist");
        addToPlaylistButton.addActionListener(e -> addSongToPlaylist());

        // Arrange the layout
        panel.add(new JLabel("Songs:"));
        panel.add(new JScrollPane(songListView));
        panel.add(searchField);
        panel.add(searchButton);
        panel.add(new JLabel("Playlists:"));
        panel.add(new JScrollPane(playlistListView));
        panel.add(playlistNameField);
        panel.add(createPlaylistButton);
        panel.add(addToPlaylistButton);

        // Add panel to frame and display
        frame.add(panel);
        frame.setVisible(true);
    }

    private void updateSongList() {
        // Update the song list view with the songs from the MusicStore
        songListModel.clear();
        for (Song song : musicStore.getAllSongs()) {
            songListModel.addElement(song.getName());
        }
    }

    private void searchSongs() {
        String query = searchField.getText().toLowerCase();
        songListModel.clear();

        // Search for songs by name
        for (Song song : musicStore.getAllSongs()) {
            if (song.getName().toLowerCase().contains(query)) {
                songListModel.addElement(song.getName());
            }
        }
    }

    private void createPlaylist() {
        String playlistName = playlistNameField.getText();
        if (!playlistName.isEmpty()) {
            libraryModel.createPlaylist(playlistName);
            playlistListModel.addElement(playlistName);
            playlistNameField.setText("");
        }
    }

    private void addSongToPlaylist() {
        String selectedSong = songListView.getSelectedValue();
        String selectedPlaylist = playlistListView.getSelectedValue();

        if (selectedSong != null && selectedPlaylist != null) {
            Song song = musicStore.searchSongByName(selectedSong);
            Playlist playlist = musicStore.getPlaylist(selectedPlaylist);

            if (song != null && playlist != null) {
                playlist.addSong(song);
                System.out.println("Song added to playlist: " + selectedPlaylist);
            }
        }
    }

    public static void main(String[] args) {
        // Create and show the GUI
        SwingUtilities.invokeLater(() -> {
            new MusicLibraryView().createAndShowGUI();
        });
    }
}
