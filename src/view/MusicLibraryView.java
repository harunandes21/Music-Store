package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Vector;

import model.Song;
import model.Playlist;
import model.LibraryModel;
import database.MusicStore;

public class MusicLibraryView {

    private MusicStore musicStore;
    private LibraryModel libraryModel;
    private DefaultListModel<String> playlistListModel;
    private JList<String> playlistListView;
    private JTextField searchField;
    private JComboBox<String> searchTypeComboBox;
    private DefaultTableModel songTableModel;
    private JTable songTable;
    private JFrame frame;
    private JComboBox<String> playlistDropdown;

    public MusicLibraryView(MusicStore musicStore) {
        this.musicStore = musicStore;
        this.libraryModel = new LibraryModel(musicStore);
        playlistListModel = new DefaultListModel<>();
        songTableModel = new DefaultTableModel(new String[]{"Title", "Artist", "Album"}, 0);
    }

    public void createAndShowGUI() {
        frame = new JFrame("Music Store");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JPanel searchPanel = createSearchPanel();
        JPanel playlistPanel = createPlaylistPanel();
        JPanel songPanel = createSongPanel();
        JPanel buttonPanel = createButtonPanel();

        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(playlistPanel, BorderLayout.WEST);
        frame.add(songPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        searchField = new JTextField();
        panel.add(searchField, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0;
        String[] searchTypes = {"Song", "Album"};
        searchTypeComboBox = new JComboBox<>(searchTypes);
        panel.add(searchTypeComboBox, gbc);

        return panel;
    }

    private JPanel createPlaylistPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        playlistListView = new JList<>(playlistListModel);
        updatePlaylistList();
        JScrollPane scrollPane = new JScrollPane(playlistListView);
        panel.add(scrollPane, BorderLayout.CENTER);

        playlistListView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    String selectedPlaylistName = playlistListView.getSelectedValue();
                    showSongsForPlaylist(selectedPlaylistName);
                }
            }
        });

        return panel;
    }

    private JPanel createSongPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        songTable = new JTable(songTableModel);
        JScrollPane scrollPane = new JScrollPane(songTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> performSearch());
        panel.add(searchButton);

        JButton createPlaylistButton = new JButton("Create Playlist");
        createPlaylistButton.addActionListener(e -> createPlaylist());
        panel.add(createPlaylistButton);

        JButton addToPlaylistButton = new JButton("Add to Playlist");
        addToPlaylistButton.addActionListener(e -> addSongToPlaylist());
        panel.add(addToPlaylistButton);

        JButton removeFromPlaylistButton = new JButton("Remove from Playlist");
        removeFromPlaylistButton.addActionListener(e -> removeSongFromPlaylist());
        panel.add(removeFromPlaylistButton);

        JButton deletePlaylistButton = new JButton("Delete Playlist");
        deletePlaylistButton.addActionListener(e -> {
            String selectedPlaylistName = playlistListView.getSelectedValue();
            if (selectedPlaylistName != null) {
                int option = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to delete the playlist '" + selectedPlaylistName + "'?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    deletePlaylist(selectedPlaylistName);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "No playlist selected.");
            }
        });

        panel.add(deletePlaylistButton);

        return panel;
    }

    private void performSearch() {
        String searchQuery = searchField.getText();
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            String searchType = (String) searchTypeComboBox.getSelectedItem();
            java.util.ArrayList<Song> results = musicStore.performSearch(searchQuery, searchType);
            songTableModel.setRowCount(0);
            for (Song song : results) {
                songTableModel.addRow(new Vector<>(java.util.Arrays.asList(
                        song.getName(), song.getArtist(), song.getAlbum().getTitle())));
            }
        }
    }

    private void createPlaylist() {
        String playlistName = JOptionPane.showInputDialog(frame, "Enter Playlist Name:");
        if (playlistName != null && !playlistName.trim().isEmpty()) {
            libraryModel.createPlaylist(playlistName);
            updatePlaylistList();
        }
    }

    private void addSongToPlaylist() {
        int row = songTable.getSelectedRow();
        if (row >= 0) {
            String songName = (String) songTableModel.getValueAt(row, 0);

            // Create a new JPanel to hold the JComboBox
            JPanel panel = new JPanel();
            panel.add(new JLabel("Select Playlist:"));

            // Initialize the playlistDropdown JComboBox in the dialog
            JComboBox<String> playlistDropdownPopup = new JComboBox<>();
            for (Playlist playlist : musicStore.getPlaylists()) {
                playlistDropdownPopup.addItem(playlist.getName());
            }

            panel.add(playlistDropdownPopup);

            // Show the dialog
            int option = JOptionPane.showConfirmDialog(frame, panel, "Add Song to Playlist", JOptionPane.OK_CANCEL_OPTION);

            // If the user presses OK, proceed with adding the song to the selected playlist
            if (option == JOptionPane.OK_OPTION) {
                String playlistName = (String) playlistDropdownPopup.getSelectedItem();
                if (playlistName != null && !playlistName.trim().isEmpty()) {
                    Playlist selectedPlaylist = musicStore.getPlaylist(playlistName);
                    
                    // Check if the song already exists in the playlist
                    for (Song song : selectedPlaylist.getSongs()) {
                        if (song.getName().equals(songName)) {
                            JOptionPane.showMessageDialog(frame, "Song '" + songName + "' already exists in the playlist '" + playlistName + "'.");
                            return;  // Exit the method if the song is already in the playlist
                        }
                    }

                    // Add the song to the playlist
                    libraryModel.addSongToPlaylist(playlistName, songName);

                    // Show confirmation message
                    JOptionPane.showMessageDialog(frame, "Successfully added the song '" + songName + "' to the playlist '" + playlistName + "'.");

                    updatePlaylistList();  // Update the dropdown list after adding a song
                }
            }
        }
    }


    private void removeSongFromPlaylist() {
        String playlistName = playlistListView.getSelectedValue();
        if (playlistName != null) {
            int row = songTable.getSelectedRow();
            if (row >= 0) {
                String songName = (String) songTableModel.getValueAt(row, 0);

                int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to remove the song '" + songName + "' from the playlist '" + playlistName + "'?", "Remove Song", JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    libraryModel.removeSongFromPlaylist(playlistName, songName);
                    showSongsForPlaylist(playlistName);
                    JOptionPane.showMessageDialog(frame, "Song '" + songName + "' has been removed from the playlist '" + playlistName + "'.");
                }
            }
        }
    }

    public void deletePlaylist(String playlistName) {
        Playlist playlist = libraryModel.getPlaylistByName(playlistName);
        if (playlist != null) {
            libraryModel.deletePlaylist(playlistName);
            musicStore.getPlaylists().remove(playlist);
            updatePlaylistList();
            JOptionPane.showMessageDialog(frame, "Playlist '" + playlistName + "' deleted.");
        } else {
            JOptionPane.showMessageDialog(frame, "Playlist '" + playlistName + "' not found.");
        }
    }

    private void updatePlaylistList() {
        playlistListModel.clear();

        for (Playlist playlist : musicStore.getPlaylists()) {
            playlistListModel.addElement(playlist.getName());
        }
    }

    private void showSongsForPlaylist(String playlistName) {
        songTableModel.setRowCount(0);

        Playlist playlist = libraryModel.getPlaylistByName(playlistName);
        if (playlist != null) {
            if (playlist.getSongs().isEmpty()) {
                songTableModel.addRow(new Vector<>(java.util.Arrays.asList("No songs in this playlist", "", "")));
            } else {
                for (Song song : playlist.getSongs()) {
                    Object[] rowData = {
                        song.getName(),
                        song.getArtist(),
                        song.getAlbum().getTitle()
                    };
                    songTableModel.addRow(new Vector<>(java.util.Arrays.asList(rowData)));
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String path = "albums/albums.txt";
        MusicStore musicStore = MusicStore.initializer(path);
        SwingUtilities.invokeLater(() -> new MusicLibraryView(musicStore).createAndShowGUI());
    }
}
