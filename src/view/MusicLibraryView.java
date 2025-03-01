package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import model.Song;
import model.Playlist;
import model.Album;
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
		songTableModel = new DefaultTableModel(new String[] { "Title", "Artist", "Album", "Rating" }, 0);
	}

	public void createAndShowGUI() {
		frame = new JFrame("Music Store");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(950, 600);

		// Use GridBagLayout for better control over component resizing
		frame.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		// Search panel
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		frame.add(createSearchPanel(), gbc);

		// Playlist panel (use a fixed width and weight for resizing control)
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.3; // Make the playlist panel take 30% of the space
		frame.add(createPlaylistPanel(), gbc);

		// Song panel (fill the remaining space)
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.7; // Song panel takes 70% of the space
		frame.add(createSongPanel(), gbc);

		// Button panel
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		frame.add(createButtonPanel(), gbc);

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
		String[] searchTypes = { "Song", "Album" };
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
			}

			else {
				JOptionPane.showMessageDialog(frame, "No playlist selected.");
			}
		});

		panel.add(deletePlaylistButton);
		JButton rateSongButton = new JButton("Rate Song");
		rateSongButton.addActionListener(e -> rateSong());
		panel.add(rateSongButton);

		JButton addAlbumButton = new JButton("Add Album");
		addAlbumButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Call the method to show the album search window
				openAlbumSearchWindow();
			}
		});
		panel.add(addAlbumButton);

		return panel;
	}
	//pop up to put album name and search
	private void openAlbumSearchWindow() {
		// Open a new search window
		JFrame searchWindow = new JFrame("Search Albums");
		searchWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JTextField searchField = new JTextField(20);
		JButton searchButton = new JButton("Search");

		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String searchQuery = searchField.getText();
				List<Album> searchResults = searchAlbums(searchQuery);
				if (!searchResults.isEmpty()) {
					showSearchResults(searchResults);
				} else {
					JOptionPane.showMessageDialog(searchWindow, "No albums found.", "Search Result",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		searchWindow.setLayout(new FlowLayout());
		searchWindow.add(new JLabel("Search for an album:"));
		searchWindow.add(searchField);
		searchWindow.add(searchButton);
		searchWindow.setSize(300, 150);
		searchWindow.setVisible(true);
	}

	private List<Album> searchAlbums(String query) {
		List<Album> results = new ArrayList<>();
		for (Album album : musicStore.getAlbums()) {
			if (album.getTitle().toLowerCase().contains(query.toLowerCase())) {
				results.add(album);
			}
		}
		return results;
	}
	//pop up window with results of searched album
	private void showSearchResults(List<Album> searchResults) {
		JFrame resultWindow = new JFrame("Select Album");
		resultWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JComboBox<String> albumDropdown = new JComboBox<>();
		for (Album album : searchResults) {
			albumDropdown.addItem(album.getTitle());
		}

		JButton confirmButton = new JButton("Confirm");
		confirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedAlbumName = (String) albumDropdown.getSelectedItem();
				Album selectedAlbum = findAlbumByName(selectedAlbumName);
				if (selectedAlbum != null) {
					addAlbumToLibrary(selectedAlbum);
					JOptionPane.showMessageDialog(resultWindow,
							selectedAlbum.getTitle() + " has been added to your library as a playlist.", "Success",
							JOptionPane.INFORMATION_MESSAGE);
					resultWindow.dispose();
				}
			}
		});

		resultWindow.setLayout(new FlowLayout());
		resultWindow.add(new JLabel("Select an album:"));
		resultWindow.add(albumDropdown);
		resultWindow.add(confirmButton);
		resultWindow.setSize(300, 150);
		resultWindow.setVisible(true);
	}
	// adding a whole album to our library
	private void addAlbumToLibrary(Album album) {
		// Create a new playlist with the album's name
		Playlist newPlaylist = new Playlist(album.getTitle(), album.getSongs());

		// Add all songs from the selected album to the playlist
		// newPlaylist.addSongs(album.getSongs());

		// Add the new playlist to the library (assuming `library` is a List or Map of
		// playlists)
		libraryModel.addPlaylist(newPlaylist);

		// Optionally, update the UI to show the new playlist
		updatePlaylistList();
	}

	private Album findAlbumByName(String name) {
		for (Album album : musicStore.getAlbums()) {
			if (album.getTitle().equals(name)) {
				return album;
			}
		}
		return null;
	}
	// search with 2 types, song or album. Both works with title and author
	private void performSearch() {
		String searchQuery = searchField.getText();
		if (searchQuery != null && !searchQuery.trim().isEmpty()) {
			String searchType = (String) searchTypeComboBox.getSelectedItem();
			java.util.ArrayList<Song> results = musicStore.performSearch(searchQuery, searchType);
			songTableModel.setRowCount(0);
			for (Song song : results) {
				String rating = (song.getRating() == 0) ? "" : String.valueOf(song.getRating());
				songTableModel.addRow(new Vector<>(
						java.util.Arrays.asList(song.getName(), song.getArtist(), song.getAlbum().getTitle(), rating)));
			}
		}
	}

	private void rateSong() {
		int row = songTable.getSelectedRow();
		if (row >= 0) {
			String songName = (String) songTableModel.getValueAt(row, 0);
			Song song = musicStore.searchSongByName(songName);

			if (song != null) {
				String[] options = { "1", "2", "3", "4", "5" };
				String ratingStr = (String) JOptionPane.showInputDialog(frame, "Rate the song (1-5):", "Rating",
						JOptionPane.QUESTION_MESSAGE, null, options, "1");

				if (ratingStr != null) {
					int rating = Integer.parseInt(ratingStr);
					song.setRating(rating);

					if (rating == 5) {
						Playlist favorites = musicStore.getPlaylist("Favorites");
						if (!favorites.getSongs().contains(song)) {
							favorites.addSong(song);
							JOptionPane.showMessageDialog(frame, "Song added to Favorites playlist.");
						}
					}

					JOptionPane.showMessageDialog(frame, "Song rated " + rating);
				}
			}
		} else
			return;
	}

	private void createPlaylist() {
		String playlistName = JOptionPane.showInputDialog(frame, "Enter Playlist Name:");
		if (playlistName != null && !playlistName.trim().isEmpty()) {
			libraryModel.createPlaylist(playlistName);
			updatePlaylistList();
		} else {
			JOptionPane.showMessageDialog(frame, "Invalid Input");
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
			int option = JOptionPane.showConfirmDialog(frame, panel, "Add Song to Playlist",
					JOptionPane.OK_CANCEL_OPTION);

			// If the user presses OK, proceed with adding the song to the selected playlist
			if (option == JOptionPane.OK_OPTION) {
				String playlistName = (String) playlistDropdownPopup.getSelectedItem();
				if (playlistName != null && !playlistName.trim().isEmpty()) {
					Playlist selectedPlaylist = musicStore.getPlaylist(playlistName);
					if (selectedPlaylist.isAlbum() == true) {
						JOptionPane.showMessageDialog(frame, "Cannot modify album.");
						return;
					}

					// Check if the song already exists in the playlist
					for (Song song : selectedPlaylist.getSongs()) {
						if (song.getName().equals(songName)) {
							JOptionPane.showMessageDialog(frame,
									"Song '" + songName + "' already exists in the playlist '" + playlistName + "'.");
							return; // Exit the method if the song is already in the playlist
						}
					}

					// Add the song to the playlist
					libraryModel.addSongToPlaylist(playlistName, songName);

					// Show confirmation message
					JOptionPane.showMessageDialog(frame,
							"Successfully added the song '" + songName + "' to the playlist '" + playlistName + "'.");

					updatePlaylistList(); // Update the dropdown list after adding a song
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
				Playlist chosenPlaylist = libraryModel.getPlaylistByName(playlistName);
				int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to remove the song '"
						+ songName + "' from the playlist '" + playlistName + "'?", "Remove Song",
						JOptionPane.YES_NO_OPTION);
				if (chosenPlaylist.isAlbum() == true) {
					JOptionPane.showMessageDialog(frame, "Cannot modify album.");
					return;
				}
				if (option == JOptionPane.YES_OPTION) {
					libraryModel.removeSongFromPlaylist(playlistName, songName);
					showSongsForPlaylist(playlistName);
					JOptionPane.showMessageDialog(frame,
							"Song '" + songName + "' has been removed from the playlist '" + playlistName + "'.");
				}
			}
		}
	}

	public void deletePlaylist(String playlistName) {
		if (playlistName.equals("Favorites")) {
			JOptionPane.showMessageDialog(frame, "Playlist '" + playlistName + "' could not be deleted.");
			return;
		}
		System.out.println(playlistName);
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
	// refresh the playlist view to see the new playlists
	private void updatePlaylistList() {
		playlistListModel.clear();

		for (Playlist playlist : musicStore.getPlaylists()) {
			playlistListModel.addElement(playlist.getName());
		}
	}
	// method to list the songs inside of a playlist
	private void showSongsForPlaylist(String playlistName) {
		songTableModel.setRowCount(0);

		Playlist playlist = libraryModel.getPlaylistByName(playlistName);
		if (playlist != null) {
			if (playlist.getSongs().isEmpty()) {
				songTableModel.addRow(new Vector<>(java.util.Arrays.asList("No songs in this playlist", "", "")));
			} else {
				for (Song song : playlist.getSongs()) {
					String rating = (song.getRating() == 0) ? "" : String.valueOf(song.getRating());
					Object[] rowData = { song.getName(), song.getArtist(), song.getAlbum().getTitle(), rating };
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
