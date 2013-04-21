package ch.bfh.ti.progr2.serie2.csv;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 15.04.13
 * Time: 08:36
 * To change this template use File | Settings | File Templates.
 */
public class CSVViewer extends JFrame {
	JTable table;
	public CSVViewer() {
		setSize(500, 400);
		// Create a menu bar and populate with menus
		JMenuBar bar = new JMenuBar();
		bar.add(createFileMenu());
		setJMenuBar(bar);


		table = new JTable(new CSVTableModel("my.csv", ';'));
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));

		//Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);

		//Add the scroll pane to this panel.
		add(scrollPane);

	}

	private JMenu createFileMenu() {
		JMenu menu = new JMenu("File");
		menu.add(new OpenCSVAction());

		return menu;
	}

	public static void main(String[] args) {
		JFrame frame = new CSVViewer();
		frame.setTitle("CSV Viewer");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	/**
	 * Load a CSV file.
	 * @param filename
	 */
	private void loadCSV(String filename, char delimiter) {
		table.setModel(new CSVTableModel(filename, delimiter));
	}

	private class OpenCSVAction extends AbstractAction {
		final JFileChooser fileChooser;

		public OpenCSVAction() {
			putValue(Action.NAME, "Open");
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl O"));
			putValue(Action.MNEMONIC_KEY, java.awt.event.KeyEvent.VK_O);

			fileChooser = new JFileChooser();
		}
		@Override
		public void actionPerformed(ActionEvent e) {
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
}