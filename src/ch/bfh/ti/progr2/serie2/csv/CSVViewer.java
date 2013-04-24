package ch.bfh.ti.progr2.serie2.csv;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Frame for showing a CSV file using a JTable
 */
public class CSVViewer extends JFrame {
	JTable table;


	private class OpenCSVAction extends AbstractAction {
		final JFileChooser fileChooser;

		public OpenCSVAction() {
			putValue(Action.NAME, "Open");
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl O"));
			putValue(Action.MNEMONIC_KEY, java.awt.event.KeyEvent.VK_O);

			// Create a new file chooser at the CWD
			fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
			// Only allow CSV files
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.setFileFilter(new FileNameExtensionFilter("CSV file", "csv"));
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// Let user choose a file and load the specified CSV file if everything's OK
			int returnValue = fileChooser.showOpenDialog(CSVViewer.this);
			if(returnValue == JFileChooser.APPROVE_OPTION) {

				Object delimiter = JOptionPane.showInputDialog(CSVViewer.this,
						"What delimiter shall be used?",
						"Delimiter type",
						JOptionPane.QUESTION_MESSAGE,
						null,
						new Character[]{';', ',', '\t'},
						';');
				loadCSV(fileChooser.getSelectedFile().getPath(), (Character)delimiter);
			}
		}
	}


	/**
	 * Create a new CSVViewer.
	 */
	public CSVViewer() {
		setSize(500, 400);

		// Create a menu bar and populate with menus
		JMenuBar bar = new JMenuBar();
		bar.add(createFileMenu());
		setJMenuBar(bar);

		// Initialize our table
		table = new JTable();

		// Create a scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);

		// Add the scroll pane to this panel.
		add(scrollPane);
	}

	private JMenu createFileMenu() {
		JMenu menu = new JMenu("File");
		menu.add(new OpenCSVAction());

		return menu;
	}

	/**
	 * Load a CSV file.
	 * @param filename
	 */
	private void loadCSV(String filename, char delimiter) {
		table.setModel(new CSVTableModel(filename, delimiter));
	}


	/**
	 * Entry point.
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new CSVViewer();
		frame.setTitle("CSV Viewer");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}